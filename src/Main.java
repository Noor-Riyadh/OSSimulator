import gui.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point for the Memory & Disk Management Simulator.
 * Run this class to launch the application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // fallback to default look and feel
            }
            new MainFrame().setVisible(true);
        });
    }
}
