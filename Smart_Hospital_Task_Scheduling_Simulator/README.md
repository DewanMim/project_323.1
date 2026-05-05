# 🏥 Smart Hospital Task Scheduling Simulator

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-11%2B-blue.svg)](https://www.oracle.com/java/)
[![React](https://img.shields.io/badge/React-18-61DAFB.svg)](https://reactjs.org/)
[![Vite](https://img.shields.io/badge/Vite-Latest-646CFF.svg)](https://vitejs.dev/)

A sophisticated scheduling simulator designed to optimize resource allocation in modern healthcare environments. This project bridges the gap between algorithmic theory and practical hospital task management.

> [!TIP]
> **New Feature:** Experience our premium **Modern Web Dashboard** with real-time glassmorphic visualizations!

---

## 📖 Table of Contents
- [✨ Key Features](#-key-features)
- [🛠 Tech Stack](#-tech-stack)
- [🚀 Quick Start](#-quick-start)
- [🧩 Algorithms Explained](#-algorithms-explained)
- [📁 Project Structure](#-project-structure)
- [🎨 Design Philosophy](#-design-philosophy)
- [📜 License](#-license)

---

## ✨ Key Features

- **Multi-Algorithm Support**: Compare FCFS, SJN, and Round Robin scheduling in real-time.
- **Dynamic Gantt Charts**: High-fidelity visual timelines for task execution.
- **Performance Metrics**: Automatic calculation of Turnaround Time (TAT) and Waiting Time (WT).
- **Healthcare Domain Logic**: Optimized for surgery prep, radiology, and emergency triage tasks.
- **Cross-Platform**: Run as a modern web dashboard or a robust Java desktop application.

---

## 🛠 Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Frontend (Web)** | React, TypeScript, Framer Motion, Lucide Icons |
| **Backend (Core)** | Java 11+, Maven |
| **Styling** | Vanilla CSS3 (Custom Design System), FlatLaf (Java) |
| **Tools** | Vite, GitHub Actions |

---

## 🚀 Quick Start

### 🌐 Modern Web Dashboard (Premium UI)
The recommended way to experience the simulator with high-end animations.

```bash
# Navigate to the dashboard directory
cd modern-dashboard

# Install dependencies
npm install

# Start the development server
npm run dev
```
🔗 **Access:** Open [http://localhost:5173](http://localhost:5173)

### ☕ Legacy Java Desktop
Original robust implementation for desktop environments.

```bash
# Build the project
mvn clean compile

# Run the GUI
mvn exec:java -Dexec.mainClass="org.hospitalscheduling.MainGUI"
```

---

## 🧩 Algorithms Explained

### 1. First-Come, First-Served (FCFS)
The simplest form of scheduling. Tasks are executed in the exact order they arrive at the triage desk.
- **Use Case:** General administrative tasks.

### 2. Shortest Job Next (SJN)
Optimizes the queue by prioritizing tasks with the shortest duration. This significantly reduces average waiting times.
- **Use Case:** Fast-track diagnostic tests.

### 3. Round Robin (RR)
Allocates a fixed "Time Quantum" to each task, cycling through the queue. Ensures no single task blocks the system.
- **Use Case:** ICU monitoring and shared resource management.

---

## 📁 Project Structure

```bash
Smart_Hospital_Simulator/
├── modern-dashboard/       # Premium React Frontend
│   ├── src/
│   │   ├── scheduling.ts   # Ported Scheduling Logic
│   │   └── App.tsx         # Dashboard UI
│   └── index.css           # Design System
├── src/                    # Core Java Implementation
│   └── main/java/org/...
│       ├── FCFS.java       # Algorithm Implementation
│       └── MainGUI.java    # Modernized Swing UI
├── pom.xml                 # Maven Configuration
└── README.md               # You are here
```

---

## 🎨 Design Philosophy

The project follows a **"Hospital-Tech"** aesthetic:
- **Colors:** Deep Slate (`#0f172a`), Primary Sky (`#0ea5e9`), and Success Emerald (`#10b981`).
- **Feel:** Professional, clean, and high-contrast for medical environments.
- **Interactions:** Smooth, non-distracting micro-animations for better cognitive load management.

---


