package org.hospitalscheduling;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

public class RR extends Scheduler {
    private int timeQuantum;

    public RR(int timeQuantum) {
        this.timeQuantum = timeQuantum;
    }

    @Override
    public void schedule() {
        if (tasks.isEmpty())
            return;

        // Sort by arrival initially
        List<HospitalTask> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.comparingInt(HospitalTask::getArrivalTime));

        int currentTime = 0;
        Queue<HospitalTask> readyQueue = new ArrayDeque<>();
        int index = 0;
        int n = sortedTasks.size();
        int completed = 0;

        // Start initialization
        if (sortedTasks.get(0).getArrivalTime() > currentTime) {
            currentTime = sortedTasks.get(0).getArrivalTime();
        }

        while (index < n && sortedTasks.get(index).getArrivalTime() <= currentTime) {
            readyQueue.add(sortedTasks.get(index));
            index++;
        }

        while (completed < n) {
            if (readyQueue.isEmpty()) {
                if (index < n) {
                    currentTime = sortedTasks.get(index).getArrivalTime();
                    readyQueue.add(sortedTasks.get(index));
                    index++;
                }
                continue;
            }

            HospitalTask currentTask = readyQueue.poll();

            if (currentTask.getStartTime() == -1) {
                currentTask.setStartTime(currentTime);
            }

            int executionTime = Math.min(timeQuantum, currentTask.getRemainingTime());
            int start = currentTime;
            currentTime += executionTime;

            currentTask.setRemainingTime(currentTask.getRemainingTime() - executionTime);

            ganttChart.add(new GanttRecord(currentTask.getTaskName(), start, currentTime));

            // Check new arrivals while the task was executing
            while (index < n && sortedTasks.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(sortedTasks.get(index));
                index++;
            }

            if (currentTask.getRemainingTime() > 0) {
                readyQueue.add(currentTask);
            } else {
                currentTask.setCompletionTime(currentTime);
                currentTask.setTurnaroundTime(currentTask.getCompletionTime() - currentTask.getArrivalTime());
                currentTask.setWaitingTime(currentTask.getTurnaroundTime() - currentTask.getTaskDuration());
                completed++;
            }
        }
    }
}
