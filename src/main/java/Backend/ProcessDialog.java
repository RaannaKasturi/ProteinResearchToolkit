package Backend;

import Frontend.MainFrame;
import Frontend.UserAccount;
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
    private UserAccount UserAccount;
    
    public ProcessDialog(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public ProcessDialog(UserAccount UserAccount) {
        this.UserAccount = UserAccount;
    }
    
    public void processDialog(Runnable function) {
        JDialog processingDialog = new JDialog();
        JLabel processingLabel = new JLabel("Processing...\nPlease Wait", SwingConstants.CENTER);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(100, 10)); // Set width to 100
        progressBar.setIndeterminate(true);
        // Create a panel to hold label and progress bar with padding
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 50, 10)); // Add padding
        panel.add(processingLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        processingDialog.setContentPane(panel);
        processingDialog.setSize(300, 200); // Increased height
        processingDialog.setLocationRelativeTo(null); // Center the dialog on the screen
        processingDialog.setModal(true);
        processingDialog.setTitle("Processing");
        //processingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Disable closing
        processingDialog.setAlwaysOnTop(true); // Show dialog on top of other windows

        Thread processingThread = new Thread(() -> {
            // Simulate a long-running process
            function.run();

            SwingUtilities.invokeLater(() -> processingDialog.dispose()); // Close the dialog when processing is done
        });
        processingThread.start();
        processingDialog.setVisible(true); // Display the processing dialog
    }
    
}
