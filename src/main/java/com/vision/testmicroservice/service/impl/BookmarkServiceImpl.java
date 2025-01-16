package com.vision.testmicroservice.service.impl;

import com.vision.testmicroservice.model.Bookmark;
import com.vision.testmicroservice.dto.BookmarkDTO;
import com.vision.testmicroservice.model.User;
import com.vision.testmicroservice.repository.BookmarkRepository;
import com.vision.testmicroservice.repository.UserRepository;
import com.vision.testmicroservice.service.BookmarkService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class BookmarkServiceImpl implements BookmarkService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Value("${vision.app.jwtSecret}")
    private String jwtSecret;


    @Override
    public BookmarkDTO createBookmark(BookmarkDTO bookmarkDTO, HttpServletRequest request) {
        // Extract user email from the request token
        String email = getUsernameFromRequest(request); // Extract email (from JWT token)

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Create a new Bookmark entity from the DTO
        Bookmark bookmark = new Bookmark();
        bookmark.setTitle(bookmarkDTO.getTitle());
        bookmark.setDescription(bookmarkDTO.getDescription());
        bookmark.setLink(bookmarkDTO.getLink());
        bookmark.setUser(user); // Associate the bookmark with the authenticated user

        // Save the bookmark to the repository
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);

        // Convert the saved bookmark entity to DTO and return
        return new BookmarkDTO(savedBookmark);
    }

    public String getUsernameFromRequest(HttpServletRequest request) {
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


