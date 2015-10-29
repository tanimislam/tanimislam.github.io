import java.awt.*;
import java.awt.event.*;
import java.io.*;

class ChaosCanvas extends Canvas implements MouseListener {
    private Image bufferDrawing;
    private Color[] color_list;
    private Thread threadRunning;
    private Dimension currSize;

    // constructors
    public ChaosCanvas() {
	this.bufferDrawing = null;
	this.threadRunning = null;
	this.currSize = new Dimension();
    }
    

    // these methods set the color map
    public void setColorMap(Color[] setColor) {
	color_list = new Color[setColor.length];
	for (int i=0; i<setColor.length; i++)
	    color_list[i] = setColor[i];
    }
    public void setColorMap(int number_colors) {
	if (number_colors < 0) return;
	double r, g, b;
	color_list = new Color[number_colors];
	for (int i=0; i<number_colors; i++) {
	    r = 1.0;
	    g = 0.0+((double)i)/number_colors;
	    b = 0.0+((double)i)/number_colors;
	    color_list[i] = new Color(r, g, b);
	}
    }

    // these methods must be implemented from MouseListener
    public void mouseClicked(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }
    public void mousePressed(MouseEvent evt) { }
    public void mouseReleased(MouseEvent evt) { }

    // general painting subroutines
    public void update(Graphics g) { paint(g);}
    public void paint(Graphics g) {
	if (threadRunning == null) {
	    currSize = getSize();
	    bufferDrawing = getImage(currSize.width, currSize.height);
	    threadRunning = new Thread(new Solve());
	    threadRunning.setPriority(Thread.MIN_PRIORITY);
	    threadRunning.start();
	}
    }

    // these are primary thread Methods
    public void start() {
	
    }
    public void stop() {
	
    }
    public void init() {
	
    }
    public void destroy() {
	
    }

    private class Solve implements Runnable {
	private int whoami;
	private int whereami;
	
	public Solve() { whoami = 0; whereami = 0;}
	public void run() { }
    }
}


