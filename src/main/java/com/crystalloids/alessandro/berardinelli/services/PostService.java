package com.crystalloids.alessandro.berardinelli.services;

import com.crystalloids.alessandro.berardinelli.api.dto.PostDto;
import com.crystalloids.alessandro.berardinelli.auth.AuthService;
import com.crystalloids.alessandro.berardinelli.db.dao.PostDao;
import com.crystalloids.alessandro.berardinelli.db.model.Post;
import com.crystalloids.alessandro.berardinelli.email.TaskQueueService;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private static final String EMAIL_SUBJECT = "Post is created";
    private static final String EMAIL_CONTENT = "You just created a post successfully";
    private AuthService authService;
    private PostDao postDao;
    private TaskQueueService taskQueueService;

    public PostService(AuthService authService, PostDao postDao, TaskQueueService taskQueueService) {
        this.authService = authService;
        this.postDao = postDao;
        this.taskQueueService = taskQueueService;
    }

    public PostService() {
    }

    public PostDto createPost(PostDto postDto, HttpServletRequest req) throws UnauthorizedException {

        String currentUserEmail = authService.verifyUser(req);

        postDto.setAuthor(currentUserEmail);
        postDto.setCreatedAt(LocalDateTime.now());

        Post post = postDto.toEntity();

        post = postDao.save(post);

        // send email
        taskQueueService.enqueueEmailTask(post.getAuthor(), EMAIL_SUBJECT, EMAIL_CONTENT);

        logger.info("New post is created");

        return PostDto.fromEntity(post);
    }

    public List<PostDto> listPosts() {
        return PostDto.fromEntity(postDao.list());
    }

    public PostDto getPost(Long postId) throws NotFoundException {
        Post existingPost = getPostEntity(postId);
        if (existingPost == null) {
            throw new NotFoundException("Post not found");
        }
        return PostDto.fromEntity(existingPost);
    }

    public PostDto updatePost(PostDto postDto, Long postId, HttpServletRequest req) throws UnauthorizedException, NotFoundException {

        String currentUserEmail = authService.verifyUser(req);

        Post existingPost = getPostEntity(postId);
        if (existingPost == null) {
            throw new NotFoundException("Post not found");
        }
        if (!existingPost.isFromAuthor(currentUserEmail)) {
            throw new UnauthorizedException("No privileges to update the post");
        }

        existingPost.setBody(postDto.getBody());
        existingPost.setSubject(postDto.getSubject());
        existingPost.setUpdatedAt(LocalDateTime.now());

        postDao.save(existingPost);

        logger.info("Post with id {} is updated", postId);

        return PostDto.fromEntity(existingPost);
    }

    private Post getPostEntity(Long id) {
        return postDao.findById(id);
    }
}
