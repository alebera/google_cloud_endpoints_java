package com.crystalloids.alessandro.berardinelli.email;

import com.crystalloids.alessandro.berardinelli.api.dto.EmailDto;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendgridEmailNotificationsService implements EmailNotificationsService {

    private static final Logger logger = LoggerFactory.getLogger(SendgridEmailNotificationsService.class);

    public static final String YOUR_SENDGRID_API_KEY = "YOUR_SENDGRID_API_KEY";
    public static final String EMAIL_SEDER = "noreply@yourapp.com";
    public static final String CONTENT_TYPE = "text/plain";
    public static final String ENDPOINT = "mail/send";

    @Override
    public void send(EmailDto emailDto) {

        Email from = new Email(EMAIL_SEDER);
        Email to = new Email(emailDto.getTo());
        Content content = new Content(CONTENT_TYPE, emailDto.getBody());
        Mail mail = new Mail(from, emailDto.getSubject(), to, content);

        SendGrid sg = new SendGrid(YOUR_SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(ENDPOINT);
            request.setBody(mail.build());
            Response response = sg.api(request);
            logger.info("Send email to Sendgrid. Response status: {}", response.getStatusCode());
        } catch (Exception e) {
            logger.error("Impossible to send email", e);
        }
    }
}
