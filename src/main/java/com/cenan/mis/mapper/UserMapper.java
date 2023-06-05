package com.cenan.mis.mapper;

import com.cenan.mis.dto.response.WatchingUser;
import com.cenan.mis.model.User;
import org.mapstruct.*;

import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    WatchingUser toDto(User user);

    Set<WatchingUser> toDto(Set<User> user);
}