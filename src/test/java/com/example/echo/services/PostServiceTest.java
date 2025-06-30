package com.example.echo.services;

import com.example.echo.auth.FirebaseAuthService;
import com.example.echo.endpoints.dao.PostDao;
import com.example.echo.endpoints.dto.PostDto;
import com.example.echo.model.Post;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.firebase.auth.FirebaseToken;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostService postService;
    private PostDao postDao;
    private HttpServletRequest req;
    private FirebaseAuthService authService;
    private FirebaseToken jwt;

    @BeforeEach
    void setUp() {
        authService = mock(FirebaseAuthService.class);
        postDao = mock(PostDao.class);
        postService = new PostService(authService, postDao);
        req = mock(HttpServletRequest.class);
        jwt = mock(FirebaseToken.class);
    }

    @Test
    void createPost_shouldSetAuthorAndTimestamps() throws UnauthorizedException {
        PostDto postDto = new PostDto();
        postDto.setSubject("new post");
        postDto.setBody("lorem ipsum");
        when(authService.verifyToken(req)).thenReturn(jwt);
        when(jwt.getEmail()).thenReturn("a@c.com");

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);
        Post savedPost = new Post();
        savedPost.setAuthor("a@c.com");
        savedPost.setSubject("new post");
        savedPost.setBody("lorem ipsum");
        savedPost.setCreatedAt(LocalDateTime.now());
        when(postDao.save(postCaptor.capture())).thenReturn(savedPost);

        PostDto result = postService.createPost(postDto, req);

        Post captured = postCaptor.getValue();
        assertEquals("new post", captured.getSubject());
        assertEquals("lorem ipsum", captured.getBody());
        assertEquals("a@c.com", captured.getAuthor());
        assertNotNull(captured.getCreatedAt());
        assertNull(captured.getUpdatedAt());

        assertEquals("new post", result.getSubject());
        assertEquals("lorem ipsum", result.getBody());
        assertEquals("a@c.com", result.getAuthor());
        assertNotNull(captured.getCreatedAt());
        assertNull(captured.getUpdatedAt());
    }


}