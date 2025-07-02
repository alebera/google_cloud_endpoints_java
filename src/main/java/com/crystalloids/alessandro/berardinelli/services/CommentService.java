package com.crystalloids.alessandro.berardinelli.services;

import com.crystalloids.alessandro.berardinelli.auth.FirebaseAuthService;
import com.crystalloids.alessandro.berardinelli.email.TaskQueueUtil;
import com.crystalloids.alessandro.berardinelli.db.dao.CommentDao;
import com.crystalloids.alessandro.berardinelli.db.dao.PostDao;
import com.crystalloids.alessandro.berardinelli.api.dto.CommentDto;
import com.crystalloids.alessandro.berardinelli.db.model.Comment;
import com.crystalloids.alessandro.berardinelli.db.model.Post;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public class CommentService {
    private FirebaseAuthService authService = new FirebaseAuthService();
    private CommentDao commentDao;
    private TaskQueueUtil taskQueueUtil;
    private PostDao postDao;

    public CommentService(FirebaseAuthService authService, CommentDao commentDao, PostDao postDao, TaskQueueUtil taskQueueUtil) {
        this.authService = authService;
        this.commentDao = commentDao;
        this.postDao = postDao;
        this.taskQueueUtil = taskQueueUtil;
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
        taskQueueUtil.enqueueEmailTask(existingPost.getAuthor(), "Your post received a comment", "Someone commented on your post, click here to see details");

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
