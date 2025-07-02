package com.crystalloids.alessandro.berardinelli.email;

import com.google.cloud.tasks.v2.*;
import com.google.protobuf.ByteString;

import java.nio.charset.StandardCharsets;

public class TaskQueueUtil {

    private static final String PROJECT_ID = "xxxxxxxx-205215";
    private static final String LOCATION = "us-central1";
    private static final String QUEUE_NAME = "email-queue";
    private static final String URL = "https://" + PROJECT_ID + ".appspot.com/_ah/api/taskApi/v1/task/sendEmail";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";


    public void enqueueEmailTask(String to, String subject, String body) {

        try (CloudTasksClient client = CloudTasksClient.create()) {

            String payload = buildPayload(to, subject, body);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .setHttpMethod(HttpMethod.POST)
                    .setUrl(URL)
                    .putHeaders(CONTENT_TYPE, APPLICATION_JSON)
                    .setBody(ByteString.copyFrom(payload, StandardCharsets.UTF_8))
                    .build();

            Task task = Task.newBuilder().setHttpRequest(httpRequest).build();
            QueueName queueName = QueueName.of(PROJECT_ID, LOCATION, QUEUE_NAME);

            client.createTask(queueName, task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildPayload(String to, String subject, String body) {
        String payload = String.format(
                "{\"to\":\"%s\",\"subject\":\"%s\",\"body\":\"%s\"}",
                to, subject.replace("\"", "\\\""), body.replace("\"", "\\\"")
        );
        return payload;
    }
}



