/**
 * File: Welcome.java
 * Date: December 1997 - January 1998
 * Author: Darsono Sutedja
 *
 * This is the "welcome" window where user can decide whether s/he
 * wants to enter the chat room or to register for a new account.
 * Note that the user will only need to register once.
 *
 * Any comments and suggestions are always welcomed.  Please send them
 * to dsutedja@cs.umb.edu
 *
 * Copyright (c) 1997 Darsono Sutedja
 */

import java.awt.*;
import java.awt.event.*; //for 1.1 event handling

public class Welcome extends Frame {

    public static void main(String[] argv) {
        new Welcome();
    }

    //====Constructor====//
    public Welcome() {
        super("Welcome to Java Chat v0.1");
        setBackground(Color.lightGray);
        setLayout(new XYLayout());
        //====
        //add the picture to position x=20, y=20 with width=275,
        //height=200
        //====
        add("20,20,275,200",new Picture());

        //====
        //Instantiate the three required buttons
        //====
        create = new Button("Create a new Account");
        logon  = new Button("Let me in!");
        quit   = new Button("Let me out!");
        //====
        //registering events for those three buttons
        //====
        create.addActionListener(new CreateAction());
        logon.addActionListener(new LogonAction());
        quit.addActionListener(new QuitAction());

        //====
        //Make those buttons responds to "enter" key
        //====
        create.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==e.VK_ENTER) {
                    Welcome.this.create();
                }
            }
        });

        logon.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==e.VK_ENTER) {
                    Welcome.this.logon();
                }
            }
        });

        quit.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==e.VK_ENTER) {
                    Welcome.this.quit();
                }
            }
        });
        //====
        //add those buttons to the appropriate location in the container
        //====
        add("0,220", create);
        add("140,220", logon);
        add("215, 220", quit);

        //====
        //Set the size of the window
        //====
        setSize(300,270);

        //====
        //Make the window appears at the center of the screen
        //====
        setLocation(((Util.getScrnWidth()-300)/2),(Util.getScrnHeight()-270)/2);

        //====
        //Make the window "closeable"
        //====
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //====
        //Display the window on screen
        //====
        show();
    }
    //======End of constructor=====//

    //=====Public sections========//

    //===
    //Show the Registration window
    //===
    public void create() {
        //dispose the current welcome screen, and...
        Welcome.this.setVisible(false);
        Welcome.this.dispose();
        //open up the Registration window
        new NewAccount();
    }

    //====
    //Quit the system
    //====
    public void quit() {
        ////System.out.println("Thank you for using JC 0.1");
        //Just quit the system
        System.exit(0);
    }

    //====
    //Show the login console
    //====
    public void logon() {
        //Dispose the welcome window
        Welcome.this.setVisible(false);
        Welcome.this.dispose();
        //open up the Login window
        new LoginConsole();
    }

    //======End of public sections====/

    //======Private section========//

    //Instance Variables
    private Button create, logon, quit;

    //==============
    //Inner classes for Event handling
    //==============

    //A canvas that will draw an image on the welcome screen
    private class Picture extends Canvas {
        public void paint(Graphics g) {
            //When the image is available, the codes below will be replaced
            g.setColor(Color.blue);
            g.fillRect(0,0,getSize().width-20, getSize().height-20);
            g.setColor(Color.black);
            g.setFont(new Font("TimesRoman",Font.BOLD,18));
            g.drawString("Add a picture here",60,60);
        }
    }

    //Listener for ActionEvent when user clicked on "Create a new account"
    //button
    private class CreateAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            create();
        }
    }

    //Listener for ActionEvent when user clicked on "Let me out" button
    private class QuitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            quit();
        }
    }

    //Listener for ActionEvent when user clicked on "Let me in" button
    private class LogonAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            logon();
        }
    }

    //======End of Private sections============//
}

//=========End of class==========//






