package org.example.Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private BlockingQueue<Task> taskQueue;
    private AtomicInteger waitingTime;
    private boolean running;

    public Server() {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.waitingTime = new AtomicInteger(0);
        this.running = true;
    }

    public void addTask(Task task) {
        taskQueue.add(task);
        waitingTime.addAndGet(task.getInitialServiceTime());
    }

    public void removeTask() {
        if (!taskQueue.isEmpty()) {
            Task task = taskQueue.poll();
            if (task != null) {
                waitingTime.addAndGet(-task.getInitialServiceTime());
            }
        }
    }

    public int getWaitingTime() {
        return waitingTime.get();
    }

    public int getQueueSize() {
        return taskQueue.size();
    }

    public void shutdown() {
        this.running = false;
    }

    public BlockingQueue<Task> getQueue() {
        return taskQueue;
    }
}