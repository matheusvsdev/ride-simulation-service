package com.matheusvsdev.ridesimulationservice.client.dto;

import java.util.List;

public record RouteResult(
        List<CoordinatePointDTO> coordinates,
        double distanceKm,
        double durationMinutes
) {
}
