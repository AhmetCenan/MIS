package com.cenan.mis.service;

import com.cenan.mis.dto.request.GeoFenceRequest;
import com.cenan.mis.dto.request.LatLon;
import com.cenan.mis.dto.request.LoginRequest;
import com.cenan.mis.dto.response.GeoFenceResponse;
import com.cenan.mis.dto.response.LoginResponse;
import com.cenan.mis.dto.response.WatchingUser;
import com.cenan.mis.enums.Role;
import com.cenan.mis.mapper.GeoFenceMapper;
import com.cenan.mis.mapper.UserMapper;
import com.cenan.mis.model.GeoFence;
import com.cenan.mis.model.NotificationSend;
import com.cenan.mis.model.User;
import com.cenan.mis.model.UserWatcher;
import com.cenan.mis.repository.GeoFenceRepository;
import com.cenan.mis.repository.NotificationSendRepository;
import com.cenan.mis.repository.UserRepository;
import com.cenan.mis.repository.UserWatcherRepository;
import com.cenan.mis.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserWatcherRepository userWatcherRepository;
    private final UserMapper userMapper;
    private final GeoFenceMapper geoFenceMapper;
    private final NotificationSendRepository notificationSendRepository;
    private final GeoFenceRepository geoFenceRepository;

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.userName(), loginRequest.password()));
        if (authenticate.isAuthenticated()) {
            var user = getByUserName(loginRequest.userName());
            return getLoginResponse(user);
        }
        return null;
    }

    public LoginResponse signUp(LoginRequest loginRequest) {
        User user = new User();
        user.setUserName(loginRequest.userName());
        user.setPassword(passwordEncoder.encode(loginRequest.password()));
        user.setRole(Role.USER);
        userRepository.save(user);
        userWatcherRepository.save(new UserWatcher(user));
        return getLoginResponse(user);
    }

    private LoginResponse getLoginResponse(User user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserName(user.getUsername());
        loginResponse.setRole(user.getRole());
        loginResponse.setToken(jwtUtils.generateJwtToken(user));
        return loginResponse;
    }

    public User getByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow();
    }

    public Set<GeoFenceResponse> setCurrentPosition(LatLon latLon, Principal principal) {
        User user = getByUserName(principal.getName());
        user.setLat(latLon.lat());
        user.setLon(latLon.lon());
        userRepository.save(user);


        Optional<UserWatcher> opt = userWatcherRepository.findByWatcher(user);
        Set<GeoFenceResponse> geoFenceResponse = new HashSet<>();
        if (opt.isPresent()) {
            UserWatcher userWatcher = opt.get();
            Set<User> monitoredUsers = userWatcher.getMonitoredUsers();
            for (GeoFence geoFence : user.getGeoFence()) {
                for (User monitoredUser : monitoredUsers) {
                    double distance = distance(geoFence.getLat(), monitoredUser.getLat(), geoFence.getLon(), monitoredUser.getLon());
                    Optional<NotificationSend> optNotification = notificationSendRepository.findByGeoFenceAndUser(geoFence, monitoredUser);
                    if (distance <= geoFence.getRadius()) {
                        if (optNotification.isEmpty()) {
                            geoFenceResponse.add(new GeoFenceResponse(monitoredUser.getUsername(), geoFence.getName()));
                            notificationSendRepository.save(new NotificationSend(geoFence, monitoredUser));
                        }
                    } else {
                        optNotification.ifPresent(notificationSendRepository::delete);
                    }
                }
            }
        }
        return geoFenceResponse;
    }

    public void watch(String userName, Principal principal) {
        User currentUser = getByUserName(principal.getName());
        User watchingUser = getByUserName(userName);
        Optional<UserWatcher> optWatcher = userWatcherRepository.findByWatcher(currentUser);
        if (optWatcher.isEmpty()) {
            throw new RuntimeException("watcher not exist");
        }
        UserWatcher userWatcher = optWatcher.get();
        userWatcher.addMonitoringUser(watchingUser);
        userWatcherRepository.save(userWatcher);

    }

    public Set<WatchingUser> getWatchingUsers(Principal principal) {
        User currentUser = getByUserName(principal.getName());
        Optional<UserWatcher> optWatcher = userWatcherRepository.findByWatcher(currentUser);
        if (optWatcher.isEmpty()) {
            throw new RuntimeException("watcher not exist");
        }
        UserWatcher userWatcher = optWatcher.get();
        return userMapper.toDto(userWatcher.getMonitoredUsers());
    }

    public GeoFenceRequest createGeoFenceRule(GeoFenceRequest geoFenceRequest, Principal principal) {
        User currentUser = getByUserName(principal.getName());
        GeoFence geoFence = geoFenceMapper.toEntity(geoFenceRequest);
        geoFenceRepository.save(geoFence);
        currentUser.getGeoFence().add(geoFence);
        userRepository.save(currentUser);
        return geoFenceRequest;
    }

    public Set<GeoFenceRequest> getGeoFenceRule(Principal principal) {
        User currentUser = getByUserName(principal.getName());
        return geoFenceMapper.toDto(currentUser.getGeoFence());
    }

    public double distance(double geoFenceLat, double userLat, double geoFenceLatLon, double userLon) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(userLat - geoFenceLat);
        double lonDistance = Math.toRadians(userLon - geoFenceLatLon);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(geoFenceLat))
                * Math.cos(Math.toRadians(userLat)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
}
