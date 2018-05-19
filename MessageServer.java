/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatservice;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Vihan
 */
@WebService(serviceName = "Server")
public class MessageServer {

    /**
     * Returning message sent to server back to client. Used in the client GUI class: Chat Window
     */
    @WebMethod(operationName = "getMessage")
    public String getMessage(@WebParam(name = "msg") String msg) {
        String message = msg;
        System.out.println("MESSAGE SERVER CLASS TEST");
        System.out.println("MESSAGE : " + msg);
        System.out.println("TEST SUCCESSFUL");
        System.out.println("");
        return message;
    }
}
