package gui;

import integration.IntegrationEngine;
import memory.*;
import model.*;
import util.InputParser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class IntegrationPanel extends JPanel {

    private JTextField framesField = new JTextField("3", 6);
    private JTextField refField = new JTextField("7, 0, 1, 2, 0, 3, 0, 4", 18);
    private JTextField headField = new JTextField("53", 6);
    private JTextField diskSizeField = new JTextField("200", 6);

    private JRadioButton rbFIFO = new JRadioButton("FIFO", true);
    private JRadioButton rbLRU = new JRadioButton("LRU");
    private JRadioButton rbOPT = new JRadioButton("OPT");
    private JRadioButton rbClock = new JRadioButton("Second Chance");

    private JRadioButton rbDiskFCFS = new JRadioButton("FCFS", true);
    private JRadioButton rbDiskSCAN = new JRadioButton("SCAN");
    private JRadioButton rbDiskCSCAN = new JRadioButton("CSCAN");
    private JRadioButton rbDiskLOOK = new JRadioButton("LOOK");
    private JRadioButton rbDiskCLOOK = new JRadioButton("CLOOK");

    private JComboBox<String> dirBox = new JComboBox<>(new String[]{"right", "left"});

    private JTextArea outputArea = new JTextArea(18, 50);

    public IntegrationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(buildTopInputs(), BorderLayout.NORTH);
        add(buildOutputArea(), BorderLayout.CENTER);
    }

    private JPanel buildTopInputs() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Integrated Simulation Input"));

        JPanel fieldsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        fieldsRow.add(new JLabel("Frames:"));
        fieldsRow.add(framesField);
        fieldsRow.add(new JLabel("Reference string:"));
        fieldsRow.add(refField);
        fieldsRow.add(new JLabel("Initial head:"));
        fieldsRow.add(headField);
        fieldsRow.add(new JLabel("Disk size:"));
        fieldsRow.add(diskSizeField);
        fieldsRow.add(new JLabel("Direction:"));
        fieldsRow.add(dirBox);

        JPanel algoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));

        JPanel pageAlgo = new JPanel();
        pageAlgo.setBorder(BorderFactory.createTitledBorder("Page Algo"));
        ButtonGroup pg = new ButtonGroup();
        for (JRadioButton rb : new JRadioButton[]{rbFIFO, rbLRU, rbOPT, rbClock}) {
            pg.add(rb);
            pageAlgo.add(rb);
        }

        JPanel diskAlgo = new JPanel();
        diskAlgo.setBorder(BorderFactory.createTitledBorder("Disk Algo"));
        ButtonGroup dg = new ButtonGroup();
        for (JRadioButton rb : new JRadioButton[]{rbDiskFCFS, rbDiskSCAN,
            rbDiskCSCAN, rbDiskLOOK,
            rbDiskCLOOK}) {
            dg.add(rb);
            diskAlgo.add(rb);
        }

        JButton runBtn = new JButton("▶  Run Integration");
        runBtn.addActionListener(e -> runIntegration());

        algoRow.add(pageAlgo);
        algoRow.add(diskAlgo);
        algoRow.add(runBtn);

        panel.add(fieldsRow, BorderLayout.NORTH);
        panel.add(algoRow, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane buildOutputArea() {
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setEditable(false);
        outputArea.setText("Run integration to see memory + disk events here...");
        return new JScrollPane(outputArea);
    }


    private void runIntegration() {
        try {
            int numFrames = InputParser.parsePositiveInt(framesField.getText(), "Frames");
            int[] refStr = InputParser.parseIntArray(refField.getText());
            int head = InputParser.parsePositiveInt(headField.getText(), "Head");
            int diskSize = InputParser.parsePositiveInt(diskSizeField.getText(), "Disk size");
            String dir = (String) dirBox.getSelectedItem();

            PageReplacementResult pageResult;
            if (rbFIFO.isSelected()) {
                pageResult = FIFO.simulate(refStr, numFrames);
            } else if (rbLRU.isSelected()) {
                pageResult = LRU.simulate(refStr, numFrames);
            } else if (rbOPT.isSelected()) {
                pageResult = OPT.simulate(refStr, numFrames);
            } else {
                pageResult = SecondChance.simulate(refStr, numFrames);
            }

            String diskAlgo;
            if (rbDiskFCFS.isSelected()) {
                diskAlgo = "FCFS";
            } else if (rbDiskSCAN.isSelected()) {
                diskAlgo = "SCAN";
            } else if (rbDiskCSCAN.isSelected()) {
                diskAlgo = "CSCAN";
            } else if (rbDiskLOOK.isSelected()) {
                diskAlgo = "LOOK";
            } else {
                diskAlgo = "CLOOK";
            }

            List<IntegrationEvent> events = IntegrationEngine.simulate(
                    pageResult, head, diskSize, diskAlgo, dir);

            displayIntegration(pageResult, events, diskAlgo);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayIntegration(PageReplacementResult pageResult,
            List<IntegrationEvent> events,
            String diskAlgo) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== INTEGRATION SIMULATION ===\n");
        sb.append("Page Algorithm  : ").append(pageResult.getAlgorithmName()).append("\n");
        sb.append("Disk Algorithm  : ").append(diskAlgo).append("\n");
        sb.append("Total Page Faults: ").append(pageResult.getTotalPageFaults()).append("\n");
        sb.append(String.format("Fault Rate      : %.1f%%\n", pageResult.getPageFaultRate()));
        sb.append("\n");

        sb.append("--- Integration Events (Page Fault → Disk Access) ---\n\n");

        if (events.isEmpty()) {
            sb.append("No page faults occurred.");
        } else {
            int totalSeek = 0;

            for (IntegrationEvent evt : events) {
                sb.append("Step ").append(evt.stepNumber).append(":\n");
                sb.append("  ").append(evt.getSummary()).append("\n");
                sb.append("  Head path : ").append(formatPath(evt.diskHeadPath)).append("\n");
                sb.append("  Seek distance this step: ").append(evt.seekDistance).append("\n");
                sb.append("\n");
                totalSeek += evt.seekDistance;
            }

            sb.append("─────────────────────────────────\n");
            sb.append("Total disk seek distance: ").append(totalSeek).append("\n");
            sb.append("Total integration events: ").append(events.size()).append("\n");
        }

        outputArea.setText(sb.toString());
        outputArea.setCaretPosition(0);
    }


    private String formatPath(List<Integer> path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1) {
                sb.append(" → ");
            }
        }
        return sb.toString();
    }
}
