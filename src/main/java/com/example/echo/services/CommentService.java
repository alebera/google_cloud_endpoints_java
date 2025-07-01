package com.example.echo.services;

import com.example.echo.auth.FirebaseAuthService;
import com.example.echo.email.TaskQueueUtil;
import com.example.echo.endpoints.dao.CommentDao;
import com.example.echo.endpoints.dao.PostDao;
import com.example.echo.endpoints.dto.CommentDto;
import com.example.echo.model.Comment;
import com.example.echo.model.Post;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public class CommentService {
    private FirebaseAuthService authService = new FirebaseAuthService();
    private CommentDao commentDao;
    private PostDao postDao;

    public CommentService(FirebaseAuthService authService, CommentDao commentDao, PostDao postDao) {
        this.authService = authService;
        this.commentDao = commentDao;
        this.postDao = postDao;
    }

    public CommentService() {
    }

    public CommentDto addComment(CommentDto commentDto, Long postId, HttpServletRequest req) throws UnauthorizedException, NotFoundException {

        Post existingPost = postDao.findById(postId);
        if (existingPost == null) {
            throw new NotFoundException("Post not found");
        }

        String currentUserEmail = authService.verifyToken(req).getEmail();

        commentDto.setAuthor(currentUserEmail);
        commentDto.setCreatedAt(LocalDateTime.now());
        commentDto.setPostId(postId);
        Comment comment = commentDto.toEntity();

        comment = commentDao.save(comment);

        // send email
        TaskQueueUtil.enqueueEmailTask(existingPost.getAuthor(), "Your post received a comment", "Someone commented on your post, click here to see details ");

        return CommentDto.fromEntity(comment);
    }

    public List<CommentDto> listCommentsForPost(Long postId) throws NotFoundException {
        Post existingPost = postDao.findById(postId);
        if (existingPost == null) {
            throw new NotFoundException("Post not found");
        }

        List<Comment> comments = commentDao.listByPostId(postId);
        return CommentDto.fromEntity(comments);
    }

}
