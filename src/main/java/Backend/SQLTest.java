package Backend;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.*;
/**
 *
 * @author prarthi kothari
 */
public class SQLTest {

    /**
     * @param args the command line arguments
     */
    
    Scanner sc = new Scanner(System.in);
    String sql_email_id, sql_password, sql_license_key;
    int choice;
    
    public void login()
    {
        try {
            boolean user_input = true;
            System.out.println("Enter Email ID: ");
            String email_id = sc.next();
            System.out.println("Enter Password: ");
            String password = sc.next();
            
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
                        user_input = false;
                    } 
                    else {
                        System.out.println("Credentials do not match with email id.");
                    }
                }

                else {
                    System.out.println("User not found. Register to continue.");
                    user_input = false;
                }
            }
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
    
    public void register_user()
    {
        try
        {
            System.out.println("Enter Email ID: ");
            String email_id = sc.next();
            System.out.println("Enter Password: ");
            String password = sc.next();
            String license_key = generateLicenseKey();
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Registered");
            Connection con;
            Statement smt;
            
            con = DriverManager.getConnection("jdbc:mysql://tgj.h.filess.io:3307/ProteinToolkit_positivewe", "ProteinToolkit_positivewe", "7cfab746c1ffd2b58544cf136cb5fbdc192d9f7b");
            System.out.println("Connection Successful");
            smt = con.createStatement();
            
            System.out.println("Registration Successful.");

            String sql1 = "INSERT INTO APPDATA VALUES('" + email_id + "','" + password + "','" + license_key + "');";
            smt.executeUpdate(sql1);
                        
            String sql2 = "SELECT * FROM APPDATA WHERE EMAIL_ID = '" + email_id + "';";
            ResultSet rs = smt.executeQuery(sql2);

            if (rs.next()) {
                sql_email_id = rs.getString("EMAIL_ID");
                sql_password = rs.getString("PASSWORD");
                sql_license_key = rs.getString("LICENSE_KEY");
                
                System.out.println("*FINAL DETAILS POST REGISTRATION*");
                System.out.println("Email ID: " + sql_email_id);
                System.out.println("Password: " + sql_password);
                System.out.println("License Key: " + sql_license_key);
            }
            
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
    
    public static String generateLicenseKey() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMMddHHmmss");
        String formattedDate = dateFormat.format(new Date(System.currentTimeMillis()));
        return "PTK" + formattedDate + "NPK";
    }
    
    public static void main(String[] args) {
        
        SQLTest obj = new SQLTest();
        System.out.println("*MAIN MENU*");
        
        boolean flag = true;
        
        while(flag)
        {
            System.out.println("\nSelect 1 or 2 or 3");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            obj.choice = obj.sc.nextInt();
            switch(obj.choice)
            {
                case 1: obj.login(); flag = false; break;
                case 2: obj.register_user(); flag = false; break;
                case 3: System.out.println("Option selected : 3. Exit"); System.exit(0); break;
                default: System.out.println("No Option selected. Select only from 1 or 2 or 3"); break;
            }
            if (flag == false)
            {
                break;
            }
        }
    }
    
}
