class Coords {
    private double xcoord;
    private double ycoord;
    private double zcoord;
    private double xprime;
    private double yprime;
    private double zprime;

    public Coords() {
	this.xcoord = 0;
	this.ycoord = 0;
	this.zcoord = 0;
	this.xprime = 0;
	this.yprime = 0;
	this.zprime = 0;
    }
    
    public Coords(double setX, double setY, double setZ) {
	this.xcoord = setX;
	this.ycoord = setY;
	this.zcoord = setZ;
	this.xprime = 0;
	this.yprime = 0;
	this.zprime = 0;
    }
    
    public Coords(double setX, double setY, double setZ,
		  double setVX, double setVY, double setVZ) {
	this.xcoord = setX;
	this.ycoord = setY;
	this.zcoord = setZ;
	this.xprime = setVX;
	this.yprime = setVY;
	this.zprime = setVZ;
    }
    
    // inputters
    public void setX(double setX) { this.xcoord = setX;}
    public void setY(double setY) { this.ycoord = setY;}
    public void setZ(double setZ) { this.zcoord = setZ;}
    public void setVX(double setVX) { this.xprime = setVX;}
    public void setVY(double setVY) { this.yprime = setVY;}
    public void setVZ(double setVZ) { this.zprime = setVZ;}
    
    // accessors
    public double getX() { return this.xcoord;}
    public double getY() { return this.ycoord;}
    public double getZ() { return this.zcoord;}
    public double getVX() { return this.xprime;}
    public double getVY() { return this.yprime;}
    public double getVZ() { return this.zprime;}
}
