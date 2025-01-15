package com.vision.testmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenValidationService {
    @Autowired
    private RestTemplate restTemplate;

    // This method calls the Vision Auth /validate endpoint
    public boolean validateToken(String token) {
        // Define the URL to call Vision Auth API for token validation
        String visionAuthUrl = "http://localhost:7077/api/v1.0/auth/validate";

        // Prepare headers with the Authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Prepare the request entity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Send GET request to Vision Auth's /validate endpoint
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    visionAuthUrl,
                    HttpMethod.GET,
                    entity,
                    Boolean.class);

            // Return whether the token is valid (true or false)
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            // Log the error and return false if validation fails
            System.out.println("Error during token validation: " + e.getMessage());
            return false;
        }
    }
}
