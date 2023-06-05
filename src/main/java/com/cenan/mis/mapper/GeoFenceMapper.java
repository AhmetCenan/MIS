package com.cenan.mis.mapper;

import com.cenan.mis.dto.request.GeoFenceRequest;
import com.cenan.mis.model.GeoFence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GeoFenceMapper {
    @Mapping(target = "name", source = "geoFenceName")
    GeoFence toEntity(GeoFenceRequest geoFenceRequest);

    @Mapping(target = "geoFenceName", source = "name")
    GeoFenceRequest toDto(GeoFence geoFence);

    Set<GeoFenceRequest> toDto(Set<GeoFence> geoFence);
}