/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatservice;

import com.sun.xml.fastinfoset.util.StringArray;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.swing.JOptionPane;

/**
 *
 * @author Vihan
 */
@WebService(serviceName = "MockDB")
public class MockDB {
    
    

    private static String url = "jdbc:mysql://localhost/users";
    private static String uname = "root";
    private static String pwd = "";
    private static String jdbc = "com.mysql.jdbc.Driver";
    private static Connection con;
    private ArrayList<CThreadWS> threads = new ArrayList<>();    
   
    
   

    
    
    /**
     * Web service operation. Does registration and database integrity validation when requested
     * by the client.
     *
     * @return 
     */

    
        /**
     * Web service operation. Returns login details to client
     * @return 
     */
    @WebMethod(operationName = "getLogin")
    public String[] getLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) {
       
     String[] login = new String[2];
       
     try{   
        if(con == null){
            
           con = connect();
        
        }      
        boolean hasLogin = hasLogin(username, password);
        
        
        if(hasLogin == true){
            
            login = readLogin(username, password);
           
        }
        else{
            System.err.println("Login Details are incorrect");
            login[0] = "wrong" ;
//            JOptionPane.showMessageDialog(null, "The login details you have entered are incorrect. Please type again", "Incorrect login details", JOptionPane.ERROR_MESSAGE);
        }
        
      }
     catch(Exception e){
         
       if(login[0] != "wrong"){
         login[0] = "exception";
       }
      
       
    }
     return login;
   }
    
    
        
    //Checks whether given username exists in database table. Returns true if it does.
    private boolean hasUsername(String username){
        
        if(con == null){
            
           con = connect();
        
        }        
        boolean isValid = false;
        
        String searchSQL ="select * from accounts where username=\""+ username + "\"";
        PreparedStatement ps = null;
        ResultSet results = null;
        
        try {
            ps = con.prepareStatement(searchSQL);
            results = ps.executeQuery();
           
            if(results.next()){
                System.out.println("Results!!!");
                isValid = true;
            }
            
            else{
                System.out.println("No results");
            }
         
        }
        
        catch(Exception e){
            System.err.println(e);
//            JOptionPane.showMessageDialog(null, e, "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        
   
        
        
        return isValid; 
    }
    
    //Checks whether the given username and password exist in the databse table.
    //Returns true if both of them exist in the same row of the table.
    private boolean hasLogin(String username, String password){
        if(con == null){
            
           con = connect();
        
        }        
        boolean isValid = false;
        
        String searchSQL ="select * from accounts where username=\""+ username + "\"" + " and  password=\""+password+"\"";
        PreparedStatement ps = null;
        ResultSet results = null;
        
        try {
            ps = con.prepareStatement(searchSQL);
            results = ps.executeQuery();
           
            if(results.next()){
                System.out.println("Results!!!");
                isValid = true;
            }
            
            else{
                System.out.println("No results");
            }
         
        }
        
        catch(Exception e){
              System.err.println(e);
//            JOptionPane.showMessageDialog(null, e, "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        return isValid;         
    }
    
    
    //Reads username and name from the database table. and return those two values in an array.
    //This method is called if the hasLogin() method returns true.
    //This method is called by getLogin() method.
    private String[] readLogin(String username, String password){
        if(con == null){
            
           con = connect();
        
        }        
        
        String[] login = new String[2];
        
        String searchSQL ="select name, username from accounts where username=? and password=?";
        PreparedStatement ps = null;
        ResultSet results = null;
        
        try {
            ps = con.prepareStatement(searchSQL);
            ps.setString(1,username );
            ps.setString(2, password);
            results = ps.executeQuery();
           
            if(results.next()){
                login[0] = results.getString("username");
                login[1] = results.getString("name");
                
            }
            
            else{
                System.out.println("No results");
            }
         
        }     
        
        catch(Exception e){
            System.err.println("LOGIN SELECTION ERROR");
            System.out.println(e);
            //JOptionPane.showMessageDialog(null, e, "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        }
            
        
        return login;
    }
    
    //Inserts name, username and password to database.
    //Only executes when hasLogin returns false.
    //Called by the getRegistration() method
    private void register(String name, String username, String password){
        if(con == null){
            
           con = connect();
        
        }        
        String sql="insert into accounts(name, username, password) values(\""+ name + "\"," +
                    "\""+ username + "\"," + "\""+ password + "\")";
        
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            System.out.println("Registration Successful");
         }
        
        catch(Exception e){
//            JOptionPane.showMessageDialog(null, e, "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
    }
    
    //The connection code with the database goes here.
    private static Connection connect(){
        
      Connection conn = null;
        if(con == null){
            
           //con = connect();       
      
            try{
                Class.forName(jdbc);
                conn = DriverManager.getConnection(url, uname, pwd);
                System.out.println("Database Connection Successful");
                return conn;
            }

            catch(Exception e){
                System.err.println(e);
                //JOptionPane.showMessageDialog(null, e, "Database connection error", JOptionPane.ERROR_MESSAGE);
            }
        
      }  
       return conn; 
    }

    /**
     * Web service operation. Returns an arraylist of CThread objects to the client.
     * Data is retrieved from the "threads" table of the database.
     * @return 
     */
    @WebMethod(operationName = "getThreads")
    public ArrayList<CThreadWS> getThreads(@WebParam(name = "user") String user) {
        if(con == null){
            
           con = connect();
        
        }        
        String sql="select name, creator, content from threads"; 
        
        PreparedStatement ps = null;
        
        try{
            ps = con.prepareStatement(sql);
            ResultSet results = ps.executeQuery();
            
            while(results.next()){
                CThreadWS t = new CThreadWS();
                t.setName(results.getString("name"));
                t.setCreator(results.getString("creator"));
                t.setContent(results.getString("content"));
                threads.add(t);
            }
            
        }
        
        catch(Exception e){
              System.err.println(e);
//            JOptionPane.showMessageDialog(null, e, "Database Communication Error", JOptionPane.ERROR_MESSAGE);
        }
        
        
        return threads;
    }

    /**
     * Fills the content column of a given row of the "threads" table of the database.
     * This method every occasion when a user sends a message to a thread.
     */
    @WebMethod(operationName = "addContent")
    public boolean addContent(@WebParam(name = "name") String name, @WebParam(name = "content") String content) {
        
      boolean success = false;
       
      if(con == null){
            
           con = connect();
      }        
      
      String sql="update threads set content=? where name=?"; 
        
      PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, content);
            ps.setString(2, name);
            ps.executeUpdate();
            success = true;
//          JOptionPane.showMessageDialog(null, "Thread was successfully updated", "Thread Edit", 0);
         
         }
        
        catch(Exception e){
            System.err.println(e);
//            JOptionPane.showMessageDialog(null, e, "Database Communication Error", JOptionPane.ERROR_MESSAGE);
        } 
        
        return success;        
    }

    /**Gets information about a particular thread and returns it to the client application.
     *
     */
    @WebMethod(operationName = "getThreadInfo")
    public String[] getThreadInfo(@WebParam(name = "name") String name) {
        
        String[] info = new String[3];
      
      if(con == null){
            
           con = connect();
      }        
      
      String sql="select * from threads where name=?"; 
        
      PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet results = ps.executeQuery();
            
            while(results.next()){
              info[0] = results.getString("name");
              info[1] = results.getString("creator");
              info[2] = results.getString("content");
            }
            
//          JOptionPane.showMessageDialog(null, "Thread was successfully updated", "Thread Edit", 0);
         
         }
        
        catch(Exception e){
            System.err.println(e);
//            JOptionPane.showMessageDialog(null, e, "Database Communication Error", JOptionPane.ERROR_MESSAGE);
        } 
           return info;
    }

    /**
     * Creates a new entry in the "threads" table with the given information.
     * Returns true if the data entry is successful and no exception is thrown.
     */
    @WebMethod(operationName = "addThread")
    public boolean addThread(@WebParam(name = "name") String name, @WebParam(name = "creator") String creator) {
        
        boolean success = false;
        
        if(con == null){
            
           con = connect();
        
        }        
      String sql="insert into threads(name, creator) values(\""+ name + "\"," +
                     "\""+ creator + "\")";
        
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(sql);
            ps.executeUpdate();
            success = true;
//            JOptionPane.showMessageDialog(null, "Thread was successfully added", "Adding the Thread", 0);
         }
        
        catch(Exception e){
            System.err.println(e);
            //JOptionPane.showMessageDialog(null, e, "Database Communication Error", JOptionPane.ERROR_MESSAGE);
        }
        
        return success;
    }

    /**
     * Removes a thread with the name given in the argument of the method. Returns true if the
     * removal is successful and no exceptions are thrown.
     */
    @WebMethod(operationName = "removeThread")
    public boolean removeThread(@WebParam(name = "name") String name) {
        boolean success = false;
        
        if(con == null){
            
           con = connect();
        
        }        
        String sql="delete from threads where name=?"; 
        
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.executeUpdate();
            success = true;
//            JOptionPane.showMessageDialog(null, "Thread was successfully removed", "Thread Removal", 0);
         }
        
        catch(Exception e){
//            JOptionPane.showMessageDialog(null, e, "Database Communication Error", JOptionPane.ERROR_MESSAGE);
        } 
        
        return success;
    }

    /**
     * Supplementary method which renames a given thread. The arguments of the method are the
     * current and new(proposed) names of the thread.
     */
    @WebMethod(operationName = "changeThread")
    public boolean changeThread(@WebParam(name = "oldThread") String oldThread, @WebParam(name = "newThread") String newThread) {
        
        boolean success = false;
        
        if(con == null){
            
           con = connect();
        
        }        
      String sql="update threads set name=? where name=?"; 
        
      PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, newThread);
            ps.setString(2, oldThread);
            ps.executeUpdate();
            success = true;
