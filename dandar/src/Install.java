//=====
//Installer for Java Chat
//by Darsono Sutedja
//Copyright 1997 Darsono Sutedja
//email: dsutedja@cs.umb.edu
//=====
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Install extends Frame {

    final float scrnWidth = (float)Util.getScrnWidth();
    final float scrnHeight= (float)Util.getScrnHeight();

    public static void main(String[] argv) {
        String version = System.getProperty("java.version");
        if (validVersion(version)) {
            new Install();
        } else {
            System.err.println("You need JDK1.1.x or later to run Java Chat");
        }
    }
    public static boolean validVersion(String version) {
        return (version.equals("11") || version.equals("1.1") || version.equals("1.1.1") ||
                version.equals("1.1.2") || version.equals("1.1.3") || version.equals("1.1.4") ||
                version.equals("1.1.5") || version.equals("1.2"));
    }
    public Install() {
        super("Java Chat Installer 1.0");
        setSize(Util.getScrnWidth(),Util.getScrnHeight());
        setLayout(new XYLayout());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        ta = new TextArea("License agrement\nBla bla bla", 10, 10, TextArea.SCROLLBARS_VERTICAL_ONLY);
        ta.setEditable(false); ta.setFont(new Font("TimesRoman", Font.PLAIN, (int)(0.023148*scrnHeight)));
        /*"150,110,500,400"*/
        add(makePos((int)(0.130208*scrnWidth), (int)(0.127314*scrnHeight), (int)(0.434027*scrnWidth), (int)(0.462962*scrnHeight)), ta);


        agree = new Button("I agree");
        agree.addActionListener(new Agree());
        disagree = new Button("I disagree");
        disagree.addActionListener(new Disagree());
        cancel   = new Button("cancel");
        cancel.addActionListener(new Cancel());
        int __y = (int)(0.60185*scrnHeight);
        int __w = (int)(0.069444444*scrnWidth);
        int __h = (int)(0.034722222*scrnHeight);
        //"150,520,80,30"
        add(makePos((int)(0.130208*scrnWidth),__y,__w,__h), agree);
        //"240,520,80,30"
        add(makePos((int)(0.208333*scrnWidth),__y,__w,__h), disagree);
        //"330,520,80,30"
        add(makePos((int)(0.286458*scrnWidth),__y,__w,__h), cancel);
        /*image = Util.getImage("back.jpg");
        Util.waitForImage(this, image);*/
        toFront();
        show();
        paintBack();
        ///////////
    }
    private String makePos(int x, int y, int w, int h) {
        if (w==0&&h==0) return (String.valueOf(x)+","+String.valueOf(y));
        return (String.valueOf(x)+","+String.valueOf(y)+","+String.valueOf(w)+","+String.valueOf(h));
    }
    public void paintBack() {
        //draw the background
        Image buffer = this.createImage(1152,864);
        Graphics g = buffer.getGraphics();
        /*for (int x=0; x<Util.getScrnWidth(); x++) {
            g.drawImage(image, x, 0, this);
        }*/
        int w = Util.getScrnWidth();
        int c = 0, counter=0;
        for (int y=0; y<Util.getScrnHeight();y++) {
            g.setColor(new Color(c/2, c, c));
            counter++;
            if (counter > 5) {
                counter = 0;
                c++;
                if (c>255) c=0;
            }
            g.drawLine(0,y,w,y);
        }
        g.setFont(new Font("TimesRoman", Font.BOLD+Font.ITALIC, (int)(0.07*scrnHeight)));
        g.setColor(Color.gray);
        g.drawString("Java Chat 1.0", (int)(0.1328125*scrnWidth)/*153*/,(int)(0.11921*scrnHeight)/*103*/);
        int x1 = (int)(0.1328125*scrnWidth), x2 = (int)(0.41927*scrnWidth);
        int _w = (int)(0.0980902*scrnWidth); /*113*/
        g.drawLine(x1,_w,x2,_w);
        g.drawLine(x1,_w+1,x2,_w+1);
        g.drawLine(x1,_w+2,x2,_w+2);
        g.setColor(Color.yellow);
        g.drawString("Java Chat 1.0", (int)(0.130208*scrnWidth)/*150*/,(int)(0.11574*scrnHeight)/*100*/);
        int _x1 = (int)(0.130208*scrnWidth), _x2=(int)(0.416666*scrnWidth);
        int w1  = (int)(0.095486*scrnWidth); /*110*/
        g.drawLine(_x1,w1,_x2,w1);
        g.drawLine(_x1,w1+1,_x2,w1+1);
        g.drawLine(_x1,w1+2,_x2,w1+2);

        g.setColor(Color.red);
        g.setFont(new Font("TimesRoman", Font.ITALIC, (int)(0.017361*scrnWidth)/*20*/));
        g.drawString("Copyright (c) 1997 DS, Inc.  Alrights reserved", (int)(0.130208*scrnWidth), (int)(0.92592*scrnHeight)/*800*/);
        g = this.getGraphics();
        g.drawImage(buffer, 0,0,this);
    }
    Image image;
    Button agree, disagree, cancel, ok, browse;
    TextArea ta;
    TextField tf;

    public void buildInstaller() {
        final Panel p = new Panel();
        Border b = new Border("");
        ok = new Button("Okay");

        //browse= new Button("browse");
        tf = new TextField((int)(0.0607638*scrnWidth)/*70*/); tf.setText("c:\\jchat");
        if (System.getProperty("os.name").equals("Windows 95"))
            tf.setFont(new Font("TimesRoman", Font.PLAIN, (int)(0.015625*scrnWidth)/*18*/));
        Label l = new Label("Path:");
        l.setFont(new Font("TimesRoman", Font.BOLD, 14));
        b.add(l);
        b.add(tf); b.add(ok); b.add(cancel);//b.add(browse);
        p.add(b);p.setBackground(Color.lightGray);
        //"150,300"
        this.add(makePos((int)(0.130208*scrnWidth), (int)(0.347222*scrnHeight),0,0), p);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.setLocation((int)(0.130208*scrnWidth)/*150*/,(int)(0.810185*scrnHeight)/*700*/);
                paintBack();
                path = tf.getText();
                File file = new File(path);
                file.mkdir();
                tf.disable(); cancel.setLabel("Quit"); cancel.disable(); ok.disable();
                try {
                    installFile();
                } catch (IOException er) {}
            }
            private void installFile() throws IOException{
                Graphics g = Install.this.getGraphics();
                g.setFont(new Font("TimesRoman", Font.BOLD, (int)(0.02546*scrnHeight)/*22*/));
                g.setColor(Color.red);
                g.drawString("Installing Java Chat...Please wait", (int)(0.130208*scrnWidth)/*150*/, (int)(0.3819444*scrnHeight)/*330*/);
                g.drawString("0%", (int)(0.130208*scrnWidth)/*150*/, (int)(0.416666*scrnHeight)/*360*/);
                g.drawString("100%", (int)(0.43402*scrnWidth)/*500*/, (int)(0.416666*scrnHeight)/*360*/);
                g.setColor(Color.blue);
                Util.copy("./jchat.zip", path+"/jchat.zip");
                int _x=(int)(0.16493*scrnWidth)/*190*/, _y=(int)(0.3912037*scrnHeight);
                int _h=(int)(0.03472222*scrnHeight);
                g.fillRect(_x,_y,(int)(0.086805*scrnWidth)/*100*/,_h);
                Util.copy("./README.doc", path+"/readme.doc");
                g.fillRect(_x,_y,(int)(0.17361*scrnWidth)/*200*/,_h);
                Util.copy("./main.JPG", path+"/main.JPG");
                Util.copy("./05.jpg", path+"/05.jpg");
                g.fillRect(_x,_y,(int)(0.24305*scrnWidth)/*280*/,_h);
                Util.copy("./home.gif", path+"/home.gif");
                Util.copy("./chat.exe", path+"/chat.exe");
                writePreference();
                g.fillRect(_x,_y,(int)(0.26475*scrnWidth)/*305*/,_h);
                /*for (int x=0; x<305; x+=10) {
                    g.fillRect(190,338,x,30);
                    Util.wait(100);
                }*/
                Util.wait(1000);
                paintBack();
                g.setColor(Color.gray);
                g.setFont(new Font("TimesRoman", Font.ITALIC+Font.BOLD, (int)(0.0347222*scrnHeight)));
                g.drawString("Thank you for purchasing this software.",(int)(0.130208*scrnWidth)/*150*/, (int)(0.40509*scrnHeight)/*350*/);
                g.drawString("Don't forget to register.", (int)(0.130208*scrnWidth)/*150*/,(int)(0.439814*scrnHeight)/*380*/);
                cancel.enable();
                try {
                    Thread.sleep(2000);
                    System.exit(0);
                } catch(Exception er) {}
            }
            private void writePreference() {
                try {
                    String os = System.getProperty("os.name");
                    String classpath = System.getProperty("java.class.path") +
                                       System.getProperty("path.separator")  +
                                       path+System.getProperty("file.separator")+"jchat.zip";
                    String filepath = System.getProperty("java.home")+System.getProperty("file.separator")+"bin";
                    FileOutputStream pathOut = new FileOutputStream(path+"/path.env"),
                    clPath  = new FileOutputStream(path+"/class.env");
                    for (int x=0; x<classpath.length(); x++) {
                        clPath.write((int)classpath.charAt(x));
                    }
                    for (int y=0; y<filepath.length();y++) {
                        pathOut.write((int)filepath.charAt(y));
                    }
                } catch (IOException er) {}
            }
        });
        show();
        paintBack();
    }

    String path="c:\\jchat";

    private class Agree implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            remove(ta); remove(agree); remove(disagree); remove(cancel);
            paintBack();
            buildInstaller();
        }
    }
    private class Disagree implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
    private class Cancel implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}

