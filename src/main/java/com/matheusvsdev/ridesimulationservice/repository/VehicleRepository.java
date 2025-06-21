package com.matheusvsdev.ridesimulationservice.repository;

import com.matheusvsdev.ridesimulationservice.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
}
