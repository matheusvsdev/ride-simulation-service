package com.matheusvsdev.ridesimulationservice.entity;

import com.matheusvsdev.ridesimulationservice.entity.enums.RideStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_ride")
@Getter
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

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private Instant startedAt;
    private Instant finishedAt;

    private Integer currentPositionInRoute = 0;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ride_id")
    private List<CoordinateEntity> routes = new ArrayList<>();

    public RideEntity(Double originLatitude,
                      Double originLongitude,
                      Double destinationLatitude,
                      Double destinationLongitude,
                      Double distanceKm,
                      Double durationMinutes,
                      RideStatus status,
                      Instant startedAt,
                      List<CoordinateEntity> routes) {
        this.originLatitude = originLatitude;
        this.originLongitude = originLongitude;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
        this.status = status;
        this.startedAt = startedAt;
        this.routes = routes;
        this.currentPositionInRoute = 0;
    }

    public void advancePosition() {
        if (currentPositionInRoute < routes.size() - 1) {
            currentPositionInRoute++;
        }
    }
}
