package gui;

import memory.*;
import model.PageReplacementResult;
import model.PageReplacementResult.Step;
import util.InputParser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PageReplacementPanel extends JPanel {

    private JTextField framesField = new JTextField("3", 6);
    private JTextField refStringField = new JTextField("7, 0, 1, 2, 0, 3, 0, 4", 18);
    private JRadioButton rbFIFO = new JRadioButton("FIFO", true);
    private JRadioButton rbLRU = new JRadioButton("LRU");
    private JRadioButton rbOPT = new JRadioButton("Optimal (OPT)");
    private JRadioButton rbClock = new JRadioButton("Second Chance");

    private JTable resultTable;
    private DefaultTableModel tableModel;
    private FrameVisualizer frameViz;

    private JLabel lblFaults = new JLabel("—");
    private JLabel lblHits = new JLabel("—");
    private JLabel lblFaultRate = new JLabel("—");
    private JLabel lblStep = new JLabel("Step: —");

    private JButton btnPrev = new JButton("◀ Prev");
    private JButton btnNext = new JButton("Next ▶");
    private JButton btnPlay = new JButton("▶▶ Auto Play");
    private JButton btnReset = new JButton("⏮ Reset");
    private JSlider speedSlider = new JSlider(200, 1500, 700);

    private List<Step> steps;
    private int currentStep = -1;
    private Timer autoTimer;

    static final Color FAULT_BG = new Color(255, 210, 210);
    static final Color HIT_BG = new Color(210, 245, 210);
    static final Color FAULT_FG = new Color(150, 0, 0);
    static final Color HIT_FG = new Color(0, 110, 0);
    static final Color CURRENT_BG = new Color(195, 225, 255);
    static final Color FRAME_NEW = new Color(170, 205, 255);
    static final Color FRAME_OLD = new Color(225, 225, 225);
    static final Color FRAME_EMPTY = new Color(245, 245, 245);

    public PageReplacementPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(buildLeft(), BorderLayout.WEST);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);
    }

    private JPanel buildLeft() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setPreferredSize(new Dimension(210, 0));
        p.setBorder(BorderFactory.createTitledBorder("Input"));
        p.add(labeled("Frames:", framesField));
        p.add(Box.createVerticalStrut(6));
        p.add(labeled("Reference string:", refStringField));
        p.add(Box.createVerticalStrut(10));

        JPanel algoBox = new JPanel();
        algoBox.setLayout(new BoxLayout(algoBox, BoxLayout.Y_AXIS));
        algoBox.setBorder(BorderFactory.createTitledBorder("Algorithm"));
        ButtonGroup bg = new ButtonGroup();
        for (JRadioButton rb : new JRadioButton[]{rbFIFO, rbLRU, rbOPT, rbClock}) {
            bg.add(rb);
            algoBox.add(rb);
        }
        p.add(algoBox);
        p.add(Box.createVerticalStrut(10));

        JButton runBtn = new JButton("▶  Run Simulation");
        runBtn.setAlignmentX(CENTER_ALIGNMENT);
        runBtn.addActionListener(e -> runSimulation());
        p.add(runBtn);
        p.add(Box.createVerticalStrut(12));

        JPanel anim = new JPanel();
        anim.setLayout(new BoxLayout(anim, BoxLayout.Y_AXIS));
        anim.setBorder(BorderFactory.createTitledBorder("Step Controls"));
        lblStep.setAlignmentX(CENTER_ALIGNMENT);
        lblStep.setFont(new Font("SansSerif", Font.BOLD, 12));
        anim.add(lblStep);
        anim.add(Box.createVerticalStrut(4));
        JPanel r1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 2));
        r1.add(btnReset);
        r1.add(btnPrev);
        JPanel r2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 2));
        r2.add(btnNext);
        r2.add(btnPlay);
        anim.add(r1);
        anim.add(r2);
        anim.add(Box.createVerticalStrut(4));
        JLabel sl = new JLabel("Speed:");
        sl.setAlignmentX(CENTER_ALIGNMENT);
        anim.add(sl);
        speedSlider.setInverted(true);
        speedSlider.setAlignmentX(CENTER_ALIGNMENT);
        speedSlider.setMaximumSize(new Dimension(185, 28));
        anim.add(speedSlider);
        p.add(anim);

        setAnimEnabled(false);
        btnReset.addActionListener(e -> goToStep(0));
        btnPrev.addActionListener(e -> goToStep(currentStep - 1));
        btnNext.addActionListener(e -> goToStep(currentStep + 1));
        btnPlay.addActionListener(e -> togglePlay());
        return p;
    }

    private JPanel buildCenter() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        tableModel = new DefaultTableModel(new String[]{"Step", "Page", "Frames", "Result"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        resultTable = new JTable(tableModel) {
            public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    String val = (String) getModel().getValueAt(row, 3);
                    if (val != null && val.contains("Fault")) {
                        c.setBackground(row == currentStep ? CURRENT_BG : FAULT_BG);
                        c.setForeground(FAULT_FG);
                    } else {
                        c.setBackground(row == currentStep ? CURRENT_BG : HIT_BG);
                        c.setForeground(HIT_FG);
                    }
                }
                if (row == currentStep) {
                    c.setBackground(CURRENT_BG);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        resultTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultTable.setRowHeight(22);
        resultTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && resultTable.getSelectedRow() >= 0) {
                goToStep(resultTable.getSelectedRow());
            }
        });
        p.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        JPanel metrics = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 5));
        metrics.setBorder(BorderFactory.createTitledBorder("Metrics"));
        style(lblFaults, Color.RED.darker());
        style(lblHits, new Color(0, 120, 0));
        style(lblFaultRate, new Color(150, 80, 0));
        metrics.add(new JLabel("Page Faults:"));
        metrics.add(lblFaults);
        metrics.add(new JLabel("Hits:"));
        metrics.add(lblHits);
        metrics.add(new JLabel("Fault Rate:"));
        metrics.add(lblFaultRate);
        p.add(metrics, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildBottom() {
        JPanel w = new JPanel(new BorderLayout());
        w.setBorder(BorderFactory.createTitledBorder("Memory Frames — Visual State"));
        frameViz = new FrameVisualizer();
        frameViz.setPreferredSize(new Dimension(0, 85));
        w.add(frameViz);
        return w;
    }

    private void runSimulation() {
        stopPlay();
        try {
            int numFrames = InputParser.parsePositiveInt(framesField.getText(), "Frames");
            int[] ref = InputParser.parseIntArray(refStringField.getText());
            PageReplacementResult result;
            if (rbFIFO.isSelected()) {
                result = FIFO.simulate(ref, numFrames);
            } else if (rbLRU.isSelected()) {
                result = LRU.simulate(ref, numFrames);
            } else if (rbOPT.isSelected()) {
                result = OPT.simulate(ref, numFrames);
            } else {
                result = SecondChance.simulate(ref, numFrames);
            }
            steps = result.getSteps();
            tableModel.setRowCount(0);
            for (Step s : steps) {
                String frames = "[" + Arrays.stream(s.frames).mapToObj(f -> f == -1 ? " —" : String.format("%2d", f)).collect(Collectors.joining(" | ")) + "]";
                tableModel.addRow(new Object[]{s.stepNumber, s.page, frames, s.isPageFault ? "✗  Page Fault" : "✓  Hit"});
            }
            lblFaults.setText(String.valueOf(result.getTotalPageFaults()));
            lblHits.setText(String.valueOf(result.getTotalHits()));
            lblFaultRate.setText(String.format("%.1f%%", result.getPageFaultRate()));
            setAnimEnabled(true);
            currentStep = -1;
            goToStep(0);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void goToStep(int idx) {
        if (steps == null || steps.isEmpty()) {
            return;
        }
        idx = Math.max(0, Math.min(idx, steps.size() - 1));
        currentStep = idx;
        Step s = steps.get(idx);
        frameViz.update(s.frames, s.page, s.isPageFault);
        resultTable.setRowSelectionInterval(idx, idx);
        resultTable.scrollRectToVisible(resultTable.getCellRect(idx, 0, true));
        resultTable.repaint();
        lblStep.setText("Step " + (idx + 1) + " / " + steps.size());
        btnPrev.setEnabled(idx > 0);
        btnNext.setEnabled(idx < steps.size() - 1);
        if (idx == steps.size() - 1) {
            stopPlay();
        }
    }

    private void togglePlay() {
        if (autoTimer != null && autoTimer.isRunning()) {
            stopPlay();
            return;
        }
        autoTimer = new Timer(speedSlider.getValue(), e -> {
            if (currentStep < steps.size() - 1) {
                goToStep(currentStep + 1);
            } else {
                stopPlay();
            }
        });
        autoTimer.start();
        btnPlay.setText("⏸ Pause");
    }

    private void stopPlay() {
        if (autoTimer != null) {
            autoTimer.stop();
        }
        btnPlay.setText("▶▶ Auto Play");
    }

    private void setAnimEnabled(boolean b) {
        btnPrev.setEnabled(b);
        btnNext.setEnabled(b);
        btnPlay.setEnabled(b);
        btnReset.setEnabled(b);
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

    private static class FrameVisualizer extends JPanel {

        private int[] frames = new int[0];
        private int page = -1;
        private boolean fault = false;

        void update(int[] frames, int page, boolean fault) {
            this.frames = frames.clone();
            this.page = page;
            this.fault = fault;
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (frames.length == 0) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int bw = 64, bh = 54, gap = 14, total = frames.length * (bw + gap) - gap;
            int sx = (getWidth() - total) / 2, sy = (getHeight() - bh) / 2 - 6;
            for (int i = 0; i < frames.length; i++) {
                int x = sx + i * (bw + gap);
                boolean empty = frames[i] == -1, isNew = !empty && frames[i] == page && fault;
                g2.setColor(empty ? FRAME_EMPTY : isNew ? FRAME_NEW : FRAME_OLD);
                g2.fillRoundRect(x, sy, bw, bh, 12, 12);
                g2.setStroke(new BasicStroke(isNew ? 2.5f : 1f));
                g2.setColor(isNew ? new Color(60, 120, 200) : Color.GRAY);
                g2.drawRoundRect(x, sy, bw, bh, 12, 12);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
                g2.setColor(Color.GRAY);
                String fl = "Frame " + i;
                g2.drawString(fl, x + (bw - g2.getFontMetrics().stringWidth(fl)) / 2, sy + 13);
                g2.setFont(new Font("SansSerif", Font.BOLD, 20));
                g2.setColor(empty ? Color.LIGHT_GRAY : isNew ? new Color(20, 70, 170) : Color.DARK_GRAY);
                String val = empty ? "—" : String.valueOf(frames[i]);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(val, x + (bw - fm.stringWidth(val)) / 2, sy + bh / 2 + fm.getAscent() / 2 + 2);
            }
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.setColor(fault ? FAULT_FG : HIT_FG);
            g2.drawString(fault ? "✗  Page Fault — page " + page + " loaded into memory" : "✓  Hit — page " + page + " already in memory", 10, sy + bh + 18);
        }
    }
}
