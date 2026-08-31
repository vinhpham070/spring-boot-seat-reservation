package com.example.quanly.config;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
@Component
public class JwtUtils {
    private static final String SECRET_KEY = "trinh_nhat_duy_project_manhattan_classified_i_dont_fucking_know";
    private static final Long EXPIRED_TIME = 300000L;
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRED_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token).getBody()
                .getSubject();


    }
    public boolean validateToken(String token, UserDetails userDetails) {
        String tenToken = extractUsername(token);
        String tenUser = userDetails.getUsername();
        return tenToken.equals(tenUser);
    }


}
