package com.matheusvsdev.ridesimulationservice.util;

import com.matheusvsdev.ridesimulationservice.dto.CoordinateDTO;
import com.matheusvsdev.ridesimulationservice.dto.RideRequestDTO;
import com.matheusvsdev.ridesimulationservice.dto.RideResponseDTO;
import com.matheusvsdev.ridesimulationservice.entity.CoordinateEntity;
import com.matheusvsdev.ridesimulationservice.entity.RideEntity;
import com.matheusvsdev.ridesimulationservice.entity.VehicleEntity;
import com.matheusvsdev.ridesimulationservice.entity.enums.RideStatus;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class RideMapper {

    public static RideEntity toEntity(
            RideRequestDTO dto,
            VehicleEntity vehicle,
            List<CoordinateEntity> routePoints,
            double distanceKm,
            double durationMinutes,
            double litersToRefuel
    ) {
        return new RideEntity(
                dto.originLatitude(),
                dto.originLongitude(),
                dto.destinationLatitude(),
                dto.destinationLongitude(),
                distanceKm,
                durationMinutes,
                dto.currentFuelLiters(),
                litersToRefuel,
                RideStatus.PENDING,
                Instant.now(),
                vehicle,
                routePoints
        );
    }

    public static RideResponseDTO toDTO(RideEntity entity) {
        List<CoordinateDTO> coordinates = entity.getRoutePoints().stream()
                .map(coordinate -> new CoordinateDTO(
                        coordinate.getLatitude(),
                        coordinate.getLongitude(),
                        coordinate.getPositionInRoute()
                )).collect(Collectors.toList());

        double progressPercentage = 0.0;
        if (entity.getRoutePoints() != null && entity.getRoutePoints().size() > 1) {
            int total = entity.getRoutePoints().size() - 1;
            progressPercentage = (entity.getCurrentPositionInRoute() * 100.0) / total;
        }

        return new RideResponseDTO(
                entity.getId(),
                entity.getOriginLatitude(),
                entity.getOriginLongitude(),
                entity.getDestinationLatitude(),
                entity.getDestinationLongitude(),
                entity.getDistanceKm(),
                entity.getDurationMinutes(),
                entity.getCurrentFuelLiters(),
                entity.getLitersToRefuel(),
                entity.getVehicle().getModel(),
                entity.getStatus(),
                progressPercentage,
                coordinates
        );
    }
}
