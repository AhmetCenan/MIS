package com.cenan.mis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "notification_send")
@NoArgsConstructor
public class NotificationSend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private GeoFence geoFence;

    @ManyToOne
    private User user;

    public NotificationSend(GeoFence geoFence, User user) {
        this.geoFence = geoFence;
        this.user = user;
    }

}