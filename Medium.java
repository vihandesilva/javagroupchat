/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystem;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Student
 */
public class Medium {
    

    private String[] details = new String[2];
    private Date date = new Date();
    SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    

   
   public void getLogin(String uname, String pwd){
       
      String[] temp = new String[2];
      
      try{
        temp = getLogin_ws(uname, pwd).toArray(new String[2]);
        
        //success scenario. Values corresponding to the given username and password are successfully
        // retrieved
        if(temp[0] != null && temp[1] !=null){
            details = temp;
        }
        //The geLogin() method in the webservice is programmed to return this if there was an error in 
        // connecting to database.
        else if(temp[0].equalsIgnoreCase("exception")){

            JOptionPane.showMessageDialog(null, "There was an error connecting to the database.", "Database Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        //This leads to the inevitable result of temp[0]="wrong". It means login details are-
        // incorrect. The getLogin() method in MockDB class is programmed to return this-
        // if its hasLogin() method returns false. (Which means the username/password combination-
        // does not exist)
        else{
            JOptionPane.showMessageDialog(null, "The login details you have entered are incorrect. Please type again", "Incorrect login details", JOptionPane.ERROR_MESSAGE);
        }
        
      }
      
      //Displays error when a Nullpointer exception arises. This is most likely caused by 
      // a communication failure with the database.
      catch(Exception e){
          JOptionPane.showMessageDialog(null, "There was an error connecting to database", "Database connection error", JOptionPane.ERROR_MESSAGE);
      }
      
      
   }  
   
   public boolean getRegistration(String name, String uname, String pwd){
       
      /*Return type used in Register GUI class to determine whether 
      login window should be loaded after pressing register button. */
     boolean success = false;
     
     try{
         
       success = getRegistration_ws(name, uname, pwd); 
       String[] temp = new String[2]; //Variable used to check for null pointer exceptions
       
       if(success == true){
           //if getRegistration_1() method returns true. That means the user has been-
           //-successfully registered.
           //Checking whether nullpointer exceptions can exist due to DB errors
            temp = getLogin_ws(uname,pwd).toArray(new String[2]);
            
       }
       
       else{
           JOptionPane.showMessageDialog(null, "The provided username is in use. Please use another username", "Username already exists", JOptionPane.ERROR_MESSAGE);
       }
    }
    //To catch nullpointer exception when MockDB webservice returns null due to database connection errors.
    catch(Exception e){
        JOptionPane.showMessageDialog(null, "There was an error connecting to database", "Database connection error", JOptionPane.ERROR_MESSAGE);
    }
       return success;
   }  
      
   //This is used by GUI classes to store user information for a session.
   public String[] getDetails(){
       
       if(details == null){
           JOptionPane.showMessageDialog(null, "No Login Information", "Error", JOptionPane.ERROR);
       }
       
       
       return details;
   }
   
   public void createThread(String threadName, String creator){
       
       boolean isAvailable = threadAvailable(threadName); 
       boolean success = false;
       
       if(isAvailable == true){
          success = addThread(threadName, creator);
          
          if(success == true){
              JOptionPane.showMessageDialog(null, "Thread Added Successfully");
          }
          
          else {
              JOptionPane.showMessageDialog(null, "There was an error adding the thread", "Error Adding Thread", JOptionPane.ERROR_MESSAGE);
          }
       }
       
       else{
           JOptionPane.showMessageDialog(null, "The given thread name is in use. Please use another", "Thread name in use", JOptionPane.ERROR_MESSAGE);
       }
   }
   
   //Saves chat data in a particular thread into the database
   public void sendContent(String threadName, String content){
       
       boolean success = addContent(threadName, content);
       
       if(success == false){
           JOptionPane.showMessageDialog(null, "There was an error adding content to database", "Error adding content to database", 0);
       }
       
   }
   
   //Gets an array of strings containing all the names of the threads in the database.
   public String[] getThreads(String creator){
       
      List<ws_db.CThread> threads = getThreads_ws(creator); 
      int size = threads.size();
      
      ws_db.CThread[] threads_array = new ws_db.CThread[size];
      
      threads_array = threads.toArray(new ws_db.CThread[size]);
      
      String[] names = new String[size];
      
      for(int i=0; i<threads_array.length; i++){
          names[i] = threads_array[i].getName();
      }
      
      
      
      
      
     return names;
   }

   //Gets information about a particular thread by referring to the database.
   public CThread getThread(String name){
       CThread thread = new CThread();
       String[] details = new String[3];
       
       //Converts return type List<String> of web service method getInfo(name) to a String Array.
       details = getThreadInfo(name).toArray(new String[3]);
       
       thread.setName(details[0]);
       thread.setCreator(details[1]);
       thread.setContent(details[2]);
       
       //Data contained in the fields of this object are used in the GUI Class: ChatWindow
       return thread;
   }
   

   
   //Used by the alterThread method when changing the name of the thread
   public boolean nameValidity(String name){
       boolean success = threadAvailable(name);
       
       return success;
   }
   
   public void deleteThread(String name){
       
       boolean isThere = !threadAvailable(name);
       
       if(isThere == true){
           boolean success = removeThread(name);
           
           if(success == false){
               JOptionPane.showMessageDialog(null, "Error connecting to database", "Database error", JOptionPane.ERROR_MESSAGE);
           }
           
           else {
               JOptionPane.showMessageDialog(null,"The Thread was removed successfully.");
           }
       }
   }
   
    //Converts a DateTime object into string and formats it. The return type is used in 
    //the GUI Class: ChatWindow. Used to show the date and time on which a particular message        
    //was posted in a thread.
   public String getDateTime(){
       date = new Date();
       ft = new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
       
       String dateTime = ft.format(date);
       
       return dateTime;
   }
   
   
  // REMOTELY CALLED METHODS OF WEBSERVICES //
   
  // Web Service: Server //
   
 //This method sends text to the webService "Server" which is used to relay chat messages
 
   public static String getMessage(java.lang.String msg) {
        ws_connection.Server service = new ws_connection.Server();
        ws_connection.MessageServer port = service.getMessageServerPort();
        return port.getMessage(msg);
    }


   private static java.util.List<ws_db.CThread> getThreads_ws(java.lang.String user) {
        ws_db.MockDB_Service service = new ws_db.MockDB_Service();
        ws_db.MockDB port = service.getMockDBPort();
        return port.getThreads(user);
    }


   private static List<String> getLogin_ws(java.lang.String username, java.lang.String password) {
        ws_db.MockDB_Service service = new ws_db.MockDB_Service();
        ws_db.MockDB port = service.getMockDBPort();
        return port.getLogin(username, password);
    }


   private static boolean addContent(java.lang.String name, java.lang.String content) {
        ws_db.MockDB_Service service = new ws_db.MockDB_Service();
        ws_db.MockDB port = service.getMockDBPort();
        return port.addContent(name, content);
    }

   private static java.util.List<java.lang.String> getThreadInfo(java.lang.String name) {
        ws_db.MockDB_Service service = new ws_db.MockDB_Service();
        ws_db.MockDB port = service.getMockDBPort();
        return port.getThreadInfo(name);
    }

   private static boolean threadAvailable(java.lang.String name) {
        ws_db.MockDB_Service service = new ws_db.MockDB_Service();
        ws_db.MockDB port = service.getMockDBPort();
        return port.threadAvailable(name);
    }

   private static boolean addThread(java.lang.String name, java.lang.String creator) {
        ws_db.MockDB_Service service = new ws_db.MockDB_Service();
        ws_db.MockDB port = service.getMockDBPort();
        return port.addThread(name, creator);
    }

   private static boolean removeThread(java.lang.String name) {
        ws_db.MockDB_Service service = new ws_db.MockDB_Service();
        ws_db.MockDB port = service.getMockDBPort();
        return port.removeThread(name);
    }

   private static boolean changeThread(java.lang.String oldThread, java.lang.String newThread) {
        ws_db.MockDB_Service service = new ws_db.MockDB_Service();
        ws_db.MockDB port = service.getMockDBPort();
        return port.changeThread(oldThread, newThread);
    }

   private static boolean getRegistration_ws(java.lang.String name, java.lang.String username, java.lang.String password) {
        ws_db.MockDB_Service service = new ws_db.MockDB_Service();
        ws_db.MockDB port = service.getMockDBPort();
        return port.getRegistration(name, username, password);
    }


}
