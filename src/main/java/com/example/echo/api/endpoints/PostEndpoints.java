package com.example.echo.api.endpoints;

import com.example.echo.api.dto.PostDto;
import com.example.echo.services.PostService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(
        name = "post",
        version = "v1",
        description = "API to handle post"
)
public class PostEndpoints {

    private final PostService service = new PostService();

    @ApiMethod(name = "createPost", path = "posts", httpMethod = ApiMethod.HttpMethod.POST)
    public PostDto createPost(PostDto postDto, HttpServletRequest req) throws UnauthorizedException {
        return service.createPost(postDto, req);
    }


    @ApiMethod(name = "getPost", path = "posts/{id}", httpMethod = ApiMethod.HttpMethod.GET)
    public PostDto getPost(@Named("id") Long id) throws NotFoundException {
        return service.getPost(id);
    }

    @ApiMethod(name = "updatePost", path = "posts/{id}", httpMethod = ApiMethod.HttpMethod.PUT)
    public PostDto updatePost(@Named("id") Long postId, PostDto postDto, HttpServletRequest req) throws UnauthorizedException, NotFoundException {
        return service.updatePost(postDto, postId, req);
    }

    @ApiMethod(
            name = "listPosts",
            path = "posts",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public List<PostDto> listPosts() {
        return service.listPosts();
    }
}

