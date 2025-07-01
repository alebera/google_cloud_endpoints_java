package com.example.echo.db.dao;

import com.example.echo.db.model.Comment;
import com.googlecode.objectify.ObjectifyService;

import java.util.List;

public class CommentDao {

    public Comment save(Comment comment) {
        return ObjectifyService.run(() -> {
            ObjectifyService.ofy().save().entity(comment).now();
            return comment;
        });
    }

    public List<Comment> listByPostId(Long postId) {
        return ObjectifyService.run(() ->
            ObjectifyService.ofy().load().type(Comment.class).filter("postId", postId).list()
        );
    }

    public Comment findById(Long id) {
        return ObjectifyService.run(() ->
            ObjectifyService.ofy().load().type(Comment.class).id(id).now()
        );
    }
}
