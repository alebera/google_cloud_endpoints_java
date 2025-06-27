package com.example.echo.services;

import com.example.echo.model.Post;
import com.googlecode.objectify.ObjectifyService;

import java.util.Date;
import java.util.List;

public class PostService {


    public Post createPost(Post post, String currentUserEmail) {
        post.setAuthor(currentUserEmail);
        post.setCreatedAt(new Date());
        post.setUpdatedAt(new Date());
        return ObjectifyService.run(() -> {
            ObjectifyService.ofy().save().entity(post).now();
            return post;
        });
    }

    public List<Post> listPosts() {
        return ObjectifyService.ofy().load().type(Post.class).list();
    }

    public Post getPost(Long id) {
        return ObjectifyService.ofy().load().type(Post.class).id(id).now();
    }

    public Post updatePost(Post post, String currentUserEmail) {
        Post existing = getPost(post.getId());
        if (existing == null || !existing.getAuthor().equals(currentUserEmail)) {
            throw new RuntimeException("Unauthorized");
        }
        post.setUpdatedAt(new Date());
        ObjectifyService.ofy().save().entity(post).now();
        return post;
    }
}
