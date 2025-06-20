package com.matheusvsdev.ridesimulationservice.repository;

import com.matheusvsdev.ridesimulationservice.entity.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<RideEntity, Long> {
}
