package com.example.echo.endpoints;

import com.example.echo.model.Comment;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;

import java.util.Arrays;
import java.util.List;

@Api(
        name = "commentApi",
        version = "v1",
        description = "API to handle comments"
)
public class CommentEndpoints {

//    private final CommentService service = new CommentService();

    @ApiMethod(name = "addComment", path = "comments", httpMethod = ApiMethod.HttpMethod.POST)
    public Comment addComment(Comment comment) {
        String currentUserEmail = "user@example.com";
//        return service.addComment(comment, currentUserEmail);
        Comment comment1 = new Comment();
        comment1.setBody("Hello World");
        return comment1;
    }

    @ApiMethod(name = "listComments", path = "comments/{postId}", httpMethod = ApiMethod.HttpMethod.GET)
    public List<Comment> listComments(@Named("postId") Long postId) {
//        return service.listCommentsForPost(postId);
        Comment comment1 = new Comment();
        comment1.setBody("Hello World");
        return Arrays.asList(comment1);
    }
}
