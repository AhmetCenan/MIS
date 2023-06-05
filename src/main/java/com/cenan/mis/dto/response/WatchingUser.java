package com.cenan.mis.dto.response;

import com.cenan.mis.model.GeoFence;

import java.util.Set;

public record WatchingUser(Long id, String username, double lat, double lon, Set<GeoFence> geoFence) {
}
