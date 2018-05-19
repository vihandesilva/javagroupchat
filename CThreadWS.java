/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatservice;

/**
 *
 * @author Vihan
 */
public class CThreadWS {
    
    //This class is used to store details of a particular chat and send it to the client application.
    
    private String name;
    private String creator;
    private String content;
    
    public CThreadWS(){
        
        name="none";
        creator = "none";
        content = "none";
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
    
    
}
