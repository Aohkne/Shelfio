package com.huukhoa.backend.member.dto.request;

import com.huukhoa.backend.common.enums.MemberStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateMemberStatusRequest {

    @NotNull(message = "Status is required")
    MemberStatus status;
}
