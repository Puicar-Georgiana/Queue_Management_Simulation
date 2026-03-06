package org.example.GUI;

import org.example.Model.Server;
import org.example.Model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Queue;

public class Logger {
    private String logFilePath;

    public Logger(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void logEvent(int currentTime, List<Task> waitingClients, List<Server> servers) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append(String.format("Time %d:\n", currentTime));

        logEntry.append("Waiting Clients: ");
        if (waitingClients.isEmpty()) {
            logEntry.append("Empty\n");
        } else {
            int i = 0;
            while (i < waitingClients.size()) {
                Task client = waitingClients.get(i);
                logEntry.append(String.format("(%d, %d, %d); ", client.getID(), client.getArrivalTime(), client.getServiceTime()));
                i++;
            }
            logEntry.append("\n");
        }

        int i = 0;
        while (i < servers.size()) {
            Server server = servers.get(i);
            logEntry.append(String.format("Queue %d: ", i));
            Queue<Task> queue = server.getQueue();
            if (queue.isEmpty()) {
                logEntry.append("closed\n");
            } else {
                Object[] tasks = queue.toArray();
                int j = 0;
                while (j < tasks.length) {
                    Task task = (Task) tasks[j];
                    logEntry.append(String.format("(%d, %d, %d); ", task.getID(), task.getArrivalTime(), task.getServiceTime()));
                    j++;
                }
                logEntry.append("\n");
            }
            i++;
        }

        logEntry.append("\n");

        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write(logEntry.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logStatistics(double averageWaitingTime, double averageServiceTime, int peakHour) {
        StringBuilder logEntry = new StringBuilder();
        logEntry.append("\n--- Simulation Summary ---\n");
        logEntry.append(String.format("Average Waiting Time: %.2f\n", averageWaitingTime));
        logEntry.append(String.format("Average Service Time: %.2f\n", averageServiceTime));
        logEntry.append(String.format("Peak Hour: %d\n", peakHour));
        logEntry.append("---------------------------\n");

        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write(logEntry.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearLogFile() {
        try (FileWriter writer = new FileWriter(logFilePath, false)) {
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
