package com.crystalloids.alessandro.berardinelli.services;

import com.crystalloids.alessandro.berardinelli.api.dto.CommentDto;
import com.crystalloids.alessandro.berardinelli.api.mappers.CommentMapper;
import com.crystalloids.alessandro.berardinelli.api.validators.CommentValidator;
import com.crystalloids.alessandro.berardinelli.auth.AuthService;
import com.crystalloids.alessandro.berardinelli.db.dao.CommentDao;
import com.crystalloids.alessandro.berardinelli.db.dao.PostDao;
import com.crystalloids.alessandro.berardinelli.db.model.Comment;
import com.crystalloids.alessandro.berardinelli.db.model.Post;
import com.crystalloids.alessandro.berardinelli.email.TaskQueueService;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private static final String EMAIL_SUBJECT = "Your post received a comment";
    private static final String EMAIL_CONTENT = "Someone commented on your post, click here to see details";
    private AuthService authService;
    private CommentDao commentDao;
    private PostDao postDao;
    private TaskQueueService taskQueueService;
    private CommentMapper commentMapper;
    private CommentValidator commentValidator;

    public CommentService(AuthService authService, CommentDao commentDao, PostDao postDao, TaskQueueService taskQueueService, CommentMapper commentMapper, CommentValidator commentValidator) {
        this.authService = authService;
        this.commentDao = commentDao;
        this.postDao = postDao;
        this.taskQueueService = taskQueueService;
        this.commentMapper = commentMapper;
        this.commentValidator = commentValidator;
    }

    public CommentService() {
    }

    public CommentDto addComment(CommentDto commentDto, Long postId, HttpServletRequest req) throws UnauthorizedException, NotFoundException, BadRequestException {

        commentValidator.validateCreation(commentDto);

        Post existingPost = postDao.findById(postId);
        if (existingPost == null) {
            throw new NotFoundException("Post not found");
        }

        String currentUserEmail = authService.verifyUser(req);

        commentDto.setAuthor(currentUserEmail);
        commentDto.setCreatedAt(LocalDateTime.now());
        commentDto.setPostId(postId);

        Comment comment = commentMapper.toEntity(commentDto);

        comment = commentDao.save(comment);

        taskQueueService.enqueueEmailTask(existingPost.getAuthor(), EMAIL_SUBJECT, EMAIL_CONTENT);

        logger.info("Comment added on post {}", postId);

        return commentMapper.toDto(comment);
    }

    public List<CommentDto> listCommentsForPost(Long postId) throws NotFoundException {
        Post existingPost = postDao.findById(postId);
        if (existingPost == null) {
            throw new NotFoundException("Post not found");
        }

        List<Comment> comments = commentDao.listByPostId(postId);
        return commentMapper.toDto(comments);}

}
