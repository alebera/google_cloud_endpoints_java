package com.crystalloids.alessandro.berardinelli.email;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@Api(name = "taskApi", version = "v1")
public class EmailNotificationTask {

    @ApiMethod(name = "sendEmail", path = "task/sendEmail", httpMethod = ApiMethod.HttpMethod.POST)
    public void sendEmail(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }

        EmailUtil.sendEmailFromJson(sb.toString());
    }
}

