package com.huukhoa.backend.member.entity;

import com.huukhoa.backend.common.enums.MemberStatus;
import com.huukhoa.backend.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @Column(name = "member_code", unique = true, nullable = false)
    String memberCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    MemberStatus status = MemberStatus.ACTIVE;

    @Column(name = "join_date", nullable = false)
    LocalDate joinDate;
}
