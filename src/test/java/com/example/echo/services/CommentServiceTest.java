package com.example.echo.services;

import com.example.echo.auth.FirebaseAuthService;
import com.example.echo.endpoints.dao.CommentDao;
import com.example.echo.endpoints.dao.PostDao;
import com.example.echo.endpoints.dto.CommentDto;
import com.example.echo.model.Comment;
import com.example.echo.model.Post;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    private CommentService commentService;
    private FirebaseAuthService authService;
    private CommentDao commentDao;
    private PostDao postDao;
    private HttpServletRequest req;
    private FirebaseToken jwt;

    @BeforeEach
    void setUp() {
        authService = mock(FirebaseAuthService.class);
        commentDao = mock(CommentDao.class);
        postDao = mock(PostDao.class);
        commentService = new CommentService(authService, commentDao, postDao);
        req = mock(HttpServletRequest.class);
        jwt = mock(FirebaseToken.class);
    }

    @Test
    void addComment_shouldSetAuthorAndTimestamps() throws UnauthorizedException, NotFoundException {
        Long postId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setBody("test comment");

        Post post = new Post();
        post.setId(postId);
        post.setAuthor("author@email.com");

        when(postDao.findById(postId)).thenReturn(post);
        when(authService.verifyToken(req)).thenReturn(jwt);
        when(jwt.getEmail()).thenReturn("user@email.com");

        Comment savedComment = new Comment();
        savedComment.setId(10L);
        savedComment.setPostId(postId);
        savedComment.setAuthor("user@email.com");
        savedComment.setBody("test comment");
        savedComment.setCreatedAt(LocalDateTime.now());

        when(commentDao.save(any(Comment.class))).thenReturn(savedComment);

        CommentDto result = commentService.addComment(commentDto, postId, req);

        assertEquals("user@email.com", result.getAuthor());
        assertEquals("test comment", result.getBody());
        assertEquals(postId, result.getPostId());
        assertNotNull(result.getCreatedAt());
    }

    @Test
    void addComment_shouldThrowNotFoundExceptionIfPostMissing() {
        Long postId = 2L;
        CommentDto commentDto = new CommentDto();
        when(postDao.findById(postId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            commentService.addComment(commentDto, postId, req);
        });
    }
}
