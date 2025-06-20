package com.matheusvsdev.ridesimulationservice.repository;

import com.matheusvsdev.ridesimulationservice.entity.CoordinateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoordinateRepository extends JpaRepository<CoordinateEntity, Long> {
}
