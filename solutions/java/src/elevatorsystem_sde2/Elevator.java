package elevatorsystem_sde2;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Elevator implements Runnable {
    private final int id;
    private int currentFloor;
    private Direction currentDirection;
    
    // SDE3: Implementing the SCAN algorithm via Priority Queues
    // upStops is a Min-Heap (closest floors processed first as we go up)
    private final PriorityQueue<Integer> upStops;
    // downStops is a Max-Heap (closest floors processed first as we go down)
    private final PriorityQueue<Integer> downStops;

    // SDE3: Abandoning blind wait/notify for explicit locks
    private final Lock lock;
    private final Condition stateChangedCondition;

    public Elevator(int id, int capacity) {
        this.id = id;
        this.currentFloor = 1;
        this.currentDirection = Direction.IDLE;
        
        // Min Heap
        this.upStops = new PriorityQueue<>();
        // Max Heap
        this.downStops = new PriorityQueue<>((a, b) -> b - a);

        this.lock = new ReentrantLock();
        this.stateChangedCondition = lock.newCondition();
    }

    public void addRequest(Request request) {
        lock.lock();
        try {
            // Break down the passenger's request into two physical elevator sweeps
            addStopAndRebalance(request.getSourceFloor());
            addStopAndRebalance(request.getDestinationFloor());
            stateChangedCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private void addStopAndRebalance(int floor) {
        if (floor == currentFloor) return; // We are already here!

        // If explicitly moving, obey the vector
        if (floor > currentFloor) {
            if (!upStops.contains(floor)) upStops.offer(floor);
        } else {
            if (!downStops.contains(floor)) downStops.offer(floor);
        }
    }

    @Override
    public void run() {
        processRequests();
    }

    private void processRequests() {
        while (true) {
            lock.lock();
            try {
                while (upStops.isEmpty() && downStops.isEmpty()) {
                    currentDirection = Direction.IDLE;
                    stateChangedCondition.await(); // Sleep explicitly waiting for work
                }
                
                // Determine direction based on SCAN algorithm logic
                if (currentDirection == Direction.IDLE) {
                    currentDirection = !upStops.isEmpty() ? Direction.UP : Direction.DOWN;
                }
                
                int nextFloor = -1;
                
                if (currentDirection == Direction.UP) {
                    if (!upStops.isEmpty()) {
                        nextFloor = upStops.poll();
                    } else {
                        currentDirection = Direction.DOWN;
                        continue;
                    }
                } else if (currentDirection == Direction.DOWN) {
                    if (!downStops.isEmpty()) {
                        nextFloor = downStops.poll();
                    } else {
                        currentDirection = Direction.UP;
                        continue;
                    }
                }
                
                if (nextFloor != -1) {
                    moveElevator(nextFloor);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                lock.unlock();
            }
        }
    }

    // Must be called with lock held to mutate state safely!
    private void moveElevator(int targetFloor) throws InterruptedException {
        // Unlock while sleeping so we can still accept requests while moving!
        lock.unlock();
        try {
            System.out.println("Elevator " + id + " moving from " + currentFloor + " to " + targetFloor + " (" + currentDirection + ")");
            Thread.sleep(Math.abs(currentFloor - targetFloor) * 200L); // 200ms per floor instead of slow 1000
        } finally {
            // Reacquire lock to mutate state
            lock.lock();
        }
        
        this.currentFloor = targetFloor;
        System.out.println("Elevator " + id + " arrived at floor " + currentFloor);
        
        // Simulate doors opening/closing
        lock.unlock();
        try {
            Thread.sleep(100); 
        } finally {
            lock.lock();
        }
    }

    // Thread-safe accessors
    public int getCurrentFloor() {
        lock.lock();
        try {
            return currentFloor;
        } finally {
            lock.unlock();
        }
    }

    public Direction getCurrentDirection() {
        lock.lock();
        try {
            return currentDirection;
        } finally {
            lock.unlock();
        }
    }
}
