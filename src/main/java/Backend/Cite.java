package Backend;

import de.undercouch.citeproc.output.Bibliography;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.jbibtex.*;

public class Cite {

    public static void main(String[] args) {
        String doi = "10.1002/bip.20734";
        String doi2bibURL = "https://www.doi2bib.org/8350e5a3e24c153df2275c9f80692773/doi2bib?id=" + doi;

        try {
            String bibData = fetchBibData(doi2bibURL);
            String apaCitation = convertToAPA(bibData, doi);

            System.out.println("APA Citation:");
            System.out.println(apaCitation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String fetchBibData(String doi2bibURL) throws Exception {
        URL url = new URL(doi2bibURL);
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        reader.close();
        return stringBuilder.toString();
    }

    public static String convertToAPA(String bibData, String doi) throws Exception {
        Bibliography bibliography = BibTeXParser.parse(bibData);
        String[] entries = bibliography.getEntries();

        if (entries.isEmpty()) {
            throw new RuntimeException("No BibTeX entries found for the provided DOI.");
        }

        BibTeXEntry entry = entries.get(0);

        // Extract required fields
        String author = entry.getField(BibTeXEntry.KEY_AUTHOR).toUserString();
        String year = entry.getField(BibTeXEntry.KEY_YEAR).toUserString();
        String title = entry.getField(BibTeXEntry.KEY_TITLE).toUserString();
        String journal = entry.getField(BibTeXEntry.KEY_JOURNAL).toUserString();
        String volume = entry.getField(BibTeXEntry.KEY_VOLUME).toUserString();
        String pages = entry.getField(BibTeXEntry.KEY_PAGES).toUserString();

        // Format APA citation
        String apaCitation = String.format("%s (%s). %s. %s, %s(%s), %s.", formatAuthors(author), year, title, journal, volume, pages, doi);

        return apaCitation;
    }

    private static String formatAuthors(String authors) {
        // Assuming authors are separated by "and"
        String[] authorList = authors.split(" and ");
        StringBuilder formattedAuthors = new StringBuilder();

        for (int i = 0; i < authorList.length; i++) {
            String[] nameParts = authorList[i].split(",");
            String lastName = nameParts[0].trim();
            String[] firstNames = nameParts[1].trim().split(" ");
            StringBuilder formattedFirstName = new StringBuilder();

            for (String name : firstNames) {
                if (name.length() > 0) {
                    formattedFirstName.append(name.charAt(0)).append(". ");
                }
            }

            formattedAuthors.append(lastName).append(", ").append(formattedFirstName);
            if (i < authorList.length - 1) {
                formattedAuthors.append(" & ");
            }
        }

        return formattedAuthors.toString();
    }
}
