package com.example.echo.email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.json.JSONObject;

public class EmailUtil {

    public static void sendEmailFromJson(String json) {
        JSONObject obj = new JSONObject(json);
        String toEmail = obj.getString("to");
        String subject = obj.getString("subject");
        String body = obj.getString("body");

        Email from = new Email("noreply@yourapp.com");
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("YOUR_SENDGRID_API_KEY");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("EMAIL STATUS: " + response.getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
