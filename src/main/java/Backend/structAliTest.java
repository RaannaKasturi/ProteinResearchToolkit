package Backend;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolViewer;
import org.jmol.viewer.Viewer;

import javax.swing.*;
import java.awt.*;

public class structAliTest {

    public static void main(String[] args) {
        // Create a JFrame to hold the Jmol panel
        JFrame frame = new JFrame("Jmol Integration Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a JmolViewer
        JmolAdapter adapter = new SmarterJmolAdapter();
        JmolViewer viewer = Viewer.allocateViewer(frame, adapter);
        
        // Load a molecular structure
        String pdbCode = "1crn"; // Example PDB code
        String pdbUrl = "https://files.rcsb.org/download/" + pdbCode + ".pdb"; // URL to download PDB file
        viewer.openFile(pdbUrl);

        // Create a JPanel to hold the Jmol viewer
        JPanel panel = new JPanel(new BorderLayout());
        panel.add((Component) viewer, BorderLayout.CENTER);

        // Add the panel to the JFrame
        frame.add(panel);
        
        // Set the JFrame visible
        frame.setVisible(true);
    }
}
