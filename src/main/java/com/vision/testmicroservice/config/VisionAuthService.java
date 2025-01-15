package com.vision.testmicroservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VisionAuthService {
    @Value("${vision.auth.url}")  // URL of Vision Auth service
    private String visionAuthUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean validateToken(String token, String domain) {
        try {
            // Construct the validation URL
            String validationUrl = visionAuthUrl + "/api/v1.0/auth/validate?token=" + token + "&domain=" + domain;

            // Call the validation endpoint and get the response
            Boolean isValid = restTemplate.getForObject(validationUrl, Boolean.class);

            return isValid != null && isValid;
        } catch (Exception e) {
            return false;  // If there's an exception, consider the token invalid
        }
    }
}
