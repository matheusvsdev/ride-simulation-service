package com.matheusvsdev.ridesimulationservice.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_coordinate")
@Getter
@NoArgsConstructor
public class CoordinateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;
    private Integer positionInRoute;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private RideEntity ride;

    public CoordinateEntity(Double latitude,
                            Double longitude,
                            Integer positionInRoute) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.positionInRoute = positionInRoute;
    }
}
