package com.crystalloids.alessandro.berardinelli.api.dto;

import com.crystalloids.alessandro.berardinelli.db.model.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long postId;
    private String author;
    private String body;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;


    public static CommentDto fromEntity(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPostId());
        dto.setAuthor(comment.getAuthor());
        dto.setBody(comment.getBody());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

    public static List<CommentDto> fromEntity(List<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            dtos.add(fromEntity(comment));
        }
        return dtos;
    }

    // Mapping from DTO to entity
    public Comment toEntity() {
        Comment comment = new Comment();
        comment.setId(this.id);
        comment.setPostId(this.postId);
        comment.setAuthor(this.author);
        comment.setBody(this.body);
        comment.setCreatedAt(this.createdAt);
        return comment;
    }
}
