import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class TriangleApplet extends Applet {
    private TriangleBoard triangleBoard;
    private Button quit;
    private Button reset;
    private String Title;
    private Dimension currSize;
    
    public class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            if (evt.getActionCommand().equals("quit")) 
		triangleBoard.quit();/*quit*/
            if (evt.getActionCommand().equals("reset")) 
		triangleBoard.reset(); /* resets */
        }
    }
    public TriangleApplet() {
    
        /* declaring the frame, canvas, and buttons */
        triangleBoard = new TriangleBoard();
        quit = new Button("quit");
        reset = new Button("reset");
        
        /* setting a BorderLayout for the screen */
        BorderLayout mainLayout = new BorderLayout();
        this.setLayout(mainLayout);
    
        /* center panel contains the Canvas */
        Panel centerPanel = new Panel();
        GridLayout centerGrid = new GridLayout(1,1);
        this.add("Center", centerPanel);
        centerPanel.setLayout(centerGrid);
        centerPanel.add(triangleBoard);
        
        /* bottom panel contains the buttons */
        Panel bottomPanel = new Panel();
        GridLayout bottomGrid = new GridLayout(1,2);
        this.add("South", bottomPanel);
	bottomPanel.setLayout(bottomGrid);
        bottomPanel.add(quit);
        bottomPanel.add(reset);
        quit.addActionListener(new ButtonListener());
        reset.addActionListener(new ButtonListener());
        
        /* setting everything to be visible */
	this.setSize(300,300);
	this.setVisible(true);
	this.currSize = getSize();
	this.setTitle("Triangle Applet");
	triangleBoard.reset();
    }
    public void setTitle(String setTitle) {
	Title = setTitle;
	repaint();
    }

    // drawing the title, which will be redone every time repaint() is called
    public void paint(Graphics g) {
	int size, xpos, ypos;
	if (currSize.width != getSize().width || 
	    currSize.height != getSize().height) {
	    currSize = getSize();
	    if (currSize.width <= currSize.height) 
		size = (int)(currSize.width/15);
	    else size = (int)(currSize.height/15);
	    xpos = (int)(0.5*currSize.width);
	    ypos = (int)(0.9*currSize.height);
	    Font titleFont = new Font("Helvetica", Font.BOLD, size);
	    g.setFont(titleFont);
	    g.drawString(Title, xpos, ypos);
	}
    }
    
    // applet commands
    public void init() { TriangleApplet ta = new TriangleApplet(); }
    public void start() { triangleBoard.reset(); }
    public void stop() { triangleBoard.stop(); }
    public void destroy() { triangleBoard.stop(); }
    
    // these commands are used for testing the main string
    public static void main(String[] args) {
	TriangleApplet ta = new TriangleApplet();
    }
} 
