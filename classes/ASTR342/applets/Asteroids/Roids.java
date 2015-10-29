import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import javax.swing.*;

// basic asteroids game class that can detect mouse events and on which
// the asteroid board is drawn
class Roids extends Canvas {
    private int difficultLevel;
    private Toolkit defaultToolKit;
    private Image spaceship;
    private Image background;
    private AffineTransform atransform;
    private Timer timeThis;

    private int xCoord;
    private int yCoord;
    private double theta;
    private double[] velocity;

    public Roids() {
	super();
	this.difficultLevel = 0;
	this.xCoord = 0;
	this.yCoord = 0;
	
	// calculating the velocities and accelerations
	this.theta = 0.0;
	this.velocity = new double[2];
	this.velocity[0] = 0;
	this.velocity[1] = 0;


	// setting the speed of the movement
	this.defaultToolKit = Toolkit.getDefaultToolkit();
	this.spaceship = defaultToolKit.getImage("images/rocketship2.gif");
	this.background = defaultToolKit.getImage("images/rocketship.gif");
	this.atransform = new AffineTransform();

	// adding in the various components of the Canvas
	this.setBackground(Color.white);
	this.addMouseListener(new CanvasMouseListener());
	this.addKeyListener(new CanvasKeyListener());
    }

    // mouse event information
    private class CanvasMouseListener extends MouseAdapter {
	public void mouseClicked(MouseEvent evt) {
	    xCoord = evt.getX();
	    yCoord = evt.getY();
	    repaint();
	}
    }

    // canvas key information
    private class CanvasKeyListener extends KeyAdapter {
	public void keyPressed(KeyEvent evt) {
	    char keyChar = evt.getKeyChar();
	    if (keyChar == 'a') theta = theta + Math.PI/180;
	    else if (keyChar == 's') theta = theta - Math.PI/180;
	    
	    // this will perform an acceleration for the particle along
	    // its vector
	    else if (keyChar == 'l') {
		velocity[0] = velocity[0] + 2.0*Math.cos(theta);
		velocity[1] = velocity[1] + 2.0*Math.sin(theta);
	    }
	    else if (keyChar == 'k') {
		velocity[0] = velocity[0] - 2.0*Math.cos(theta);
		velocity[1] = velocity[1] - 2.0*Math.sin(theta);
	    }
	}
    }

    // simple animation class used by the thread
    private class AnimateEvent implements ActionListener {
	private boolean shouldRun;
	public AnimateEvent() { this.shouldRun = true;}
	public void actionPerformed(ActionEvent evt) {
	    xCoord = xCoord + (int)(velocity[0]);
	    yCoord = yCoord + (int)(velocity[1]);
	    if (xCoord > 0) xCoord = xCoord%getSize().width;
	    else xCoord = getSize().width - xCoord;
	    if (yCoord > 0) yCoord = yCoord%getSize().height;
	    else yCoord = getSize().height - yCoord;
	    repaint();
	}
    }
    
    public void initialize() {
	if (this.isVisible() == false) return;
	this.timeThis = new Timer(20, new AnimateEvent());
	this.startAnimation();
    }
    public void stopAnimation() { timeThis.stop();}
    public void startAnimation() { timeThis.start();}

    public void paint(Graphics g) { 
	Graphics2D g2 = (Graphics2D) g;
	atransform.setToIdentity();
	atransform.translate(xCoord, yCoord);
	atransform.rotate(theta);
	g2.drawImage(spaceship, atransform, this);
    }
}
