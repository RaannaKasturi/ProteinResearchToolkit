package Backend;

import Frontend.MainFrame;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;


public class SQLTest {

    private MainFrame mainFrame;

    public SQLTest(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    Scanner sc = new Scanner(System.in);
    String sql_email_id, sql_password, sql_license_key;
    int choice;
    
    public String generateLicenseKey() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMMddHHmmss");
        String formattedDate = dateFormat.format(new java.util.Date());
        return "PTK" + formattedDate + "NPK";
    }
    
    public void savedata(String email, String pass, String key){
        String[] words = {email, pass, key};

        // Relative file path within the project directory
        String filePath = "./assets/Credentials.txt";

        try {
            // Create a BufferedWriter to write to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Write nothing to erase the contents
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            // Write each word to the file on a separate line
            for (String word : words) {
                writer.write(word); // Write the word
                writer.newLine();   // Write a new line
            }

            // Close the writer
            writer.close();

            System.out.println("Words have been written to the file successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }
    
    public void login()
    {
        try {
            boolean user_input = true;
            String email_id = mainFrame.jTextField5.getText();
            String password = mainFrame.jTextField6.getText();
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Registered");
            Connection con;
            Statement smt;
            con = DriverManager.getConnection("jdbc:mysql://tgj.h.filess.io:3307/ProteinToolkit_positivewe", "ProteinToolkit_positivewe", "7cfab746c1ffd2b58544cf136cb5fbdc192d9f7b");
            System.out.println("Connection Successful");
            smt = con.createStatement();
            
            /*
            EMAIL_ID PRIMARY KEY
            PASSWORD
            LICENSE_KEY (AUTOMATICALLY GENERATED)
            
            */
            String sql = "SELECT * FROM APPDATA WHERE EMAIL_ID = '" + email_id + "';";
            ResultSet rs = smt.executeQuery(sql);
            
            while(user_input)
            {
                if (rs.next()) {
                    sql_email_id = rs.getString("EMAIL_ID");
                    sql_password = rs.getString("PASSWORD");
                    sql_license_key = rs.getString("LICENSE_KEY");

                    if (email_id.equals(sql_email_id) && password.equals(sql_password)) {
                        System.out.println("Login Successful.");
                        mainFrame.jLabel9.setText("Login Succesfull");
                        user_input = false;
                    } 
                    else {
                        mainFrame.jLabel9.setText("Credentials do not match with Email ID");
                        System.out.println("Credentials do not match with email id.");
                    }
                }
                else {
                    mainFrame.jLabel9.setText("User not found. Please Register");
                    System.out.println("User not found. Register to continue.");
                    user_input = false;
                }
            }
            savedata(sql_email_id, sql_password, sql_license_key);
            mainFrame.jLabel2.setText(sql_email_id);
            mainFrame.jLabel3.setText(sql_license_key);
            mainFrame.jLabel4.setText("Registered");
            rs.close();
            smt.close();
            con.close();
        }
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void register()
    {
        try
        {
            String email_id = mainFrame.jTextField5.getText();
            String password = mainFrame.jTextField6.getText();
            String rlicense_key = generateLicenseKey();
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Registered");
            Connection con;
            Statement smt;
            
            con = DriverManager.getConnection("jdbc:mysql://tgj.h.filess.io:3307/ProteinToolkit_positivewe", "ProteinToolkit_positivewe", "7cfab746c1ffd2b58544cf136cb5fbdc192d9f7b");
            System.out.println("Connection Successful");
            smt = con.createStatement();
            
            System.out.println("Registration Successful.");

            String sql1 = "INSERT INTO APPDATA VALUES('" + email_id + "','" + password + "','" + rlicense_key + "');";
            smt.executeUpdate(sql1);
                        
            String sql2 = "SELECT * FROM APPDATA WHERE EMAIL_ID = '" + email_id + "';";
            ResultSet rs = smt.executeQuery(sql2);

            if (rs.next()) {
                String sql_remail_id = rs.getString("EMAIL_ID");
                String sql_rpassword = rs.getString("PASSWORD");
                String sql_rlicense_key = rs.getString("LICENSE_KEY");
                
                System.out.println("*FINAL DETAILS POST REGISTRATION*");
                System.out.println("Email ID: " + sql_remail_id);
                System.out.println("Password: " + sql_rpassword);
                System.out.println("License Key: " + sql_rlicense_key);
                savedata(sql_remail_id, sql_rpassword, sql_rlicense_key);
            }
            mainFrame.jTextField7.setText(rlicense_key);
            mainFrame.jLabel9.setText("Registration Succesfull");
            smt.close();
            con.close();
        }
        
        catch(SQLException se)
        {
            se.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }    
}
