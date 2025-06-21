package com.matheusvsdev.ridesimulationservice.service;

import com.matheusvsdev.ridesimulationservice.client.RouteClient;
import com.matheusvsdev.ridesimulationservice.client.dto.RouteClientResult;
import com.matheusvsdev.ridesimulationservice.dto.RideRequestDTO;
import com.matheusvsdev.ridesimulationservice.entity.CoordinateEntity;
import com.matheusvsdev.ridesimulationservice.util.RouteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoordinateService {

    @Autowired
    private RouteClient routeClient;

    public RouteResult getRoute(RideRequestDTO dto) {

        // 1. Consulta a API externa via client
        RouteClientResult result = routeClient.getRoute(dto);

        // Converte DTOs em entidades
        List<CoordinateEntity> entities = result.coordinates().stream()
                .map(coordinates -> new CoordinateEntity(
                        coordinates.latitude(),
                        coordinates.longitude(),
                        coordinates.positionInRoute()
                )).toList();

        return new RouteResult(entities, result.distanceKm(), result.durationMinutes());
    }
}
