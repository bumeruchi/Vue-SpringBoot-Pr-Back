package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;  // <-- Spring의 @Value
import org.springframework.stereotype.Component;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;


@Slf4j
@Component
public class JWTUtil {

    @Value("${jwt.secret}")   // Spring @Value
    private String secret;

    public String createToken(String userId, String userName) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("vue-board")
                .withClaim("userId", userId)
                .withClaim("userName", userName)
                .withIssuedAt(new Date())
                .sign(algorithm);
    }

    public DecodedJWT decodeToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("vue-board")
                    .build();
            return verifier.verify(token);

        } catch (JWTVerificationException e) {
            log.error("JWTVerificationException: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return null;
    }
}
