package com.matheusvsdev.ridesimulationservice.dto;

import com.matheusvsdev.ridesimulationservice.entity.enums.RideStatus;

public record RideProgressResponseDTO(
        Long id,
        Double currentFuelLiters,
        Double progressPercentage,
        Double estimatedTimeRemainingMinutes,
        Double estimatedDistanceRemainingKm,
        RideStatus status
) {
}
