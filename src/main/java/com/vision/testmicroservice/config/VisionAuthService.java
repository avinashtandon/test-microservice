package com.vision.testmicroservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class VisionAuthService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String AUTH_SERVICE_URL = "http://localhost:7077/api/v1.0/auth";

    public void validateToken(String token) {
        String url = AUTH_SERVICE_URL + "/validate-token";
        Boolean isValidToken = restTemplate.postForObject(url, token, Boolean.class);

        if (isValidToken == null || !isValidToken) {
            throw new RuntimeException("Invalid or expired token");
        }
    }

    public void validateTokenAndRole(String token, String requiredRole) {
        String url = AUTH_SERVICE_URL + "/validate-role";
        Map<String, Object> response = restTemplate.postForObject(url, token, Map.class);

        if (response == null || !(Boolean) response.get("isValid")) {
            throw new RuntimeException("Invalid or expired token");
        }

        String userRole = (String) response.get("role");
        if (!userRole.equals(requiredRole)) {
            throw new RuntimeException("Access denied: insufficient permissions.");
        }
    }
}
