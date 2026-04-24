package gui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Memory & Disk Management Simulator — OS2 Spring 2026");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null); 

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));

        tabs.addTab("Page Replacement", new PageReplacementPanel());
        tabs.addTab("Disk Scheduling",  new DiskSchedulingPanel());
        tabs.addTab("Integration",       new IntegrationPanel());
        tabs.addTab("Comparison",        new ComparisonPanel());

        add(tabs, BorderLayout.CENTER);

        // Status bar at the bottom
        JLabel status = new JLabel("  Ready — select an algorithm and click Run");
        status.setBorder(BorderFactory.createEtchedBorder());
        status.setFont(new Font("SansSerif", Font.PLAIN, 11));
        add(status, BorderLayout.SOUTH);
    }
}
