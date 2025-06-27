package com.example.echo.endpoints;

import com.example.echo.model.Post;
import com.example.echo.services.PostService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.ObjectifyService;

import java.util.Arrays;
import java.util.List;

@Api(
        name = "postApi",
        version = "v1",
        description = "API to handle post"
)
public class PostEndpoints {

    private final PostService service = new PostService();

    @ApiMethod(name = "createPost", path = "posts", httpMethod = ApiMethod.HttpMethod.POST)
    public Post createPost(Post post) {
        String currentUserEmail = "user@example.com";
//        return service.createPost(post, currentUserEmail);
        Post post1 = new Post();
        post1.setAuthor("author1");
        return post1;
    }

    @ApiMethod(name = "getPost", path = "posts/{id}", httpMethod = ApiMethod.HttpMethod.GET)
    public Post getPost(@Named("id") Long id) {
//        return service.getPost(id);
        Post post1 = new Post();
        post1.setAuthor("author1");
        return post1;
    }

    @ApiMethod(name = "updatePost", path = "posts", httpMethod = ApiMethod.HttpMethod.PUT)
    public Post updatePost(Post post) {
        String currentUserEmail = "user@example.com";
//        return service.updatePost(post, currentUserEmail);
        Post post1 = new Post();
        post1.setAuthor("author1");
        return post1;
    }

    @ApiMethod(
            name = "listPosts",
            path = "posts",
            httpMethod = ApiMethod.HttpMethod.GET
    )
    public List<Post> listPosts() {

//        return service.listPosts();
        Post post1 = new Post();
        post1.setAuthor("author1");


//        ObjectifyService.ofy().save().entity(post1).now();

        service.createPost(post1, "a@b.c");

        return Arrays.asList(post1);
    }
}

