package com.huukhoa.backend.mapper;


import com.huukhoa.backend.dto.request.UserCreationRequest;
import com.huukhoa.backend.dto.request.UserUpdateRequest;
import com.huukhoa.backend.dto.response.UserResponse;
import com.huukhoa.backend.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    //    @Mapping(source = "fullname", target = "username")
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
