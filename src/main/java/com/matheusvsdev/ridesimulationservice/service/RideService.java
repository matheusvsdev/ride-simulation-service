package com.matheusvsdev.ridesimulationservice.service;

import com.matheusvsdev.ridesimulationservice.dto.RideFinalizationResponseDTO;
import com.matheusvsdev.ridesimulationservice.dto.RideProgressResponseDTO;
import com.matheusvsdev.ridesimulationservice.dto.RideRequestDTO;
import com.matheusvsdev.ridesimulationservice.dto.RideResponseDTO;
import com.matheusvsdev.ridesimulationservice.entity.CoordinateEntity;
import com.matheusvsdev.ridesimulationservice.entity.RideEntity;
import com.matheusvsdev.ridesimulationservice.entity.VehicleEntity;
import com.matheusvsdev.ridesimulationservice.entity.enums.RideStatus;
import com.matheusvsdev.ridesimulationservice.repository.RideRepository;
import com.matheusvsdev.ridesimulationservice.repository.VehicleRepository;
import com.matheusvsdev.ridesimulationservice.util.RideMapper;
import com.matheusvsdev.ridesimulationservice.util.RouteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CoordinateService coordinateService;

    @Transactional
    public RideResponseDTO simulateRide(RideRequestDTO request) {

        // Buscar o veículo
        VehicleEntity vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado."));

        // Consultar a rota via API externa (retorna lista de CoordinateEntity, distância e duração)
        RouteResult routeResult = coordinateService.getRoute(request);

        List<CoordinateEntity> coordinates = routeResult.coordinates();

        // Calcular autonomia e custo
        double availableRange = request.currentFuelLiters() * vehicle.getFuelEfficiencyKmPerLiter();
        double missingKm = Math.max(0, routeResult.distanceKm() - availableRange);
        double litersToRefuel = roundToTwoDecimals(missingKm / vehicle.getFuelEfficiencyKmPerLiter());

        // Criar a entidade da simulação
        RideEntity ride = RideMapper.toEntity(
                request,
                vehicle,
                coordinates,
                routeResult.distanceKm(),
                routeResult.durationMinutes(),
                litersToRefuel
        );

        // Salvar no banco
        rideRepository.save(ride);

        // Retornar resposta formatada
        return RideMapper.toDTO(ride);
    }

    @Transactional
    public RideResponseDTO startRide(Long rideId) {
        RideEntity ride = getRideById(rideId);

        if (!ride.getStatus().equals(RideStatus.PENDING)) {
            throw new IllegalStateException("A simulação já foi iniciada ou finalizada.");
        }

        ride.setStatus(RideStatus.STARTED);
        ride.setStartedAt(Instant.now());

        rideRepository.save(ride);

        return RideMapper.toDTO(ride);
    }

    @Transactional
    public Object progressRide(Long rideId, int positionInRoute) {
        RideEntity ride = getRideById(rideId);

        if (!ride.getStatus().equals(RideStatus.STARTED)) {
            throw new IllegalStateException("Simulação ainda não foi iniciada ou já foi finalizada.");
        }

        if (positionInRoute < ride.getCurrentPositionInRoute()) {
            throw new IllegalStateException("Não é possível voltar na rota.");
        }

        List<CoordinateEntity> points = ride.getRoutePoints();

        int totalPoints = points.size() - 1;
        int current = Math.min(positionInRoute, totalPoints);

        double traveledKm = calculateDistanceBetweenPoints(points, ride.getCurrentPositionInRoute(), current);
        double kmPerLiter = ride.getVehicle().getFuelEfficiencyKmPerLiter();
        double fuelUsed = calculateFuelUsed(traveledKm, kmPerLiter);

        double updatedFuel = roundToTwoDecimals(Math.max(0, ride.getCurrentFuelLiters() - fuelUsed));
        ride.setCurrentFuelLiters(updatedFuel);
        ride.setCurrentPositionInRoute(positionInRoute);

        double progressPercentage = roundToTwoDecimals(calculateProgress(current, totalPoints));

        if (current >= totalPoints) {
            ride.setStatus(RideStatus.FINISHED);
            ride.setFinishedAt(Instant.now());

            double totalDistanceTraveled = ride.getDistanceKm();
            double finalFuel = ride.getCurrentFuelLiters();

            rideRepository.save(ride);

            return new RideFinalizationResponseDTO(
                    ride.getId(),
                    RideStatus.FINISHED,
                    totalDistanceTraveled,
                    finalFuel,
                    ride.getStartedAt(),
                    ride.getFinishedAt()
            );
        }

        double remainingKm = roundToTwoDecimals(ride.getDistanceKm() * (1 - (progressPercentage / 100.0)));
        double avgSpeedKmPerMin = ride.getDistanceKm() / ride.getDurationMinutes();
        double estimatedTimeRemaining = roundToTwoDecimals(calculateEstimatedTime(remainingKm, avgSpeedKmPerMin));

        rideRepository.save(ride);

        return new RideProgressResponseDTO(
                ride.getId(),
                updatedFuel,
                progressPercentage,
                estimatedTimeRemaining,
                remainingKm,
                RideStatus.STARTED
        );
    }

    @Transactional
    public RideFinalizationResponseDTO finishRide(Long rideId) {
        RideEntity ride = getRideById(rideId);

        if (!ride.getStatus().equals(RideStatus.STARTED)) {
            throw new IllegalStateException("Simulação não está em andamento.");
        }

        List<CoordinateEntity> points = ride.getRoutePoints();
        int current = ride.getCurrentPositionInRoute();

        double distanceTraveled = roundToTwoDecimals(calculateDistanceBetweenPoints(points, 0, current));
        double fuelUsed = calculateFuelUsed(distanceTraveled, ride.getVehicle().getFuelEfficiencyKmPerLiter());
        double remainingFuel = roundToTwoDecimals(Math.max(0, ride.getCurrentFuelLiters() - fuelUsed));

        ride.setStatus(RideStatus.FINISHED);
        ride.setFinishedAt(Instant.now());
        ride.setCurrentFuelLiters(remainingFuel);

        rideRepository.save(ride);

        return new RideFinalizationResponseDTO(
                ride.getId(),
                RideStatus.FINISHED,
                distanceTraveled,
                remainingFuel,
                ride.getStartedAt(),
                ride.getFinishedAt()
        );
    }

    private RideEntity getRideById(Long id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Simulação não encontrada."));
    }

    private double calculateDistanceBetweenPoints(List<CoordinateEntity> points, int from, int to) {
        double total = 0.0;
        for (int i = from; i < to && i < points.size() - 1; i++) {
            CoordinateEntity p1 = points.get(i);
            CoordinateEntity p2 = points.get(i + 1);
            total += haversine(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());
        }
        return roundToTwoDecimals(total);
    }

    private double calculateFuelUsed(double distanceKm, double efficiencyKmPerLiter) {
        return distanceKm / efficiencyKmPerLiter;
    }

    private double calculateProgress(int current, int total) {
        if (total <= 0) return 0.0;
        return (current * 100.0) / total;
    }

    private double calculateEstimatedTime(double remainingKm, double speedKmPerMin) {
        return remainingKm / speedKmPerMin;
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLon / 2), 2);
        return R * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }
}
