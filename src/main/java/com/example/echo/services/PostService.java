package com.example.echo.services;

import com.example.echo.auth.FirebaseAuthService;
import com.example.echo.email.TaskQueueUtil;
import com.example.echo.endpoints.dto.PostDto;
import com.example.echo.model.Post;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public class PostService {

    private final FirebaseAuthService authService = new FirebaseAuthService();

    public PostDto createPost(PostDto postDto, HttpServletRequest req) throws UnauthorizedException {

        String currentUserEmail = authService.verifyToken(req).getEmail();

        postDto.setAuthor(currentUserEmail);
        postDto.setCreatedAt(LocalDateTime.now());
        postDto.setUpdatedAt(LocalDateTime.now());

        TaskQueueUtil.enqueueEmailTask(postDto.getAuthor(), "post email subject", "post email body");

        Post post = postDto.toEntity();

        // save in DB
        Post finalPost = post;
        post = ObjectifyService.run(() -> {
            ObjectifyService.ofy().save().entity(finalPost).now();
            return finalPost;
        });

        // send email
        TaskQueueUtil.enqueueEmailTask(post.getAuthor(), "post email subject", "post email body");

        return PostDto.fromEntity(post);
    }

    public List<PostDto> listPosts() {
        return PostDto.fromEntity(ObjectifyService.ofy().load().type(Post.class).list());
    }

    public PostDto getPost(Long id) {
        return PostDto.fromEntity(getPostEntity(id));
    }

    private Post getPostEntity(Long id) {
        return ObjectifyService.ofy().load().type(Post.class).id(id).now();
    }

    public PostDto updatePost(PostDto postDto, HttpServletRequest req) throws UnauthorizedException, NotFoundException {

        String currentUserEmail = authService.verifyToken(req).getEmail();

        Post existingPost = getPostEntity(postDto.getId());
        if (existingPost == null) {
            throw new NotFoundException("Unauthorized");
        }
        if (!existingPost.isFromAuthor(currentUserEmail)) {
            throw new UnauthorizedException("No privileges to update the post");
        }

        existingPost.setBody(postDto.getBody());
        existingPost.setSubject(postDto.getSubject());


        existingPost.setUpdatedAt(LocalDateTime.now());

        ObjectifyService.ofy().save().entity(existingPost).now();

        return PostDto.fromEntity(existingPost);
    }
}
