/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatsystem;

import java.util.ArrayList;

/**
 *
 * @author Student
 */
public class User {
    
    private static String username = "";
    private static String password = "";
    private static String name ="";
    private static ArrayList<String> threads = new ArrayList<String>();

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        User.name = name;
    }
    
    
    
    
    
}
