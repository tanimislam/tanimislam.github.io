import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;

class TriangleBoard extends Canvas implements MouseListener {
    
    /* these give the allowed moves for the 15-hole triangle game */
    private int[] first = {0, 0, 1, 1, 2, 2, 3, 3, 3, 3, 4, 4, 5, 5, 5, 5, 6,
			   6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 12,
			   13, 13, 14, 14};
    private int[] middle = {1, 2, 3, 4, 4, 5, 1, 4, 7, 6, 7, 8, 2, 4, 8, 9, 3, 
			    7, 4, 8, 4, 7, 5, 8, 6, 11, 7, 12, 7, 8, 13, 8, 
			    12, 9, 13};
    private int[] last = {3, 5, 6, 8, 7, 9, 0, 5, 12, 10, 11, 13, 0, 3, 12, 
			  14, 1, 8, 2, 9, 1, 6, 2, 7, 3, 12, 4, 13, 3, 5, 14, 
			  4, 11, 5, 12};

    /* these give the initial firsting position of the triangle game */
    private int[] board = {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private int[] bread_board = {1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    
    /* these give the coordinates of the holes/pegs in the triangle game */
    private int[] board_X = {300, 250, 350, 200, 300, 400, 150, 250, 350, 450, 
			     100, 200, 300, 400, 500};
    private int[] board_Y = {100, 200, 200, 300, 300, 300, 400, 400, 400, 400, 
			     500, 500, 500, 500, 500};

    /* this is the size of the balls used in the Triangle game */
    private int ballSize = 20;

    /* this is the canvas or drawing upon which to draw */
    private Dimension currSize;
    protected Thread threadRunning;
    protected Image bufferDrawing;

    /* these commands have to do with mouse Clicking events */
    private int initX;
    private int initY;
    private int finalX;
    private int finalY;
    private boolean isClicked = false;
    public void mouseClicked(MouseEvent evt) {
	if (isClicked == false) {
	    isClicked = true;
	    if (threadRunning != null) threadRunning.stop();
	}
	else if (isClicked == true) {
	    isClicked = false;
	    if (threadRunning != null) threadRunning.stop();
	    threadRunning = new Thread(new Solve(false));
	    threadRunning.setPriority(Thread.MIN_PRIORITY);
	    threadRunning.start();
	}
    }
    public void mousePressed(MouseEvent evt) { }
    public void mouseEntered(MouseEvent evt) { }
    public void mouseExited(MouseEvent evt) { }
    public void mouseReleased(MouseEvent evt) { }

    // constructors
    public TriangleBoard() { 
        super();
        currSize = new Dimension();
        initializeBreadBoard();
        threadRunning = null;
	this.setBackground(Color.white);
	this.isClicked = false;
    }

    public TriangleBoard(int whereFirst) {
        super();
        currSize = new Dimension();
        if (whereFirst >= 15) return;
        if (whereFirst < 0) return;
        for (int i=0; i<15; i++) board[i] = 1;
        board[whereFirst] = 0;
        initializeBreadBoard();
        threadRunning = null;
	this.setBackground(Color.white);
    }
    public void initializeBreadBoard() {
        for (int i=0; i<15; i++) {
	    bread_board[i] = board[i];
	}
    }
    
    public void drawBoard() {
        Graphics g = bufferDrawing.getGraphics();
	double xscale = currSize.width*1.0/600;
        double yscale = currSize.height*1.0/600;
        int xcoord = 0;
        int ycoord = 0;
        int scaledBallSize = (int)(ballSize*xscale);
        for (int i=0; i<15; i++) {
            if (bread_board[i] == 0) g.setColor(Color.red);
            if (bread_board[i] == 1) g.setColor(Color.blue);
            xcoord = (int)(board_X[i]*xscale);
            ycoord = (int)(board_Y[i]*yscale);
            scaledBallSize = (int)(ballSize*xscale);
            g.fillOval(xcoord, ycoord, scaledBallSize, scaledBallSize);
        }
        repaint();
    }

    // these are button commands, as well as commands which can be called
    // by other programs as well as other methods
    public void reset() {
	currSize = getSize();
	bufferDrawing = createImage(currSize.width, currSize.height);
	if (threadRunning != null) threadRunning.stop();
        threadRunning = new Thread(new Solve(true));
        threadRunning.setPriority(Thread.MIN_PRIORITY);
        threadRunning.start();
    }
    public void quit() { System.exit(0); }
    public void start() {
	currSize = getSize();
	bufferDrawing = createImage(currSize.width, currSize.height);
	if (threadRunning != null) threadRunning.stop();
	threadRunning = new Thread(new Solve(false));
	threadRunning.setPriority(Thread.MIN_PRIORITY);
	threadRunning.start();
    }
    public void stop() {
	if (threadRunning != null) threadRunning.stop();
    }

    // this is the runnable class which the thread runs
    public class Solve implements Runnable {
	public boolean initBreadBoard;
	public Solve(boolean setBreadBoard) {initBreadBoard = setBreadBoard;}
        public int number(int[] bb) {
            int length = bb.length;
            int n = 0;
            for (int i=0; i<length; i++) {
                if (bb[i] == 1) n = n+1;
            }
            return n;
        }
        public int solve() {
	    if (number(bread_board) == 1) {
		    System.out.println("Game Solved!");
		    return 1;
	    }
            for (int i=0; i<36; i++) {
		if ((bread_board[first[i]] == 1) && 
		    (bread_board[middle[i]] == 1) &&
                    (bread_board[last[i]] == 0)) {
                    bread_board[first[i]] = 0;
                    bread_board[middle[i]] = 0;
                    bread_board[last[i]] = 1;
                    drawBoard();
                    if (solve() == 1) return 1;
                }
                else {
                    bread_board[first[i]] = 1;
                    bread_board[middle[i]] = 1;
                    bread_board[last[i]] = 0;
                    drawBoard();
		    if (solve() == 1) return 1;
                }
            }
            return 0;
        }
        public void run() {
	    if (initBreadBoard == true) initializeBreadBoard();
            this.solve();
        }
    }
    
    public void update(Graphics g) { paint(g);}
    public void paint(Graphics g) {
        Dimension mySize = this.getSize();
        if (bufferDrawing == null) {
            currSize = getSize();
            bufferDrawing = createImage(currSize.width, currSize.height);
            threadRunning = new Thread(new Solve(true));
            threadRunning.setPriority(Thread.MIN_PRIORITY);
            threadRunning.start();
        }
        else if (currSize.width != mySize.width ||
		 currSize.height != mySize.height) {
            currSize = mySize;
            bufferDrawing = createImage(currSize.width, currSize.height);
            if (threadRunning != null) threadRunning.stop();
            threadRunning = new Thread(new Solve(false));
            threadRunning.setPriority(Thread.MIN_PRIORITY);
            threadRunning.start();
        }
        g.drawImage(bufferDrawing, 0, 0, this);
    }
}
