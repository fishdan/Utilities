/**
 * File: NewAccount.java
 * Date: December 1997 - January 1998
 * Author: Darsono Sutedja
 *
 * This is the registration window that will allow
 * user to register and gain access to the chat room.
 *
 * There are two news here, the good and the bad.
 *
 * Here is the bad one first:
 * There are several rules that user must abide in order
 * to register successfully:
 * 1. User must provide First, and Last name
 * 2. User must provide his/her e-mail address (in correct
 *    format--usually username@domain.com).
 * 3. User must pick a unique username, and not allowed to
 *    entered "naughty" word as his/her username (YOU WILL
 *    BE BANNED FOREVER, YOUR IP ADDRESS IS LOGGED........
 *    .......just kidding ^_^).
 * 4. User must provide a unique password (must be longer
 *    than 4 characters).
 * 5. The last thing.  User must reenter the password, to
 *    make sure s/he really typed what s/he wants (because
 *    what is typed will not be shown in readable characters.
 *
 * Now the good news:  You will only need to register once.
 *
 * Any comments and suggestions are always welcomed.  Please send them
 * to dsutedja@cs.umb.edu
 *
 * Copyright (c) 1997 Darsono Sutedja
 */

import java.awt.*;
import java.awt.event.*;
import java.net.*;  //to connect to the server
import java.io.*;   //gain I/O access

public class NewAccount extends Frame {
    public static void main(String[] argv) {
        new NewAccount();
    }

