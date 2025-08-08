/**
 * File: Connector.java
 * Date: December 1997 - January 1998
 * Author: Daniel Fishman
 *
 * This is the middle man who takes care of the server-client trafic
 *
 * Comments and suggestions are welcomed.  Please send them to
 * dfish@cs.umb.edu
 *
 * Copyright (c) 1997 Daniel Fishman
 */
import java.io.*;
import java.awt.*;
import java.util.*;
import java.net.*;

public class Connector extends Thread {
    //====Constructor====//
    public Connector(Socket s, ChatClient client) {
        connection = s;
        this.client= client;
    }
    //====Starter engine of the connector=====//
    public void run() {
        try {
            ////System.out.println("I'm running in Connector");
            //This object will allow client to receive info from server
            DataInputStream in = new DataInputStream(connection.getInputStream());
            boolean done = false;
            //loop whenever client is still connected to server
            while (client.connected) {
                ///System.out.println("Waiting to read");
                str = in.readLine();
                //Ignore blank strings
                if (!str.equals("")) {
                    ////System.out.println("read: "+str);
                    //Let the client parse the strings, and decide the action
                    client.parseCommand(str);
                    //Let the thread sleeps for 1/10th seconds...just in case
                    sleep(100);
                }
            }
            //I'm done, terminate the connection
            connection.close();
        } catch (Exception er) {
            if (warning==null) warning=new Warning(new Frame(), "Error");
            warning.setErrorMessage("You've been disconnected.  The server might be down.");
            warning.show();
            client.parseCommand("chat|You need to quit and login again."+
                                "\nSorry, for any inconvenience.");
            ///System.err.println(er+" in connector");
        }
    }
    Socket connection;
    ChatClient client;
    String str="";
    String name;
    Warning warning;

}
