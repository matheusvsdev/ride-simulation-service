package com.matheusvsdev.ridesimulationservice.client;

import com.matheusvsdev.ridesimulationservice.client.dto.RouteClientResult;
import com.matheusvsdev.ridesimulationservice.dto.RideRequestDTO;

public interface RouteClient {
    RouteClientResult getRoute(RideRequestDTO request);
}
