package com.cenan.mis.repository;

import com.cenan.mis.model.GeoFence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeoFenceRepository extends JpaRepository<GeoFence, Long> {
}