package org.cpuscheduling;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends JFrame {

    private JTable inputTable;
    private DefaultTableModel inputTableModel;
    private JTable outputTable;
    private DefaultTableModel outputTableModel;
    private JComboBox<String> algorithmComboBox;
    private JTextField quantumField;
    private JPanel ganttChartPanel;

    private List<Scheduler.GanttRecord> currentGanttChart = new ArrayList<>();

    public MainGUI() {
        setTitle("CPU Scheduling Simulator");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Panel: Control controls
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] algos = { "FCFS", "SJN", "Round Robin" };
        algorithmComboBox = new JComboBox<>(algos);
        algorithmComboBox.addActionListener(e -> {
            quantumField.setEnabled(algorithmComboBox.getSelectedItem().equals("Round Robin"));
        });

        quantumField = new JTextField("2", 5);
        quantumField.setEnabled(false);

        JButton addProcessBtn = new JButton("Add Process");
        addProcessBtn.addActionListener(e -> addProcessRow());

        JButton removeProcessBtn = new JButton("Remove Process");
        removeProcessBtn.addActionListener(e -> removeProcessRow());

        JButton runSimulationBtn = new JButton("Run Simulation");
        runSimulationBtn.addActionListener(e -> runSimulation());

        topPanel.add(new JLabel("Algorithm:"));
        topPanel.add(algorithmComboBox);
        topPanel.add(new JLabel("Time Quantum (RR):"));
        topPanel.add(quantumField);
        topPanel.add(addProcessBtn);
        topPanel.add(removeProcessBtn);
        topPanel.add(runSimulationBtn);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Panel: Input/Output Tables
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Input Table
        String[] inCols = { "Process ID", "Arrival Time", "Burst Time", "Priority" };
        inputTableModel = new DefaultTableModel(inCols, 0);
        inputTable = new JTable(inputTableModel);
        JScrollPane inScroll = new JScrollPane(inputTable);
        inScroll.setBorder(BorderFactory.createTitledBorder("Input Processes"));

        // Add some default data
        inputTableModel.addRow(new Object[] { "P1", 0, 5, 1 });
        inputTableModel.addRow(new Object[] { "P2", 1, 3, 2 });
        inputTableModel.addRow(new Object[] { "P3", 2, 8, 1 });
        inputTableModel.addRow(new Object[] { "P4", 3, 6, 3 });

        // Output Table
        String[] outCols = { "Process ID", "Arrival Time", "Burst Time", "Completion Time", "Turnaround Time",
                "Waiting Time" };
        outputTableModel = new DefaultTableModel(outCols, 0);
        outputTable = new JTable(outputTableModel);
        JScrollPane outScroll = new JScrollPane(outputTable);
        outScroll.setBorder(BorderFactory.createTitledBorder("Simulation Results"));

        centerPanel.add(inScroll);
        centerPanel.add(outScroll);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel: Gantt Chart Visualization
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Gantt Chart timeline"));
        bottomPanel.setPreferredSize(new Dimension(800, 150));

        ganttChartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGanttChart(g);
            }
        };
        ganttChartPanel.setBackground(Color.WHITE);
        bottomPanel.add(ganttChartPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addProcessRow() {
        int count = inputTableModel.getRowCount() + 1;
        inputTableModel.addRow(new Object[] { "P" + count, 0, 1, 1 });
    }

    private void removeProcessRow() {
        int selected = inputTable.getSelectedRow();
        if (selected != -1) {
            inputTableModel.removeRow(selected);
        } else if (inputTableModel.getRowCount() > 0) {
            inputTableModel.removeRow(inputTableModel.getRowCount() - 1);
        }
    }

    private void runSimulation() {
        // Collect Input Data
        List<Process> processList = new ArrayList<>();
        try {
            for (int i = 0; i < inputTableModel.getRowCount(); i++) {
                String id = inputTableModel.getValueAt(i, 0).toString();
                int arrival = Integer.parseInt(inputTableModel.getValueAt(i, 1).toString());
                int burst = Integer.parseInt(inputTableModel.getValueAt(i, 2).toString());
                int priority = Integer.parseInt(inputTableModel.getValueAt(i, 3).toString());
                processList.add(new Process(id, arrival, burst, priority));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input data. Please ensure values are integers.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one process to run.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String algoStr = (String) algorithmComboBox.getSelectedItem();
        Scheduler scheduler;

        if (algoStr.equals("FCFS")) {
            scheduler = new FCFS();
        } else if (algoStr.equals("SJN")) {
            scheduler = new SJN();
        } else {
            int quantum = 2;
            try {
                quantum = Integer.parseInt(quantumField.getText());
            } catch (Exception ex) {
                quantum = 2;
                quantumField.setText("2");
            }
            scheduler = new RR(quantum);
        }

        for (Process p : processList) {
            scheduler.addProcess(p);
        }

        // Execute Schedule
        scheduler.schedule();

        // Update Output Table
        outputTableModel.setRowCount(0);
        for (Process p : scheduler.getProcesses()) {
            outputTableModel.addRow(new Object[] {
                    p.getId(),
                    p.getArrivalTime(),
                    p.getBurstTime(),
                    p.getCompletionTime(),
                    p.getTurnaroundTime(),
                    p.getWaitingTime()
            });
        }

        // Add averages at the bottom
        outputTableModel.addRow(new Object[] {
                "AVERAGE", "-", "-", "-",
                String.format("%.2f", scheduler.getAverageTurnaroundTime()),
                String.format("%.2f", scheduler.getAverageWaitingTime())
        });

        // Update Gantt Chart
        this.currentGanttChart = scheduler.getGanttChart();
        ganttChartPanel.repaint();
    }

    private void drawGanttChart(Graphics g) {
        if (currentGanttChart == null || currentGanttChart.isEmpty())
            return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = ganttChartPanel.getWidth();
        int panelHeight = ganttChartPanel.getHeight();

        // Find total time to compute scaling
        int maxTime = currentGanttChart.get(currentGanttChart.size() - 1).endTime;
        if (maxTime == 0)
            return;

        int marginX = 20;
        int usableWidth = panelWidth - 2 * marginX;
        double scale = (double) usableWidth / maxTime;

        int rectHeight = 50;
        int y = (panelHeight - rectHeight) / 2 - 10;

        for (Scheduler.GanttRecord record : currentGanttChart) {
            int x = marginX + (int) (record.startTime * scale);
            int currentWidth = (int) ((record.endTime - record.startTime) * scale);

            // Draw Box
            g2d.setColor(new Color(100, 150, 255));
            g2d.fillRect(x, y, currentWidth, rectHeight);

            // Draw Border
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, currentWidth, rectHeight);

            // Draw Process ID
            g2d.setColor(Color.BLACK);
            FontMetrics fm = g2d.getFontMetrics();
            int strWidth = fm.stringWidth(record.processId);
            if (currentWidth >= strWidth) {
                g2d.drawString(record.processId, x + (currentWidth - strWidth) / 2, y + rectHeight / 2 + 5);
            }

            // Draw Time bounds
            g2d.drawString(String.valueOf(record.startTime), x, y + rectHeight + 15);
        }
        // Draw very last time
        int lastX = marginX + (int) (maxTime * scale);
        g2d.drawString(String.valueOf(maxTime), lastX, y + rectHeight + 15);
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}
