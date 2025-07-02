package com.crystalloids.alessandro.berardinelli.services;

import com.crystalloids.alessandro.berardinelli.auth.FirebaseAuthService;
import com.crystalloids.alessandro.berardinelli.db.dao.CommentDao;
import com.crystalloids.alessandro.berardinelli.db.dao.PostDao;
import com.crystalloids.alessandro.berardinelli.api.dto.CommentDto;
import com.crystalloids.alessandro.berardinelli.db.model.Comment;
import com.crystalloids.alessandro.berardinelli.db.model.Post;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
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

    // addComment - START
    @Test
    void addComment_shouldCreateAComment() throws UnauthorizedException, NotFoundException {
        //GIVEN

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

        //DO
        CommentDto result = commentService.addComment(commentDto, postId, req);

        //VERIFY
        assertEquals("user@email.com", result.getAuthor());
        assertEquals("test comment", result.getBody());
        assertEquals(postId, result.getPostId());
        assertNotNull(result.getCreatedAt());

        verify(postDao, times(1)).findById(postId);
        verify(authService, times(1)).verifyToken(req);
        verify(commentDao, times(1)).save(any());
    }

    @Test
    void addComment_shouldThrowNotFoundExceptionIfPostMissing() throws UnauthorizedException {
        Long postId = 2L;
        CommentDto commentDto = new CommentDto();
        when(postDao.findById(postId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            commentService.addComment(commentDto, postId, req);
        });

        verify(postDao, times(1)).findById(postId);
        verify(authService, times(0)).verifyToken(req);
        verify(commentDao, times(0)).save(any());
    }

    @Test
    void addComment_shouldThrowUnauthorizedExceptionIfTokenInvalid() throws UnauthorizedException {
        Long postId = 3L;
        CommentDto commentDto = new CommentDto();
        Post post = new Post();
        post.setId(postId);

        when(postDao.findById(postId)).thenReturn(post);
        when(authService.verifyToken(req)).thenThrow(new UnauthorizedException("Invalid token"));

        assertThrows(UnauthorizedException.class, () -> {
            commentService.addComment(commentDto, postId, req);
        });

        verify(postDao, times(1)).findById(postId);
        verify(authService, times(1)).verifyToken(req);
        verify(commentDao, times(0)).save(any());
    }

    // addComment - END

    // listComment - START
    @Test
    void listCommentsForPost_shouldThrowNotFoundExceptionIfPostMissing() {
        Long postId = 4L;
        when(postDao.findById(postId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            commentService.listCommentsForPost(postId);
        });

        verify(postDao, times(1)).findById(postId);
        verify(commentDao, times(0)).listByPostId(postId);
    }

    @Test
    void listCommentsForPost_shouldReturnCommentsIfPresent() throws NotFoundException {
        Long postId = 5L;
        Post post = new Post();
        post.setId(postId);

        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setPostId(postId);
        comment1.setBody("Comment 1");

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setPostId(postId);
        comment2.setBody("Comment 2");

        when(postDao.findById(postId)).thenReturn(post);
        when(commentDao.listByPostId(postId)).thenReturn(asList(comment1, comment2));

        List<CommentDto> result = commentService.listCommentsForPost(postId);

        assertEquals(2, result.size());
        assertEquals("Comment 1", result.get(0).getBody());
        assertEquals("Comment 2", result.get(1).getBody());

        verify(postDao, times(1)).findById(postId);
        verify(commentDao, times(1)).listByPostId(postId);
    }

    @Test
    void listCommentsForPost_shouldReturnEmptyListIfNoComments() throws NotFoundException {
        Long postId = 6L;
        Post post = new Post();
        post.setId(postId);

        when(postDao.findById(postId)).thenReturn(post);
        when(commentDao.listByPostId(postId)).thenReturn(java.util.Collections.emptyList());

        List<CommentDto> result = commentService.listCommentsForPost(postId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(postDao, times(1)).findById(postId);
        verify(commentDao, times(1)).listByPostId(postId);
    }

    // listComment - END


}
