package com.matheusvsdev.ridesimulationservice.client.dto;

public record CoordinatePointDTO(
        double latitude,
        double longitude,
        int position
) {
}
