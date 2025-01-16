package com.vision.testmicroservice;

import com.vision.testmicroservice.model.User;
import com.vision.testmicroservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class VisionAuthSecurityFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix

            // Validate the token and extract user information from Vision Auth
            boolean isValid = validateToken(token);
            if (isValid) {
                // Token is valid, extract user details
                String username = getUsernameFromRequest(request);

                if (username != null) {
                    // Look up the user in the database using the extracted username (email)
                    Optional<User> userOptional = userRepository.findByEmail(username);

                    if (userOptional.isPresent()) {
                        // User found, set user info in request attributes or headers
                        request.setAttribute("username", username);  // Or use headers if needed

                        // Proceed with the request
                        filterChain.doFilter(request, response);
                    } else {
                        // User not found, respond with 404 Not Found
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("User not found with username: " + username);
                    }
                } else {
                    // Username is null, invalid token
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: Invalid token");
                }
            } else {
                // Token is invalid, respond with 401 Unauthorized
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid token");
            }
        } else {
            // If no token is provided, respond with 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: No token provided");
        }
    }

    private boolean validateToken(String token) {
        try {
            String validationUrl = "http://localhost:7077/api/v1.0/auth/validate";

            // Set the Authorization header
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // Send the request with the Authorization header
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    validationUrl,
                    HttpMethod.GET,
                    requestEntity,
                    Boolean.class
            );

            return response.getBody() != null && response.getBody();
        } catch (HttpClientErrorException.Unauthorized e) {
            return false;  // Treat as invalid if an exception occurs
        } catch (Exception e) {
            return false;
        }
    }

    private String getUsernameFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix

            try {
                // Decode the JWT token and extract the claims
                Claims claims = Jwts.parser()
                        .setSigningKey("HugoBoss#Rocket@9594")  // Use the same secret key used to sign the token
                        .parseClaimsJws(token)
                        .getBody();

                // Log the claims to inspect the structure
                System.out.println("JWT Claims: " + claims);

                // Extract the username (or email) from the claims
                String username = claims.getSubject();  // "sub" typically contains the username or email
                return username;

            } catch (SignatureException e) {
                // Token is invalid
                throw new RuntimeException("Invalid token signature", e);
            } catch (Exception e) {
                // Handle other exceptions
                throw new RuntimeException("Failed to extract user details from token", e);
            }
        }
        return null;
    }
}
