import java.awt.*;
import java.io.*;

class SpecialCanvas extends Canvas {
    private Image bufferDrawing;
    private Thread threadRunning;
    private Dimension currSize;
    private Coords[] coordinates;
    
    public SpecialCanvas() {
	super();
	threadRunning = null;
	bufferDrawing = null;
	currSize = new Dimension();
    }

    public void initialize() {
	currSize = this.getSize();
	this.setBackground(Color.white);
	this.coordinates = new Coords[10];
	for (int i=0; i<10; i++) this.coordinates[i] = new Coords();
	bufferDrawing = createImage(currSize.width, currSize.height);
	threadRunning = new Thread(new gravCalc(10, 10000));
	threadRunning.setPriority(Thread.MIN_PRIORITY);
	threadRunning.start();

	// this is only a test
	this.drawSimple();
    }

    // very basic drawing commands
    public void drawSimple() {
	Graphics g = bufferDrawing.getGraphics();
	int initX, initY, finalX, finalY, width, height;
	width = (this.getSize()).width;
	height = (this.getSize()).height;
	
	g.setColor(Color.white);
	g.fillRect(0,0,width,height);
	g.setColor(Color.black);
	
	initX = (int)(0.2*width);
	initY = (int)(0.2*height);
	finalX = (int)(0.8*width);
	finalY = (int)(0.2*height);
	g.drawLine(initX, initY, finalX, finalY);

	initX = (int)(0.2*width);
	initY = (int)(0.2*height);
	finalX = (int)(0.2*width);
	finalY = (int)(0.8*height);
	g.drawLine(initX, initY, finalX, finalY);
	
	initX = (int)(0.8*width);
	initY = (int)(0.8*height);
	finalX = (int)(0.2*width);
	finalY = (int)(0.8*height);
	g.drawLine(initX, initY, finalX, finalY);
	
	initX = (int)(0.8*width);
	initY = (int)(0.8*height);
	finalX = (int)(0.8*width);
	finalY = (int)(0.2*height);
	g.drawLine(initX, initY, finalX, finalY);
	g.drawImage(bufferDrawing, 0, 0, this);
	repaint();
    }

    // another slightly more complicated drawing command
    public void drawComplex() {
	Graphics g = bufferDrawing.getGraphics();
	int x, y;
	
	// wipe out the screen after one is done
	g.setColor(Color.white);
	g.fillRect(0, 0, currSize.width, currSize.height);

	// now draw in the separate coordinates
	g.setColor(Color.black);
	for (int i=0; i<coordinates.length; i++) {
	    x = currSize.width+(int)(coordinates[i].getX()/10.0*
				     currSize.width/2.0);
	    y = currSize.height+(int)(coordinates[i].getY()/10.0*
				      currSize.height/2.0);
	    g.fillRect(x, y, 1, 1);
	}
	this.repaint();
    }

    // this command puts in the rotated coordinates into this canvas
    public void setCoordinate(Coords arg, int index) {
	if (index < 0) return;
	if (index >= coordinates.length) return;
	this.coordinates[index].setX(arg.getX());
	this.coordinates[index].setY(arg.getY());
    }

    public void update(Graphics g) { paint(g);}
    
    public void paint(Graphics g) {
	int width, height;
	width = (getSize()).width;
	height = (getSize()).height;
	if (bufferDrawing == null) {
	    bufferDrawing = this.createImage(width, height);
	    if (threadRunning != null) threadRunning.stop();
	    threadRunning = new Thread(new gravCalc(10,10000));
	    threadRunning.setPriority(Thread.MIN_PRIORITY);
	    threadRunning.start();
	}
	else if (width != currSize.width || height != currSize.height) {
	    currSize = getSize();
	    bufferDrawing = this.createImage(width, height);
	    if (threadRunning != null) threadRunning.stop();
	    threadRunning = new Thread(new gravCalc(10,10000));
	    threadRunning.setPriority(Thread.MIN_PRIORITY);
	    threadRunning.start();
	}
	g.drawImage(bufferDrawing, 0, 0, this);
    }

    private class gravCalc implements Runnable {

	// variables which belong to myFile
	private Matrix rotMatrix;
	private Coords[] stars;
	private Coords[] stars_prime;
	private Coords[] stars_intermediate;
	private int curr_iteration;
	private int iterations;
	private int clusterSize;
	private double G = 1.0;
	private double time_iter;

