package com.matheusvsdev.ridesimulationservice.client.impl;

import com.matheusvsdev.ridesimulationservice.client.RouteClient;
import com.matheusvsdev.ridesimulationservice.client.dto.RouteClientResult;
import com.matheusvsdev.ridesimulationservice.dto.CoordinateDTO;
import com.matheusvsdev.ridesimulationservice.dto.RideRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RouteClientImpl implements RouteClient {

    private final WebClient webClient;

    public RouteClientImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public RouteClientResult getRoute(RideRequestDTO request) {
        Map<String, Object> body = Map.of(
                "coordinates", List.of(
                        List.of(request.originLongitude(), request.originLatitude()),
                        List.of(request.destinationLongitude(), request.destinationLatitude())
                )
        );

        // 2. Chama a API com WebClient, passando o Map como body
        Map response = webClient.post()
                .uri("/v2/directions/driving-car/geojson")
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> features = ((List<Map<String, Object>>) response.get("features")).get(0);
        Map<String, Object> properties = (Map<String, Object>) features.get("properties");
        Map<String, Object> summary = (Map<String, Object>) properties.get("summary");

        double distanceKm = roundToTwoDecimals(((Number) summary.get("distance")).doubleValue() / 1000.0);
        double durationMin = roundToTwoDecimals(((Number) summary.get("duration")).doubleValue() / 60.0);

        Map<String, Object> geometry = (Map<String, Object>) features.get("geometry");
        List<List<Double>> coordinates = (List<List<Double>>) geometry.get("coordinates");

        List<CoordinateDTO> routePoints = new ArrayList<>();
        for (int i = 0; i < coordinates.size(); i++) {
            List<Double> point = coordinates.get(i);
            routePoints.add(new CoordinateDTO(point.get(1), point.get(0), i));
        }

        return new RouteClientResult(routePoints, distanceKm, durationMin);
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
