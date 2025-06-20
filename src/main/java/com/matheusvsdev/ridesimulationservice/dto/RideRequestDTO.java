package com.matheusvsdev.ridesimulationservice.dto;

public record RideRequestDTO(
        Double originLatitude,
        Double originLongitude,
        Double destinationLatitude,
        Double destinationLongitude
) {}
