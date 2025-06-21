package com.matheusvsdev.ridesimulationservice.client.dto;

import com.matheusvsdev.ridesimulationservice.dto.CoordinateDTO;

import java.util.List;

public record RouteClientResult(
        List<CoordinateDTO> coordinates,
        Double distanceKm,
        Double durationMinutes
) {
}
