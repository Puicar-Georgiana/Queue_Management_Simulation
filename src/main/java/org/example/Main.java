package org.example;

import org.example.GUI.SimulationFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimulationFrame().setVisible(true);
            }
        });
    }
}