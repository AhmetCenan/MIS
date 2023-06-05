package com.cenan.mis.repository;

import com.cenan.mis.model.User;
import com.cenan.mis.model.UserWatcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserWatcherRepository extends JpaRepository<UserWatcher, Long> {
    Optional<UserWatcher> findByWatcher(User watcher);
}