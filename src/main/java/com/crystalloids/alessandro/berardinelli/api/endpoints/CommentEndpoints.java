package com.crystalloids.alessandro.berardinelli.api.endpoints;

import com.crystalloids.alessandro.berardinelli.api.dto.CommentDto;
import com.crystalloids.alessandro.berardinelli.services.CommentService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(
        name = "comment",
        version = "v1",
        description = "API to handle comments"
)
public class CommentEndpoints {

    private final CommentService service = new CommentService();

    @ApiMethod(name = "addComment", path = "posts/{postId}/comments", httpMethod = ApiMethod.HttpMethod.POST)
    public CommentDto addComment( @Named("postId") Long postId, CommentDto commentDto, HttpServletRequest req) throws UnauthorizedException, NotFoundException {
        return service.addComment(commentDto, postId, req);
    }

    @ApiMethod(name = "listComments", path = "posts/{postId}/comments", httpMethod = ApiMethod.HttpMethod.GET)
    public List<CommentDto> listComments(@Named("postId") Long postId) throws NotFoundException {
        return service.listCommentsForPost(postId);
    }
}
