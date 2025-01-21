package com.vision.testmicroservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {
    @Value("${vision.app.jwtSecret}")
    private String jwtSecret;

    public String extractUsernameFromRequest(HttpServletRequest request) {
        // Extract the "Authorization" header (Bearer token)
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix

            // Decode the JWT token and extract the "sub" (subject) claim, which is the email
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret)  // Use the same secret key used to sign the token
                        .parseClaimsJws(token)
                        .getBody();

                return claims.getSubject();  // This should be the email
            } catch (Exception e) {
                throw new RuntimeException("Failed to extract user info from token", e);
            }
        }

        throw new RuntimeException("Authorization token is missing or invalid");
    }
}
