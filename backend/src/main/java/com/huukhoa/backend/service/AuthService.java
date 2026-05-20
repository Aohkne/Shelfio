package com.huukhoa.backend.service;

import com.huukhoa.backend.common.enums.MemberStatus;
import com.huukhoa.backend.dto.request.*;
import com.huukhoa.backend.dto.response.AuthenticationResponse;
import com.huukhoa.backend.dto.response.IntrospectResponse;
import com.huukhoa.backend.dto.response.RegisterResponse;
import com.huukhoa.backend.entity.InvalidatedToken;
import com.huukhoa.backend.entity.User;
import com.huukhoa.backend.enums.ErrorCode;
import com.huukhoa.backend.enums.Role;
import com.huukhoa.backend.exception.CustomException;
import com.huukhoa.backend.mapper.UserMapper;
import com.huukhoa.backend.member.entity.Member;
import com.huukhoa.backend.member.repository.MemberRepository;
import com.huukhoa.backend.repository.InvalidatedTokenRepository;
import com.huukhoa.backend.repository.RoleRepository;
import com.huukhoa.backend.repository.UserRepository;
import com.huukhoa.backend.util.JwtUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    MemberRepository memberRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    JwtUtil jwtUtil;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public AuthenticationResponse login(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getIdentifier())
                .orElseGet(() -> userRepository.findByUsername(request.getIdentifier())
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }

        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accesstoken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    @Transactional
    public RegisterResponse register(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_EXISTS);
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.USER_USERNAME_ALREADY_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(List.of(Role.MEMBER.name()));
        user.setRoles(new HashSet<>(roles));

        User savedUser = userRepository.save(user);

        String memberCode = generateMemberCode();
        Member member = Member.builder()
                .user(savedUser)
                .memberCode(memberCode)
                .status(MemberStatus.ACTIVE)
                .joinDate(LocalDate.now())
                .build();
        Member savedMember = memberRepository.save(member);

        return RegisterResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .fullname(savedUser.getFullname())
                .memberCode(savedMember.getMemberCode())
                .memberStatus(savedMember.getStatus())
                .joinDate(savedMember.getJoinDate())
                .build();
    }

    public IntrospectResponse verifyToken(IntrospectRequest request) {
        return jwtUtil.introspectToken(request);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) {
        try {
            String tokenType = jwtUtil.extractTokenType(request.getRefreshtoken());
            if (!"REFRESH".equals(tokenType)) {
                throw new CustomException(ErrorCode.INVALID_TOKEN_TYPE);
            }

            String jti = jwtUtil.extractJti(request.getRefreshtoken());
            if (invalidatedTokenRepository.existsById(jti)) {
                throw new CustomException(ErrorCode.UNAUTHENTICATED);
            }

            Date expiry = jwtUtil.extractExpiration(request.getRefreshtoken());
            if (expiry.before(new Date())) {
                throw new CustomException(ErrorCode.UNAUTHENTICATED);
            }

            String username = jwtUtil.extractUsername(request.getRefreshtoken());
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            String newAccessToken = jwtUtil.generateToken(user);

            return AuthenticationResponse.builder()
                    .accesstoken(newAccessToken)
                    .refreshToken(request.getRefreshtoken())
                    .authenticated(true)
                    .build();

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Refresh token failed: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public void logout(LogoutRequest request) {
        invalidateToken(request.getAccessToken());
        invalidateToken(request.getRefreshToken());
    }

    private void invalidateToken(String token) {
        if (token == null || token.isBlank()) return;
        try {
            String jti = jwtUtil.extractJti(token);
            Date expiryTime = jwtUtil.extractExpiration(token);
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expiryTime(expiryTime)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        } catch (Exception e) {
            log.warn("Failed to invalidate token: {}", e.getMessage());
        }
    }

    private String generateMemberCode() {
        return "MEM-" + String.format("%04d", (int) (Math.random() * 10000));
    }
}