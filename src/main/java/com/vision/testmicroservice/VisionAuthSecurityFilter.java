package com.vision.testmicroservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class VisionAuthSecurityFilter extends OncePerRequestFilter {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix

            // Validate the token with Vision Auth microservice
            boolean isValid = validateToken(token);
            if (isValid) {
                // Token is valid, proceed with the request
                filterChain.doFilter(request, response);
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // Treat as invalid if an exception occurs
        }
    }

}
