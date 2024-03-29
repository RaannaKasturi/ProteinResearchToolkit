package Backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PDBQuery {
    public static String[] getPDBID() {
        List<String> identifiersList = new ArrayList<>();
        
        try {
            // URL endpoint for fetching details
            String query = "Insulin";
            String urlEndpoint = "https://search.rcsb.org/rcsbsearch/v2/query?json=%7B%22query%22%3A%7B%22type%22%3A%22terminal%22%2C%22service%22%3A%22full_text%22%2C%22parameters%22%3A%7B%22value%22%3A%22"+query+"%22%7D%7D%2C%22return_type%22%3A%22entry%22%7D";
            
            // Create URL object
            URL url = new URL(urlEndpoint);
            
            // Create HttpURLConnection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // Set request method
            conn.setRequestMethod("GET");
            
            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray resultSet = jsonResponse.getJSONArray("result_set");
            
            // Iterate through result set
            for (int i = 0; i < resultSet.length(); i++) {
                JSONObject entry = resultSet.getJSONObject(i);
                String identifier = entry.getString("identifier");
                
                // Add identifier to the list
                identifiersList.add(identifier);
            }
            
            // Close connection
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Convert list to array
        String[] identifiersArray = identifiersList.toArray(new String[identifiersList.size()]);
        
        // Return the array of identifiers
        return identifiersArray;
    }
    
    public static void main(String[] args) {
        getPDBID();
    }
}
