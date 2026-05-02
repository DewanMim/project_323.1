package org.cpuscheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJN extends Scheduler {

    @Override
    public void schedule() {
        if (processes.isEmpty())
            return;

        int currentTime = 0;
        int completed = 0;
        int n = processes.size();

        // Ensure we process all
        List<Process> remaining = new ArrayList<>(processes);

        while (completed < n) {
            int finalCurrentTime = currentTime;

            // Get all processes that have arrived
            Process shortest = remaining.stream()
                    .filter(p -> p.getArrivalTime() <= finalCurrentTime)
                    .min(Comparator.comparingInt(Process::getBurstTime))
                    .orElse(null);

            if (shortest == null) {
                // If no process has arrived yet, advance time to the next arrival
                int nextArrival = remaining.stream()
                        .mapToInt(Process::getArrivalTime)
                        .min().orElse(currentTime + 1);
                currentTime = nextArrival;
            } else {
                shortest.setStartTime(currentTime);
                shortest.setWaitingTime(currentTime - shortest.getArrivalTime());

                int start = currentTime;
                currentTime += shortest.getBurstTime();
                int end = currentTime;

                shortest.setCompletionTime(currentTime);
                shortest.setTurnaroundTime(shortest.getCompletionTime() - shortest.getArrivalTime());
                shortest.setRemainingTime(0);

                ganttChart.add(new GanttRecord(shortest.getId(), start, end));
                remaining.remove(shortest);
                completed++;
            }
        }
    }
}
