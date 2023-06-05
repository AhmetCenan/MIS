package com.cenan.mis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserWatcher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User watcher;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<User> monitoredUsers = new HashSet<>();

    public UserWatcher(User watcher) {
        this.watcher = watcher;
    }

    public void addMonitoringUser(User user) {
        this.monitoredUsers.add(user);
    }

}
