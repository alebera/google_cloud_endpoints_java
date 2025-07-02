package com.crystalloids.alessandro.berardinelli.api.validators;

import com.crystalloids.alessandro.berardinelli.api.dto.CommentDto;
import com.google.api.server.spi.response.BadRequestException;
import io.netty.util.internal.StringUtil;

public class CommentValidator {

    public void validateCreation(CommentDto comment) throws BadRequestException {
        if (comment == null || StringUtil.isNullOrEmpty(comment.getBody())){
            throw new BadRequestException("Invalid data");
        }
    }
}
