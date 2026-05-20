package com.huukhoa.backend.member.mapper;

import com.huukhoa.backend.member.dto.response.MemberResponse;
import com.huukhoa.backend.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.fullname", target = "fullname")
    MemberResponse toMemberResponse(Member member);
}
