package gui;

import disk.*;
import model.DiskSchedulingResult;
import util.InputParser;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DiskSchedulingPanel extends JPanel {

    private JTextField headField = new JTextField("53", 6);
    private JTextField diskSizeField = new JTextField("200", 6);
    private JTextField requestsField = new JTextField("98, 183, 37, 122, 14, 124, 65, 67", 22);

    private JRadioButton rbFCFS = new JRadioButton("FCFS", true);
    private JRadioButton rbSCAN = new JRadioButton("SCAN");
    private JRadioButton rbCSCAN = new JRadioButton("CSCAN");
    private JRadioButton rbLOOK = new JRadioButton("LOOK");
    private JRadioButton rbCLOOK = new JRadioButton("CLOOK");

    private JComboBox<String> dirBox = new JComboBox<>(new String[]{"right", "left"});
    private JLabel lblTotal = new JLabel("—");
    private JLabel lblAvg = new JLabel("—");
    private JLabel lblOrder = new JLabel(" ");
    private DiskChartPanel chartPanel;

    public DiskSchedulingPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buildLeft(), BorderLayout.WEST);
        add(buildCenter(), BorderLayout.CENTER);
    }

    private JPanel buildLeft() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setPreferredSize(new Dimension(215, 0));
        p.setBorder(BorderFactory.createTitledBorder("Input"));
        p.add(labeled("Initial head position:", headField));
        p.add(Box.createVerticalStrut(6));
        p.add(labeled("Disk size (cylinders):", diskSizeField));
        p.add(Box.createVerticalStrut(6));
        p.add(labeled("Request queue:", requestsField));
        p.add(Box.createVerticalStrut(8));
        JPanel dp = new JPanel(new BorderLayout(2, 2));
        dp.add(new JLabel("Direction (SCAN/LOOK):"), BorderLayout.NORTH);
        dp.add(dirBox, BorderLayout.CENTER);
        dp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        p.add(dp);
        p.add(Box.createVerticalStrut(10));

        JPanel algoBox = new JPanel();
        algoBox.setLayout(new BoxLayout(algoBox, BoxLayout.Y_AXIS));
        algoBox.setBorder(BorderFactory.createTitledBorder("Algorithm"));
        ButtonGroup bg = new ButtonGroup();
        for (JRadioButton rb : new JRadioButton[]{rbFCFS, rbSCAN, rbCSCAN, rbLOOK, rbCLOOK}) {
            bg.add(rb);
            algoBox.add(rb);
        }
        p.add(algoBox);
        p.add(Box.createVerticalStrut(12));

        JButton runBtn = new JButton("▶  Run Simulation");
        runBtn.setAlignmentX(CENTER_ALIGNMENT);
        runBtn.addActionListener(e -> runSimulation());
        p.add(runBtn);
        return p;
    }

    private JPanel buildCenter() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        chartPanel = new DiskChartPanel();
        chartPanel.setBorder(BorderFactory.createTitledBorder("Head Movement Chart"));
        p.add(chartPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(4, 4));
        lblOrder.setFont(new Font("Monospaced", Font.PLAIN, 12));
        lblOrder.setBorder(BorderFactory.createTitledBorder("Movement Order"));
        bottom.add(lblOrder, BorderLayout.CENTER);

        JPanel metrics = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 5));
        metrics.setBorder(BorderFactory.createTitledBorder("Metrics"));
        style(lblTotal, new Color(0, 80, 160));
        style(lblAvg, new Color(120, 60, 0));
        metrics.add(new JLabel("Total Seek Distance:"));
        metrics.add(lblTotal);
        metrics.add(new JLabel("Avg Seek Time:"));
        metrics.add(lblAvg);
        bottom.add(metrics, BorderLayout.SOUTH);
        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private void runSimulation() {
        try {
            int head = InputParser.parsePositiveInt(headField.getText(), "Head position");
            int diskSize = InputParser.parsePositiveInt(diskSizeField.getText(), "Disk size");
            int[] reqs = InputParser.parseIntArray(requestsField.getText());
            String dir = (String) dirBox.getSelectedItem();

            DiskSchedulingResult result;
            if (rbFCFS.isSelected()) {
                result = FCFS.simulate(head, reqs);
            } else if (rbSCAN.isSelected()) {
                result = SCAN.simulate(head, reqs, diskSize, dir);
                
            } else if (rbCSCAN.isSelected()) {
                result = CSCAN.simulate(head, reqs, diskSize);
            } else if (rbLOOK.isSelected()) {
                result = LOOK.simulate(head, reqs, dir);
            } else {
                result = CLOOK.simulate(head, reqs);
            }

            chartPanel.setData(result.getHeadMovementOrder(), diskSize, result.getAlgorithmName());
            lblTotal.setText(String.valueOf(result.getTotalSeekDistance()));
            lblAvg.setText(String.format("%.2f", result.getAverageSeekTime()));
            lblOrder.setText(" " + result.getMovementString());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel labeled(String lbl, JTextField f) {
        JPanel p = new JPanel(new BorderLayout(2, 2));
        p.add(new JLabel(lbl), BorderLayout.NORTH);
        p.add(f, BorderLayout.CENTER);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return p;
    }

    private void style(JLabel l, Color c) {
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(c);
    }

    private static class DiskChartPanel extends JPanel {

        private List<Integer> order;
        private int diskSize = 200;
        private String algoName = "";

        private static final Color LINE_COLOR = new Color(50, 110, 200);
        private static final Color DOT_COLOR = new Color(220, 60, 60);
        private static final Color START_COLOR = new Color(0, 160, 80);
        private static final Color GRID_COLOR = new Color(220, 220, 220);
        private static final Color AXIS_COLOR = new Color(100, 100, 100);

        void setData(List<Integer> order, int diskSize, String algoName) {
            this.order = order;
            this.diskSize = diskSize;
            this.algoName = algoName;
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (order == null || order.isEmpty()) {
                g2.setColor(Color.GRAY);
                g2.setFont(new Font("SansSerif", Font.ITALIC, 13));
                g2.drawString("Run a simulation to see the chart", 30, getHeight() / 2);
                return;
            }

            int W = getWidth(), H = getHeight(), ml = 60, mr = 20, mt = 30, mb = 40;
            int cw = W - ml - mr, ch = H - mt - mb;

            g2.setColor(new Color(250, 252, 255));
            g2.fillRect(ml, mt, cw, ch);

            g2.setStroke(new BasicStroke(0.8f));
            for (int i = 0; i <= 10; i++) {
                int y = mt + i * ch / 10;
                g2.setColor(GRID_COLOR);
                g2.drawLine(ml, y, ml + cw, y);
                int v = diskSize - i * diskSize / 10;
                g2.setColor(AXIS_COLOR);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                String lbl = String.valueOf(v);
                g2.drawString(lbl, ml - g2.getFontMetrics().stringWidth(lbl) - 4, y + 4);
            }

            g2.setColor(AXIS_COLOR);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawLine(ml, mt, ml, mt + ch);
            g2.drawLine(ml, mt + ch, ml + cw, mt + ch);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Cylinder", ml - 30, mt - 8);
            g2.drawString("Time (request order) \u2192", ml + cw / 2 - 60, mt + ch + 30);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.setColor(new Color(40, 40, 40));
            g2.drawString(algoName + " \u2014 Disk Head Movement", ml + 10, mt - 8);

            int n = order.size();
            int[] xs = new int[n], ys = new int[n];
            for (int i = 0; i < n; i++) {
                xs[i] = ml + i * cw / Math.max(n - 1, 1);
                ys[i] = mt + ch - (int) ((double) order.get(i) / diskSize * ch);
            }

            g2.setColor(LINE_COLOR);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < n - 1; i++) {
                g2.drawLine(xs[i], ys[i], xs[i + 1], ys[i + 1]);
            }

            for (int i = 0; i < n; i++) {
                boolean first = i == 0;
                int r = first ? 7 : 5;
                g2.setColor(first ? START_COLOR : DOT_COLOR);
                g2.fillOval(xs[i] - r, ys[i] - r, r * 2, r * 2);
                g2.setColor(first ? START_COLOR.darker() : DOT_COLOR.darker());
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawOval(xs[i] - r, ys[i] - r, r * 2, r * 2);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.setColor(Color.DARK_GRAY);
                String lbl = String.valueOf(order.get(i));
                int lx = xs[i] - g2.getFontMetrics().stringWidth(lbl) / 2;
                int ly = ys[i] - r - 3;
                if (ly < mt + 10) {
                    ly = ys[i] + r + 11;
                }
                g2.drawString(lbl, lx, ly);
            }

            int lx = ml + cw - 145, ly = mt + 10;
            g2.setColor(START_COLOR);
            g2.fillOval(lx, ly, 10, 10);
            g2.setColor(Color.DARK_GRAY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.drawString("Start position", lx + 14, ly + 9);
            g2.setColor(DOT_COLOR);
            g2.fillOval(lx, ly + 18, 10, 10);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("Request serviced", lx + 14, ly + 27);
        }
    }
}
