import java.awt.*;
import java.awt.event.*;

class RoidFrame extends Frame {
    private Roids roids;
    private Button quit;
    private Button reset;
    private Button die;
    private Dimension currSize;
    
    public RoidFrame() {
	super("Asteroid Canvas");
	roids = new Roids();
	quit = new Button("quit");
	reset = new Button("reset");
	die = new Button("die");

	BorderLayout mainLayout = new BorderLayout();
	this.setLayout(mainLayout);

	Panel centerPanel = new Panel();
	GridLayout centerGrid = new GridLayout(1,1);
	this.add("Center", centerPanel);
	centerPanel.setLayout(centerGrid);
	centerPanel.add(roids);

	Panel bottomPanel = new Panel();
	GridLayout bottomGrid = new GridLayout(1,3);
	this.add("South", bottomPanel);
	bottomPanel.setLayout(bottomGrid);
	bottomPanel.add(quit);
	bottomPanel.add(reset);
	bottomPanel.add(die);
	quit.addActionListener(new ButtonListener());
	reset.addActionListener(new ButtonListener());
	die.addActionListener(new ButtonListener());

	this.setSize(300,300);
	this.setVisible(true);
	roids.initialize();
    }

    private class ButtonListener implements ActionListener {
	public void actionPerformed(ActionEvent evt) {
	    if (evt.getActionCommand().equals("quit")) System.exit(0);
	    if (evt.getActionCommand().equals("reset")) System.exit(0);
	    if (evt.getActionCommand().equals("die")) System.exit(0);
	}
    }
    
    public static void main(String[] args) {
	RoidFrame rf = new RoidFrame();
    }
}
