package com.huukhoa.backend.member.repository;

import com.huukhoa.backend.member.entity.Member;
import com.huukhoa.backend.common.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUser_Id(String userId);
    long countByStatus(MemberStatus status);
}
