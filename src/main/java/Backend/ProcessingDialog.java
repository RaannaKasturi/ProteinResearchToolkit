package Backend;

import java.io.BufferedReader;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProcessingDialog {
    public static void main(String[] args) {
        boolean success = false;
        
        while (!success) {
            try {
                prntStructData();
                success = true;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            }
        }
        
        JOptionPane.showMessageDialog(null, "Data successfully printed");
    }
    
    public void rukhja(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessingDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
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