package com.example.echo.endpoints.dao;

import com.example.echo.model.Post;
import com.googlecode.objectify.ObjectifyService;

import java.util.List;

public class PostDao {

    public Post save(Post post) {
        return ObjectifyService.run(() -> {
            ObjectifyService.ofy().save().entity(post).now();
            return post;
        });
    }

    public List<Post> list() {
        return ObjectifyService.run(() ->
            ObjectifyService.ofy().load().type(Post.class).list()
        );
    }

    public Post findById(Long id) {
        return ObjectifyService.run(() ->
            ObjectifyService.ofy().load().type(Post.class).id(id).now()
        );
    }
}