package com.matheusvsdev.ridesimulationservice.entity;

import com.matheusvsdev.ridesimulationservice.entity.enums.RideStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_ride")
@Getter
@Setter
@NoArgsConstructor
public class RideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double originLatitude;
    private Double originLongitude;

    private Double destinationLatitude;
    private Double destinationLongitude;

    private Double distanceKm;
    private Double durationMinutes;

    private Double currentFuelLiters;
    private Double litersToRefuel;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private Instant startedAt;
    private Instant finishedAt;

    private Integer currentPositionInRoute = 0;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ride_id")
    private List<CoordinateEntity> routePoints = new ArrayList<>();

    public RideEntity(Double originLatitude,
                      Double originLongitude,
                      Double destinationLatitude,
                      Double destinationLongitude,
                      Double distanceKm,
                      Double durationMinutes,
                      Double currentFuelLiters,
                      Double litersToRefuel,
                      RideStatus status,
                      Instant startedAt,
                      VehicleEntity vehicle,
                      List<CoordinateEntity> routePoints) {
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
        this.currentFuelLiters = currentFuelLiters;
        this.litersToRefuel = litersToRefuel;
        this.status = status;
        this.startedAt = startedAt;
        this.vehicle = vehicle;
        this.routePoints = routePoints;
    }
}
