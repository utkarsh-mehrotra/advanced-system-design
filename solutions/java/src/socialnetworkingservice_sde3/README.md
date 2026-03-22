# 👥 Social Networking Service — SDE3 Redesign

The legacy single-node implementation struggled with the **Celebrity Fan-Out Problem** where writing a post required iterating over millions of followers' lists in memory, creating catastrophic `ConcurrentModificationException` and OutOfMemory errors.

This project implements an SDE3 Distributed Systems approach utilizing the **Hybrid Push/Pull Architecture**.

## Architecture & Data Flow

1. **Gateways and Microservices:** `SocialGraphService`, `PostService`, `NewsfeedService`.
2. **Message Broker (Kafka):** Every created post triggers a `PostCreatedEvent` on the bus. This allows the API to return HTTP 200 immediately (No blocking on timeline generation!).
3. **Fan-Out Worker:** An async consumer listens to the `PostCreatedEvent`.
   - **For Normal Users:** Executes `Fan-Out-On-Write`. It pushes the Post ID directly to their followers' materialized `RedisCache` lists.
   - **For Celebrities:** Ignores the event (Drops it).
4. **Hybrid Merging on Read:** When a user opens their app, the `NewsfeedService` executes:
   - **O(1)** fetch of their pre-calculated `RedisCache` timeline.
   - **Pull-On-Demand** fetch from the `PostRepository` for the celebrities they follow.
   - Aggregates and sorts them instantly.

## Run the Demo
```bash
javac $(find . -name "*.java")
java socialnetworkingservice_sde3.SDE3SocialNetworkingDemo
```
