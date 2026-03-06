package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.List;

public class ConcreteSTrategyTime implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        if (servers == null || servers.isEmpty()) {
            return;
        }

        Server targetServer = servers.get(0);
        int minWaitingTime = targetServer.getWaitingTime();

        for (int i = 1; i < servers.size(); i++) {
            Server currentServer = servers.get(i);
            int currentWaitingTime = currentServer.getWaitingTime();

            if (currentWaitingTime < minWaitingTime) {
                targetServer = currentServer;
                minWaitingTime = currentWaitingTime;
            }
        }

        targetServer.addTask(task);
    }
}