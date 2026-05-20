package com.huukhoa.backend.util;

import com.huukhoa.backend.dto.request.IntrospectRequest;
import com.huukhoa.backend.dto.response.IntrospectResponse;
import com.huukhoa.backend.entity.User;
import com.huukhoa.backend.enums.ErrorCode;
import com.huukhoa.backend.exception.CustomException;
import com.huukhoa.backend.repository.InvalidatedTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class JwtUtil {
    final InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.secret}")
    String SECRET;

    @NonFinal
    @Value("${jwt.expiration}")
    Long EXPIRATION; // Access token expiration

    @NonFinal
    @Value("${jwt.refresh-duration}")
    Long REFRESH_DURATION; // Refresh token expiration

    // Generate ACCESS TOKEN
    public String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.builder()
                .subject(user.getId())
                .id(UUID.randomUUID().toString())
                .claim("username", user.getUsername())
                .claim("scope", buildScope(user))
                .claim("type", "ACCESS")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Duration.ofSeconds(EXPIRATION).toMillis()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Generate REFRESH TOKEN
    public String generateRefreshToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.builder()
                .subject(user.getId())
                .id(UUID.randomUUID().toString())
                .claim("username", user.getUsername())
                .claim("type", "REFRESH")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + Duration.ofSeconds(REFRESH_DURATION).toMillis()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Convert role to list
    public String buildScope(User user) {
        StringJoiner scopes = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                scopes.add("ROLE_" + role.getName());

                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions()
                            .forEach(permission -> {
                                scopes.add(permission.getName());
                            });
                }
            });
        }

        return scopes.toString();
    }

    // Extract JTI (JWT ID)
    public String extractJti(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getId();
    }

    // Extract Expiration
    public Date extractExpiration(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    // Extract username từ token
    public String extractUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    // Extract token type
    public String extractTokenType(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("type", String.class);
    }

    // Introspect token
    public IntrospectResponse introspectToken(IntrospectRequest request) {
        String token = request.getToken();

        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

            // Parse và validate token
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            // Check token có trong blacklist
            String jti = claims.getId();
            boolean isInBlacklist = invalidatedTokenRepository.existsById(jti);

            // Check token có hết hạn VÀ không bị blacklist
            boolean isValid = !claims.getExpiration().before(new Date()) && !isInBlacklist;

            return IntrospectResponse.builder()
                    .valid(isValid)
                    .build();

        } catch (Exception e) {
            throw new CustomException(ErrorCode.UNAUTHENTICATED);
        }
    }

    // Expired token
    public boolean isTokenExpired(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

        Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }
}