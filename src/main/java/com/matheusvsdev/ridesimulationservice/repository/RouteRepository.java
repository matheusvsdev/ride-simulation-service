package com.matheusvsdev.ridesimulationservice.repository;

import com.matheusvsdev.ridesimulationservice.entity.RouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, Long> {
}