	//initializes everything here
	public gravCalc(int setclusterSize, int setIterations) {
	    double r, theta, phi;
	    if (setclusterSize < 1) return;
	    this.clusterSize = setclusterSize;
	    if (iterations < 1) return;
	    this.iterations = setIterations;
	    this.curr_iteration = 1;
	    this.time_iter = 1E-3;
	    
	    this.stars = new Coords[clusterSize+1];
	    this.stars_prime = new Coords[clusterSize+1];
	    this.stars_intermediate = new Coords[clusterSize+1];
	    this.rotMatrix = new Matrix(3,3);
	
	    this.setRotMatrix(0,0,0);
	    for (int i=1; i<=clusterSize; i++) {
		this.stars[i] = new Coords();
		this.stars_prime[i] = new Coords();
		this.stars_intermediate[i] = new Coords();
		
		phi = Math.random()*2*Math.PI;
		theta = Math.random()*Math.PI;
		r = 1.0*Math.random();
		this.stars[i].setX(r*Math.cos(phi)*Math.cos(theta));
		this.stars[i].setY(r*Math.sin(phi)*Math.cos(theta));
		this.stars[i].setZ(r*Math.sin(theta));
		
		phi = Math.random()*2*Math.PI;
		theta = Math.random()*Math.PI;
		r = 1.0*Math.random();
		this.stars[i].setVX(r*Math.cos(phi)*Math.cos(theta));
		this.stars[i].setVY(r*Math.sin(phi)*Math.cos(theta));
		this.stars[i].setVZ(r*Math.sin(theta));
		this.stars_prime[i].setX(this.stars[i].getVX());
		this.stars_prime[i].setY(this.stars[i].getVY());
		this.stars_prime[i].setZ(this.stars[i].getVZ());
	    }
	}
	
	// calculates the values of the primed variables
	public void find_prime(Coords[] inputs) {
	    double r, x, y, z, ax, ay, az;
	    int i,j;
	    for (i=1; i<=clusterSize; i++) {
		this.stars_prime[i].setX(inputs[i].getVX());
		this.stars_prime[i].setY(inputs[i].getVY());
		this.stars_prime[i].setZ(inputs[i].getVZ());
		ax = 0; ay = 0; az = 0;
		
		for (j=1; j<=clusterSize; j++) {
		    if (j != i) {
			x = inputs[i].getX() - inputs[j].getX();
			y = inputs[i].getY() - inputs[j].getY();
			z = inputs[i].getZ() - inputs[j].getZ();
			r = Math.sqrt(x*x+y*y+z*z);
			
			ax = ax - G*x/(r*r*r);
			ay = ay - G*y/(r*r*r);
			az = az - G*z/(r*r*r);
		    }
		}
		this.stars_prime[i].setVX(ax);
		this.stars_prime[i].setVY(ay);
		this.stars_prime[i].setVZ(az);
	    }
	}
	
