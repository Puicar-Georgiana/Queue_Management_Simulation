package org.example.GUI;

import org.example.BusinessLogic.SimulationManager;
import org.example.Model.Server;
import org.example.Model.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SimulationFrame extends JFrame {
    private JTextField nField;
    private JTextField qField;
    private JTextField tmaxField;
    private JTextField arrivalMinField, arrivalMaxField, serviceMinField, serviceMaxField;
    private JButton generateButton, test1Button, test2Button, test3Button;
    private JTable taskTable;
    private DefaultTableModel tableModel;
    private SimulationManager simulationManager;
    private QueueAnimationPanel animationPanel;

    public SimulationFrame() {
        setTitle("Queue Management Simulation");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
    }

    private void initializeComponents() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Simulation Parameters"));

        createInputField(inputPanel, "Number of Clients (N):", nField = new JTextField(10));
        createInputField(inputPanel, "Number of Queues (Q):", qField = new JTextField(10));
        createInputField(inputPanel, "Simulation Interval (tmax):", tmaxField = new JTextField(10));
        createInputField(inputPanel, "Arrival Time Min:", arrivalMinField = new JTextField(10));
        createInputField(inputPanel, "Arrival Time Max:", arrivalMaxField = new JTextField(10));
        createInputField(inputPanel, "Service Time Min:", serviceMinField = new JTextField(10));
        createInputField(inputPanel, "Service Time Max:", serviceMaxField = new JTextField(10));

        generateButton = new JButton("Start Simulation");
        generateButton.addActionListener(e -> startCustomSimulation());

        test1Button = new JButton("Run Test 1");
        test1Button.addActionListener(e -> runTest(1, 4, 2, 60, 2, 30, 2, 4));

        test2Button = new JButton("Run Test 2");
        test2Button.addActionListener(e -> runTest(2, 50, 5, 60, 2, 40, 1, 7));

        test3Button = new JButton("Run Test 3");
        test3Button.addActionListener(e -> runTest(3, 1000, 20, 200, 10, 100, 3, 9));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateButton);
        buttonPanel.add(test1Button);
        buttonPanel.add(test2Button);
        buttonPanel.add(test3Button);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Time");
        tableModel.addColumn("Waiting Clients");
        tableModel.addColumn("Queue 1");
        tableModel.addColumn("Queue 2");

        taskTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(taskTable);

        animationPanel = new QueueAnimationPanel();

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.EAST);
        add(animationPanel, BorderLayout.SOUTH);
    }

    private void createInputField(JPanel panel, String labelText, JTextField textField) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.add(new JLabel(labelText), BorderLayout.WEST);
        fieldPanel.add(textField, BorderLayout.CENTER);
        panel.add(fieldPanel);
    }

    private void startCustomSimulation() {
        try {
            int n = Integer.parseInt(nField.getText().trim());
            int q = Integer.parseInt(qField.getText().trim());
            int tmax = Integer.parseInt(tmaxField.getText().trim());
            int arrivalMin = Integer.parseInt(arrivalMinField.getText().trim());
            int arrivalMax = Integer.parseInt(arrivalMaxField.getText().trim());
            int serviceMin = Integer.parseInt(serviceMinField.getText().trim());
            int serviceMax = Integer.parseInt(serviceMaxField.getText().trim());

            simulationManager = new SimulationManager(this, "simulation.txt");
            simulationManager.setSimulationParameters(tmax, n, q, arrivalMin, arrivalMax, serviceMin, serviceMax);
            simulationManager.startSimulation();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateUI(int currentTime, List<Task> waitingClients, List<Server> servers) {
        String waitingClientsStr;
        if (waitingClients.isEmpty()) {
            waitingClientsStr = "Empty";
        } else {
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < waitingClients.size()) {
                sb.append(waitingClients.get(i).toString());
                if (i < waitingClients.size() - 1) {
                    sb.append(", ");
                }
                i++;
            }
            waitingClientsStr = sb.toString();
        }

        String queuesStr = "";
        int j = 0;
        while (j < servers.size()) {
            Server server = servers.get(j);
            if (server.getQueue().isEmpty()) {
                queuesStr += "closed ";
            } else {
                queuesStr += server.getQueue().toString() + " ";
            }
            j++;
        }

        tableModel.addRow(new Object[]{currentTime, waitingClientsStr, queuesStr});
        animationPanel.updateAnimation(servers, waitingClients);
    }

    public void showStatistics(double averageWaitingTime, double averageServiceTime, int peakHour) {
        String stats = String.format(
                "--- Simulation Summary ---\n" +
                        "Average Waiting Time: %.2f\n" +
                        "Average Service Time: %.2f\n" +
                        "Peak Hour: %d\n" +
                        "---------------------------",
                averageWaitingTime, averageServiceTime, peakHour
        );
        JOptionPane.showMessageDialog(this, stats, "Simulation Results", JOptionPane.INFORMATION_MESSAGE);
    }

    public void runTest(int testNumber, int n, int q, int tmax, int arrivalMin, int arrivalMax, int serviceMin, int serviceMax) {
        simulationManager = new SimulationManager(this, "test" + testNumber + ".txt");
        simulationManager.setSimulationParameters(tmax, n, q, arrivalMin, arrivalMax, serviceMin, serviceMax);
        simulationManager.startSimulation();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SimulationFrame().setVisible(true);
            }
        });
    }
}
