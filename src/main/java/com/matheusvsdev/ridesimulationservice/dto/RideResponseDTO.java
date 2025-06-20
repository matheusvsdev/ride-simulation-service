package com.matheusvsdev.ridesimulationservice.dto;

import java.util.List;

public record RideResponseDTO(
        Long id,
        Double originLatitude,
        Double originLongitude,
        Double destinationLatitude,
        Double destinationLongitude,
        Double distanceKm,
        Double durationMinutes,
        String status,
        Integer currentPositionInRoute,
        List<CoordinateDTO> route
) {}
