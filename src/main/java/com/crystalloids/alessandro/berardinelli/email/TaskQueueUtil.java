package com.crystalloids.alessandro.berardinelli.email;

import com.google.cloud.tasks.v2.*;
import com.google.protobuf.ByteString;

import java.nio.charset.StandardCharsets;

public class TaskQueueUtil {

    public static void enqueueEmailTask(String to, String subject, String body) {
        try {
            String projectId = "xxxxxxxx-205215";
            String location = "us-central1";
            String queue = "email-queue";
            String url = "https://" + projectId + ".appspot.com/_ah/api/taskApi/v1/task/sendEmail";

            String payload = String.format(
                    "{\"to\":\"%s\",\"subject\":\"%s\",\"body\":\"%s\"}",
                    to, subject.replace("\"", "\\\""), body.replace("\"", "\\\"")
            );

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .setHttpMethod(HttpMethod.POST)
                    .setUrl(url)
                    .putHeaders("Content-Type", "application/json")
                    .setBody(ByteString.copyFrom(payload, StandardCharsets.UTF_8))
                    .build();

            Task task = Task.newBuilder().setHttpRequest(httpRequest).build();
            QueueName queueName = QueueName.of(projectId, location, queue);

            try (CloudTasksClient client = CloudTasksClient.create()) {
                client.createTask(queueName, task);
                System.out.println("Task enqueued to send email to " + to);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



