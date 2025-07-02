package com.crystalloids.alessandro.berardinelli.api.mappers;

import com.crystalloids.alessandro.berardinelli.api.dto.PostDto;
import com.crystalloids.alessandro.berardinelli.db.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostMapper {

    public PostDto toDto(Post post) {
        if (post == null) return null;
        return new PostDto(
                post.getId(),
                post.getAuthor(),
                post.getSubject(),
                post.getBody(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    public List<PostDto> toDto(List<Post> posts) {
        List<PostDto> dtos = new ArrayList<>();
        for (Post post : posts) {
            dtos.add(toDto(post));
        }
        return dtos;
    }

    public Post toEntity(PostDto dto) {
        if (dto == null) return null;
        Post post = new Post();
        post.setId(dto.getId());
        post.setAuthor(dto.getAuthor());
        post.setSubject(dto.getSubject());
        post.setBody(dto.getBody());
        post.setCreatedAt(dto.getCreatedAt());
        post.setUpdatedAt(dto.getUpdatedAt());
        return post;
    }


}