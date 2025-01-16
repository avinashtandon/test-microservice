package com.vision.testmicroservice.dto;

import com.vision.testmicroservice.model.Bookmark;

public class BookmarkDTO {
    private Long id;
    private String title;
    private String description;
    private String link;
    // Default constructor (required for Jackson)
    public BookmarkDTO() {
    }

    public BookmarkDTO(Long id, String title, String description, String link) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
    }


    public BookmarkDTO(Bookmark bookmark) {
        this.id = bookmark.getId();
        this.title = bookmark.getTitle();
        this.description = bookmark.getDescription();
        this.link = bookmark.getLink();
    }

    public BookmarkDTO(Long id, String title, String description, String link, String username) {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
