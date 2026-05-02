package org.hospitalscheduling;

public class HospitalTask {
    private String taskName;
    private int arrivalTime;
    private int taskDuration;
    private int priority;
    
    // Execution metrics
    private int remainingTime;
    private int startTime = -1;
    private int completionTime;
    private int turnaroundTime;
    private int waitingTime;

    public HospitalTask(String taskName, int arrivalTime, int taskDuration, int priority) {
        this.taskName = taskName;
        this.arrivalTime = arrivalTime;
        this.taskDuration = taskDuration;
        this.remainingTime = taskDuration;
        this.priority = priority;
    }

    // Copy Constructor for simulation isolation
    public HospitalTask(HospitalTask other) {
        this.taskName = other.taskName;
        this.arrivalTime = other.arrivalTime;
        this.taskDuration = other.taskDuration;
        this.priority = other.priority;
        this.remainingTime = other.taskDuration;
        this.startTime = -1;
    }

    public String getTaskName() { return taskName; }
    public int getArrivalTime() { return arrivalTime; }
    public int getTaskDuration() { return taskDuration; }
    public int getPriority() { return priority; }
    
    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }
    
    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    
    public int getCompletionTime() { return completionTime; }
    public void setCompletionTime(int completionTime) { this.completionTime = completionTime; }
    
    public int getTurnaroundTime() { return turnaroundTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }
    
    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }
    
    public void decrementRemainingTime() {
        if (remainingTime > 0) {
            remainingTime--;
        }
    }
}
