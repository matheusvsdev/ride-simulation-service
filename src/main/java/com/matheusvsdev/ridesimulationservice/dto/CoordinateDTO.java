package com.matheusvsdev.ridesimulationservice.dto;

public record CoordinateDTO(
        Double latitude,
        Double longitude,
        Integer positionInRoute
) {}
