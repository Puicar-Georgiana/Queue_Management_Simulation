package org.example.Model;

import java.util.Random;

public class Task {
    private int ID;
    private int arrivalTime;
    private int initialServiceTime;
    private int serviceTime;
    private int waitingTime;
    private int departureTime;

    public Task(int ID, int arrivalTime, int serviceTime) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.initialServiceTime = serviceTime;
        this.serviceTime = serviceTime;
        this.waitingTime = 0;
        this.departureTime = -1;
    }

    public static Task generateRandomTask(int ID, int minArrival, int maxArrival, int minService, int maxService) {
        Random random = new Random();
        int arrivalTime = minArrival + random.nextInt(maxArrival - minArrival + 1);
        int serviceTime = minService + random.nextInt(maxService - minService + 1);

        if (serviceTime == 0) {
            serviceTime = 1;
        }

        return new Task(ID, arrivalTime, serviceTime);
    }

    public int getID() {
        return ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void decrementServiceTime() {
        if (serviceTime > 0) {
            serviceTime--;
        }
    }

    public void incrementWaitingTime() {
        waitingTime++;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getInitialServiceTime() {
        return initialServiceTime;
    }

    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d)", ID, arrivalTime, serviceTime);
    }
}