package gui;

import disk.FCFS;
import disk.SCAN;
import disk.CSCAN;
import disk.LOOK;
import disk.CLOOK;
import memory.FIFO;
import memory.LRU;
import memory.OPT;
import memory.SecondChance;
import model.DiskSchedulingResult;
import model.PageReplacementResult;
import util.InputParser;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import memory.*;
import disk.*;
import model.*;
import util.InputParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ComparisonPanel extends JPanel {

    private JTextField framesField = new JTextField("3", 6);
    private JTextField refField = new JTextField("7, 0, 1, 2, 0, 3, 0, 4", 20);
    private JTextField headField = new JTextField("53", 6);
    private JTextField requestsField = new JTextField("98, 183, 37, 122, 14, 124, 65, 67", 22);
    private JTextField diskSizeField = new JTextField("200", 6);

    private JTable pageTable;
    private JTable diskTable;
    private DefaultTableModel pageModel;
    private DefaultTableModel diskModel;

    public ComparisonPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        add(buildInputBar(), BorderLayout.NORTH);
        add(buildTablesPanel(), BorderLayout.CENTER);
    }

    private JPanel buildInputBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        p.setBorder(BorderFactory.createTitledBorder("Compare All Algorithms"));
        p.add(new JLabel("Frames:"));
        p.add(framesField);
        p.add(new JLabel("Reference:"));
        p.add(refField);
        p.add(new JLabel("Head:"));
        p.add(headField);
        p.add(new JLabel("Requests:"));
        p.add(requestsField);
        p.add(new JLabel("Disk size:"));
        p.add(diskSizeField);

        JButton runBtn = new JButton("▶  Compare All");
        runBtn.addActionListener(e -> runComparison());
        p.add(runBtn);
        return p;
    }

    private JSplitPane buildTablesPanel() {
        String[] pageCols = {"Algorithm", "Page Faults", "Hits", "Fault Rate (%)"};
        pageModel = new DefaultTableModel(pageCols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        pageTable = new JTable(pageModel);
        styleTable(pageTable);
        JScrollPane pageScroll = new JScrollPane(pageTable);
        pageScroll.setBorder(BorderFactory.createTitledBorder("Page Replacement Comparison"));

        String[] diskCols = {"Algorithm", "Total Seek Distance", "Avg Seek Time"};
        diskModel = new DefaultTableModel(diskCols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        diskTable = new JTable(diskModel);
        styleTable(diskTable);
        JScrollPane diskScroll = new JScrollPane(diskTable);
        diskScroll.setBorder(BorderFactory.createTitledBorder("Disk Scheduling Comparison"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pageScroll, diskScroll);
        split.setResizeWeight(0.5);
        return split;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
    }

    private void runComparison() {
        try {
            int numFrames = InputParser.parsePositiveInt(framesField.getText(), "Frames");
            int[] refStr = InputParser.parseIntArray(refField.getText());
            int head = InputParser.parsePositiveInt(headField.getText(), "Head");
            int[] reqs = InputParser.parseIntArray(requestsField.getText());
            int diskSize = InputParser.parsePositiveInt(diskSizeField.getText(), "Disk size");

            pageModel.setRowCount(0);

            PageReplacementResult r1 = FIFO.simulate(refStr, numFrames);
            PageReplacementResult r2 = LRU.simulate(refStr, numFrames);
            PageReplacementResult r3 = OPT.simulate(refStr, numFrames);
            PageReplacementResult r4 = SecondChance.simulate(refStr, numFrames);

            for (PageReplacementResult r : new PageReplacementResult[]{r1, r2, r3, r4}) {
                pageModel.addRow(new Object[]{
                    r.getAlgorithmName(),
                    r.getTotalPageFaults(),
                    r.getTotalHits(),
                    String.format("%.1f%%", r.getPageFaultRate())
                });
            }

            diskModel.setRowCount(0);

            DiskSchedulingResult d1 = FCFS.simulate(head, reqs);
            DiskSchedulingResult d2 = SCAN.simulate(head, reqs, diskSize, "right");
            DiskSchedulingResult d3 = CSCAN.simulate(head, reqs, diskSize);
            DiskSchedulingResult d4 = LOOK.simulate(head, reqs, "right");
            DiskSchedulingResult d5 = CLOOK.simulate(head, reqs);

            for (DiskSchedulingResult r : new DiskSchedulingResult[]{d1, d2, d3, d4, d5}) {
                diskModel.addRow(new Object[]{
                    r.getAlgorithmName(),
                    r.getTotalSeekDistance(),
                    String.format("%.2f", r.getAverageSeekTime())
                });
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
