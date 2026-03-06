package org.example.BusinessLogic;

import org.example.GUI.Logger;
import org.example.GUI.SimulationFrame;
import org.example.Model.Server;
import org.example.Model.Task;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SimulationManager implements Runnable {
    private int timeLimit;
    private int numberOfClients;
    private int numberOfServers;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int minServiceTime;
    private int maxServiceTime;

    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> generatedTasks;
    private int currentTime;
    private List<Task> waitingClients;
    private Logger logger;

    public SimulationManager(SimulationFrame frame, String logFilePath) {
        this.frame = frame;
        this.currentTime = 0;
        this.waitingClients = new ArrayList<>();
        this.logger = new Logger(logFilePath);
        this.logger.clearLogFile();
    }

    public void setSimulationParameters(int timeLimit, int numberOfClients, int numberOfServers,
                                        int minArrivalTime, int maxArrivalTime,
                                        int minServiceTime, int maxServiceTime) {
        this.timeLimit = timeLimit;
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        this.scheduler = new Scheduler(numberOfServers);
        this.scheduler.setStrategy(new ConcreteSTrategyQueue());
    }

    private void generateTasks() {
        generatedTasks = new ArrayList<>();
        waitingClients = new ArrayList<>();
        int i = 1;
        while (i <= numberOfClients) {
            Task task = Task.generateRandomTask(i, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
            generatedTasks.add(task);
            waitingClients.add(task);
            i++;
        }

        generatedTasks.sort(new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return Integer.compare(t1.getArrivalTime(), t2.getArrivalTime());
            }
        });

        waitingClients.sort(new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return Integer.compare(t1.getArrivalTime(), t2.getArrivalTime());
            }
        });
    }

    @Override
    public void run() {
        generateTasks();
        try {
            while (currentTime <= timeLimit || !waitingClients.isEmpty()) {

                Iterator<Task> iterator = waitingClients.iterator();
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (task.getArrivalTime() == currentTime) {
                        scheduler.dispatchTask(task);
                        iterator.remove();
                    }
                }

                List<Server> servers = scheduler.getServers();
                int i = 0;
                while (i < servers.size()) {
                    Server server = servers.get(i);
                    if (!server.getQueue().isEmpty()) {
                        Task task = server.getQueue().peek();
                        if (task != null) {
                            task.incrementWaitingTime();
                            task.decrementServiceTime();
                            if (task.getServiceTime() == 0) {
                                task.setDepartureTime(currentTime + 1);
                                server.removeTask();
                            }
                        }
                    }
                    i++;
                }

                frame.updateUI(currentTime, waitingClients, servers);
                logger.logEvent(currentTime, waitingClients, servers);

                Thread.sleep(1000);
                currentTime++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            scheduler.shutdown();
            calculateStatistics();
        }
    }

    private void calculateStatistics() {
        double totalWaitingTime = 0;
        double totalServiceTime = 0;
        int peakHour = 0;
        int maxClients = 0;

        int i = 0;
        while (i < generatedTasks.size()) {
            Task task = generatedTasks.get(i);
            totalWaitingTime += task.getWaitingTime();
            totalServiceTime += task.getInitialServiceTime();
            i++;
        }

        double averageWaitingTime = totalWaitingTime / generatedTasks.size();
        double averageServiceTime = totalServiceTime / generatedTasks.size();

        i = 0;
        while (i <= timeLimit) {
            int currentClients = 0;
            int j = 0;
            while (j < generatedTasks.size()) {
                Task task = generatedTasks.get(j);
                if (task.getArrivalTime() <= i && task.getDepartureTime() > i) {
                    currentClients++;
                }
                j++;
            }

            if (currentClients > maxClients) {
                maxClients = currentClients;
                peakHour = i;
            }
            i++;
        }

        logger.logStatistics(averageWaitingTime, averageServiceTime, peakHour);
        frame.showStatistics(averageWaitingTime, averageServiceTime, peakHour);
    }

    public void startSimulation() {
        Thread simulationThread = new Thread(this);
        simulationThread.start();
    }

    public void runTest(int testNumber, int n, int q, int tmax, int arrivalMin, int arrivalMax, int serviceMin, int serviceMax) {
        String logFileName = "test" + testNumber + ".txt";
        logger = new Logger(logFileName);
        logger.clearLogFile();

        setSimulationParameters(tmax, n, q, arrivalMin, arrivalMax, serviceMin, serviceMax);
        startSimulation();
    }
}
