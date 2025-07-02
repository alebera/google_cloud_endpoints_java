package com.crystalloids.alessandro.berardinelli.services;

import com.crystalloids.alessandro.berardinelli.api.dto.PostDto;
import com.crystalloids.alessandro.berardinelli.api.mappers.PostMapper;
import com.crystalloids.alessandro.berardinelli.auth.FirebaseAuthService;
import com.crystalloids.alessandro.berardinelli.db.dao.PostDao;
import com.crystalloids.alessandro.berardinelli.db.model.Post;
import com.crystalloids.alessandro.berardinelli.email.TaskQueueService;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    private PostService postService;
    private PostDao postDao;
    private TaskQueueService taskQueueService;
    private HttpServletRequest req;
    private FirebaseAuthService authService;
    private PostMapper postMapper;

    @BeforeEach
    void setUp() {
        authService = mock(FirebaseAuthService.class);
        postDao = mock(PostDao.class);
        taskQueueService = mock(TaskQueueService.class);
        postMapper = new PostMapper();
        postService = new PostService(authService, postDao, taskQueueService, postMapper);
        req = mock(HttpServletRequest.class);
    }

    // createPost - START

    @Test
    void createPost_shouldCreateAPost() throws UnauthorizedException {
        PostDto postDto = new PostDto();
        postDto.setSubject("new post");
        postDto.setBody("lorem ipsum");
        when(authService.verifyUser(req)).thenReturn("a@c.com");

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

        verify(authService, times(1)).verifyUser(req);
        verify(postDao, times(1)).save(captured);
        verify(taskQueueService, times(1)).enqueueEmailTask(captured.getAuthor(), "Post is created", "You just created a post successfully");
    }

    @Test
    void createPost_shouldThrowUnauthorizedExceptionIfTokenInvalid() throws UnauthorizedException {
        PostDto postDto = new PostDto();
        when(authService.verifyUser(req)).thenThrow(new UnauthorizedException("Invalid token"));

        assertThrows(UnauthorizedException.class, () -> {
            postService.createPost(postDto, req);
        });

        verify(authService, times(1)).verifyUser(req);
        verify(postDao, times(0)).save(any());
        verify(taskQueueService, times(0)).enqueueEmailTask(any(),any(),any());
    }

    // createPost - END

    // listPosts - START

    @Test
    void listPosts_shouldReturnListOfPostDtos() {
        Post post1 = new Post();
        post1.setId(1L);
        post1.setAuthor("a@c.com");
        post1.setSubject("subject1");
        post1.setBody("body1");

        Post post2 = new Post();
        post2.setId(2L);
        post2.setAuthor("b@c.com");
        post2.setSubject("subject2");
        post2.setBody("body2");

        when(postDao.list()).thenReturn(asList(post1, post2));

        List<PostDto> result = postService.listPosts();

        assertEquals(2, result.size());
        assertEquals("a@c.com", result.get(0).getAuthor());
        assertEquals("subject1", result.get(0).getSubject());
        assertEquals("b@c.com", result.get(1).getAuthor());
        assertEquals("subject2", result.get(1).getSubject());

        verify(postDao, times(1)).list();
    }

    @Test
    void listPosts_shouldReturnEmptyListIfNoPosts() {
        when(postDao.list()).thenReturn(emptyList());

        List<PostDto> result = postService.listPosts();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(postDao, times(1)).list();
    }

    // listPosts - END

    // getPost - START

    @Test
    void getPost_shouldThrowNotFoundExceptionIfPostMissing() {
        Long postId = 10L;
        when(postDao.findById(postId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            postService.getPost(postId);
        });

        verify(postDao, times(1)).findById(postId);
    }

    @Test
    void getPost_shouldReturnPostIfPresent() throws NotFoundException {
        Long postId = 11L;
        Post post = new Post();
        post.setId(postId);
        post.setAuthor("a@c.com");
        post.setSubject("subject");
        post.setBody("body");
        when(postDao.findById(postId)).thenReturn(post);

        PostDto result = postService.getPost(postId);

        assertEquals("a@c.com", result.getAuthor());
        assertEquals("subject", result.getSubject());
        assertEquals("body", result.getBody());
        verify(postDao, times(1)).findById(postId);

        verify(postDao, times(1)).findById(postId);
    }
    // getPost - END

    // updatePost - START

    @Test
    void updatePost_shouldUpdatePost() throws UnauthorizedException, NotFoundException {
        Long postId = 15L;
        PostDto postDto = new PostDto();
        postDto.setBody("new body");
        postDto.setSubject("new subject");
        Post post = mock(Post.class);
        when(authService.verifyUser(req)).thenReturn("a@c.com");
        when(postDao.findById(postId)).thenReturn(post);
        when(post.isFromAuthor("a@c.com")).thenReturn(true);

        when(postDao.save(post)).thenReturn(post);

        PostDto result = postService.updatePost(postDto, postId, req);

        verify(post, times(1)).setBody("new body");
        verify(post, times(1)).setSubject("new subject");
        verify(post, times(1)).setUpdatedAt(any());
        verify(postDao, times(1)).save(post);
        assertNotNull(result);
    }

    @Test
    void updatePost_shouldThrowUnauthorizedExceptionIfTokenInvalid() throws UnauthorizedException, NotFoundException {
        Long postId = 12L;
        PostDto postDto = new PostDto();
        when(authService.verifyUser(req)).thenThrow(new UnauthorizedException("Invalid token"));

        assertThrows(UnauthorizedException.class, () -> {
            postService.updatePost(postDto, postId, req);
        });

        verify(authService, times(1)).verifyUser(req);
        verify(postDao, times(0)).findById(postId);
        verify(postDao, times(0)).save(any(Post.class));
    }

    @Test
    void updatePost_shouldThrowNotFoundExceptionIfPostMissing() throws UnauthorizedException {
        Long postId = 13L;
        PostDto postDto = new PostDto();
        when(authService.verifyUser(req)).thenReturn("a@c.com");
        when(postDao.findById(postId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            postService.updatePost(postDto, postId, req);
        });

        verify(authService, times(1)).verifyUser(req);
        verify(postDao, times(1)).findById(postId);
        verify(postDao, times(0)).save(any(Post.class));
    }

    @Test
    void updatePost_shouldThrowUnauthorizedExceptionIfNotAuthor() throws UnauthorizedException {
        Long postId = 14L;
        PostDto postDto = new PostDto();
        Post post = mock(Post.class);
        when(authService.verifyUser(req)).thenReturn("user@other.com");
        when(postDao.findById(postId)).thenReturn(post);
        when(post.isFromAuthor("user@other.com")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> {
            postService.updatePost(postDto, postId, req);
        });

        verify(authService, times(1)).verifyUser(req);
        verify(postDao, times(1)).findById(postId);
        verify(postDao, times(0)).save(any(Post.class));
        verify(post, times(1)).isFromAuthor("user@other.com");
    }

    // updatePost - END





}