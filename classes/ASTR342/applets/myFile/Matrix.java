import java.util.*;
import java.lang.*;

class Matrix {

    // specially constructed class that iteratively approaches a solution
    // to the determinant of a square matrix
    private class DetermFrag {
	private double value;
	private Vector coefficients;
	private Vector values;
	private Vector children;
	private DetermFrag parent;
	private int node_index;
	public DetermFrag() {
	    value = 0;
	    coefficients = new Vector();
	    values = new Vector();
	    children = new Vector();
	    parent = null;
	    node_index = -1;
	}
	public void setNodeIndex(int i) { node_index = i;}
	public void setParent(DetermFrag thisFrag) { parent = thisFrag; }
	public void setValues(int index, Double val) { values.add(index, val);}

	// iteratively calculates out the 
	public void determine(Matrix squareMatrix) {
	    int rows, cols;
	    double coefficient, argument;
	    rows = squareMatrix.getRows();
	    cols = squareMatrix.getCols();
	    if (rows != cols) return;
	    if (rows == 1) {
		value = squareMatrix.getElement(0,0);
		if (node_index != -1) 
		    parent.setValues(node_index, new Double(value));
	    }
	    else {
		for (int i=0; i<rows; i++) {
		    DetermFrag newFrag = new DetermFrag();
		    newFrag.setNodeIndex(i);
		    newFrag.setParent(this);
		    value = squareMatrix.getElement(0,i)*Math.pow(-1,i);
		    coefficients.add(i, new Double(value));
		    newFrag.determine(squareMatrix.subMatrix(0, i));
		    children.add(i, newFrag);
		}
		value = 0;
		for (int i=0; i<rows; i++) {
		    coefficient = 
			((Double)(coefficients.elementAt(i))).doubleValue();
		    argument = ((Double)(values.elementAt(i))).doubleValue();
		    value = value + coefficient*argument;
		    if (node_index != -1) {
			parent.setValues(node_index, new Double(value));
		    }
		}
	    }
	}

	// accessors. These are used in the finding the determinant
	public double getValue() { return value;}
	public DetermFrag getParent() { return parent;}
	public Vector getChildren() { return children;}
	public Vector getValues() { return values;}
	public int getNodeIndex() { return node_index;}
    }
    
    private int num_rows;
    private int num_columns;
    private double[][] arg;
    public Matrix() {
	num_rows = 1;
	num_columns = 1;
	arg = new double[1][1];
	arg[0][0] = 0;
    }
    public Matrix(double value) {
	num_rows = 1;
	num_columns = 1;
	arg = new double[1][1];
	arg[0][0] = value;
    }
    public Matrix(int row, int col) {
	if (row < 1) return;
	if (col < 1) return;
	num_rows = row;
	num_columns = col;
	arg = new double[num_rows][num_columns];
	for (int i=0; i<num_rows; i++) {
	    for (int j=0; j<num_rows; j++) {
		arg[i][j] = 0;
	    }
	}
    }
    public void identity_matrix(int size) {
	if (size < 1) return;
	num_rows = size;
	num_columns = size;
	arg = new double[size][size];
	for (int i=0; i<num_rows; i++) {
	    for (int j=0; j<num_columns; j++) {
		if (i != j) arg[i][j] = 0;
		else arg[i][j] = 1;
	    }
	}
    }
    public void identity_matrix() {
	if (num_rows != num_columns) return;
	for (int i=0; i<num_rows; i++) {
	    for (int j=0; j<num_columns; j++) {
		if (i != j) arg[i][j] = 0;
		else arg[i][i] = 1.0;
	    }
	}
    }
    public void rotation_matrix(int dim, int axis1, int axis2, double theta) {
	if (dim < 1) return;
	if (axis1 < 1) return;
	if (axis2 < 1) return;
	if (axis1 > dim) return;
	if (axis2 > dim) return;
	if (axis1 >= axis2) return;
	
	num_rows = dim;
	num_columns = dim;
	arg = new double[dim][dim];
	for (int i=0; i<num_rows; i++) {
	    for (int j=0; j<num_columns; j++) {
		arg[i][j] = 0;
	    }
	}
	arg[axis1-1][axis1-1] = Math.cos(theta);
	arg[axis1-1][axis2-1] = -Math.sin(theta);
	arg[axis2-1][axis1-1] = Math.sin(theta);
	arg[axis2-1][axis2-1] = Math.cos(theta);
	for (int i=0; i<num_rows; i++) {
	    if (i != axis1-1 && i != axis2-1) arg[i][i] = 1;
	}
    }
    public void create_column_vector(double[] input) {
	num_rows = input.length;
	num_columns = 1;
	arg = new double[num_rows][num_columns];
	for (int i=0; i<num_rows; i++) {
	    arg[i][0] = input[i];
	}
    }
    public void create_row_vector(double[] input) {
	num_rows = 1;
	num_columns = input.length;
	arg = new double[num_rows][num_columns];
	for (int i=0; i<num_columns; i++) {
	    arg[0][i] = input[i];
	}
    }
    public Matrix multiply(Matrix otherMatrix) {
	int thisMatrixRows, thisMatrixColumns, otherMatrixRows, 
	    otherMatrixColumns;
	double value, x, y;
	thisMatrixRows = this.getRows();
	thisMatrixColumns = this.getCols();
	otherMatrixRows = otherMatrix.getRows();
	otherMatrixColumns = otherMatrix.getCols();
	if (thisMatrixColumns != otherMatrixRows) return new Matrix();
	Matrix newMatrix = new Matrix(thisMatrixRows, otherMatrixColumns);
	for (int i=0; i<thisMatrixRows; i++) {
	    for (int j=0; j<otherMatrixColumns; j++) {
		value = 0;
		for (int k=0; k<thisMatrixColumns; k++) {
		    x = this.getElement(i, k);
		    y = otherMatrix.getElement(k, j);
		    value = value+x*y;
		}
		newMatrix.setElement(i, j, value);
	    }
	}
	return newMatrix;
    }

