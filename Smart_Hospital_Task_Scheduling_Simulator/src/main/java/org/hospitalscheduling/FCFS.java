package org.hospitalscheduling;

import java.util.Comparator;

public class FCFS extends Scheduler {

    @Override
    public void schedule() {
        if (tasks.isEmpty())
            return;

        // Sort by arrival time
        tasks.sort(Comparator.comparingInt(HospitalTask::getArrivalTime));

        int currentTime = 0;

        for (HospitalTask task : tasks) {
            if (currentTime < task.getArrivalTime()) {
                currentTime = task.getArrivalTime(); // Wait until task arrives
            }

            task.setStartTime(currentTime);
            task.setWaitingTime(currentTime - task.getArrivalTime());

            int start = currentTime;
            currentTime += task.getTaskDuration();
            int end = currentTime;

            task.setCompletionTime(currentTime);
            task.setTurnaroundTime(task.getCompletionTime() - task.getArrivalTime());
            task.setRemainingTime(0);

            ganttChart.add(new GanttRecord(task.getTaskName(), start, end));
        }
    }
}
