/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Backend;
import java.sql.*;
import java.util.*;
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
            
            System.out.println("Enter Email ID: ");
            String email_id = sc.next();
            System.out.println("Enter Password: ");
            String password = sc.next();
            System.out.println("Enter License Key: ");
            String license_key = sc.next();
            
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Registered");
            Connection con;
            Statement smt;
            con = DriverManager.getConnection("jdbc:mysql://mysql1.serv00.com/m1762_proteintoolkit", "m1762_admin", "PTKnpk@2024");
            System.out.println("Connection Successful");
            smt = con.createStatement();
            
            /*
            EMAIL_ID PRIMARY KEY
            PASSWORD
            LICENSE_KEY (AUTOMATICALLY GENERATED)
            
            */
            String sql = "SELECT * FROM appData WHERE EMAIL_ID = '" + email_id + "';";
            ResultSet rs = smt.executeQuery(sql);

            if (rs.next()) {
                sql_email_id = rs.getString("EMAIL_ID");
                sql_password = rs.getString("PASSWORD");
                sql_license_key = rs.getString("LICENSE_KEY");

                if (email_id.equals(sql_email_id) && password.equals(sql_password) && license_key.equals(sql_license_key)) {
                    System.out.println("Login Successful.");
                } 
                else {
                    System.out.println("Credentials do not match.");
                }
            }
 
            else {
                System.out.println("User not found. Register to continue.");
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
            
            //GENERATE LICENSE_KEY AUTOMATICALLY - CODE REMAINING
            String license_key = "";
            
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Registered");
            Connection con;
            Statement smt;
            con = DriverManager.getConnection("jdbc:mysql://mysql1.serv00.com/m1762_proteintoolkit", "m1762_admin", "PTKnpk@2024");
            System.out.println("Connection Successful");
            smt = con.createStatement();
            
            System.out.println("Registration Successful.");

            String sql1 = "INSERT INTO appData VALUES('" + email_id + "','" + password + "','" + license_key + "');";
            smt.executeUpdate(sql1);
            
            //DISPLAY FINAL USER CREDENTIALS POST SUCCESSFUL REGISTRATION
            
            String sql2 = "SELECT * FROM appData WHERE EMAIL_ID = '" + email_id + "';";
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
                case 1: System.out.println("Option selected : 1. Login"); flag = false; break; //obj.login(); flag = false; break;
                case 2: System.out.println("Option selected : 2. Register"); flag = false; break; //obj.register_user(); flag = false; break;
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
