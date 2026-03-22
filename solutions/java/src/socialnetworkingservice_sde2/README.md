# 👥 Social Networking Service — SDE3 Upgraded

## Overview
A Facebook/Instagram-style social platform with user connections, post creation, newsfeed generation, and likes/comments. The SDE3 upgrade replaces the catastrophically slow O(N×M) pull-model feed with a Fan-Out-On-Write push architecture.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| `getNewsfeed()` traverses every friend's post list at read time — O(N×M) | `NewsfeedService` fans post IDs into each friend's pre-computed `newsfeedPostIds` list asynchronously — O(1) feed reads |
| `ArrayList<String> likes` throws `ConcurrentModificationException` under viral load | `CopyOnWriteArrayList` for likes, comments, and friend lists |
| Monolithic service | Split into `SocialNetworkingFacade`, `NewsfeedService`, `NotificationService` |

## Class Diagram

```mermaid
classDiagram
    class SocialNetworkingFacade {
        -Map~String,User~ users
        -Map~String,Post~ posts
        -NewsfeedService newsfeedService
        -NotificationService notificationService
        +addFriend(userIdA, userIdB)
        +createPost(userId, content) Post
        +getNewsfeed(userId) List~Post~
        +likePost(userId, postId)
    }
    class NewsfeedService {
        -ExecutorService fanOutPool
        +pushPostToFollowersTimeline(authorId, postId)
    }
    class NotificationService {
        -ExecutorService dispatchPool
        +dispatchNotification(userId, type, message)
    }
    class User {
        -String id
        -CopyOnWriteArrayList~String~ friendIds
        -CopyOnWriteArrayList~String~ newsfeedPostIds
    }
    class Post {
        -String id
        -CopyOnWriteArrayList~String~ likes
        -CopyOnWriteArrayList~Comment~ comments
    }

    SocialNetworkingFacade --> NewsfeedService
    SocialNetworkingFacade --> NotificationService
    SocialNetworkingFacade "1" *-- "many" User
    SocialNetworkingFacade "1" *-- "many" Post
    User ..> Post : newsfeed references
```

## Run
```bash
javac $(find socialnetworkingservice_upgraded -name "*.java")
java socialnetworkingservice_upgraded.SocialNetworkingDemoUpgraded
```
