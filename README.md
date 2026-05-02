# CPU Scheduling Simulator

A desktop-based CPU scheduling simulator built with Java Swing and Maven. The application lets users enter processes, run common scheduling algorithms, and inspect execution results through a results table and Gantt chart visualization.

## Overview

This project is designed as a small operating systems simulation tool for comparing how different CPU scheduling strategies behave on the same workload. Users can define a set of processes with arrival time, burst time, and priority, then execute a scheduling policy and review:

- Completion time
- Turnaround time
- Waiting time
- Average turnaround time
- Average waiting time
- Gantt chart execution order

## Supported Algorithms

- `FCFS` (First-Come, First-Served)
- `SJN` (Shortest Job Next, non-preemptive)
- `Round Robin` with configurable time quantum

## Features

- Simple desktop GUI built with Java Swing
- Editable input table for process definitions
- One-click simulation run from the interface
- Output table with per-process scheduling metrics
- Automatic average waiting and turnaround calculations
- Gantt chart timeline for execution visualization
- Maven-based build and run setup

## Technology Stack

- Java 11
- Swing
- Maven

## Project Structure

```text
src/main/java/org/cpuscheduling/
├── MainGUI.java      # Swing interface and simulation flow
├── Scheduler.java    # Abstract base scheduler and shared metrics helpers
├── Process.java      # Process model and execution state
├── FCFS.java         # First-Come, First-Served implementation
├── SJN.java          # Shortest Job Next implementation
└── RR.java           # Round Robin implementation
```

## Prerequisites

- Java 11 or later
- Maven 3.6 or later

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd project_323.1
```

### 2. Run the application

```bash
mvn compile exec:java
```

The Maven configuration launches `org.cpuscheduling.MainGUI`.

### 3. Build the project

```bash
mvn clean package
```

Compiled classes and build artifacts are generated in `target/`.

## How to Use

1. Launch the application.
2. Choose a scheduling algorithm from the dropdown.
3. For Round Robin, enter a time quantum.
4. Add, remove, or edit process rows in the input table.
5. Click `Run Simulation`.
6. Review the computed metrics in the results table and the execution timeline in the Gantt chart.

## Input Fields

Each process row contains:

- `Process ID`: Label used in results and the Gantt chart
- `Arrival Time`: Time at which the process enters the ready queue
- `Burst Time`: Required CPU execution time
- `Priority`: Currently collected by the UI, but not used by the implemented algorithms

## Example Default Dataset

The application starts with four sample processes:

| Process | Arrival Time | Burst Time | Priority |
| --- | ---: | ---: | ---: |
| P1 | 0 | 5 | 1 |
| P2 | 1 | 3 | 2 |
| P3 | 2 | 8 | 1 |
| P4 | 3 | 6 | 3 |

## Output Metrics

After each simulation, the application reports:

- `Completion Time`: When the process finishes
- `Turnaround Time`: `Completion Time - Arrival Time`
- `Waiting Time`: Time spent waiting before CPU service

An `AVERAGE` row is also appended to summarize overall turnaround and waiting time.

## Current Limitations

- No priority scheduling algorithm is implemented yet
- No export or persistence for saved simulations
- Input validation is basic and expects integer values
- The Gantt chart focuses on execution slices and does not visualize idle time explicitly

## Future Improvement Ideas

- Add Priority Scheduling and SRTF
- Support importing/exporting process datasets
- Add validation feedback for negative or invalid values
- Improve Gantt chart labeling and idle-period rendering
- Add automated unit tests for scheduler implementations

## License

No license file is currently included in this repository. Add one if you plan to distribute or reuse the project publicly.
