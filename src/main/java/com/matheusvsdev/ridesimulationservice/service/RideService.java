package com.matheusvsdev.ridesimulationservice.service;

import com.matheusvsdev.ridesimulationservice.client.RouteClient;
import com.matheusvsdev.ridesimulationservice.client.dto.RouteResult;
import com.matheusvsdev.ridesimulationservice.dto.RideRequestDTO;
import com.matheusvsdev.ridesimulationservice.dto.RideResponseDTO;
import com.matheusvsdev.ridesimulationservice.entity.CoordinateEntity;
import com.matheusvsdev.ridesimulationservice.entity.RideEntity;
import com.matheusvsdev.ridesimulationservice.repository.RideRepository;
import com.matheusvsdev.ridesimulationservice.util.RideMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RideService {

    private final RideRepository rideRepository;
    private final RouteClient routeClient;

    public RideService(RideRepository rideRepository, RouteClient routeClient) {
        this.rideRepository = rideRepository;
        this.routeClient = routeClient;
    }

    @Transactional
    public RideResponseDTO simulateRide(RideRequestDTO request) {
        RouteResult routeResult = routeClient.getRoute(request);

        List<CoordinateEntity> coordinates = routeResult.coordinates().stream()
                .map(coordinate -> new CoordinateEntity(
                        coordinate.latitude(),
                        coordinate.longitude(),
                        coordinate.position()))
                .toList();

        RideEntity ride = RideMapper.toEntity(
                request,
                coordinates,
                routeResult.distanceKm(),
                routeResult.durationMinutes());

        rideRepository.save(ride);

        return RideMapper.toDTO(ride);
    }
}
