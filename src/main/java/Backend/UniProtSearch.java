package Backend;

import Frontend.MainFrame;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UniProtSearch {
    private MainFrame mainFrame;

    public UniProtSearch(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    private List<String[]> fetchData(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        List<String[]> data = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            String[] columns = line.split("\t");
            data.add(columns);
        }

        reader.close();
        connection.disconnect();

        return data;
    }

    public void dispUniProt() {
        String pquery = mainFrame.getUniSearchText();
        String url = "https://rest.uniprot.org/uniprotkb/search?download=true&fields=accession%2Cid%2Cgene_names%2Corganism_name%2Clength%2Csequence&format=tsv&query=%28" + pquery + "%29&size=50";
        DefaultTableModel model = mainFrame.getTableModel();
        model.setRowCount(0);
        try {
            List<String[]> data = fetchData(url);
            if (data != null && !data.isEmpty()) {
                for (int i = 1; i < data.size(); i++) {
                    String[] row = data.get(i);
                    String entry = row[0];
                    String entryName = row[1];
                    String geneNames = row[2];
                    String organism = row[3];
                    String length = row[4];
                    String seq = row[5];
                    model.addRow(new Object[]{entry, entryName, length, organism, geneNames, seq});
                }
            } else {
                System.out.println("No data found.");
            }
        } catch (IOException e) {
            model.addRow(new Object[]{"No data found.", "No data found.", "No data found.", "No data found.", "No data found.", "No data found."});
        }
    }
}
