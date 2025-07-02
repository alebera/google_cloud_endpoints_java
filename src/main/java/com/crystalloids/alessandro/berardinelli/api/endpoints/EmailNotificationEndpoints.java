package com.crystalloids.alessandro.berardinelli.api.endpoints;

import com.crystalloids.alessandro.berardinelli.api.dto.EmailDto;
import com.crystalloids.alessandro.berardinelli.email.EmailNotificationsService;
import com.crystalloids.alessandro.berardinelli.email.SendgridEmailNotificationsService;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

@Api(name = "taskApi", version = "v1")
public class EmailNotificationEndpoints {

    private final EmailNotificationsService emailNotificationsService = new SendgridEmailNotificationsService();

    @ApiMethod(name = "sendEmail", path = "task/sendEmail", httpMethod = ApiMethod.HttpMethod.POST)
    public void sendEmail(EmailDto email) {
        emailNotificationsService.send(email);
    }
}

