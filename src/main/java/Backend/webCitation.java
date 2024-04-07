package Backend;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebCitation {
    public static void main(String[] args) {
        String pmcId = "35585814"; // Replace with your desired PMC ID
        String url = "https://api.ncbi.nlm.nih.gov/lit/ctxp/v1/pubmed/?format=citation&id=" + pmcId;

        // Set system property for ChromeDriver path if not using Maven
        // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        WebDriver driver = null;

        try {
            // Instantiate ChromeDriver
            driver = new ChromeDriver();

            // Navigate to the URL
            driver.get(url);

            // Extract data using appropriate methods based on response format (e.g., parsing HTML)
            String data = driver.getPageSource(); // Example: Get entire response for further processing
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Quit the driver to close the browser
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
