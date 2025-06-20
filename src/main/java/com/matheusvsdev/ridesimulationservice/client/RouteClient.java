package com.matheusvsdev.ridesimulationservice.client;

import com.matheusvsdev.ridesimulationservice.client.dto.RouteResult;
import com.matheusvsdev.ridesimulationservice.dto.RideRequestDTO;

public interface RouteClient {
    RouteResult getRoute(RideRequestDTO request);
}
