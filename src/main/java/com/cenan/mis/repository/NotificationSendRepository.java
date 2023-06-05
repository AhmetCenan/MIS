package com.cenan.mis.repository;

import com.cenan.mis.model.GeoFence;
import com.cenan.mis.model.NotificationSend;
import com.cenan.mis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSendRepository extends JpaRepository<NotificationSend, Long> {
    Optional<NotificationSend> findByGeoFenceAndUser(GeoFence geoFence, User user);
}