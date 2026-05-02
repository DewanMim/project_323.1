package org.hospitalscheduling;

import java.util.ArrayList;
import java.util.List;

public abstract class Scheduler {
    protected List<HospitalTask> tasks;
    protected List<GanttRecord> ganttChart;

    public Scheduler() {
        this.tasks = new ArrayList<>();
        this.ganttChart = new ArrayList<>();
    }

    public void addTask(HospitalTask t) {
        // We use a copy of the task so we can run multiple simulations on the same original set
        this.tasks.add(new HospitalTask(t));
    }

    public List<HospitalTask> getTasks() {
        return tasks;
    }

    public List<GanttRecord> getGanttChart() {
        return ganttChart;
    }

    public double getAverageWaitingTime() {
        if (tasks.isEmpty()) return 0;
        double total = 0;
        for (HospitalTask t : tasks) {
            total += t.getWaitingTime();
        }
        return total / tasks.size();
    }

    public double getAverageTurnaroundTime() {
        if (tasks.isEmpty()) return 0;
        double total = 0;
        for (HospitalTask t : tasks) {
            total += t.getTurnaroundTime();
        }
        return total / tasks.size();
    }

    // Subclasses must implement the scheduling algorithm logic
    public abstract void schedule();

    // Helper class for UI timeline visualization
    public static class GanttRecord {
        public String taskName;
        public int startTime;
        public int endTime;

        public GanttRecord(String taskName, int startTime, int endTime) {
            this.taskName = taskName;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }
}
