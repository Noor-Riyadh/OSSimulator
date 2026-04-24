# Memory & Disk Management Simulator
### OS2 Final Project — Spring 2026 | Future University in Egypt

---

## How to Open in NetBeans

1. Open NetBeans
2. File → New Project → Java → Java Application → Next
3. Project Name: `OSSimulator`
4. Uncheck "Create Main Class" → Finish
5. Delete the auto-generated `src` folder content
6. Copy ALL files from this zip into the `src` folder (keep the package subfolders)
7. Right-click project → Properties → Sources → verify Source Package Folders = `src`
8. Right-click `Main.java` → Run File

---

## Project Structure

```
src/
├── Main.java                        ← Entry point (run this)
│
├── model/
│   ├── PageReplacementResult.java   ← Data model for memory simulation
│   ├── DiskSchedulingResult.java    ← Data model for disk simulation
│   └── IntegrationEvent.java        ← Data model for integration events
│
├── memory/                          ← TEAMMATE: Person 1 & 2
│   ├── FIFO.java                    ← Person 1
│   ├── LRU.java                     ← Person 1
│   ├── OPT.java                     ← Person 2
│   └── SecondChance.java            ← Person 2
│
├── disk/                            ← TEAMMATE: Person 3
│   ├── FCFS.java
│   ├── SCAN.java
│   └── CSCAN_LOOK_CLOOK.java        ← Contains CSCAN, LOOK, CLOOK classes
│
├── integration/                     ← TEAMMATE: Person 4
│   └── IntegrationEngine.java       ← Core integration logic
│
├── gui/                             ← TEAMMATE: Person 4
│   ├── MainFrame.java               ← Main window with 4 tabs
│   ├── PageReplacementPanel.java    ← Tab 1
│   ├── DiskSchedulingPanel.java     ← Tab 2
│   ├── IntegrationPanel.java        ← Tab 3
│   └── ComparisonPanel.java         ← Tab 4
│
└── util/
    └── InputParser.java             ← Input validation helper
```

---

## Team Task Split

| Person | Files to complete |
|--------|-------------------|
| **Person 1** | `memory/FIFO.java`, `memory/LRU.java` — already done, test thoroughly |
| **Person 2** | `memory/OPT.java`, `memory/SecondChance.java` — already done, test thoroughly |
| **Person 3** | `disk/FCFS.java`, `disk/SCAN.java` done. Complete `CSCAN_LOOK_CLOOK.java` and wire into `DiskSchedulingPanel` and `ComparisonPanel` (search for `TODO Person 3`) |
| **Person 4** | Complete GUI panels, wire up all algorithms. Search for `TODO` comments. Run integration tests. |

---

## GitHub Workflow

```bash
# Clone the repo (everyone does this once)
git clone https://github.com/YOUR_USERNAME/OSSimulator.git

# Create your branch (each person)
git checkout -b feature/your-name

# Daily: before starting work, pull latest changes
git pull origin main

# Save your work
git add .
git commit -m "Describe what you did"
git push origin feature/your-name

# Then open a Pull Request on GitHub to merge into main
```

---

## TODO Checklist

- [ ] Person 3: Expose CSCAN, LOOK, CLOOK as public classes in separate files
- [ ] Person 3: Wire CSCAN/LOOK/CLOOK into `DiskSchedulingPanel.runSimulation()`
- [ ] Person 3: Wire CSCAN/LOOK/CLOOK into `ComparisonPanel.runComparison()`
- [ ] Person 4: Wire CSCAN/LOOK/CLOOK into `IntegrationEngine.simulate()` switch case
- [ ] Everyone: Test with sample inputs from project PDF
- [ ] Person 4: Take screenshots of all 4 tabs for the report

---

## Sample Test Inputs (from project PDF)

**Page Replacement:**
- Frames: `3`
- Reference string: `7, 0, 1, 2, 0, 3, 0, 4`

**Disk Scheduling:**
- Initial head: `53`
- Disk size: `200`
- Requests: `98, 183, 37, 122, 14, 124, 65, 67`
- Direction: `right`
