package Backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import org.json.JSONArray;
import org.json.JSONObject;

public class test {
    public static void main(String[] args) {
        JDialog processingDialog = new JDialog();
        JLabel processingLabel = new JLabel("Processing...", SwingConstants.CENTER);
        
        processingDialog.add(processingLabel);
        processingDialog.setSize(200, 100);
        processingDialog.setLocationRelativeTo(null); // Center the dialog on the screen
        processingDialog.setModal(true); // Make the dialog modal
        
        Thread processingThread = new Thread(() -> {
            try {
                // Simulate a long-running process
                prntStructData();
            } catch (IOException ex) {
                Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            processingDialog.dispose(); // Close the dialog when processing is done
        });
        
        processingThread.start();
        
        processingDialog.setVisible(true); // Display the processing dialog
    }
    public static void prntStructData() throws IOException {
        ProcessingDialog a = new ProcessingDialog();
        String PDBID = "4hhb";
        String endpoint = "https://data.rcsb.org/rest/v1/core/entry/"+PDBID;
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        JSONObject jsonResponse = new JSONObject(response.toString());
        String rcsbId = jsonResponse.getString("rcsb_id");
        String title = jsonResponse.getJSONObject("struct").getString("title");
        JSONArray exptlArray = jsonResponse.getJSONArray("exptl");
        StringBuilder methods = new StringBuilder();
        for (int i = 0; i < exptlArray.length(); i++) {
            JSONObject exptlObj = exptlArray.getJSONObject(i);
            String method = exptlObj.getString("method");
            methods.append(method).append(", ");
        }
        if (methods.length() > 0) {
            methods.setLength(methods.length() - 2);
        }
        System.out.println(rcsbId +"\n" + title + "\n" + methods.toString());
        for (int i =0; i<=10; i++){
            a.rukhja();
            System.out.println(i+"s");
        }
    }
}