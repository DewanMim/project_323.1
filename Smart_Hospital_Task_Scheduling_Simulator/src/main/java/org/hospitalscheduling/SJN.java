package org.hospitalscheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJN extends Scheduler {

    @Override
    public void schedule() {
        if (tasks.isEmpty())
            return;

        int currentTime = 0;
        int completed = 0;
        int n = tasks.size();

        // Ensure we process all
        List<HospitalTask> remaining = new ArrayList<>(tasks);

        while (completed < n) {
            int finalCurrentTime = currentTime;

            // Get all tasks that have arrived
            HospitalTask shortest = remaining.stream()
                    .filter(t -> t.getArrivalTime() <= finalCurrentTime)
                    .min(Comparator.comparingInt(HospitalTask::getTaskDuration))
                    .orElse(null);

            if (shortest == null) {
                // If no task has arrived yet, advance time to the next arrival
                int nextArrival = remaining.stream()
                        .mapToInt(HospitalTask::getArrivalTime)
                        .min().orElse(currentTime + 1);
                currentTime = nextArrival;
            } else {
                shortest.setStartTime(currentTime);
                shortest.setWaitingTime(currentTime - shortest.getArrivalTime());

                int start = currentTime;
                currentTime += shortest.getTaskDuration();
                int end = currentTime;

                shortest.setCompletionTime(currentTime);
                shortest.setTurnaroundTime(shortest.getCompletionTime() - shortest.getArrivalTime());
                shortest.setRemainingTime(0);

                ganttChart.add(new GanttRecord(shortest.getTaskName(), start, end));
                remaining.remove(shortest);
                completed++;
            }
        }
    }
}
