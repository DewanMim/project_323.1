package org.cpuscheduling;

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
        if (processes.isEmpty())
            return;

        // Sort by arrival initially
        List<Process> sortedProcesses = new ArrayList<>(processes);
        sortedProcesses.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        Queue<Process> readyQueue = new ArrayDeque<>();
        int index = 0;
        int n = sortedProcesses.size();
        int completed = 0;

        // Start initialization
        if (sortedProcesses.get(0).getArrivalTime() > currentTime) {
            currentTime = sortedProcesses.get(0).getArrivalTime();
        }

        while (index < n && sortedProcesses.get(index).getArrivalTime() <= currentTime) {
            readyQueue.add(sortedProcesses.get(index));
            index++;
        }

        while (completed < n) {
            if (readyQueue.isEmpty()) {
                if (index < n) {
                    currentTime = sortedProcesses.get(index).getArrivalTime();
                    readyQueue.add(sortedProcesses.get(index));
                    index++;
                }
                continue;
            }

            Process currentProcess = readyQueue.poll();

            if (currentProcess.getStartTime() == -1) {
                currentProcess.setStartTime(currentTime);
            }

            int executionTime = Math.min(timeQuantum, currentProcess.getRemainingTime());
            int start = currentTime;
            currentTime += executionTime;

            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);

            ganttChart.add(new GanttRecord(currentProcess.getId(), start, currentTime));

            // Check new arrivals while the process was executing
            while (index < n && sortedProcesses.get(index).getArrivalTime() <= currentTime) {
                readyQueue.add(sortedProcesses.get(index));
                index++;
            }

            if (currentProcess.getRemainingTime() > 0) {
                readyQueue.add(currentProcess);
            } else {
                currentProcess.setCompletionTime(currentTime);
                currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                completed++;
            }
        }
    }
}
