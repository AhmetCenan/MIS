package com.cenan.mis.controller;

import com.cenan.mis.dto.request.GeoFenceRequest;
import com.cenan.mis.dto.request.LatLon;
import com.cenan.mis.dto.request.LoginRequest;
import com.cenan.mis.dto.response.GeoFenceResponse;
import com.cenan.mis.dto.response.LoginResponse;
import com.cenan.mis.dto.response.WatchingUser;
import com.cenan.mis.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @PostMapping("/signup")
    public LoginResponse signUp(@RequestBody LoginRequest loginRequest) {
        return userService.signUp(loginRequest);
    }

    @PostMapping("/set-current-position")
    public Set<GeoFenceResponse> setCurrentPosition(@RequestBody LatLon latLon, Principal principal) {
        return userService.setCurrentPosition(latLon, principal);
    }

    @PostMapping("/watch")
    public void watchUser(@RequestParam String userName, Principal principal) {
        userService.watch(userName, principal);
    }

    @GetMapping("/get-watching-users")
    public Set<WatchingUser> getWatchingUsers(Principal principal) {
        return userService.getWatchingUsers(principal);
    }

    @PostMapping("/create-geofence")
    public GeoFenceRequest createGeoFenceRule(@RequestBody GeoFenceRequest geoFenceRequest, Principal principal) {
        return userService.createGeoFenceRule(geoFenceRequest, principal);
    }

    @GetMapping("/get-geofence")
    public Set<GeoFenceRequest> getGeoFenceRule(Principal principal) {
        return userService.getGeoFenceRule(principal);
    }

}
