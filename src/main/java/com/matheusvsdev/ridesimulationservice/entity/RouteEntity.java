package com.matheusvsdev.ridesimulationservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_route")
@Getter
@NoArgsConstructor
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String summary;
    private Double distanceKm;
    private Double durationMinutes;
    private Instant createdAt;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoordinateEntity> coordinates = new ArrayList<>();

    public RouteEntity(String summary, Double distanceKm, Double durationMinutes, Instant createdAt) {
        this.summary = summary;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
        this.createdAt = createdAt;
    }

    public void addCoordinate(CoordinateEntity coordinate) {
        coordinate.setRoute(this); // garante a ligação bidirecional
        this.coordinates.add(coordinate);
    }

    public void removeCoordinate(CoordinateEntity coordinate) {
        this.coordinates.remove(coordinate);
        coordinate.setRoute(null); // quebra o vínculo bidirecional
    }
}
