package cz.martinkostelecky.springbootsecurity.config;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;



@Service
public class JwtService {

    private static final String SECRET_KEY = "";

    public String extractUsername(String token) {
        return null;
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
