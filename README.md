# CPU Scheduling Simulator

<p align="center">
  A Java Swing desktop application for simulating and comparing classic CPU scheduling algorithms.
</p>

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-11+-0B5FFF">
  <img alt="Build Tool" src="https://img.shields.io/badge/Build-Maven-7A3E00">
  <img alt="UI" src="https://img.shields.io/badge/UI-Swing-0F766E">
  <img alt="Status" src="https://img.shields.io/badge/Project-Academic%20Simulator-374151">
</p>

## Overview

This project is a desktop-based CPU scheduling simulator built for learning, demonstration, and lightweight experimentation with operating system scheduling strategies. It allows a user to define a set of processes, execute a selected algorithm, and immediately inspect both numeric results and a visual Gantt chart timeline.

The application is especially useful for:

- understanding how different schedulers order work
- comparing waiting time and turnaround time across algorithms
- demonstrating scheduling behavior in coursework or lab work
- quickly testing small process sets through a GUI instead of manual calculation

## Highlights

| Area | What the project provides |
| --- | --- |
| Scheduling | `FCFS`, `SJN` (non-preemptive), and `Round Robin` |
| Interface | Java Swing GUI with editable process table |
| Analysis | Per-process completion, turnaround, and waiting time |
| Visualization | Gantt chart timeline showing execution order |
| Build Setup | Maven project with a configured GUI entry point |

## Supported Algorithms

| Algorithm | Type | Notes |
| --- | --- | --- |
| `FCFS` | Non-preemptive | Executes processes in arrival order |
| `SJN` | Non-preemptive | Selects the shortest available burst among arrived processes |
| `Round Robin` | Preemptive | Uses a configurable time quantum |

## What the Application Does

After entering process information, the simulator computes and displays:

- `Completion Time`
- `Turnaround Time`
- `Waiting Time`
- `Average Turnaround Time`
- `Average Waiting Time`
- execution order in a Gantt chart

## Interface Workflow

```text
Input Process Table
   -> Select Scheduling Algorithm
   -> Set Time Quantum (Round Robin only)
   -> Run Simulation
   -> Review Result Table
   -> Inspect Gantt Chart Timeline
```

## Screens and Behavior

The GUI is organized into three clear sections:

- a top control bar for algorithm selection, process actions, and simulation run
- a center area with input and output tables
- a bottom Gantt chart panel for timeline visualization

The simulator also ships with a small default dataset so the interface can be explored immediately after launch.

## Technology Stack

| Layer | Choice |
| --- | --- |
| Language | Java 11 |
| UI Toolkit | Swing |
| Build Tool | Maven |
| Packaging Style | Standard Maven Java project |

## Project Structure

```text
project_323.1/
├── pom.xml
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── cpuscheduling/
│                   ├── MainGUI.java
│                   ├── Scheduler.java
│                   ├── Process.java
│                   ├── FCFS.java
│                   ├── SJN.java
│                   └── RR.java
└── target/
```

## Core Components

| File | Responsibility |
| --- | --- |
| `MainGUI.java` | Builds the Swing interface, collects input, runs simulations, and paints the Gantt chart |
| `Scheduler.java` | Abstract scheduler base class with process storage, averages, and Gantt record support |
| `Process.java` | Data model for process attributes and execution metrics |
| `FCFS.java` | First-Come, First-Served scheduling implementation |
| `SJN.java` | Shortest Job Next scheduling implementation |
| `RR.java` | Round Robin scheduling implementation with time quantum |

## Prerequisites

- Java 11 or later
- Maven 3.6 or later

## Getting Started

### Clone the repository

```bash
git clone <repository-url>
cd project_323.1
```

### Run the simulator

```bash
mvn compile exec:java
```

The configured startup class is `org.cpuscheduling.MainGUI`.

### Build the project

```bash
mvn clean package
```

Build output is generated in `target/`.

## How to Use

1. Launch the application.
2. Choose `FCFS`, `SJN`, or `Round Robin`.
3. Enter or edit the process list in the input table.
4. If using Round Robin, provide a time quantum.
5. Click `Run Simulation`.
6. Review the result table and the Gantt chart.

## Input Model

Each process row contains the following fields:

| Field | Description |
| --- | --- |
| `Process ID` | Name shown in the result table and chart |
| `Arrival Time` | Time at which the process becomes ready |
| `Burst Time` | CPU time required by the process |
| `Priority` | Captured by the UI, but not currently used by implemented schedulers |

## Default Sample Data

The application starts with these sample processes:

| Process | Arrival Time | Burst Time | Priority |
| --- | ---: | ---: | ---: |
| `P1` | 0 | 5 | 1 |
| `P2` | 1 | 3 | 2 |
| `P3` | 2 | 8 | 1 |
| `P4` | 3 | 6 | 3 |

## Metrics Produced

| Metric | Meaning |
| --- | --- |
| `Completion Time` | Time when a process fully finishes |
| `Turnaround Time` | `Completion Time - Arrival Time` |
| `Waiting Time` | Time spent waiting for CPU execution |

An `AVERAGE` row is appended after each run to summarize overall turnaround and waiting performance.

## Current Scope and Limitations

> This README reflects the current implementation in the codebase, not planned behavior.

- priority-based scheduling is not implemented yet
- input validation is basic and expects integer values
- there is no import, export, or save/load support
- the Gantt chart shows execution slices but does not explicitly render idle periods
- automated tests are not included yet

## Improvement Opportunities

- add Priority Scheduling and Shortest Remaining Time First
- validate negative values and malformed inputs more clearly
- support file-based import/export for process sets
- improve the Gantt chart with idle slots, colors, and legends
- add unit tests for each scheduler implementation
- package the app as a runnable desktop artifact

## Academic Value

This project is a solid foundation for:

- operating systems coursework
- scheduling algorithm demonstrations
- classroom presentations
- beginner-to-intermediate Java GUI practice

## Author

DewanMim
