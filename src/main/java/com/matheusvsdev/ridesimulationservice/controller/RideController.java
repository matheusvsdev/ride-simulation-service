package com.matheusvsdev.ridesimulationservice.controller;

import com.matheusvsdev.ridesimulationservice.dto.RideRequestDTO;
import com.matheusvsdev.ridesimulationservice.dto.RideResponseDTO;
import com.matheusvsdev.ridesimulationservice.service.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ride")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/simulation")
    public ResponseEntity<RideResponseDTO> simulate(@RequestBody RideRequestDTO request) {
        RideResponseDTO response = rideService.simulateRide(request);
        return ResponseEntity.ok(response);
    }
}
