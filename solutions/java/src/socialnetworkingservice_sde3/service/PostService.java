package socialnetworkingservice_sde3.service;

import socialnetworkingservice_sde3.database.PostRepository;
import socialnetworkingservice_sde3.messaging.MessageBus;
import socialnetworkingservice_sde3.model.Post;
import socialnetworkingservice_sde3.model.events.PostCreatedEvent;

/**
 * Handles authoring of posts.
 * Persists the post to the database and raises the domain event.
 */
public class PostService {
    private final PostRepository postDb;

    public PostService(PostRepository postDb) {
        this.postDb = postDb;
    }

    public Post createPost(String authorId, String content) {
        Post post = new Post(authorId, content);
        
        // 1. Synchronously save to durable primary store (Cassandra)
        postDb.save(post);
        System.out.println("[POST SERVICE] Persisted " + post);

        // 2. Asynchronously publish to MessageBus (Kafka topic: `posts.created`)
        MessageBus.getInstance().publishPostCreated(new PostCreatedEvent(post));

        return post;
    }
}
