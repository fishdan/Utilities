/**
 * File: Warning.java
 * Date: January 1998
 * Author: Darsono Sutedja
 *
 * This is a dialog box that is used to displayed error messages that might be
 * generated during execution (e.g. : server is down, invalid login, etc.);
 *
 * This is a universal error dialog box, anyone can use it (as long as he provides the
 * the error message(s)...otherwise it will be simply a blank window).
 *
 * Copyright (c) 1998 Darsono Sutedja
 */
import java.awt.*;
import java.awt.event.*;

public class Warning extends Dialog {
    public Warning(Frame parent, String title) {
        super(parent, title, true);
        this.setLayout(new BorderLayout());

        font = new Font("TimesRoman", Font.BOLD, 14);

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Warning.this.dispose();   // pop down dialog
                if (listeners != null)        // notify all registered listeners
                listeners.actionPerformed(new ActionEvent(Warning.this,
                e.getID(), e.getActionCommand()));
            }
        };

        //allow the window to dispose itself if user close it
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        Panel p = new Panel(); p.setLayout(new FlowLayout(FlowLayout.CENTER));
        ok = new Button("Acknowledge");
        ok.setActionCommand("Okay");
        ok.addActionListener(listener);
        ok.addKeyListener(new DisposeMe());
        p.add(ok);

        this.setResizable(false);
        this.add("South", p);
        this.setSize(500,100);
        Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
        int scrnWidth = scrnSize.width;
        int scrnHeight= scrnSize.height;
        this.setLocation((scrnWidth-500)/2, (scrnHeight-100)/2);
    }
    //Enable "enter"
    private class DisposeMe extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if (e.getComponent().equals(ok) && e.getKeyCode()==e.VK_ENTER) {
                Warning.this.dispose();
            }
        }
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

   /** This field will hold a list of registered ActionListeners, thanks
    *  to the magic of AWTEventMulticaster
    */
    protected ActionListener listeners = null;

   /** Register an action listener to be notified when a button is pressed
    *  AWTEventMulticaster makes this easy. */
    public void addActionListener(ActionListener l) {
        listeners = AWTEventMulticaster.add(listeners, l);
    }

   /** Remove an action listener from our list of interested listeners */
    public void removeActionListener(ActionListener l) {
        listeners = AWTEventMulticaster.remove(listeners, l);
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

