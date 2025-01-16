package com.vision.testmicroservice.controller.bookmark;

import com.vision.testmicroservice.dto.BookmarkDTO;
import com.vision.testmicroservice.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    @Autowired
    private BookmarkService bookmarkService;

    @PostMapping("/create")
    public ResponseEntity<?> createBookmark(@RequestBody BookmarkDTO bookmarkDTO, HttpServletRequest request) {

        try{
            BookmarkDTO createdBookmarkDTO = bookmarkService.createBookmark(bookmarkDTO, request);
            return new ResponseEntity<>(createdBookmarkDTO, HttpStatus.CREATED);

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
