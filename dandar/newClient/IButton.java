// IButton.java


import java.awt.*;
import java.awt.event.*;  //for 1.1 event handling

/**
 * IButton.java
 *
 * Image button
 *
 * @author Darsono Sutedja
 * <p>
 * <a href="mailto: dsutedja@cs.umb.edu">e-mail: dsutedja@cs.umb.edu
 * </a>
 */
public class IButton extends CustomButton {

    /**
     * Construct an image button using the default thickness (3)
     *
     * @param image   the image to be displayed in the button
     */
    public IButton(Image image) {
        this(image, DEFAULT_THICKNESS, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Construct an image button w/ a specified thickness
     *
     * @param image the image
     * @param thickness the thickness of the button
     */
    public IButton(Image image, int thickness) {
        this(image, thickness, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Construct an image button w/ specified thickness, width, and height
     *
     * @param image   the image to be displayed in the button
     * @param thickness   the thickness of the border of the button
     * @param width the width of the button
     * @param height the height of the button
     */
    public IButton(Image image, int thickness, int width, int height) {
        this.thickness = thickness;
        this.image = image;  //this is the image that we're gonna use
        //this.who   = who;  //for debugging
        this.setBackground(Color.lightGray); //set the background color to lightGray (my favorite :) )
        //wait for the image to fully loaded to memory...
        Util.waitForImage(this, image);

        _x = (width-image.getWidth(this))/2;
        _y = (height-image.getWidth(this))/2;

        //reshape in 1.0 API...
        this.setBounds(0,0,width,height);
        //tell the program that we want to include a MouseEvent
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    public void paint(Graphics g) {
        //is the mouse entered the Object, paint the raised 3DRect
        //...anyone uses Word'97 will know what this is about
        if (entered) {
            g.setColor(Color.lightGray);
            for (int x=0; x<thickness; x++) {
                g.draw3DRect(2+x,2+x,40-(2*x),40-(2*x),true);
            }
        }
        //paint the image to the "button"
        g.drawImage(image, _x, _y, this);
        entered = false; //fighting flicker
    }

    public void processMouseEvent(MouseEvent e) {
        Graphics g;
        switch(e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
            //clear the image
            //paint an unraised 3DRect
            //draw the image on different coordinate
            //======
            //this is how the "real" button work...I guess
            g = this.getGraphics();
            g.setColor(Color.lightGray);
            g.fillRect(0,0,45,45);
            for (int x=0; x<thickness; x++) {
                g.draw3DRect(2+x,2+x,40-(2*x),40-(2*x),false);
            }
            g.drawImage(image,_x+2,_y+2,this);
            //System.out.println(who+" pressed with action="+action);
            break;

            case MouseEvent.MOUSE_RELEASED:
            entered = true;  //tell the program that we're in the Object
            repaint();
            //System.out.println(who+" released");
            break;

            case MouseEvent.MOUSE_ENTERED:
            entered = true; //tell the program that we're in the Object
            repaint();
            //System.out.println(who+" entered");
            break;

            case MouseEvent.MOUSE_EXITED:
            entered = false; //tell the program that we're not in the Object
            repaint();
            //System.out.println(who+"exited");
            break;
        }
        super.processMouseEvent(e); //I don't know why we need this
    }

    int _x=0, _y=0, width=0, height=0;
}

