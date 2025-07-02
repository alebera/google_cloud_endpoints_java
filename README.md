# Blog API - Usage Guide

This is a simple java web app to expose CRUD operations on blog posts with comments.

Technologies used:
- Google Cloud App Engine
- Google Cloud Endpoints
- Google Cloud Datastore Firestore
- Google Cloud Firebase Auth

Prerequisites:
- Java 8
- Maven
- Google Cloud SDK

## Setup

```shell
gcloud auth application-default login
gcloud config set project xxxxxxxx-205215
export DATASTORE_PROJECT_ID=xxxxxxxx-205215
mvn clean install
mvn appengine:run -Dappengine.projectId=xxxxxxxx-205215
```

https://cloud.google.com/endpoints/docs/frameworks/java/get-started-frameworks-java

## Data example:

Here an example of Post and Comment data:

# Post :
```json
{
"id": 1,
"author": "userXXXXXX@example.com",
"subject": "Sample Post Subject",
"body": "This is the body of the post.",
"createdAt": "2025-06-30T12:34:56",
"updatedAt": "2025-06-30T12:34:56"
}
```

# Comment
```json
{
  "id": 1,
  "postId": 1,
  "author": "userXXXXXX@example.com",
  "body": "This is the body of the post.",
  "createdAt": "2025-06-30T12:34:56"
}
```


## Test the APIs

# 1. Create a new post 
This endpoint is protected by JWT
```shell
curl -X POST -H "Content-Type: application/json" \
-H "Authorization: Bearer <YOUR_ID_TOKEN>" \
-d '{"author":"John Doe","subject":"Title","body":"My first post"}' \
http://localhost:8080/_ah/api/post/v1/posts
```

# 2. Get a post by ID (replace {postId} with the real post ID)
```shell
curl http://localhost:8080/_ah/api/post/v1/posts/{postId}
```

# 3. Update a post (replace {postId} with the real post ID)
This endpoint is protected by JWT
```shell
curl -X PUT -H "Content-Type: application/json" \
-H "Authorization: Bearer <YOUR_ID_TOKEN>" \
-d '{"id":1", author":"John Doe","subject":"Title","body":"My first post", "createdAt": "2025-06-30T12:34:56"}' \ \
http://localhost:8080/_ah/api/post/v1/posts/{postId}
```

# 4. List all posts
```shell
curl http://localhost:8080/_ah/api/post/v1/posts
```

# 5. Add a comment
This endpoint is protected by JWT
```shell
curl -X POST -H "Content-Type: application/json" \
-H "Authorization: Bearer <YOUR_ID_TOKEN>" \
-d '{"postId":1, "author":"Max Delpiero","body":"Nice post!"}' \
http://localhost:8080/_ah/api/comment/v1/posts/1/comments
```

# 6. List comments for a post (replace {postId} with the real post ID)
```shell
curl http://localhost:8080/_ah/api/comment/v1/posts/{postId}/comments
```