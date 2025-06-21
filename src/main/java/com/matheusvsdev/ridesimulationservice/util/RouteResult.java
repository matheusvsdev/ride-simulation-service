package com.matheusvsdev.ridesimulationservice.util;

import com.matheusvsdev.ridesimulationservice.entity.CoordinateEntity;

import java.util.List;

public record RouteResult(
        List<CoordinateEntity> coordinates,
        double distanceKm,
        double durationMinutes
) {
}
