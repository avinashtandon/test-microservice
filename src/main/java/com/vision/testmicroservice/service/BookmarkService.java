package com.vision.testmicroservice.service;

import com.vision.testmicroservice.dto.BookmarkDTO;

import javax.servlet.http.HttpServletRequest;

public interface BookmarkService {
BookmarkDTO createBookmark(BookmarkDTO bookmarkDTO, HttpServletRequest request);
}
