package Backend;

import Frontend.MainFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;

public class PDBSearch {
    private MainFrame mainFrame;

    public PDBSearch(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    public String[] getPDBID() {
        List<String> identifiersList = new ArrayList<>();
        String query = mainFrame.getPDBSearchText();
        try {
            String urlEndpoint = "https://search.rcsb.org/rcsbsearch/v2/query?json=%7B%22query%22%3A%7B%22type%22%3A%22terminal%22%2C%22service%22%3A%22full_text%22%2C%22parameters%22%3A%7B%22value%22%3A%22"+query+"%22%7D%7D%2C%22return_type%22%3A%22entry%22%7D";
            
            URL url = new URL(urlEndpoint);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            conn.setRequestMethod("GET");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray resultSet = jsonResponse.getJSONArray("result_set");
            
            for (int i = 0; i < resultSet.length(); i++) {
                JSONObject entry = resultSet.getJSONObject(i);
                String identifier = entry.getString("identifier");
                
                identifiersList.add(identifier);
            }
            
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String[] identifiersArray = identifiersList.toArray(new String[identifiersList.size()]);
        return identifiersArray;
    }
    
    public void dispPDB(String PDBID) throws IOException {
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

        // Get the table model
        DefaultTableModel model = (DefaultTableModel) mainFrame.jTable2.getModel();

        // Add a new row for the current PDB entry
        SwingUtilities.invokeLater(() -> {
            model.addRow(new Object[]{rcsbId, title, methods.toString()});
        });

        connection.disconnect();
    }
}
