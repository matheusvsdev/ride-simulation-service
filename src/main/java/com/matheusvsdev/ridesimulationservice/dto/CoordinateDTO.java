package com.matheusvsdev.ridesimulationservice.dto;

import com.matheusvsdev.ridesimulationservice.entity.CoordinateEntity;

public record CoordinateDTO(
        Double latitude,
        Double longitude,
        Integer positionInRoute
) {
    public CoordinateDTO(CoordinateEntity entity) {
        this(entity.getLatitude(), entity.getLongitude(), entity.getPositionInRoute());
    }
}
