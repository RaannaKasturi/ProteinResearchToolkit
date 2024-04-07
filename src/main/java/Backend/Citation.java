package Backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class Citation {
    public static String getJSONResponse(String endpoint) {
        StringBuilder jsonResponse = new StringBuilder();

        try {
            // Create a URL object
            URL url = new URL(endpoint);

            // Create a HttpURLConnection object
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Check if the response code is OK (200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Create a BufferedReader to read the response
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String inputLine;

                    // Read the response line by line and append to StringBuilder
                    while ((inputLine = in.readLine()) != null) {
                        jsonResponse.append(inputLine);
                    }
                }
            } else {
                System.out.println("Failed to fetch data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonResponse.toString();
    }
    
    public static void main(String[] args) {
        try {
            // Define the URL endpoint
            String endpoint = "https://api.ncbi.nlm.nih.gov/lit/ctxp/v1/pubmed/?format=citation&id=35585814";
            String jsonResponse = getJSONResponse(endpoint);

            // Display the JSON response
            System.out.println("JSON Response:");
            System.out.println(jsonResponse);

            // Parse JSON response
            JSONObject jsonObject = new JSONObject(jsonResponse);

            // Check if the expected keys exist in the JSON response
            if (jsonObject.has("ama") && jsonObject.has("apa") && jsonObject.has("mla") && jsonObject.has("nlm")) {
                JSONObject ama = jsonObject.getJSONObject("ama");
                String amaOrig = ama.getString("orig");
                String amaFormat = ama.getString("format");

                JSONObject apa = jsonObject.getJSONObject("apa");
                String apaOrig = apa.getString("orig");
                String apaFormat = apa.getString("format");

                JSONObject mla = jsonObject.getJSONObject("mla");
                String mlaOrig = mla.getString("orig");
                String mlaFormat = mla.getString("format");

                JSONObject nlm = jsonObject.getJSONObject("nlm");
                String nlmOrig = nlm.getString("orig");
                String nlmFormat = nlm.getString("format");

                // Print orig and format for each citation format
                System.out.println("AMA format");
                System.out.println("AMA orig: " + amaOrig);
                System.out.println("AMA format: " + amaFormat);
                System.out.println("APA format");
                System.out.println("APA orig: " + apaOrig);
                System.out.println("APA format: " + apaFormat);
                System.out.println("MLA format");
                System.out.println("MLA orig: " + mlaOrig);
                System.out.println("MLA format: " + mlaFormat);
                System.out.println("NLM format");
                System.out.println("NLM orig: " + nlmOrig);
                System.out.println("NLM format: " + nlmFormat);
            } else {
                System.out.println("Missing one or more citation formats in the JSON response.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}