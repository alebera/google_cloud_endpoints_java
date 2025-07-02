package com.crystalloids.alessandro.berardinelli.api.validators;

import com.crystalloids.alessandro.berardinelli.api.dto.PostDto;
import com.google.api.server.spi.response.BadRequestException;
import io.netty.util.internal.StringUtil;

public class PostValidator {

    public void validateCreation(PostDto post) throws BadRequestException {
        if (post == null || StringUtil.isNullOrEmpty(post.getBody()) || StringUtil.isNullOrEmpty(post.getSubject())){
            throw new BadRequestException("Invalid data");
        }
    }

    public void validateUpdate(PostDto post) throws BadRequestException {
        validateCreation(post);
    }
}
