package com.huukhoa.backend.member.dto.response;

import com.huukhoa.backend.common.enums.MemberStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberResponse {
    UUID id;
    String memberCode;
    MemberStatus status;
    LocalDate joinDate;
    // User info
    String userId;
    String username;
    String email;
    String fullname;
}
