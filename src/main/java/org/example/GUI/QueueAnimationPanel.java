package org.example.GUI;

import org.example.Model.Server;
import org.example.Model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class QueueAnimationPanel extends JPanel {
    private List<Server> servers;
    private List<Task> waitingClients;

    public QueueAnimationPanel() {
        setPreferredSize(new Dimension(800, 300));
    }

    public void updateAnimation(List<Server> servers, List<Task> waitingClients) {
        this.servers = servers;
        this.waitingClients = waitingClients;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (servers == null || waitingClients == null) return;

        g.setColor(Color.RED);
        int x = 50;
        for (int i = 0; i < waitingClients.size(); i++) {
            Task client = waitingClients.get(i);
            g.fillOval(x, 50, 30, 30);
            g.setColor(Color.GREEN);
            g.drawString(String.valueOf(client.getID()), x + 5, 65);
            x += 40;
        }

        g.setColor(Color.GREEN);
        int queueY = 120;
        for (int i = 0; i < servers.size(); i++) {
            Server server = servers.get(i);
            g.drawRect(50, queueY, 150, 50);
            g.drawString("Queue " + (i + 1), 70, queueY + 20);

            List<Task> queueTasks = new ArrayList<>(server.getQueue());
            x = 120;
            for (int j = 0; j < queueTasks.size(); j++) {
                Task task = queueTasks.get(j);
                g.setColor(Color.RED);
                g.fillOval(x, queueY + 10, 30, 30);
                g.setColor(Color.BLACK);
                g.drawString(String.valueOf(task.getID()), x + 5, queueY + 35);
                x += 40;
            }

            queueY += 70;
        }
    }
}
