import java.awt.*;
import java.awt.event.*;

class WindowFrame implements ActionListener {
    private Frame newFrame;
    private Button quit;
    private Button draw;
    private Button reset;
    private SpecialCanvas setCanvas;

    public WindowFrame() {
	setCanvas = new SpecialCanvas();
	newFrame = new Frame("Basic Frame");
	quit = new Button("quit");
	draw = new Button("draw");
	reset = new Button("reset");
	quit.addActionListener(this);
	draw.addActionListener(this);
	reset.addActionListener(this);
	this.createWindow(250, 250);
    }
    public WindowFrame(int sizeX, int sizeY) {
	if (sizeX < 100) sizeX = 250;
	if (sizeY < 100) sizeY = 250;
	
	setCanvas = new SpecialCanvas();
	newFrame = new Frame("Basic Frame");
	quit = new Button("quit");
	draw = new Button("draw");
	reset = new Button("reset");
	quit.addActionListener(this);
	draw.addActionListener(this);
	reset.addActionListener(this);
	this.createWindow(sizeX, sizeY);
    }
    public void actionPerformed(ActionEvent evt) {
	if (evt.getActionCommand().equals("quit")) System.exit(0);
	if (evt.getActionCommand().equals("reset")) System.exit(0);
	if (evt.getActionCommand().equals("draw")) System.exit(0);
    }

    private void createWindow(int sizeX, int sizeY) {

	// creating the layout
	BorderLayout mainLayout = new BorderLayout();
	newFrame.setLayout(mainLayout);

	// adding bottom panel, which contains the buttons
	Panel bottomPanel = new Panel();
	GridLayout bottomGrid = new GridLayout(1,3);
	newFrame.add("South", bottomPanel);
	bottomPanel.setLayout(bottomGrid);
	bottomPanel.add(quit);
	bottomPanel.add(draw);
	bottomPanel.add(reset);

	// adding top panel, which contains the canvas
	Panel centerPanel = new Panel();
	GridLayout centerGrid = new GridLayout(1, 1);
	newFrame.add("Center", centerPanel);
	centerPanel.setLayout(centerGrid);
	centerPanel.add(setCanvas);

	// setting everything to be visible and start thread
	newFrame.setSize(sizeX, sizeY);
	newFrame.setVisible(true);

	// this is a simple test
	setCanvas.initialize();
    }

    public static void main(String[] Args) {
	WindowFrame wf = new WindowFrame(300,300);

    }
}
