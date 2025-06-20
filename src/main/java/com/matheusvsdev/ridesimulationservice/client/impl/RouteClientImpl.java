package com.matheusvsdev.ridesimulationservice.client.impl;

import com.matheusvsdev.ridesimulationservice.client.RouteClient;
import com.matheusvsdev.ridesimulationservice.client.dto.CoordinatePointDTO;
import com.matheusvsdev.ridesimulationservice.client.dto.RouteResult;
import com.matheusvsdev.ridesimulationservice.dto.RideRequestDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteClientImpl implements RouteClient {

    @Override
    public RouteResult getRoute(RideRequestDTO request) {
        List<CoordinatePointDTO> fakeRoute = List.of(
                new CoordinatePointDTO(request.originLatitude(), request.originLongitude(), 0),
                new CoordinatePointDTO(
                        (request.originLatitude() + request.destinationLatitude()) / 2,
                        (request.originLongitude() + request.destinationLongitude()) / 2,
                        1),
                new CoordinatePointDTO(request.destinationLatitude(), request.destinationLongitude(), 2)
        );

        return new RouteResult(fakeRoute, 12.5, 18.0);
    }
}
