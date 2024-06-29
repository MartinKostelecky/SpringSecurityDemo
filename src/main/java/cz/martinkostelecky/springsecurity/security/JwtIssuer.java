package cz.martinkostelecky.springsecurity.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtIssuer {

    public final cz.martinkostelecky.springsecurity.security.JwtProperties jwtProperties;

    public String issue(Request request) {
        var now = Instant.now();

        return JWT.create()
                .withSubject(String.valueOf(request.userId))
                .withIssuedAt(now)
                //TODO to minutes
                .withExpiresAt(now.plus(Duration.ofDays(1)))
                .withClaim("email", request.getEmail())
                //.withClaim("authorities", request.getRoles())
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey()));
    }

    //TODO
    @Getter
    @Builder
    public static class Request {
        private final Long userId;
        private final String email;
        private final List<String> roles;
    }
}
