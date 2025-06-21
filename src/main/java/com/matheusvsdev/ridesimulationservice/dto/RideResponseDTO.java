package com.matheusvsdev.ridesimulationservice.dto;

import com.matheusvsdev.ridesimulationservice.entity.enums.RideStatus;

import java.util.List;

public record RideResponseDTO(
        Long id,
        Double originLatitude,
        Double originLongitude,
        Double destinationLatitude,
        Double destinationLongitude,
        Double distanceKm,
        Double durationMinutes,
        Double currentFuelLiters,
        Double litersToRefuel,
        String vehicleModel,
        RideStatus status,
        Double progressPercentage,
        List<CoordinateDTO> route
) {}
