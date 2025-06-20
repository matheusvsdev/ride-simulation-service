package com.matheusvsdev.ridesimulationservice.util;

import com.matheusvsdev.ridesimulationservice.dto.CoordinateDTO;
import com.matheusvsdev.ridesimulationservice.dto.RideRequestDTO;
import com.matheusvsdev.ridesimulationservice.dto.RideResponseDTO;
import com.matheusvsdev.ridesimulationservice.entity.CoordinateEntity;
import com.matheusvsdev.ridesimulationservice.entity.RideEntity;
import com.matheusvsdev.ridesimulationservice.entity.enums.RideStatus;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class RideMapper {

    public static RideEntity toEntity(RideRequestDTO dto, List<CoordinateEntity> routes, double distanceKm, double durationMinutes) {
        return new RideEntity(
                dto.originLatitude(),
                dto.originLongitude(),
                dto.destinationLatitude(),
                dto.destinationLongitude(),
                distanceKm,
                durationMinutes,
                RideStatus.INICIADA,
                Instant.now(),
                routes
        );
    }

    public static RideResponseDTO toDTO(RideEntity entity) {
        List<CoordinateDTO> coordinates = entity.getRoutes().stream()
                .map(CoordinateDTO::new)
                .collect(Collectors.toList());

        return new RideResponseDTO(
                entity.getId(),
                entity.getOriginLatitude(),
                entity.getOriginLongitude(),
                entity.getDestinationLatitude(),
                entity.getDestinationLongitude(),
                entity.getDistanceKm(),
                entity.getDurationMinutes(),
                entity.getStatus().name(),
                entity.getCurrentPositionInRoute(),
                coordinates
        );
    }
}
