package org.hospitalscheduling;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        setTitle("Smart Hospital Task Scheduling Simulator");
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

        JButton addTaskBtn = new JButton("Add Process");
        addTaskBtn.addActionListener(e -> addTaskRow());

        JButton removeTaskBtn = new JButton("Remove Process");
        removeTaskBtn.addActionListener(e -> removeTaskRow());

        JButton runSimulationBtn = new JButton("Run Simulation");
        runSimulationBtn.addActionListener(e -> runSimulation());

        topPanel.add(new JLabel("Algorithm:"));
        topPanel.add(algorithmComboBox);
        topPanel.add(new JLabel("Time Quantum (RR):"));
        topPanel.add(quantumField);
        topPanel.add(addTaskBtn);
        topPanel.add(removeTaskBtn);
        topPanel.add(runSimulationBtn);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Center Panel: Input/Output Tables
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Input Table
        String[] inCols = { "Task Name", "Arrival Time", "Task Duration", "Priority" };
        inputTableModel = new DefaultTableModel(inCols, 0);
        inputTable = new JTable(inputTableModel);
        JScrollPane inScroll = new JScrollPane(inputTable);
        inScroll.setBorder(BorderFactory.createTitledBorder("Input Hospital Tasks"));

        // Add default hospital data
        inputTableModel.addRow(new Object[] { "Emergency Patient", 0, 3, 1 });
        inputTableModel.addRow(new Object[] { "Billing", 1, 5, 2 });
        inputTableModel.addRow(new Object[] { "Lab Report", 2, 4, 2 });
        inputTableModel.addRow(new Object[] { "Appointment Booking", 3, 2, 3 });

        // Output Table
        String[] outCols = { "Task Name", "Arrival Time", "Task Duration", "Completion Time", "Turnaround Time",
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
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Task Execution Timeline (Gantt Chart)"));
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

    private final String[] RELATABLE_TASKS = {
        "X-Ray Scan", "Blood Test", "Surgery Prep", "Patient Discharge",
        "Doctor Consult", "Pharmacy Dispense", "Vitals Check", "MRI Scan"
    };

    private void addTaskRow() {
        int count = inputTableModel.getRowCount() + 1;
        String taskName = RELATABLE_TASKS[(count - 1) % RELATABLE_TASKS.length] + " " + count;
        inputTableModel.addRow(new Object[] { taskName, 0, 1, 1 });
    }

    private void removeTaskRow() {
        int selected = inputTable.getSelectedRow();
        if (selected != -1) {
            inputTableModel.removeRow(selected);
        } else if (inputTableModel.getRowCount() > 0) {
            inputTableModel.removeRow(inputTableModel.getRowCount() - 1);
        }
    }

    private void runSimulation() {
        // Collect Input Data
        List<HospitalTask> taskList = new ArrayList<>();
        try {
            for (int i = 0; i < inputTableModel.getRowCount(); i++) {
                String name = inputTableModel.getValueAt(i, 0).toString();
                int arrival = Integer.parseInt(inputTableModel.getValueAt(i, 1).toString());
                int duration = Integer.parseInt(inputTableModel.getValueAt(i, 2).toString());
                int priority = Integer.parseInt(inputTableModel.getValueAt(i, 3).toString());
                taskList.add(new HospitalTask(name, arrival, duration, priority));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input data. Please ensure numeric values for time and priority.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (taskList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one task to run.", "Warning",
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

        for (HospitalTask t : taskList) {
            scheduler.addTask(t);
        }

        // Execute Schedule
        scheduler.schedule();

        // Update Output Table
        outputTableModel.setRowCount(0);
        for (HospitalTask t : scheduler.getTasks()) {
            outputTableModel.addRow(new Object[] {
                    t.getTaskName(),
                    t.getArrivalTime(),
                    t.getTaskDuration(),
                    t.getCompletionTime(),
                    t.getTurnaroundTime(),
                    t.getWaitingTime()
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

            // Draw Task Name
            g2d.setColor(Color.BLACK);
            FontMetrics fm = g2d.getFontMetrics();
            int strWidth = fm.stringWidth(record.taskName);
            if (currentWidth >= strWidth) {
                g2d.drawString(record.taskName, x + (currentWidth - strWidth) / 2, y + rectHeight / 2 + 5);
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
