package net.engineeringdigest.journalApp.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "abcdefghijklmnopqrstuvwxyz123456";

    public String generateToken(String username) {
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(HashMap claims, String username) {
        String jwt = Jwts.builder()
                .claims(claims)
                .subject(username)
                .header().empty().add("typ", "JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(getSigninKey())
                .compact();
        return jwt;
    }

    private SecretKey getSigninKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String jwt) {
        Claims claims = extractAllClaims(jwt);
        return claims.getSubject();
    }

    private Claims extractAllClaims(String jwt) {
        Claims payload = Jwts.parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return payload;
    }

    public boolean validateToken(String jwt) {
        Date date = extractExpiration(jwt);
        return !date.before(new Date());
    }

    private Date extractExpiration(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration;
    }
}
