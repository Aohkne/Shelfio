package com.huukhoa.backend.service;

import com.huukhoa.backend.dto.request.*;
import com.huukhoa.backend.dto.response.AuthenticationResponse;
import com.huukhoa.backend.dto.response.IntrospectResponse;
import com.huukhoa.backend.entity.InvalidatedToken;
import com.huukhoa.backend.entity.User;
import com.huukhoa.backend.enums.ErrorCode;
import com.huukhoa.backend.enums.Role;
import com.huukhoa.backend.exception.CustomException;
import com.huukhoa.backend.mapper.UserMapper;
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
    InvalidatedTokenRepository invalidatedTokenRepository;

    JwtUtil jwtUtil;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public AuthenticationResponse login(AuthenticationRequest request) {
        // Find user
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // Verify password
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }

        // Generate tokens
        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accesstoken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    public User register(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(ErrorCode.USER_USERNAME_ALREADY_EXISTS);
        }
        User user = userMapper.toUser(request);

        // PASSWORD
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ROLE
        var roles = roleRepository.findAllById(List.of(Role.USER.name()));
        user.setRoles(new HashSet<>(roles));

        return userRepository.save(user);
    }

    public IntrospectResponse verifyToken(IntrospectRequest request) {
        return jwtUtil.introspectToken(request);
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) {
        try {
            // === VERIFY REFRESH TOKEN ===
            IntrospectRequest introspectRequest = IntrospectRequest.builder()
                    .token(request.getRefreshtoken())
                    .build();

            var introspectResponse = verifyToken(introspectRequest);

            if (!introspectResponse.isValid()) {
                throw new CustomException(ErrorCode.UNAUTHENTICATED);
            }

            // Check refresh token
            String tokenType = jwtUtil.extractTokenType(request.getRefreshtoken());
            if (!"REFRESH".equals(tokenType)) {
                throw new CustomException(ErrorCode.INVALID_TOKEN_TYPE);
            }

            // Invalidate refresh token cũ
            String jti = jwtUtil.extractJti(request.getRefreshtoken());
            Date expiryTime = jwtUtil.extractExpiration(request.getRefreshtoken());

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);

            // === GET USER ===
            var username = jwtUtil.extractUsername(request.getRefreshtoken());

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            // === GENERATE NEW TOKENS ===
            String newAccessToken = jwtUtil.generateToken(user);
            String newRefreshToken = jwtUtil.generateRefreshToken(user);

            return AuthenticationResponse.builder()
                    .accesstoken(newAccessToken)
                    .refreshToken(newRefreshToken)
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
        try {
            // Verify token
            IntrospectRequest introspectRequest = IntrospectRequest.builder()
                    .token(request.getToken())
                    .build();

            var introspectResponse = verifyToken(introspectRequest);

            if (!introspectResponse.isValid()) {
                throw new CustomException(ErrorCode.UNAUTHENTICATED);
            }

            // Invalidate token
            String jti = jwtUtil.extractJti(request.getToken());
            Date expiryTime = jwtUtil.extractExpiration(request.getToken());

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
    }
}