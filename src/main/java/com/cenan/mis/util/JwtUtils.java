package com.cenan.mis.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cenan.mis.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtUtils {

    @Value("${spring.security.secret}")
    private String secret;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512(secret.getBytes());
    }

    public String generateJwtToken(User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(Instant.now())
                .sign(getAlgorithm());
    }

    public String getUserNameFromJwtToken(String token) {
        return JWT.require(getAlgorithm())
                .build()
                .verify(token)
                .getSubject();
    }

}
