package com.huukhoa.backend.admin.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsResponse {
    long totalBooks;
    long totalMembers;
    long activeBorrows;
    long overdueBorrows;
}
