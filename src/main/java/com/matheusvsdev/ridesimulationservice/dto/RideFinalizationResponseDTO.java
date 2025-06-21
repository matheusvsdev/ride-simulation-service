package com.matheusvsdev.ridesimulationservice.dto;

import com.matheusvsdev.ridesimulationservice.entity.enums.RideStatus;

import java.time.Instant;

public record RideFinalizationResponseDTO(
        Long rideId,
        RideStatus status,
        Double distanceTraveled,
        Double fuelRemaining,
        Instant startedAt,
        Instant finishedAt
) {
}
