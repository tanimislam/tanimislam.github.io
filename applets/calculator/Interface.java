import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;

public class Interface extends Applet implements ActionListener {
	private String argument;
	private Label inputLabel = new Label("input = ");
	private Label outputLabel = new Label("output = ");
    	private Label symbolLabel = new Label("z = ");
    	private TextField inputField = new TextField("2+2");
    	private Label outputField = new Label("4");
    	private TextField symbolField = new TextField("0.0");
    	private Formula f = new Formula(); 
   
	public void init() { this.initializeApplet();}
    
	private static void addComponent(Component comp, Container cont,		 
						          	  GridBagLayout gridbag, 
							  	  GridBagConstraints c) {
		gridbag.setConstraints(comp, c);
        	cont.add(comp);
	}
    
	private void initializeApplet() {
        	this.setLayout(new BorderLayout());
        	Panel centerPanel = new Panel();
        	Panel southPanel = new Panel();
        	GridBagLayout gridbag = new GridBagLayout();
        	GridBagConstraints c = new GridBagConstraints();

        	// first set the fonts here properly
		this.inputLabel.setFont(new Font("Arial", Font.BOLD, 14));
		this.symbolLabel.setFont(new Font("Arial", Font.BOLD, 14));
        	this.outputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        	this.inputField.setFont(new Font("Helvetica", Font.BOLD, 12));
        	this.outputField.setFont(new Font("Helvetica", Font.BOLD, 12));
        	this.symbolField.setFont(new Font("Helvetica", Font.BOLD, 12));
        
        	// set the backgrounds of all associated objects to white
        	this.inputLabel.setBackground(Color.decode("#877777"));
        	this.outputLabel.setBackground(Color.decode("#877777"));
        	this.symbolLabel.setBackground(Color.decode("#877777"));
        	this.inputField.setBackground(Color.decode("#877777"));
        	this.outputField.setBackground(Color.decode("#877777"));
        	this.symbolField.setBackground(Color.decode("#877777"));
        
        	// set foregrounds of all labels to red, all fields to blue
        	this.inputLabel.setForeground(Color.white);
        	this.outputLabel.setForeground(Color.white);
        	this.symbolLabel.setForeground(Color.white);
        	this.inputField.setForeground(Color.white);
        	this.outputField.setForeground(Color.white);
        	this.symbolField.setForeground(Color.white);
        
        	// add everything together
        	this.add(centerPanel, "Center");
        	centerPanel.setLayout(gridbag);
        	c.fill = GridBagConstraints.BOTH;
        	c.weightx = 0.0; c.gridwidth = 1; c.weighty = 1.0;
        	Interface.addComponent(inputLabel, centerPanel, gridbag, c);
        	c.weightx = 1.0;
        	c.gridwidth = GridBagConstraints.REMAINDER;
        	Interface.addComponent(inputField, centerPanel, gridbag, c);
        	c.weightx = 0.0; c.gridwidth = 1;
        	Interface.addComponent(outputLabel, centerPanel, gridbag, c);
        	c.weightx = 1.0;
        	c.gridwidth = GridBagConstraints.REMAINDER;
        	Interface.addComponent(outputField, centerPanel, gridbag, c);
        	c.weightx = 0.0; c.gridwidth = 1;
        	Interface.addComponent(symbolLabel, centerPanel, gridbag, c);
        	c.weightx = 1.0;
        	c.gridwidth = GridBagConstraints.REMAINDER;
        	Interface.addComponent(symbolField, centerPanel, gridbag, c);
		inputField.addActionListener(this);
		symbolField.addActionListener(this);
	}
    
	public void actionPerformed(ActionEvent e) {
		String line = "";
		double[] values = { 0};
		String[] symbols = {"z"};
		double val = 0.0;
		if (e.getSource().equals(inputField) || e.getSource().equals(symbolField)) {
		f.flush();
		line = inputField.getText();
		try {
			values[0] = Double.parseDouble(symbolField.getText());
			val = f.getValue(line, symbols, values);
			outputField.setText(Double.toString(val));
		} catch(Exception g) { outputField.setText("<invalid>");}
		}
	}
}
