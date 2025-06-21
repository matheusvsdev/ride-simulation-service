package com.matheusvsdev.ridesimulationservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_vehicle")
@Getter
@NoArgsConstructor
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private Double fuelTankCapacity;
    private Double fuelEfficiencyKmPerLiter;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<RideEntity> rides = new ArrayList<>();
}
