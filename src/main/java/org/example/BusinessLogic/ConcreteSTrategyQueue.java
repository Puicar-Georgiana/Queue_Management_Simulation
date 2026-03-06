package org.example.BusinessLogic;

import org.example.Model.Server;
import org.example.Model.Task;

import java.util.List;

public class ConcreteSTrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        if (servers == null || servers.isEmpty()) {
            return;
        }

        Server targetServer = servers.get(0);
        int minQueueSize = targetServer.getQueueSize();

        for (int i = 1; i < servers.size(); i++) {
            Server currentServer = servers.get(i);
            int currentQueueSize = currentServer.getQueueSize();

            if (currentQueueSize < minQueueSize) {
                targetServer = currentServer;
                minQueueSize = currentQueueSize;
            }
        }

        targetServer.addTask(task);
    }
}