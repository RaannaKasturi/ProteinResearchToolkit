package Backend;

import Frontend.MainFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class ProcessDialog {
    private MainFrame mainFrame;
    
    public ProcessDialog(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    public void processDialog(Runnable function) {
        JDialog processingDialog = new JDialog();
        JLabel processingLabel = new JLabel("Processing...\nPlease Wait", SwingConstants.CENTER);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(100, 10)); 
        progressBar.setIndeterminate(true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 50, 10)); 
        panel.add(processingLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);
        processingDialog.setContentPane(panel);
        processingDialog.setSize(300, 200); 
        processingDialog.setLocationRelativeTo(null); 
        processingDialog.setModal(true);
        processingDialog.setTitle("Processing");
        processingDialog.setAlwaysOnTop(true); 
        Thread processingThread = new Thread(() -> {
            function.run();
            SwingUtilities.invokeLater(() -> processingDialog.dispose());
        });
        processingThread.start();
        processingDialog.setVisible(true);
    }
}
