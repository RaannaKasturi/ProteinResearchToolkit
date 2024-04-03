package Backend;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LicenseKeyGenVerify {
    public static String generateLicenseKey() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMMddHHmmss");
        String formattedDate = dateFormat.format(new Date());
        return "PTK" + formattedDate + "NPK";
    }
    
    public static boolean isValidLicenseKey(String licenseKey) {
        // Define the regex pattern for the license key
        String regex = "PTK\\d{4}(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)\\d{2}\\d{6}NPK";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

        // Match the license key against the pattern
        Matcher matcher = pattern.matcher(licenseKey);

        // Return true if the license key matches the pattern, otherwise false
        return matcher.matches();
    }
    
    public static void main(String[] args) {
        String licenseKey = generateLicenseKey().toUpperCase();
        System.out.println("Generated License Key: " + licenseKey);
        boolean isValid = isValidLicenseKey(licenseKey);
        System.out.println("Is valid license key? " + isValid);
    }
}
