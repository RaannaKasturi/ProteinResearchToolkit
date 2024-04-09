package Backend;

import Frontend.MainFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONObject;

public class LiteratureSearch {
    private MainFrame mainFrame;

    public LiteratureSearch(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    public static boolean isPDF(String urlString) {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder pageSource = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                pageSource.append(line).append("\n");
            }
            reader.close();
            String pageSourceString = pageSource.toString(); 
            if (pageSourceString != null && (pageSourceString.contains("application/pdf") || pageSourceString.contains("application/x-pdf"))) {
                //System.out.println("The URL " + urlString + " contains a PDF file.");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
    
    public void ArticleSearch(String query) {
        String actualquery = query.replace(" ", "+");
        try {
            String apiUrl = "https://api.crossref.org/works?query.title="+actualquery+"&select=DOI,title,author,issued,is-referenced-by-count&rows=100&mailto=support@crossref.org";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }

            conn.disconnect();

            JSONObject jsonObject = new JSONObject(output.toString());
            JSONArray items = jsonObject.getJSONObject("message").getJSONArray("items");
            DefaultTableModel model = (DefaultTableModel) mainFrame.jTable3.getModel();
            model.setRowCount(0);
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                String doi = item.optString("DOI", "Data not found");
                String title = item.getJSONArray("title").optString(0, "Data not found");
                JSONArray authorsArray = item.optJSONArray("author");
                StringBuilder authors = new StringBuilder();
                if (authorsArray != null) {
                    for (int j = 0; j < authorsArray.length(); j++) {
                        JSONObject author = authorsArray.getJSONObject(j);
                        if (j != 0) {
                            authors.append(", ");
                        }
                        authors.append(author.getString("given")).append(" ").append(author.getString("family"));
                    }
                } else {
                    authors.append("Data not found");
                }
                String issuedDate = item.optJSONObject("issued")
                        .optJSONArray("date-parts")
                        .optJSONArray(0)
                        .join(", ");
                issuedDate = issuedDate.isEmpty() ? "Data not found" : issuedDate;
                final String date = issuedDate;
                String timescited = item.optString("is-referenced-by-count", "Data not found");
                String articleURL = "https://sci-hub.se/" + doi;
                if (isPDF(articleURL)==true) {
                    articleURL = "https://sci-hub.se/" + doi;
                } else {
                    articleURL = "https://dx.doi.org/" + doi;
                }
                final String aURL = articleURL;
                SwingUtilities.invokeLater(() -> {
                    model.addRow(new Object[]{doi, title,authors.toString(), date, timescited, aURL});
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
