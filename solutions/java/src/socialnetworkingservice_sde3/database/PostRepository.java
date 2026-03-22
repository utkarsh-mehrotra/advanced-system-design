package socialnetworkingservice_sde3.database;

import socialnetworkingservice_sde3.model.Post;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Simulates Cassandra / PostgreSQL for durable Post storage.
 */
public class PostRepository {
    private final ConcurrentHashMap<String, Post> postsTable = new ConcurrentHashMap<>();
    
    // Secondary index for fast querying by author (Partition Key = authorId)
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Post>> postsByAuthorIndex = new ConcurrentHashMap<>();

    public void save(Post post) {
        postsTable.put(post.getId(), post);
        postsByAuthorIndex.computeIfAbsent(post.getAuthorId(), k -> new CopyOnWriteArrayList<>()).add(0, post); // Add to head
    }

    public Post getPost(String postId) {
        return postsTable.get(postId);
    }

    /** Retrieves recent posts for Pull-model timelines (e.g. for Celebrities) */
    public List<Post> getRecentPostsByAuthor(String authorId, int limit) {
        CopyOnWriteArrayList<Post> authorPosts = postsByAuthorIndex.get(authorId);
        if (authorPosts == null) return List.of();
        return authorPosts.stream().limit(limit).collect(Collectors.toList());
    }
}
