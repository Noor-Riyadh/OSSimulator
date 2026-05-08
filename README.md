# 🖥️ Memory & Disk Management Simulator

### OS2 Final Project — Spring 2026 | Future University in Egypt
### Faculty of Computers and Information Technology | Computer Science Department

---

## 📋 Project Overview

A Java desktop application that simulates how an operating system manages **virtual memory** and **disk scheduling**. The simulator visualizes page replacement algorithms and disk head movement in real time, with full integration between memory and disk subsystems.

---

## ✨ Features

- **4 Page Replacement Algorithms** with step-by-step animation and color-coded results
- **5 Disk Scheduling Algorithms** with a live head movement chart
- **Integration Engine** — connects memory faults directly to disk requests
- **Comparison Table** — runs all algorithms side by side with performance metrics
- Built with **Java Swing** — no external libraries required

---

## 🧠 Algorithms Implemented

### Page Replacement
| Algorithm | Description |
|---|---|
| FIFO | Replaces the page that arrived first in memory |
| LRU | Replaces the least recently used page |
| OPT | Replaces the page not needed for the longest time (optimal benchmark) |
| Second Chance | Like FIFO but gives pages a second chance using reference bits |

### Disk Scheduling
| Algorithm | Description |
|---|---|
| FCFS | Services requests in arrival order |
| SCAN | Moves in one direction, reverses at disk end (elevator) |
| CSCAN | One direction only, jumps back to cylinder 0 |
| LOOK | Like SCAN but stops at last request instead of disk end |
| CLOOK | Like CSCAN but jumps to smallest request instead of cylinder 0 |

---

## 🔗 Integration (Core Feature)

When a page fault occurs:
1. Memory checks if a frame is available
2. If full → selected page replacement algorithm evicts a page
3. A disk request is generated for the new page
4. Selected disk scheduling algorithm services the request
5. The full event is recorded and displayed

---

## 🚀 How to Run

### Option 1 — JAR file (easiest)
1. Download `OSSimulator1.1.jar` from the `dist` folder
2. Double-click the JAR file
3. The simulator opens immediately

### Option 2 — NetBeans
1. Clone the repository:
git clone https://github.com/Noor-Riyadh/OSSimulator.git
2. Open NetBeans → File → Open Project → select `OSSimulator1.1`
3. Right-click `Main.java` → Run File

---

## 📁 Project Structure
src/
├── Main.java                    ← Entry point
├── disk/
│   ├── FCFS.java
│   ├── SCAN.java
│   ├── CSCAN.java
│   ├── LOOK.java
│   └── CLOOK.java
├── memory/
│   ├── FIFO.java
│   ├── LRU.java
│   ├── OPT.java
│   └── SecondChance.java
├── integration/
│   └── IntegrationEngine.java
├── model/
│   ├── PageReplacementResult.java
│   ├── DiskSchedulingResult.java
│   └── IntegrationEvent.java
├── gui/
│   ├── MainFrame.java
│   ├── PageReplacementPanel.java
│   ├── DiskSchedulingPanel.java
│   ├── IntegrationPanel.java
│   └── ComparisonPanel.java
└── util/
└── InputParser.java

---

## 📊 Sample Results

### Page Replacement — Frames: 3, Reference: 7,0,1,2,0,3,0,4
| Algorithm | Page Faults | Hits | Fault Rate |
|---|---|---|---|
| FIFO | 7 | 1 | 87.5% |
| LRU | 6 | 2 | 75.0% |
| OPT | 6 | 2 | 75.0% |
| Second Chance | 6 | 2 | 75.0% |

### Disk Scheduling — Head: 53, Requests: 98,183,37,122,14,124,65,67
| Algorithm | Total Seek Distance | Avg Seek Time |
|---|---|---|
| FCFS | 640 | 80.00 |
| SCAN | 331 | 36.78 |
| CSCAN | 382 | 38.20 |
| LOOK | 299 | 37.38 |
| CLOOK | 322 | 40.25 |

---

## 👥 Team

| Name | Role | Algorithms |
|---|---|---|
| Noor | Group Leader | Integration Engine, Comparison Panel, Report |
| Manar | Developer | FIFO, LRU, LOOK |
| Rawan | Developer | OPT, Second Chance, SCAN |
| Hala | Developer | FCFS, CSCAN, CLOOK |

---

## 🛠️ Built With

- **Java 24**
- **Java Swing** — GUI framework
- **Apache NetBeans IDE 25**
- **Git & GitHub** — version control

---

## 📄 Requirements

- Java JDK 11 or higher
- No external libraries needed — pure Java