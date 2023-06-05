package com.cenan.mis.dto.request;

public record GeoFenceRequest(double lat, double lon, double radius, String geoFenceName) {
}
