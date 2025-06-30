package com.example.echo.services;

import com.example.echo.auth.FirebaseAuthService;
import com.example.echo.endpoints.dto.CommentDto;
import com.example.echo.model.Comment;
import com.google.api.server.spi.response.UnauthorizedException;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public class CommentService {
    private final FirebaseAuthService authService = new FirebaseAuthService();

    public CommentDto addComment(CommentDto commentDto, Long postId, HttpServletRequest req) throws UnauthorizedException {

        String currentUserEmail = authService.verifyToken(req).getEmail();

        commentDto.setAuthor(currentUserEmail);
        commentDto.setCreatedAt(LocalDateTime.now());
        commentDto.setPostId(postId);
        Comment comment = commentDto.toEntity();

        ObjectifyService.run(() -> {
            ObjectifyService.ofy().save().entity(comment).now();
            return null;
        });
        return commentDto;
    }

    public List<CommentDto> listCommentsForPost(Long postId) {
        List<Comment> comments = ObjectifyService.run(() ->
                ObjectifyService.ofy().load().type(Comment.class)
                        .filter("postId", postId).list()
        );
        return CommentDto.fromEntity(comments);
    }
}
