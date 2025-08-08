/**
 * File: ClientApplet
 * Date: February 12, 1998 -
 * Author: Darsono Sutedja
 *
 * This program is a conversion from ChatClient java application that I wrote.
 *
 * Copyright (c) 1998 Darsono Sutedja
 */

///==============================================================================
/// NOTE:
/// There are several "screens" in this applet, and since I want all elements
/// to be embedded into the browser, I don't want to use Frame (except for the
/// error dialog windows.  In order to do that, I defined several methods that
/// will destroy previous "screen", and create a new one.  Here are the methods'
/// name:
/// -  buildWelcomeScreen()  --  will create the welcome "screen" where user can
///                              either register for a new account or begin the
///                              chat session.
/// -  buildNewAccountScreen()  --  will take user to the registration "screen".
/// -  buildLoginScreen()  --  This is where user enter his/her username, and
///                            login to the server.
/// -  buildClientScreen()  --  This is the chat "screen".
///
/// So, simply call these methods, and the applet will automatically destroy, and
/// display the designated screen.
///==============================================================================

import java.applet.*;
import java.awt.*;
//import java.awt.event.*; //for debugging only
import java.net.*;
import java.io.*;
import java.util.*;

public class ClientApplet extends Applet {

    public void init() {
        buildServerChoice();
    }

    //=============================================================//
    /**Server Choosing screen **/
    TextField serverF;
    Button okB;

    public void buildServerChoice() {
        removeAll();
        setBackground(Color.lightGray);
        setLayout(new XYLayout());
        serverF = new TextField(10);
        serverF.setText("127.0.0.1");
        okB = new Button("Okay");
        add("30,30,70,25", new Label("server name: "));
        add("105,30,150,25", serverF);
        add("260,30,70,25", okB);
        validate();
    }

    //====================================================================================//
    /**Welcome screen**/
    public void buildWelcomeScreen() {
        removeAll();

        setBackground(Color.lightGray);
        setLayout(new XYLayout());
        //====
        //add the picture to position x=20, y=20 with width=275,
        //height=200
        //====
        Image image = getImage(getDocumentBase(), getParameter("WelcomeImage"));
        //***wait for image***//
        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(image, 0);
        try {
            tracker.waitForID(0);
        } catch (Exception er) {}
        add("20,20,275,200",new Picture(image));

        //====
        //Instantiate the three required buttons
        //====
        create = new Button("Create a new Account");
        logon  = new Button("Connect now!");

        //====
        //add those buttons to the appropriate location in the container
        //====
        add("20,220", create);
        add("180,220", logon);

        //====
        //Set the size of the window
        //====
        resize(300,270);

        //force applet to repaint all the components
        validate();
    }



    //welcome screen instance variables
    Button create, logon, quit;
    Image image;

