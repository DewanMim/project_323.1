package org.cpuscheduling;

import java.util.ArrayList;
import java.util.List;

public abstract class Scheduler {
    protected List<Process> processes;
    protected List<GanttRecord> ganttChart;

    public Scheduler() {
        this.processes = new ArrayList<>();
        this.ganttChart = new ArrayList<>();
    }

    public void addProcess(Process p) {
        // We use a copy of the process so we can run multiple simulations on the same original set
        this.processes.add(new Process(p));
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public List<GanttRecord> getGanttChart() {
        return ganttChart;
    }

    public double getAverageWaitingTime() {
        if (processes.isEmpty()) return 0;
        double total = 0;
        for (Process p : processes) {
            total += p.getWaitingTime();
        }
        return total / processes.size();
    }

    public double getAverageTurnaroundTime() {
        if (processes.isEmpty()) return 0;
        double total = 0;
        for (Process p : processes) {
            total += p.getTurnaroundTime();
        }
        return total / processes.size();
    }

    // Subclasses must implement the scheduling algorithm logic
    public abstract void schedule();

    // Helper class for UI timeline visualization
    public static class GanttRecord {
        public String processId;
        public int startTime;
        public int endTime;

        public GanttRecord(String processId, int startTime, int endTime) {
            this.processId = processId;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}
