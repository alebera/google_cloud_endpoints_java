gcloud config set project xxxxxxxx-205215
mvn clean install
mvn appengine:run -Dappengine.projectId=xxxxxxxx-205215

https://cloud.google.com/endpoints/docs/frameworks/java/get-started-frameworks-java


## CURL commands for APIs

# 1. Create a new post
curl -X POST -H "Content-Type: application/json" -d '{"title":"Test Post","content":"Hello world"}' http://localhost:8080/_ah/api/postApi/v1/posts

# 2. Get a post by ID (replace {id} with the real ID)
curl http://localhost:8080/_ah/api/postApi/v1/posts/{id}

# 3. Update a post (replace the fields and the ID)
curl -X PUT -H "Content-Type: application/json" -d '{"id":1,"title":"Updated","content":"Updated content"}' http://localhost:8080/_ah/api/postApi/v1/posts

# 4. List all posts
curl http://localhost:8080/_ah/api/postApi/v1/posts

# 5. Add a comment
curl -X POST -H "Content-Type: application/json" -d '{"postId":1,"content":"Nice post!"}' http://localhost:8080/_ah/api/commentApi/v1/comments

# 6. List comments for a post (replace {postId} with the real ID)
curl http://localhost:8080/_ah/api/commentApi/v1/comments/{postId}