//          JOptionPane.showMessageDialog(null, "Thread was successfully updated", "Thread Edit", 0);
         
         }
        
        catch(Exception e){
            System.err.println(e);
//            JOptionPane.showMessageDialog(null, e, "Database Communication Error", JOptionPane.ERROR_MESSAGE);
        }        
        return success;
    }

    /**
     * Selects the thread with the given name in the method argument from the "threads" table.
     * returns false when a thread by the name passed to the method does not exist.
     * 
     */
    @WebMethod(operationName = "threadAvailable")
    public boolean threadAvailable(@WebParam(name = "name") String name) {
        
      boolean available = true;
      
      if(con == null){
            
           con = connect();
      }        
      
      String sql="select * from threads where name=?"; 
        
      PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet results = ps.executeQuery();
            
            if(results.next() == true){
               available = false;
            }
            
            
//          JOptionPane.showMessageDialog(null, "Thread was successfully updated", "Thread Edit", 0);
         
         }
        
        catch(Exception e){
            System.err.println(e);
//            JOptionPane.showMessageDialog(null, e, "Database Communication Error", JOptionPane.ERROR_MESSAGE);
        }         
        
       return available;
    }

    /**
     * 
     */
    @WebMethod(operationName = "getRegistration")
    public boolean getRegistration(@WebParam(name = "name") String name, @WebParam(name = "username") String username, @WebParam(name = "password") String password) {
        
        boolean success = false;
    
        if(con == null){
            
           con = connect();
        
        }
        boolean hasUsername = hasUsername(username);
        success = !hasUsername;
      
        if(hasUsername==true){
            System.err.println("The Given Username is in Use.");

        }
        else{
            register(name, username, password);
        }

      return success;
    }
    


}
