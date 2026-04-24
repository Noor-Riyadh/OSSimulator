package gui;

import integration.IntegrationEngine;
import memory.*;
import model.*;
import util.InputParser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class IntegrationPanel extends JPanel {

    private JTextField framesField    = new JTextField("3", 6);
    private JTextField refField       = new JTextField("7, 0, 1, 2, 0, 3, 0, 4", 18);
    private JTextField headField      = new JTextField("53", 6);
    private JTextField diskSizeField  = new JTextField("200", 6);

    private JRadioButton rbFIFO  = new JRadioButton("FIFO", true);
    private JRadioButton rbLRU   = new JRadioButton("LRU");
    private JRadioButton rbOPT   = new JRadioButton("OPT");
    private JRadioButton rbClock = new JRadioButton("Second Chance");

    private JRadioButton rbDiskFCFS = new JRadioButton("FCFS", true);
    private JRadioButton rbDiskSCAN = new JRadioButton("SCAN");

    private JTextArea outputArea = new JTextArea(18, 50);

    public IntegrationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(buildTopInputs(), BorderLayout.NORTH);
        add(buildOutputArea(), BorderLayout.CENTER);
    }

    private JPanel buildTopInputs() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Integrated Simulation Input"));

        panel.add(new JLabel("Frames:")); panel.add(framesField);
        panel.add(new JLabel("Reference string:")); panel.add(refField);
        panel.add(new JLabel("Initial head:")); panel.add(headField);
        panel.add(new JLabel("Disk size:")); panel.add(diskSizeField);

        JPanel pageAlgo = new JPanel();
        pageAlgo.setBorder(BorderFactory.createTitledBorder("Page Algo"));
        ButtonGroup pg = new ButtonGroup();
        for (JRadioButton rb : new JRadioButton[]{rbFIFO, rbLRU, rbOPT, rbClock}) {
            pg.add(rb); pageAlgo.add(rb);
        }
        panel.add(pageAlgo);

        JPanel diskAlgo = new JPanel();
        diskAlgo.setBorder(BorderFactory.createTitledBorder("Disk Algo"));
        ButtonGroup dg = new ButtonGroup();
        for (JRadioButton rb : new JRadioButton[]{rbDiskFCFS, rbDiskSCAN}) {
            dg.add(rb); diskAlgo.add(rb);
        }
        panel.add(diskAlgo);

        JButton runBtn = new JButton("▶  Run Integration");
        runBtn.addActionListener(e -> runIntegration());
        panel.add(runBtn);

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
            int[] refStr  = InputParser.parseIntArray(refField.getText());
            int head      = InputParser.parsePositiveInt(headField.getText(), "Head");
            int diskSize  = InputParser.parsePositiveInt(diskSizeField.getText(), "Disk size");

            PageReplacementResult pageResult;
            if      (rbFIFO.isSelected())  pageResult = FIFO.simulate(refStr, numFrames);
            else if (rbLRU.isSelected())   pageResult = LRU.simulate(refStr, numFrames);
            else if (rbOPT.isSelected())   pageResult = OPT.simulate(refStr, numFrames);
            else                           pageResult = SecondChance.simulate(refStr, numFrames);

            String diskAlgo = rbDiskSCAN.isSelected() ? "SCAN" : "FCFS";

            List<IntegrationEvent> events = IntegrationEngine.simulate(
                pageResult, head, diskSize, diskAlgo, "right");

            displayIntegration(pageResult, events);

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayIntegration(PageReplacementResult pageResult,
                                     List<IntegrationEvent> events) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== INTEGRATION SIMULATION ===\n");
        sb.append("Page Algorithm : ").append(pageResult.getAlgorithmName()).append("\n");
        sb.append("Total Page Faults: ").append(pageResult.getTotalPageFaults()).append("\n");
        sb.append(String.format("Fault Rate: %.1f%%\n\n", pageResult.getPageFaultRate()));

        sb.append("--- Integration Events (Page Fault → Disk Access) ---\n\n");

        if (events.isEmpty()) {
            sb.append("No page faults with eviction occurred.");
        } else {
            for (IntegrationEvent evt : events) {
                sb.append("Step ").append(evt.stepNumber).append(": ")
                  .append(evt.getSummary()).append("\n");
                sb.append("  Head path: ").append(formatPath(evt.diskHeadPath)).append("\n\n");
            }
        }

        outputArea.setText(sb.toString());
        outputArea.setCaretPosition(0);
    }

    private String formatPath(java.util.List<Integer> path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1) sb.append(" → ");
        }
        return sb.toString();
    }
}