    //=====================================================================================//
    /***New Account screen***/
    public void buildNewAccountScreen() {
        //destroy all cimponents
        removeAll();

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
        pass.setEchoCharacter('*');
        again.setEchoCharacter('*');

        submit = new Button("Submit");
        clear  = new Button("Clear");
        cancel = new Button("Cancel");

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
        resize(330,245);

        //=============
        //Make it closeable

        //force applet to repaint all the components
        validate();
    }
    //Submit info to server, if fail display error window
    private void submitNewAccountInfo() {
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
                if (warning==null) warning=new Warning(new Frame(), "Error");
                warning.setErrorMessage("Invalid e-mail format, please reenter");
                warning.show();
                email.setText("");
            }
            //check if the password is more than 4 characters...
            //less than 4 characters password is too easy to hack
            else if (pass1.length()<4) {
                if (warning==null) warning=new Warning(new Frame(), "Error");
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
            if (warning==null) warning=new Warning(new Frame(), "Error");
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
            //Socket s = new Socket("eris.cs.umb.edu", 8255);
            Socket s = new Socket(serverName, PORT);
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
                /////saveFile(_name, _user, _pass, _date);
                removeAll();
                buildWelcomeScreen();
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
            if (warning==null) warning=new Warning(new Frame(), "Error");
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
            /////disp(er.toString()); //debug

            //if name exists, and the error is sent by server, then clear all the fields
            if (connected) {
                name.setText("");
                user.setText("");
                pass.setText("");
                email.setText("");
                again.setText("");
                //System.err.println(er+" when sending info to server in NewAcount");
            }
        }
    }

    //Private instance variables
    private Button submit, clear, cancel;
    private TextField name, email, pass, again, user;
    private String _name, _email, _pass, _again, _date, _user;
    //private Warning warning;
    private boolean named = true;

    //=====================================================================================//
    /***Login Screen****/
    public void buildLoginScreen() {
        removeAll();
        setLayout(new BorderLayout());
        setBackground(Color.lightGray);

        Panel p = new Panel();
        p.setLayout(new GridLayout(2,2));
        Label loginPassL = new Label("Password: "),
              loginUserL = new Label("Username: ");
        loginPass = new TextField(10); loginPass.setEchoChar('*');
        loginUser = new TextField(10);
        p.add(loginUserL); p.add(loginUser);
        p.add(loginPassL); p.add(loginPass);

        Panel pp = new Panel();
        pp.setLayout(new FlowLayout(FlowLayout.CENTER));
        loginSubmit = new Button("Submit");
        loginClear  = new Button("Clear");
        loginCancel = new Button("Cancel");

        pp.add(loginSubmit); pp.add(loginClear); pp.add(loginCancel);
        this.add("North", p);
        this.add("Center", pp);

        //force applet to repaint all the components
        validate();
    }

    //====
    //submit the information
    //====
    private void submitFromLogin() {
        try {
            ////disp("Trying to connect to server...");

            //try to send the information to server
            sendInfoToServer();
            ////System.out.println("Server says everything is fine");
        } catch (MalInputException er) {
            //receive error from server
            if (warning==null) warning=new Warning(new Frame(), "Error");
            warning.setErrorMessage("Invalid login information...please try again");
            warning.show();
            user.setText(""); user.requestFocus();
            pass.setText("");
        } catch (Exception er) {
            if (warning==null) warning=new Warning(new Frame(), "Error");
            warning.setErrorMessage("Connection with server can't be established.  The server might be down.");
            warning.show();
        }
    }

    //sending info
    private void sendInfoToServer()
    throws MalInputException, IOException {
        String username = loginUser.getText();
        String password = loginPass.getText();

        //=====Try to connect to server at port 8255===//
        System.out.println("Got here");
        //Socket s = new Socket("eris.cs.umb.edu", 8255);
        Socket s = new Socket(serverName, PORT);
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
            ////new ChatClient(ps, s);
            buildClientScreen(s, ps);
            //s.close();
        } else if (line.equals("illegal user")) {
            throw new MalInputException("Illegal user");
        } else if (line.equals("illegal password")) {
            throw new MalInputException("Illegal password");
        } else if (line.equals("")) {
            System.out.println("It's empty damnit");
        }
    }

    //clear all Text fields
    private void clearLoginFields() {
        loginPass.setText("");
        loginUser.setText("");
    }

    //cancel
    private void cancelLogin() {
        buildWelcomeScreen();
    }

    //instance variables for login console
    Button loginClear, loginSubmit, loginCancel;
    TextField loginPass, loginUser;

    //=====================================================================================//
    /***Chat Client Screen***/
    public void buildClientScreen(Socket s, PrintStream ps) {
        removeAll();
        new ChatClient(ps, s, this);
    }

    //=====================================================================================//
    /****************EVENT HANDLER FOR EVERYONE********************/
    public boolean action(Event e, Object o) {
        //for server choosing
        if (e.target == okB) {
            try {
                String temp = serverF.getText();
                Socket socket = new Socket(temp, PORT);
                //succeeded
                socket.close();
                buildWelcomeScreen();
            } catch (Exception er) {
                if (warning==null) warning=new Warning(new Frame(), "Error");
                warning.setErrorMessage("Unknown host or server is down, try acu.umb.edu");
                warning.show();
            }
        }
        //for welcome
        if (e.target == create) {
            buildNewAccountScreen();
        }
        //for new account
        if (e.target == logon) {
            buildLoginScreen();
        }
        if (e.target == cancel) {
            buildWelcomeScreen();
        }
        if (e.target == submit) {
            submitNewAccountInfo();
        }
        //for login
        if (e.target == loginClear) {
            clearLoginFields();
        }
        if (e.target == loginSubmit) {
            submitFromLogin();
        }
        if (e.target == loginCancel) {
            cancelLogin();
        }
        return true;
    }

    public void paint(Graphics g) {
        if (inClientMode) {
            g.setColor(Color.lightGray);
            g.draw3DRect(27,42,347,126,true); //ta
            g.draw3DRect(27,204,347,26,true); //tf
            g.draw3DRect(27,272,236,66,true); //list
        }
    }

    public void triggerMode() {
        inClientMode = !inClientMode;
    }


    //===========Instance variable(s) for everyone==============//
    Warning warning;
    String serverName="127.0.0.1";
    final int PORT=8255;
    boolean inClientMode = false;
}

//***Picture class for welcome screen***//
class Picture extends Canvas {
    Image image;
    public Picture(Image image) {
        this.image = image;
    }
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, 255, 180, this);
    }
}

//***************Error dialog box****************//
class Warning extends Dialog {
    public Warning(Frame parent, String title) {
        super(parent, title, true);
        this.setLayout(new BorderLayout());

        font = new Font("TimesRoman", Font.BOLD, 14);

        Panel p = new Panel(); p.setLayout(new FlowLayout(FlowLayout.CENTER));
        ok = new Button("Acknowledge");
        p.add(ok);

        this.setResizable(false);
        this.add("South", p);
        this.resize(500,130);
        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
        int scrnWidth = scrnSize.width;
        int scrnHeight= scrnSize.height;
        this.move((scrnWidth-500)/2, (scrnHeight-130)/2);
    }

    //Let caller decide what error message to be displayed
    //if none, then the default error message will be displayed (blank)
    public void setErrorMessage(String error) {
        this.error = error;
        FontMetrics f = this.getFontMetrics(font);
        textWidth  = f.stringWidth(error);
    }

    //let caller decide what the title to be displayed
    public void setTitle(String title) {
        this.setTitle(title);
    }

    public boolean action(Event e, Object o) {
        if (e.target == ok) {
            this.hide();
            this.dispose();
        }
        return true;
    }

    public boolean handleEvent(Event e) {
        if (e.id == Event.WINDOW_DESTROY) {
            this.hide();
            this.dispose();
        }
        return super.handleEvent(e);
    }

    //paint the error message
    public void paint(Graphics g) {
        g.setColor(Color.red);
        g.setFont(font);
        g.drawString(error, (500-textWidth)/2, 50);
    }

    private String error = "";
    private Button ok;
    private int textWidth;
    private Font font;
}

class MalInputException extends Exception {
    //zero-argument constructor...designed for lazy programmer
    public MalInputException() {
        super();
    }
    //constructor that takes caller error message
    public MalInputException(String error) {
        super(error);
        this.error = error;
    }
    //used when caller wants to convert the error to strings
    public String toString() {
        return error;
    }
    //default error message.
    String error="Damn!!!";
}

