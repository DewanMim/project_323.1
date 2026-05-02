package org.cpuscheduling;

import java.util.Comparator;

public class FCFS extends Scheduler {

    @Override
    public void schedule() {
        if (processes.isEmpty())
            return;

        // Sort by arrival time
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;

        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime(); // Wait until process arrives
            }

            process.setStartTime(currentTime);
            process.setWaitingTime(currentTime - process.getArrivalTime());

            int start = currentTime;
            currentTime += process.getBurstTime();
            int end = currentTime;

            process.setCompletionTime(currentTime);
            process.setTurnaroundTime(process.getCompletionTime() - process.getArrivalTime());
            process.setRemainingTime(0);

            ganttChart.add(new GanttRecord(process.getId(), start, end));
        }
    }
}
