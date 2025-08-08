/**
 * File: ChatClient.java
 * Date: December 1997-January 1998
 * Author: Darsono Sutedja
 *
 * This is the client window.
 *
 * Revision 1:  it's now an applet.
 *
 * Copyright (c) 1997 Darsono Sutedja
 */

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class ChatClient {

    //====Constructor====//
    public ChatClient(PrintStream ps, Socket s, ClientApplet parentApplet) {
        this.parent = parentApplet;

        /*MenuBar mb = new MenuBar();
        Menu file = new Menu("File");
        this.setMenuBar(mb);
        mb.add(file);*/

        parent.setLayout(new XYLayout());

        /*a = new IButton(Util.getImage("home.gif"),2);
        a.addMouseListener(new MouseAction());

        b = new IButton(Util.getImage("home.gif"), 2);
        b.addMouseListener(new MouseAction());
        c = new IButton(Util.getImage("home.gif"), 2);
        c.addMouseListener(new MouseAction());*/

        list = new List(4, false);
        list.addActionListener(new WhoisAction());
        list.addItemListener(new PrivateAction());

        ta = new TextArea("", 2, 2, TextArea.SCROLLBARS_VERTICAL_ONLY);
        ta.setEditable(false);
        tf = new TextField(10);
        //register an event for tf, so that it will responds to certain
        //key pressed by user ("enter", "up-arrow", and "down-arrow");
        tf.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==e.VK_ENTER) {
                    send(tf.getText());
                }
                if (e.getKeyCode()==e.VK_UP) {
                    try {
                        tf.setText(history[--index]);
                    } catch (Exception er) {}
                }
                if (e.getKeyCode()==e.VK_DOWN) {
                    try {
                        tf.setText(history[index++]);
                    } catch (Exception er) {}
                }
            }
        });

        checkProperty();

        Image im = Util.getImage("taL.jpg"),
              imm= Util.getImage("tfL.jpg");
        Util.waitForImage(parent, im); Util.waitForImage(parent, imm);
        Label taL = new Label("History"),

              tfL = new Label("Message");
              taL.setFont(new Font("TimesRoman", Font.ITALIC+Font.BOLD, 14));
              tfL.setFont(new Font("TimesRoman", Font.ITALIC+Font.BOLD, 14));
        Label  lL  = new Label("On-line users");
               lL.setFont(new Font("TimesRoman", Font.ITALIC+Font.BOLD, 14));

        /* creating info button */
        image = parent.createImage(30,30);
        Graphics g = image.getGraphics();
        g.setFont(new Font("TimesRoman", Font.BOLD+Font.ITALIC, 26));
        g.setColor(Color.blue);
        g.drawString("I", 10, 23);
        a = new IButton(image, 3);
        a.addMouseListener(new MouseAction());
        /****/

        parent.add("325,285,45,45", a);
        //=====
        //add every thing to the frame
        //=====
        parent.add("30,20,60,20", taL);
        parent.add("30,45,340,120",ta);
        parent.add("30,182,60,20",tfL);
        parent.add("30,207,340,20",tf);
        /*parent.add("30,218,45,45", a);
        parent.add("105,218,45,45",b);
        parent.add("182,218,45,45",c);*/
        parent.add("30,250,80,20", lL);
        parent.add("30,275,230,60",list);

        //create a new image object, then wait until it's completely
        //loaded in memory
        image = Util.getImage("05.jpg");
        Util.waitForImage(parent, image);

        //resize the window
        parent.setSize(400,380);


        //=========
        //Create a middle man that will take care of every server-client trafic
        //=========
        mps = ps;//new PrintStream(ps);
        new Connector(s, this).start();
        connected = true;

        parent.triggerMode();

        parent.validate();
    }

    /*public void createInfo() {

    }*/

    //==============
    //The text appeared in the text field under motif is not readable
    //because it's too big, but everything is fine under win95.  Therefore
    //I have to check the property in which this program is running, if it's
    //running under Solaris, then decrease the font size from 12 to 10
    //==============
    private void checkProperty() {
        String system = System.getProperty("os.name");
        if (system.equals("Windows 95")) {
            ta.setFont(new Font("TimesRoman", Font.BOLD, 14));
            list.setForeground(Color.red);
            list.setFont(new Font("Courier", Font.BOLD+Font.ITALIC, 14));
        } else if (system.equals("Solaris")) {
            tf.setFont(new Font("TimesRoman", Font.PLAIN, 10));
        }

    }

    //might not be needed...if needed this inner class is for handling
    //event dispatched by the three buttons
    private class MouseAction extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getComponent().equals(a)) {
                //ta.append("Event dispatched from a\n");
                if (info==null) info = new Info(new Frame(), "About this applet");
                info.show();
            }
            /*if (e.getComponent().equals(b)) {
                ta.append("Event dispatched from b\n");
            }
            if (e.getComponent().equals(c)) {
                ta.append("Event dispatched from c\n");
            }*/
        }
    }

    //========
    //Listener for Action event when user double clicked on the list.
    //It will get the information of the selected user and display it on the
    //text area.  Note that this information is private, which means that
    //only the user that requested the "whois" command will receive the
    //information
    //========
    private class WhoisAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ////System.out.println("Double clicked");
            mps.println("whois|"+list.getSelectedItem());
        }
    }

    //========
    //Listener for ItemEvent when user click on the list.
    //Since Java doesn't select and deselect
    //item on the list automatically every time user click on that item,
    //I have to use a boolean value to toggle them.
    //If an item (user) is selected, then the user is in private mode, and
    //every text typed will be sent only to the selected user.
    //========
    private class PrivateAction implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            if (selected) {
                int index = list.getSelectedIndex();
                list.deselect(index);
            }
            selected = !selected;
            ////System.out.println("Selected: "+selected);
        }
    }

    //==============
    // Instance variables
    //==============
    IButton a, b, c;
    TextArea ta;
    TextField tf;
    List list;
    boolean selected = false;
    String[] history = new String[256];
    int index = 0;
    String mode;
    Image image;
    PrintStream mps;
    boolean connected=false;
    ClientApplet parent;
    Info info;

    //==============
    //Parse the strings sent by server via Connector
    //==============
    public void parseCommand(String text) {
        ////System.out.println(text+" in ChatClient");
        StringTokenizer st = new StringTokenizer(text, "|");
        try {
            //get the first token of the string (which is the command)
            String cmd = st.nextToken();
            //decide the action...
            if (cmd.equals("chat")) updateTextArea(st);
            else if (cmd.equals("list")) updateList(st);
            else if (cmd.equals("BYE")) quit();
            ////System.out.println("parseCmd called");
        } catch (Exception er) {
            System.err.println(er+" in parseCommand");
        }
    }

    //===============
    //Update the list according to the command passed by server,
    //if "+" then add user to list, if "-" remove the user from list
    //===============
    public void updateList(StringTokenizer st) {
        //get the command (+ or -)
        String cmd = st.nextToken();
        //get the user name
        String user= st.nextToken();
        //decide what to do...
        if (cmd.equals("+")) list.add(user);
        else if (cmd.equals("-")) list.remove(user);
    }

    //================
    //self explanatory
    //================
    public void updateTextArea(StringTokenizer st) {
        //Since the strings might contains several tokens
        //we need to make sure that all of them is appended to TextArea
        while (st.hasMoreTokens()) {
            ta.append(st.nextToken()+"\n");
        }
    }

    //================
    //Send text to server, and clear the textfield
    //================
    public void send(String text) {
        try {
            //This will allow the "history" on textfields
            history[index++]=text;
        } catch (ArrayIndexOutOfBoundsException er) {
            //erase all the history, and starts from index 0
            index = 0;
        }
        //Check whether there is a selected username on the list.
        //If there is then send private message to that user
        //otherwise send the message to public
        if (selected) {
            mps.println("private|"+list.getSelectedItem()+"|"+text);
        } else {
            mps.println("chat|"+text);
        }
        //don't forget to clear the text field
        tf.setText("");
    }

    //==========
    //Tell server user's quit, then exit the system
    //==========
    void quit() {
        mps.println("BYE");
        System.exit(0);
    }
}

