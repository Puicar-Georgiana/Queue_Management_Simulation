package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Server> servers;
    private Strategy strategy;

    public Scheduler(int numberOfServers) {
        this.servers = new ArrayList<>();
        int i = 0;
        while (i < numberOfServers) {
            Server server = new Server();
            servers.add(server);
            i++;
        }
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void dispatchTask(Task task) {
        if (strategy != null) {
            strategy.addTask(servers, task);
        } else {
            throw new IllegalStateException("Strategy not set! Call setStrategy() first.");
        }
    }

    public List<Server> getServers() {
        return servers;
    }

    public void shutdown() {
        int i = 0;
        while (i < servers.size()) {
            servers.get(i).shutdown();
            i++;
        }
    }

    public Strategy getStrategy() {
        return strategy;
    }
}
