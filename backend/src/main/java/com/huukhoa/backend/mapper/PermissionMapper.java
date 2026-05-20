package com.huukhoa.backend.mapper;


import com.huukhoa.backend.dto.request.PermissionCreationRequest;
import com.huukhoa.backend.dto.request.UserCreationRequest;
import com.huukhoa.backend.dto.request.UserUpdateRequest;
import com.huukhoa.backend.dto.response.PermissionResponse;
import com.huukhoa.backend.dto.response.UserResponse;
import com.huukhoa.backend.entity.Permission;
import com.huukhoa.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionCreationRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