class Info extends Dialog {
    public Info(Frame parent, String title) {
        super(parent, title, false);
        this.setLayout(new BorderLayout());

        font = new Font("TimesRoman", Font.BOLD, 14);

        Panel p = new Panel(); p.setLayout(new FlowLayout(FlowLayout.CENTER));
        ok = new Button("Okay");
        p.add(ok);

        this.setResizable(false);
        this.add("South", p);
        this.resize(300,300);
        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
        int scrnWidth = scrnSize.width;
        int scrnHeight= scrnSize.height;
        this.move((scrnWidth-300)/2, (scrnHeight-300)/2);
    }

    public void paint(Graphics g) {
        g.setFont(new Font("TimesRoman", Font.BOLD+Font.ITALIC, 26));
        g.setColor(Color.gray);
        g.drawString("Client for Java Chat 1.0", 23,73);
        g.setColor(Color.blue);
        g.drawString("Client for Java Chat 1.0", 20,70);
        g.setColor(Color.black);
        g.drawLine(20,90,280,90);
        g.setColor(Color.white);
        g.drawLine(21,91,281,91);
        g.setFont(new Font("TimesRoman", Font.PLAIN+Font.ITALIC, 18));
        g.setColor(Color.black);
        g.drawString("  Credits:", 110,120);
        g.drawString("  Darsono Sutedja -- Chat Client", 25,150);
        g.drawString("            &       ", 85,180);
        g.drawString("   Daniel Fishman -- Chat Server ", 20,210);
        g.setColor(Color.black);
        g.drawLine(20,240,280,240);
        g.setColor(Color.white);
        g.drawLine(21,241,281,241);
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

    private String error = "";
    private Button ok;
    private int textWidth;
    private Font font;
}

