package com.huukhoa.backend.member.service;

import com.huukhoa.backend.common.exception.ResourceNotFoundException;
import com.huukhoa.backend.common.exception.UnauthorizedException;
import com.huukhoa.backend.enums.ErrorCode;
import com.huukhoa.backend.exception.CustomException;
import com.huukhoa.backend.member.dto.request.UpdateMemberStatusRequest;
import com.huukhoa.backend.member.dto.response.MemberResponse;
import com.huukhoa.backend.member.entity.Member;
import com.huukhoa.backend.member.mapper.MemberMapper;
import com.huukhoa.backend.member.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MemberService {
    MemberRepository memberRepository;
    MemberMapper memberMapper;

    public Page<MemberResponse> getMembers(Pageable pageable) {
        return memberRepository.findAll(pageable).map(memberMapper::toMemberResponse);
    }

    public MemberResponse getMyProfile() {
        String userId = getCurrentUserId();
        Member member = memberRepository.findByUser_Id(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        return memberMapper.toMemberResponse(member);
    }

    public MemberResponse getMember(UUID id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // MEMBER can only view their own profile
        String currentUserId = getCurrentUserId();
        boolean isAdmin = hasAdminOrLibrarianRole();
        if (!isAdmin && !member.getUser().getId().equals(currentUserId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return memberMapper.toMemberResponse(member);
    }

    @Transactional
    public MemberResponse updateStatus(UUID id, UpdateMemberStatusRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.setStatus(request.getStatus());
        return memberMapper.toMemberResponse(memberRepository.save(member));
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getSubject();
    }

    private boolean hasAdminOrLibrarianRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                        || a.getAuthority().equals("ROLE_LIBRARIAN"));
    }
}
