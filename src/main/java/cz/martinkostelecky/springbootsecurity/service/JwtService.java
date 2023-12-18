package cz.martinkostelecky.springbootsecurity.service;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    //minimum for JWT tokens is 256 bits
    private static final String SECRET_KEY = "4a01facb75275b656bafba8c76cb63e776ebf9c3ae31b516aaf01ba2a6078ccd";

    /**
     * method to extract unique User detail which is email
     * @param token
     * @return extracts User´s email
     */
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        //object of Claim extracted from passed token
        final Claims claims = extractAllClaims(token);
        //returns any claim or claims from passed token
        return claimsResolver.apply(claims);
    }

    /**
     * method for generation a token only from some user detail
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                //use of Spring method .getUsername() to get unique User detail which is email
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //expiration set for 24 hours
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 24)))
                .signWith(getSignInKey(),SignatureAlgorithm.HS256)
                //will generate and return the token
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        //"HMAC" stands for "Hash-based Message Authentication Code." It is a specific type of
        // message authentication code (MAC) involving a cryptographic hash function and a secret cryptographic key.
        // HMACs are used for data integrity and authentication in various security protocols and applications.
        //
        //The HMAC construction involves a hash function (such as SHA-256 or SHA-3) and a secret key.
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
