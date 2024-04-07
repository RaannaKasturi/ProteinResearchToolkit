package Backend;

import Frontend.MainFrame;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PMCSearch {
    
    private MainFrame mainFrame;

    public PMCSearch(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    public void DispPubMed (String query) throws Exception {
        // Search for PubMed records
        String searchUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&RetMax=50&term=" + query;
        String pubmedIdList = fetchPubmedIdList(searchUrl);
        // Fetch the details for each article
        if (pubmedIdList != null && !pubmedIdList.isEmpty()) {
            String fetchUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=" + pubmedIdList + "&retmode=xml";
            String xmlResponse = fetchXmlResponse(fetchUrl);
            // Parse the response to extract the desired details
            Document doc = parseXmlString(xmlResponse);
            // Process and store the extracted details
            processArticleDetails(doc);
        }
    }

    private String fetchPubmedIdList(String url) throws Exception {
        String pubmedIdList = "";
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            pubmedIdList = parsePubmedIdList(response.toString());
        }
        return pubmedIdList;
    }

    private String parsePubmedIdList(String xmlString) {
        StringBuilder pubmedIdList = new StringBuilder();
        String pattern = "<Id>(\\d+)</Id>";
        Matcher matcher = Pattern.compile(pattern).matcher(xmlString);
        while (matcher.find()) {
            pubmedIdList.append(matcher.group(1)).append(",");
        }
        if (pubmedIdList.length() > 0) {
            pubmedIdList.deleteCharAt(pubmedIdList.length() - 1);
        }
        return pubmedIdList.toString();
    }

    private String fetchXmlResponse(String url) throws Exception {
        StringBuilder response = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }

    private Document parseXmlString(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new ByteArrayInputStream(xmlString.getBytes()));
    }
    
    public String formatDate(String pubDate) {
        String year;
        if (pubDate.length() >= 7) {
            // Extract year and month (e.g., "2024Apr")
            year = pubDate.substring(0, 4);
            String month = pubDate.substring(4, 7);
            year += " " + month;
        } else if (pubDate.length() >= 10) {
            // Extract year, month, and day (e.g., "2024Apr19")
            year = pubDate.substring(0, 4);
            String month = pubDate.substring(4, 7);
            String day = pubDate.substring(7, 9);
            year += " " + month + " " + day;
        } else {
            // Only year is available (e.g., "2024")
            year = pubDate;
        }
        return year;
    }

    private void processArticleDetails(Document doc) {
        NodeList articleList = doc.getElementsByTagName("PubmedArticle");
        for (int i = 0; i < articleList.getLength(); i++) {
            Node articleNode = articleList.item(i);
            if (articleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element articleElement = (Element) articleNode;
                String pmid = articleElement.getElementsByTagName("PMID").item(0).getTextContent();
                String title = articleElement.getElementsByTagName("ArticleTitle").item(0).getTextContent();
                String journaltitle = articleElement.getElementsByTagName("Title").item(0).getTextContent();
                String jtabbrevation = articleElement.getElementsByTagName("ISOAbbreviation").item(0).getTextContent();
                String pubDate = articleElement.getElementsByTagName("PubDate").item(0).getTextContent();
                String year = pubDate.substring(pubDate.lastIndexOf(" ") + 1);
                String doi = articleElement.getElementsByTagName("ELocationID").item(0).getTextContent();
                String date = formatDate(year);
                DefaultTableModel model = (DefaultTableModel) mainFrame.jTable3.getModel();
                SwingUtilities.invokeLater(() -> {
                    model.addRow(new Object[]{pmid, title,journaltitle +" ("+jtabbrevation+")", date, doi});
                });
//                System.out.println("PMID: " + pmid);
//                System.out.println("Title: " + title);
//                System.out.println("Title: " + journaltitle);
//                System.out.println("Title: " + jtabbrevation);
//                System.out.println("Year: " + date);
//                System.out.println("DOI: " + doi);
//                System.out.println("--------------------------");
            }
        }
    }
}
