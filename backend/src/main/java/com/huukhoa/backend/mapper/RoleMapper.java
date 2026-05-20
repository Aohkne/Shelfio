package com.huukhoa.backend.mapper;


import com.huukhoa.backend.dto.request.RoleCreationRequest;
import com.huukhoa.backend.dto.response.RoleResponse;
import com.huukhoa.backend.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleCreationRequest request);

    RoleResponse toRoleResponse(Role role);
}
