package com.vision.testmicroservice.service.impl;

import com.vision.testmicroservice.jwt.JwtUtil;
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
    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public BookmarkDTO createBookmark(BookmarkDTO bookmarkDTO, HttpServletRequest request) {
        // Extract user email from the request token
        String email = jwtUtil.extractUsernameFromRequest(request); // Extract email (from JWT token)

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



}


