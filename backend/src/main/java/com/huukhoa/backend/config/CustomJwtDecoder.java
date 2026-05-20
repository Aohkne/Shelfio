package com.huukhoa.backend.config;

import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

import com.huukhoa.backend.enums.ErrorCode;
import com.huukhoa.backend.exception.CustomException;
import com.huukhoa.backend.util.JwtUtil;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.huukhoa.backend.dto.request.IntrospectRequest;
import com.huukhoa.backend.service.AuthService;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @NonFinal
    @Value("${jwt.secret}")
    private String SECRET;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // === CHECK TOKEN TYPE ===
            String tokenType = jwtUtil.extractTokenType(token);

            if (!"ACCESS".equals(tokenType)) {
                throw new JwtException("Invalid token type - only ACCESS tokens are allowed for API requests");
            }

            // === VERIFY TOKEN ===
            var response = authService.verifyToken(
                    IntrospectRequest.builder().token(token).build());

            if (!response.isValid()) {
                throw new JwtException("Token invalid or expired");
            }

            // === DECODE TOKEN ===
            if (Objects.isNull(nimbusJwtDecoder)) {
                SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(), "HS256");
                nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                        .macAlgorithm(MacAlgorithm.HS256)
                        .build();
            }

            return nimbusJwtDecoder.decode(token);

        } catch (CustomException e) {
            throw new JwtException("Token verification failed: " + e.getMessage());
        } catch (Exception e) {
            throw new JwtException("Token decoding failed: " + e.getMessage());
        }
    }
}