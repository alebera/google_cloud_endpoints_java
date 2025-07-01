package com.crystalloids.alessandro.berardinelli.api.dto;

import com.crystalloids.alessandro.berardinelli.db.model.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostDto {
    private Long id;
    private String author;
    private String subject;
    private String body;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    // Getters and setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Mapping from entity to DTO
    public static PostDto fromEntity(Post post) {
        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setAuthor(post.getAuthor());
        dto.setSubject(post.getSubject());
        dto.setBody(post.getBody());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        return dto;
    }

    public static List<PostDto> fromEntity(List<Post> posts) {
        List<PostDto> dtos = new ArrayList<>();
        for (Post post : posts) {
            dtos.add(fromEntity(post));
        }
        return dtos;
    }

    // Mapping from DTO to entity
    public Post toEntity() {
        Post post = new Post();
        post.setId(this.id);
        post.setAuthor(this.author);
        post.setSubject(this.subject);
        post.setBody(this.body);
        post.setCreatedAt(this.createdAt);
        post.setUpdatedAt(this.updatedAt);
        return post;
    }
}