	public void rk4() {
	    int i;
	    double x, y, z, vx, vy, vz, h;
	    double[][] k1 = new double[clusterSize+1][7];
	    double[][] k2 = new double[clusterSize+1][7];
	    double[][] k3 = new double[clusterSize+1][7];
	    double[][] k4 = new double[clusterSize+1][7];
	    h = this.time_iter;
	    
	    for (i=1; i<=clusterSize; i++) {
		x = this.stars[i].getX();
		y = this.stars[i].getY();
		z = this.stars[i].getZ();
		vx = this.stars[i].getVX();
		vy = this.stars[i].getVY();
		vz = this.stars[i].getVZ();
		
		this.stars_intermediate[i].setX(x);
		this.stars_intermediate[i].setY(y);
		this.stars_intermediate[i].setZ(z);
		this.stars_intermediate[i].setVX(vx);
		this.stars_intermediate[i].setVY(vy);
		this.stars_intermediate[i].setVZ(vz);
	    }
	    find_prime(this.stars_intermediate);
	    for (i=1; i<=clusterSize; i++) {
		x = this.stars[i].getX();
		y = this.stars[i].getY();
		z = this.stars[i].getZ();
		vx = this.stars[i].getVX();
		vy = this.stars[i].getVY();
		vz = this.stars[i].getVZ();
		
		k1[i][1] = h*this.stars_prime[i].getX();
		k1[i][2] = h*this.stars_prime[i].getY();
		k1[i][3] = h*this.stars_prime[i].getZ();
		k1[i][4] = h*this.stars_prime[i].getVX();
		k1[i][5] = h*this.stars_prime[i].getVY();
		k1[i][6] = h*this.stars_prime[i].getVZ();

		this.stars_intermediate[i].setX(x+k1[i][1]/2);
		this.stars_intermediate[i].setY(y+k1[i][2]/2);
		this.stars_intermediate[i].setZ(z+k1[i][3]/2);
		this.stars_intermediate[i].setVX(vx+k1[i][4]/2);
		this.stars_intermediate[i].setVY(vy+k1[i][5]/2);
		this.stars_intermediate[i].setVZ(vz+k1[i][6]/2);
	    }
	    find_prime(this.stars_intermediate);
	    for (i=1; i<=clusterSize; i++) {
		x = this.stars[i].getX();
		y = this.stars[i].getY();
		z = this.stars[i].getZ();
		vx = this.stars[i].getVX();
		vy = this.stars[i].getVY();
		vz = this.stars[i].getVZ();
		
		k2[i][1] = h*this.stars_prime[i].getX();
		k2[i][2] = h*this.stars_prime[i].getY();
		k2[i][3] = h*this.stars_prime[i].getZ();
		k2[i][4] = h*this.stars_prime[i].getVX();
		k2[i][5] = h*this.stars_prime[i].getVY();
		k2[i][6] = h*this.stars_prime[i].getVZ();
		
		this.stars_intermediate[i].setX(x+k2[i][1]/2);
		this.stars_intermediate[i].setY(y+k2[i][2]/2);
		this.stars_intermediate[i].setZ(z+k2[i][3]/2);
		this.stars_intermediate[i].setVX(vx+k2[i][4]/2);
		this.stars_intermediate[i].setVY(vy+k2[i][5]/2);
		this.stars_intermediate[i].setVZ(vz+k2[i][6]/2);
	    }
	    find_prime(this.stars_intermediate);
	    for (i=1; i<=clusterSize; i++) {
		x = this.stars[i].getX();
		y = this.stars[i].getY();
		z = this.stars[i].getZ();
		vx = this.stars[i].getVX();
		vy = this.stars[i].getVY();
		vz = this.stars[i].getVZ();
		
		k3[i][1] = h*this.stars_prime[i].getX();
		k3[i][2] = h*this.stars_prime[i].getY();
		k3[i][3] = h*this.stars_prime[i].getZ();
		k3[i][4] = h*this.stars_prime[i].getVX();
		k3[i][5] = h*this.stars_prime[i].getVY();
		k3[i][6] = h*this.stars_prime[i].getVZ();
		
		this.stars_intermediate[i].setX(x+k3[i][1]);
		this.stars_intermediate[i].setY(y+k3[i][2]);
		this.stars_intermediate[i].setZ(z+k3[i][3]);
		this.stars_intermediate[i].setVX(vx+k3[i][4]);
		this.stars_intermediate[i].setVY(vy+k3[i][5]);
		this.stars_intermediate[i].setVZ(vz+k3[i][6]);	    
	    }
	    find_prime(this.stars_intermediate);
	    for (i=1; i<=clusterSize; i++) {
		x = this.stars[i].getX();
		y = this.stars[i].getY();
		z = this.stars[i].getZ();
		vx = this.stars[i].getVX();
		vy = this.stars[i].getVY();
		vz = this.stars[i].getVZ();
		
		k4[i][1] = h*this.stars_prime[i].getX();
		k4[i][2] = h*this.stars_prime[i].getY();
		k4[i][3] = h*this.stars_prime[i].getZ();
		k4[i][4] = h*this.stars_prime[i].getVX();
		k4[i][5] = h*this.stars_prime[i].getVY();
		k4[i][6] = h*this.stars_prime[i].getVZ();
		
		this.stars[i].setX(x+k1[i][1]/6+k2[i][1]/3+k3[i][1]/3+
				   k4[i][1]/6);
		this.stars[i].setY(y+k1[i][2]/6+k2[i][2]/3+k3[i][2]/3+
				   k4[i][2]/6);
		this.stars[i].setZ(z+k1[i][3]/6+k2[i][3]/3+k3[i][3]/3+
				   k4[i][3]/6);
		this.stars[i].setVX(vx+k1[i][4]/6+k2[i][4]/3+k3[i][4]/3+
				    k4[i][4]/6);
		this.stars[i].setVY(vy+k1[i][5]/6+k2[i][5]/3+k3[i][5]/3+
				    k4[i][5]/6);
		this.stars[i].setVZ(vz+k1[i][6]/6+k2[i][6]/3+k3[i][6]/3+
				    k4[i][6]/6);
	    }
	    this.curr_iteration = this.curr_iteration + 1;
	}
	
	// this calculates the rotation matrix
	public void setRotMatrix(double theta_XY, double theta_XZ, 
				 double theta_YZ) {
	    Matrix XYmatrix = new Matrix(3,3);
	    Matrix XZmatrix = new Matrix(3,3);
	    Matrix YZmatrix = new Matrix(3,3);
	    XYmatrix.rotation_matrix(3,1,2,theta_XY);
	    XZmatrix.rotation_matrix(3,1,3,theta_XZ);
	    YZmatrix.rotation_matrix(3,2,3,theta_YZ);
	    this.rotMatrix.copy(Matrix.multiply(XZmatrix, YZmatrix));
	    this.rotMatrix.copy(Matrix.multiply(XYmatrix, this.rotMatrix));
	}
	
	// put painting subroutines here
	public void run() {
	    for (int i=1; i<iterations; i++) {
		for (int j=0; j<clusterSize; j++) {
		    setCoordinate(this.getPoint(j), j);
		}
		drawComplex();
		this.rk4();
	    }
	}
	
	public Coords getPoint(int j) {
	    
	    // convert the coordinate into a matrix
	    Matrix m = new Matrix(3,1);
	    m.setElement(0, 0, stars[j].getX());
	    m.setElement(1, 0, stars[j].getY());
	    m.setElement(2, 0, stars[j].getZ());
	    
	    // multiply new matrix by the rotation matrix
	    m = Matrix.multiply(rotMatrix, m);
	    
	    // now put into a new coordinate
	    Coords newCoord = new Coords();
	    newCoord.setX(m.getElement(0,0));
	    newCoord.setY(m.getElement(1,0));
	    newCoord.setZ(m.getElement(2,0));
	    return newCoord;
	}
    }
}
