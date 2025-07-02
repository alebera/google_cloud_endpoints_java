package com.crystalloids.alessandro.berardinelli.api.mappers;

import com.crystalloids.alessandro.berardinelli.api.dto.CommentDto;
import com.crystalloids.alessandro.berardinelli.db.model.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        if (comment == null) return null;
        return new CommentDto(
                comment.getId(),
                comment.getPostId(),
                comment.getAuthor(),
                comment.getBody(),
                comment.getCreatedAt()
        );
    }

    public List<CommentDto> toDto(List<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<>();
        for (Comment comment : comments) {
            dtos.add(toDto(comment));
        }
        return dtos;
    }

    public Comment toEntity(CommentDto dto) {
        if (dto == null) return null;
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setPostId(dto.getPostId());
        comment.setAuthor(dto.getAuthor());
        comment.setBody(dto.getBody());
        comment.setCreatedAt(dto.getCreatedAt());
        return comment;
    }
}