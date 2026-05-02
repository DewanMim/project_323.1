# Smart Hospital Task Scheduling Simulator

<p align="center">
  A Java Swing desktop application demonstrating how CPU scheduling algorithms can optimize resource allocation in a hospital.
</p>

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-11+-0B5FFF">
  <img alt="Build Tool" src="https://img.shields.io/badge/Build-Maven-7A3E00">
  <img alt="UI" src="https://img.shields.io/badge/UI-Swing-0F766E">
  <img alt="Status" src="https://img.shields.io/badge/Project-Academic%20Simulator-374151">
</p>

## Overview

This project is a desktop-based hospital task scheduling simulator built for learning, demonstration, and lightweight experimentation with operating system scheduling strategies applied to healthcare. It allows a user to define a set of hospital tasks (like patient registration, emergency processing, and billing), execute a selected algorithm, and immediately inspect both numeric results and a visual Gantt chart timeline.

The application is especially useful for:

- understanding how different schedulers order hospital workflows
- comparing waiting time and turnaround time across algorithms
- studying how scheduling affects emergency response performance
- quickly testing small hospital task sets through a GUI instead of manual calculation

## Highlights

| Area | What the project provides |
| --- | --- |
| Scheduling | `FCFS`, `SJN` (non-preemptive), and `Round Robin` |
| Interface | Java Swing GUI with editable hospital task table |
| Analysis | Per-task completion, turnaround, and waiting time |
| Visualization | Gantt chart timeline showing task execution order |
| Build Setup | Maven project with a configured GUI entry point |

## Supported Algorithms

| Algorithm | Type | Notes |
| --- | --- | --- |
| `FCFS` | Non-preemptive | Processes tasks exactly in their arrival order |
| `SJN` | Non-preemptive | Selects the shortest available task among arrived tasks |
| `Round Robin` | Preemptive | Shares resources fairly using a configurable time quantum |

## What the Application Does

After entering hospital task information, the simulator computes and displays:

- `Completion Time`
- `Turnaround Time`
- `Waiting Time`
- `Average Turnaround Time`
- `Average Waiting Time`
- task execution order in a Gantt chart

## Interface Workflow

```text
Input Hospital Task Table
   -> Select Scheduling Algorithm
   -> Set Time Quantum (Round Robin only)
   -> Run Simulation
   -> Review Result Table
   -> Inspect Gantt Chart Timeline
```

## Screens and Behavior

The GUI is organized into three clear sections:

- a top control bar for algorithm selection, task actions, and simulation run
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
hospital_scheduler/
├── pom.xml
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── hospitalscheduling/
│                   ├── MainGUI.java
│                   ├── Scheduler.java
│                   ├── HospitalTask.java
│                   ├── FCFS.java
│                   ├── SJN.java
│                   └── RR.java
└── target/
```

## Core Components

| File | Responsibility |
| --- | --- |
| `MainGUI.java` | Builds the Swing interface, collects input, runs simulations, and paints the Gantt chart |
| `Scheduler.java` | Abstract scheduler base class with task storage, averages, and Gantt record support |
| `HospitalTask.java` | Data model for hospital task attributes and execution metrics |
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
cd hospital_scheduler
```

### Run the simulator

```bash
mvn compile exec:java
```

The configured startup class is `org.hospitalscheduling.MainGUI`.

### Build the project

```bash
mvn clean package
```

Build output is generated in `target/`.

## How to Use

1. Launch the application.
2. Choose `FCFS`, `SJN`, or `Round Robin`.
3. Enter or edit the task list in the input table.
4. If using Round Robin, provide a time quantum.
5. Click `Run Simulation`.
6. Review the result table and the Gantt chart.

## Input Model

Each task row contains the following fields:

| Field | Description |
| --- | --- |
| `Task Name` | Name shown in the result table and chart |
| `Arrival Time` | Time at which the task becomes ready |
| `Task Duration` | Time required to complete the task |
| `Priority` | Captured by the UI, but not currently used by implemented schedulers |

## Default Sample Data

The application starts with these realistic sample tasks:

| Task Name | Arrival Time | Task Duration | Priority |
| --- | ---: | ---: | ---: |
| `Emergency Patient` | 0 | 3 | 1 |
| `Billing` | 1 | 5 | 2 |
| `Lab Report` | 2 | 4 | 2 |
| `Appointment Booking` | 3 | 2 | 3 |

## Metrics Produced

| Metric | Meaning |
| --- | --- |
| `Completion Time` | Time when a task fully finishes |
| `Turnaround Time` | `Completion Time - Arrival Time` |
| `Waiting Time` | Time spent waiting for execution |

An `AVERAGE` row is appended after each run to summarize overall turnaround and waiting performance.

## Current Scope and Limitations

> This README reflects the current implementation in the codebase, not planned behavior.

- priority-based scheduling is not implemented yet
- input validation is basic and expects numeric values
- there is no import, export, or save/load support
- the Gantt chart shows execution slices but does not explicitly render idle periods
- automated tests are not included yet

## Improvement Opportunities

- add Priority Scheduling
- add emergency real-time scheduling
- store hospital data in a database
- improve the Gantt chart with colored tasks
- add report export system
- add unit tests for each scheduler implementation

## Academic Value

This project shows how CPU scheduling concepts can be applied to real-world hospital management systems. It is a solid foundation for:

- operating systems coursework
- scheduling algorithm demonstrations
- classroom presentations
- beginner-to-intermediate Java GUI practice

## Author

DewanMim
