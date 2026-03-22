# ✅ Task Management System (Jira) — SDE3 Upgraded

## Overview
A Jira-style task lifecycle management system supporting task creation, state transitions, assignment, and full audit logging. The GoF State Pattern makes illegal transitions (e.g., COMPLETED → IN_PROGRESS without reopening) structurally impossible rather than guarded by conditionals.

## SDE3 Upgrades Applied

| Issue | Fix |
|-------|-----|
| Any code can call `task.setStatus(COMPLETED)` from any state | `PendingState`, `InProgressState`, `CompletedState` concrete objects — invalid transitions throw `IllegalStateException` |
| No record of who changed what and when | `AuditObserver` with `ExecutorService` fires immutable `TaskHistoryEntry` events asynchronously |
| O(N) linear `for` loop in `searchTasks` | `parallelStream().filter()` using all CPU cores |

## Class Diagram

```mermaid
classDiagram
    class TaskManagementFacade {
        -ConcurrentHashMap~String,Task~ taskStore
        -AuditObserver auditObserver
        +createTask(title, desc, priority, dueDate, assignee) Task
        +startTask(taskId, triggeredBy)
        +completeTask(taskId, triggeredBy)
        +reopenTask(taskId, triggeredBy)
        +searchTasks(keyword) List~Task~
    }
    class Task {
        -String id
        -TaskState state
        +startProgress()
        +complete()
        +reopen()
        +getStatus() String
    }
    class TaskState {
        <<interface>>
        +startProgress(Task) TaskState
        +complete(Task) TaskState
        +reopen(Task) TaskState
        +getStatusName() String
    }
    class PendingState
    class InProgressState
    class CompletedState
    class AuditObserver {
        -ExecutorService auditPool
        -CopyOnWriteArrayList~TaskHistoryEntry~ auditLog
        +recordTransition(taskId, user, from, to)
    }
    class TaskHistoryEntry {
        -String taskId
        -String triggeredByUser
        -String fromState
        -String toState
        -Instant timestamp
    }

    TaskManagementFacade "1" *-- "many" Task
    TaskManagementFacade --> AuditObserver
    Task --> TaskState
    TaskState <|.. PendingState
    TaskState <|.. InProgressState
    TaskState <|.. CompletedState
    AuditObserver "1" *-- "many" TaskHistoryEntry
```

## Run
```bash
javac $(find taskmanagementsystem_upgraded -name "*.java")
java taskmanagementsystem_upgraded.TaskManagementDemoUpgraded
```
