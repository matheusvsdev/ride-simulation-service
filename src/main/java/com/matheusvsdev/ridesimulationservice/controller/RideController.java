package com.matheusvsdev.ridesimulationservice.controller;

import com.matheusvsdev.ridesimulationservice.dto.*;
import com.matheusvsdev.ridesimulationservice.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ride")
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping("/simulation")
    public ResponseEntity<RideResponseDTO> simulateRide(@RequestBody RideRequestDTO request) {
        RideResponseDTO response = rideService.simulateRide(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/start")
    public ResponseEntity<RideResponseDTO> startRide(@PathVariable Long id) {
        RideResponseDTO response = rideService.startRide(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<?> progressRide(@PathVariable Long id, @RequestBody RideProgressRequestDTO request) {
        Object response = rideService.progressRide(id, request.positionInRoute());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<RideFinalizationResponseDTO> finishRide(@PathVariable Long id) {
        RideFinalizationResponseDTO response = rideService.finishRide(id);
        return ResponseEntity.ok(response);
    }
}
