package Backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Citation {

    public static void main(String[] args) {
        String doi = "10.1126/science.169.3946.635";
        String encodedDoi = null;
        try {
            encodedDoi = URLEncoder.encode(doi, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String citationURL = "https://citation.crosscite.org/format?doi=" + encodedDoi + "&style=apa&lang=en-US";

        try {
            // Send HTTP GET request
            URL url = new URL(citationURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Print response
            System.out.println("Response:");
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
