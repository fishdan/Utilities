/**
 * File: LoginConsole.java
 * Date: December 1997- January 1998
 * Author: Darsono Sutedja
 *
 * This the "login" window where user types in his/her username
 * and password, then log into the chat room.
 *
 * Any comments and suggestions are always welcomed.  Please send them
 * to dsutedja@cs.umb.edu
 *
 * Copyright (c) 1997 Darsono Sutedja
 */

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;

public class LoginConsole extends Frame {
    public static void main(String[] argv) {
        new LoginConsole();
    }

    //====Constructor=====//
    public LoginConsole() {
        super("Login");
        setBackground(Color.lightGray);

        Panel p = new Panel();
        p.setLayout(new GridLayout(2,2));
        Label passL = new Label("Password: "),
              userL = new Label("Username: ");
        pass = new TextField(10); pass.setEchoChar('*');
        user = new TextField(10);
        p.add(userL); p.add(user);
        p.add(passL); p.add(pass);

        Panel pp = new Panel();
        pp.setLayout(new FlowLayout(FlowLayout.CENTER));
        submit = new Button("Submit"); submit.addActionListener(new SubmitAction());
        submit.requestFocus();
        clear  = new Button("Clear");  clear.addActionListener(new ClearAction());
        cancel = new Button("Cancel"); cancel.addActionListener(new CancelAction());

        //'Submit' will be called whenever user pressed enter, and the focus is on 'submit'
        submit.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==e.VK_ENTER) {
                    LoginConsole.this.submit();
                }
            }
        });

        //'clear' will be called whenever user pressed enter, and the focus is on 'clear'
        clear.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==e.VK_ENTER) {
                    LoginConsole.this.clear();
                }
            }
        });

        //'cancel' will be called whenever user pressed enter, and the focus is on 'cancel'
        cancel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==e.VK_ENTER) {
                    LoginConsole.this.cancel();
                }
            }
        });
        pp.add(submit); pp.add(clear); pp.add(cancel);
        this.add("North", p);
        this.add("Center", pp);

        //========
        //Let Java determine the correct size of this window
        //========
        pack();

        //========
        //Make this window closeable
        //========
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //========
        //Make it display on the center of the screen
        //========
        setLocation((Util.getScrnWidth()-getSize().width)/2,(Util.getScrnHeight()-getSize().height)/2);

        show();

        //========
        //Fill text fields with the data in "users.dat"
        //If the file doesn't exist, then the text fields will be left blank
        //========                    /|\
        fillTextFields();         //   |
    }                             //   |  explanation
                                  //   |
    //============                     |
    //--------------------------------/
    //============
    private void fillTextFields() {
        try {
            File file = new File("users.dat");
            if (!file.exists()) {
                return; //leave the text fields blank
            } else {
                FileInputStream in = new FileInputStream("users.dat");
                BufferedReader d = new BufferedReader(new InputStreamReader(in));
                String junk = d.readLine();
                String _user = d.readLine();
                String _pass = d.readLine();
                pass.setText(_pass);
                user.setText(_user);
            }
        } catch (Exception er) {
             if (warning==null) warning=new Warning(LoginConsole.this, "Error");
             warning.setErrorMessage("Error at LoginConsole...please send mail to administrator");
             warning.show();
        }
    }

    //=========
    //Listener for ActionEvent when user clicked on "Submit" button
    //=========
    private class SubmitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ////disp(user.getText()+" loggin in");
            submit();
        }
    }

    //====
    //submit the information
    //====
    public void submit() {
        try {
            ////disp("Trying to connect to server...");

            //try to send the information to server
            sendInfoToServer();
            ////System.out.println("Server says everything is fine");
        } catch (MalInputException er) {
            //receive error from server
            if (warning==null) warning=new Warning(LoginConsole.this, "Error");
            warning.setErrorMessage("Invalid login information...please try again");
            warning.show();
            user.setText(""); user.requestFocus();
            pass.setText("");
        } catch (Exception er) {
            if (warning==null) warning=new Warning(LoginConsole.this, "Error");
            warning.setErrorMessage("Connection with server can't be established.  The server might be down.");
            warning.show();
        }
    }

    //sending info
    public void sendInfoToServer()
    throws MalInputException, IOException {
        String username = user.getText();
        String password = pass.getText();

        //=====Try to connect to server at port 8255===//
        Socket s = new Socket("acu.umb.edu", 8255);
        //Socket s = new Socket("127.0.0.1", 8255);

        OutputStream output = s.getOutputStream();
        //This object will allow client to receive info from server
        DataInputStream in = new DataInputStream(s.getInputStream());
        //This object will allow client to send info to server
        PrintStream ps  = new PrintStream(output);
        //Tell server that the client requests a login
        ps.println("login|"+username+"|"+password);
        //Get the status of the login
        String line = in.readLine();
        //Check the status, and responds to that.
        if (line.equals("ok")) {
            System.out.println("SendInfoToServer succeeded");
            LoginConsole.this.setVisible(false);
            LoginConsole.this.dispose();
            new ChatClient(ps, s);
            //s.close();
        } else if (line.equals("illegal user")) {
            throw new MalInputException("Illegal user");
        } else if (line.equals("illegal password")) {
            throw new MalInputException("Illegal password");
        } else if (line.equals("")) {
            System.out.println("It's empty damnit");
        }
    }

    //===========
    //Listener for ActionEvent when user clicked on "clear" button
    //===========
    private class ClearAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            clear();
        }
    }

    //===
    //Clear text fields
    //===
    public void clear() {
        pass.setText("");
        user.setText("");
    }

    //===========
    //Listener for ActionEvent when user clicked on "cancel" button
    //===========
    private class CancelAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            cancel();
        }
    }

    //===
    //Cancel operation and go back to welcome screen
    //===
    public void cancel() {
        //dispose this window
        LoginConsole.this.setVisible(false);
        LoginConsole.this.dispose();
        //go back to welcome window
        new Welcome();
    }

    //debug
    private void disp(String text) {
        System.out.println(text);
    }
    Button clear, submit, cancel;
    TextField pass, user;
    Warning warning;
}

