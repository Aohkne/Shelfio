package com.huukhoa.backend.dto.response;

import com.huukhoa.backend.common.enums.MemberStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    String id;
    String username;
    String email;
    String fullname;
    String memberCode;
    MemberStatus memberStatus;
    LocalDate joinDate;
}