    // creates a submatrix at index i, j; row i and column j are eliminated
    public Matrix subMatrix(int i, int j) {
	double value;
	int q, r;
	if (i < 0) return new Matrix();
	if (i >= this.num_rows) return new Matrix();
	if (j < 0) return new Matrix();
	if (j >= this.num_columns) return new Matrix();
	Matrix newMatrix = new Matrix(this.num_rows-1, this.num_columns-1);
	for (q=0; q<i; q++) {
	    for (r=0; r<j; r++) {
		value = this.getElement(q, r);
		newMatrix.setElement(q, r, value);
	    }
	}
	for (q=i+1; q<this.num_rows; q++) {
	    for (r=0; r<j; r++) {
		value = this.getElement(q, r);
		newMatrix.setElement(q-1, r, value);
	    }
	}
	for (q=0; q<i; q++) {
	    for (r=j+1; r<this.num_columns; r++) {
		value = this.getElement(q, r);
		newMatrix.setElement(q, r-1, value);
	    }
	}
	for (q=i+1; q<this.num_rows; q++) {
	    for (r=j+1; r<this.num_columns; r++) {
		value = this.getElement(q, r);
		newMatrix.setElement(q-1, r-1, value);
	    }
	}
	return newMatrix;
    }

    public double determinant() {
	int row = this.getRows();
	int col = this.getCols();
	if (row != col) return 0;
	DetermFrag df = new DetermFrag();
	df.determine(this);
	return df.getValue();
    }
    
    public Matrix inverse() {
	double value, derm;
	if (num_rows != num_columns) return new Matrix();
	if (num_rows == 0) return new Matrix();
	derm = this.determinant();
	Matrix newMatrix = new Matrix(num_rows, num_rows);
	for (int i=0; i<num_rows; i++) {
	    for (int j=0; j<num_columns; j++) {
		value = 
		    this.subMatrix(j,i).determinant()*Math.pow(-1,i+j)/derm;
		newMatrix.setElement(i, j, value);
	    }
	}
	return newMatrix;
    }

    // accessors
    public int getRows() { return this.num_rows; }
    public int getCols() { return this.num_columns; }
    public double getElement(int row_index, int col_index) {
	if (row_index < 0) return 0;
	if (row_index >= this.num_rows) return 0;
	if (col_index < 0) return 0;
	if (col_index >= this.num_columns) return 0;
	return arg[row_index][col_index];
    }

    // parameter-setting methods
    public void setElement(int row_index, int col_index, double value) {
	if (row_index < 0) return;
	if (row_index >= this.num_rows) return;
	if (col_index < 0) return;
	if (col_index >= this.num_columns) return;
	arg[row_index][col_index] = value;
    }

    // copying method
    public void copy(Matrix otherMatrix) {
	int rows = otherMatrix.getRows();
	int cols = otherMatrix.getCols();
	if (this.num_rows != rows) return;
	if (this.num_columns != cols) return;

	for (int i=0; i<num_rows; i++) {
	    for (int j=0; j<num_columns; j++) {
		this.arg[i][j] = otherMatrix.getElement(i,j);
	    }
	}
    }

    // testing method
    public static void main(String[] args) {
	String line = "";
	Matrix thisMatrix = new Matrix();
	thisMatrix.identity_matrix(10);
	for (int i=0; i<thisMatrix.getRows(); i++) {
	    for (int j=0; j<thisMatrix.getCols(); j++)
		thisMatrix.setElement(i, j, 10*Math.random());
	}
	double determ = thisMatrix.determinant();
	System.out.println("determinant = "+determ);
	System.out.println("Matrix = ");
	for (int i=0; i<thisMatrix.getRows(); i++) {
	    line = "[ ";
	    for (int j=0; j<thisMatrix.getCols(); j++) {
		line = line + thisMatrix.getElement(i,j) + " ";
	    }
	    line = line + "]";
	    System.out.println(line);
	}
	Matrix newMatrix = thisMatrix.inverse();
	System.out.println("Matrix inverse = ");
	for (int i=0; i<newMatrix.getRows(); i++) {
	    line = "[ ";
	    for (int j=0; j<newMatrix.getCols(); j++) {
		line = line + newMatrix.getElement(i,j) + " ";
	    }
	    line = line + "]";
	    System.out.println(line);
	}
	Matrix multMatrix = thisMatrix.multiply(newMatrix);
	System.out.println("Matrix times Matrix inverse = ");
	for (int i=0; i<multMatrix.getRows(); i++) {
	    line = "[ ";
	    for (int j=0; j<multMatrix.getCols(); j++) {
		line = line + multMatrix.getElement(i,j) + " ";
	    }
	    line = line + "]";
	    System.out.println(line);
	}
    }
}