    //======
    //Constructor
    //======
    public NewAccount() {
        super("Registration form");
        setBackground(Color.lightGray);
        //You won't find XYLayout in Java API :)
        setLayout(new XYLayout());
        Label nameL = new Label("Name: "),
              userL = new Label("User name: "),
              emailL= new Label("E-mail: "),
              passL = new Label("Password: "),
              againL= new Label("Confirm password: ");

        name   = new TextField(10);
        user   = new TextField(10);
        email  = new TextField(10);
        pass   = new TextField(10);
        again  = new TextField(10);

        //make the password fields hide all the typed characters
        pass.setEchoChar('*');
        again.setEchoChar('*');

        submit = new Button("Submit");
        clear  = new Button("Clear");
        cancel = new Button("Cancel");

        //registering event for those buttons
        submit.addActionListener(new Submit());
        clear.addActionListener(new Clear());
        cancel.addActionListener(new Cancel());

        //=============
        //Add those labels, and buttons to the specified
        //x-, y-axis, width, and height
        //=============
        add("20,20,120,25", nameL ); add("160,20,150,25", name );
        add("20,55,120,25", userL); add("160,55,150,25", user);
        add("20,90,120,25", emailL ); add("160,90,150,25", email );
        add("20,125,120,25", passL); add("160,125,150,25", pass);
        add("20,160,120,25", againL);  add("160,160,150,25", again);

        //=============
        //I need a panel to contains all the buttons and
        //set them to the center of the window (and it will
        //be pretty time consuming to calculate for XYLayout
        //...but, hey nothing is perfect ^_^)
        //=============
        Panel p = new Panel();
        p.setLayout(new FlowLayout(FlowLayout.CENTER));
        p.add(submit); p.add(clear); p.add(cancel);
        add("80,185", p);

        //set the size of window
        setSize(330,245);

        //=============
        //Make it closeable
        //=============
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.runFinalization();
                System.exit(0);
            }
        });

        //============
        //Make it appear on the center of the screen
        //============
        setLocation((Util.getScrnWidth()-330)/2,(Util.getScrnHeight()-220)/2);

        //============
        //Make it visible
        //============
        show();
    }
    //=======End of constructor...pretty long, huh?==========//

    //=====Private sections==========//

    //=====
    //Listener for ActionEvent that is fired when user clicked on
    //"Submit" button
    //=====
    private class Submit implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //==========
            //get the information from "password", and "Confirm password"
            //text fields
            //==========
            String pass1 = pass.getText();
            String pass2 = again.getText();

            if (user.getText().equals("") || name.getText().equals("")) {
                named = false;
            } else {
                named = true;
            }
            //Make sure they matched
            if (pass1.equals(pass2) && named) {
                //check if the mail is in valid format
                //this will only checks for '@' sign...so it's up to user
                //to provide "real" e-mail address
                if (!isValidEmailFormat(email.getText())) {
                    if (warning==null) warning=new Warning(NewAccount.this, "Error");
                    warning.setErrorMessage("Invalid e-mail format, please reenter");
                    warning.show();
                    email.setText("");
                }
                //check if the password is more than 4 characters...
                //less than 4 characters password is too easy to hack
                else if (pass1.length()<4) {
                    if (warning==null) warning=new Warning(NewAccount.this, "Error");
                    warning.setErrorMessage("Please enter a password with more than 4 characters");
                    warning.show();
                    pass.setText("");
                    again.setText("");
                }
                //If it satisfied all the requirements, then let the real thing
                //begins
                else {
                    try {
                        //get user's info
                        _name = name.getText();
                        _user = user.getText();
                        _pass = pass.getText();
                        _email= email.getText();
                        _date = ((java.util.Calendar.getInstance()).getTime()).toString();

                        //try to send the info to server
                        //if it fails to send info to server, then reset everything
                        sendToServer(_name, _email, _user, _pass);
                    }
                    //"Ooops, something is wrong", says the server
                    catch (MalInputException er) {}
                }
            }
            //Complains if pass1, and pass2 don't match
            else {
                if (warning==null) warning=new Warning(NewAccount.this, "Error");
                if (!named) {
                    warning.setErrorMessage("You must enter your name, and pick a user name");
                } else {
                    warning.setErrorMessage("You have entered a mismatched password, please try again");
                }
                warning.show();
                if (named) {
                    pass.setText("");
                    again.setText("");
                }
            }
        }

        //========
        //Method for checking if the e-mail strings contains an '@' sign
        //========
        private boolean isValidEmailFormat(String _email) {
            int length = _email.length();
            char temp[] = _email.toCharArray();
            for (int x=0; x<length; x++) {
                //if (_email.chatAt(x)=='@') return true;
                if (temp[x]=='@') return true;
            }
            return false;
        }


        //=======
        //Method that sends the user's info to server
        //Will throw a new instance of MalInputException if
        //something went wrong (the error is determined by server)
        //=======
        private void sendToServer(String _name, String _email, String _user, String _pass)
        throws MalInputException {
            try {
                //===Try to establish a connection on port 8255===//
                //Socket s = new Socket("127.0.0.1", 8255);
                Socket s = new Socket("acu.umb.edu", 8255);

                OutputStream sender = s.getOutputStream();

                //this object will be used to get info from server
                DataInputStream in = new DataInputStream(s.getInputStream());
                //this object will be used to send info to server
                PrintStream ps = new PrintStream(sender);
                //tell server that registration is requested
                ps.println("reg|"+_user+"|"+_name+"|"+_email+"|"+_pass);
                //get the result send by server, and check what's the result is
                //and responds.
                String result = in.readLine();
                if (result.equals("ok")) {
                    s.close();
                    //if succeeded, then dispose this window,
                    //save the info to client's place
                    //(this will be used for login console)
                    //then launch the welcome window again.
                    saveFile(_name, _user, _pass, _date);
                    NewAccount.this.setVisible(false);
                    NewAccount.this.dispose();
                    new Welcome();
                }
                else if (result.equals("Illegal")) {
                    s.close(); //something is wrong...close the connection
                    throw new MalInputException("Illegal name, please enter again");
                } else if (result.equals("duplicate")) {
                    s.close();
                    throw new MalInputException("The name already exists, please enter again");
                } else if (result.equals("dirty")) {
                    s.close();
                    throw new MalInputException("Hey!!! Watch your mouth!!!");
                }
            } catch (Exception er) {
                boolean connected = false;
                if (warning==null) warning=new Warning(NewAccount.this, "Error");
                String error = er.toString();
                //determine what kind of error
                if (er instanceof ConnectException || er instanceof UnknownHostException) {
                    connected = false;
                    warning.setErrorMessage("Connection to server can't be established.  The server might be down");
                } else {
                    connected = true;
                    warning.setErrorMessage(er.toString());
                }
                warning.show();
                disp(er.toString()); //debug

                //if name exists, and the error is sent by server, then clear all the fields
                if (connected) {
                    name.setText("");
                    user.setText("");
                    pass.setText("");
                    email.setText("");
                    again.setText("");
                    //System.err.println(er+" when sending info to server in NewAccount");
                }
            }
        }

        //=========
        //Method for saving information on client's place.
        //=========
        private void saveFile(String _name, String _user, String _pass, String _date ) {
            try {
                java.io.FileOutputStream out = new java.io.FileOutputStream("users.dat");
                //write(_date, out);
                write(_name, out);
                write(_user, out);
                write(_pass, out);

            } catch (java.io.IOException er) {}
        }

        //Helper class for writing the file
        private void write(String text, java.io.FileOutputStream out) throws java.io.IOException {
            for (int x=0; x<text.length(); x++) {
                out.write((int)text.charAt(x));
            }
            out.write((int)'\n');
        }
    }

    //===========
    //Listener for ActionEvent when user clicked the "clear" button
    //===========
    private class Clear implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            name.setText("");
            email.setText("");
            pass.setText("");
            again.setText("");
        }
    }

    //===========
    //Listener for ActionEvent when user clicked the "cancel" button
    //===========
    private class Cancel implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //dispose this window, and launch the welcome window
            NewAccount.this.setVisible(false);
            NewAccount.this.dispose();
            new Welcome();
        }
    }
    //temp...will be deleted
    private void disp(String text) {
        System.out.println(text);
    }

    //Private instance variables
    private Button submit, clear, cancel;
    private TextField name, email, pass, again, user;
    private String _name, _email, _pass, _again, _date, _user;
    private Warning warning;
    private boolean named = true;
}

