import java.awt.*;
import java.awt.event.*;

public class TriangleFrame extends Frame implements ActionListener {
    private TriangleBoard triangleBoard;
    private Button quit;
    private Button reset;
    private String Title;
    private Dimension currSize;

    // event handling method
    public void actionPerformed(ActionEvent evt) {
	if (evt.getActionCommand().equals("quit"))
	    triangleBoard.quit();/*quit*/
	if (evt.getActionCommand().equals("reset"))
	    triangleBoard.reset(); /* resets */
	if (evt.getActionCommand().equals("stop"))
	    triangleBoard.start();
	if (evt.getActionCommand().equals("stop"))
	    triangleBoard.stop();
    }

    public TriangleFrame() {
    
        /* declaring the frame, canvas, and buttons */
        super("Basic Frame");
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
	bottomPanel.add(start);
	bottomPanel.add(stop);
        quit.addActionListener(this);
        reset.addActionListener(this);
	start.addActionListener(this);
	stop.addActionListener(this);
        
        /* setting everything to be visible */
        this.setSize(300,300);
        this.setVisible(true);
	currSize = this.getSize();
        triangleBoard.reset();
    }
    public static void main(String[] args) {
        TriangleFrame tf = new TriangleFrame();
    }
}
