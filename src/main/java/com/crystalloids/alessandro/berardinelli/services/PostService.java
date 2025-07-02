package com.crystalloids.alessandro.berardinelli.services;

import com.crystalloids.alessandro.berardinelli.auth.FirebaseAuthService;
import com.crystalloids.alessandro.berardinelli.email.TaskQueueUtil;
import com.crystalloids.alessandro.berardinelli.db.dao.PostDao;
import com.crystalloids.alessandro.berardinelli.api.dto.PostDto;
import com.crystalloids.alessandro.berardinelli.db.model.Post;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public class PostService {

    private FirebaseAuthService authService = new FirebaseAuthService();
    private PostDao postDao = new PostDao();
    private TaskQueueUtil taskQueueUtil;

    public PostService(FirebaseAuthService authService, PostDao postDao, TaskQueueUtil taskQueueUtil) {
        this.authService = authService;
        this.postDao = postDao;
        this.taskQueueUtil = taskQueueUtil;
    }

    public PostService() {
    }

    public PostDto createPost(PostDto postDto, HttpServletRequest req) throws UnauthorizedException {

        String currentUserEmail = authService.verifyToken(req).getEmail();

        postDto.setAuthor(currentUserEmail);
        postDto.setCreatedAt(LocalDateTime.now());

        Post post = postDto.toEntity();

        post = postDao.save(post);

        // send email
        taskQueueUtil.enqueueEmailTask(post.getAuthor(), "Post is created", "You just created a post successfully");

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

        String currentUserEmail = authService.verifyToken(req).getEmail();

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

        return PostDto.fromEntity(existingPost);
    }

    private Post getPostEntity(Long id) {
        return postDao.findById(id);
    }
}
