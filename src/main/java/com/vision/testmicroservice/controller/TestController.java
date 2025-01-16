package com.vision.testmicroservice.controller;
import com.vision.testmicroservice.service.TokenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private TokenValidationService tokenValidationService;

    // Secure endpoint that checks the token validity
    @GetMapping("/secure")
    public String secureEndpoint(@RequestHeader("Authorization") String token) {
        // Remove "Bearer " prefix from the token
        String cleanToken = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Validate the token
        boolean isValid = tokenValidationService.validateToken(cleanToken);

        if (isValid) {
            return "Token is valid. Access granted!";
        } else {
            return "Invalid token. Access denied!";
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<?> helloworld() {
        return ResponseEntity.ok("Hello World!");
    }
}
