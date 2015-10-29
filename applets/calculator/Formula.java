import java.util.*;
import java.text.*;

// uses recursion to determine values of all the nodes and also all the 
// operations, and when it reaches the end of the list (after delimiting),
// then it determines the final value
public class Formula {
    private double value = Double.NaN;
    private Vector nodes = new Vector();
    private Vector operations = new Vector();
    
    // these are important operators for the tokenization
    public static String plus = "+";
    public static String minus = "-";
    public static String mult = "*";
    public static String div = "/";
    public static String pow = "^";
    public static String lparen = "(";
    public static String rparen = ")";
    
    // now the one-argument functions
    public static String SIN = "sin";
    public static String COS = "cos";
    public static String TAN = "tan";
    public static String SINH = "sinh";
    public static String COSH = "cosh";
    public static String TANH = "tanh";
    public static String LOG = "log";
    public static String EXP = "exp";
    public static String ACOS = "acos";
    public static String ASIN = "asin";
    public static String ATAN = "atan";
    public static String ASINH = "asinh";
    public static String ACOSH = "acosh";
    public static String ATANH = "atanh";
    public static String CSCH = "csch";
    public static String COTH = "coth";
    public static String SECH = "sech";
    public static String SEC = "sec";
    public static String CSC = "csc";
    public static String COT = "cot";
    public static String UNITSTEP = "unitstep";
    public static String DELTAFUNC = "deltafunc";
    public static String ERF = "erf";
    
    // now here are two-function arguments
    public static String POW = "pow";
    public static String BESSELJ = "besselj";
    public static String BESSELK = "besselk";
    public static String BESSELI = "besseli";
    public static String BESSELY = "bessely";

    // basic constructor
    public Formula() { }
    
    public static final double atanh(double x) {
        if (x > 1) return Double.NaN;
        if (x < -1) return Double.NaN;
        double y = Math.log((1+x)/(1-x));
        return y;
    }
    public static final double acosh(double x) {
        if (x < 1) return Double.NaN;
        double y = Math.log(x+Math.sqrt(x*x-1));
        return y;
    }
    public static final double asinh(double x) {
        double y = Math.log(x+Math.sqrt(x*x+1));
        return y;
    }
    public static final double tanh(double x) {
        double y = (Math.exp(x)-Math.exp(-x))/(Math.exp(x)+Math.exp(-x));
        return y;
    }
    public static final double sinh(double x) {
        double y = 0.5*(Math.exp(x) - Math.exp(-x));
        return y;
    }
    public static final double cosh(double x) {
        double y = 0.5*(Math.exp(x) + Math.exp(-x));
        return y;
    }
    public static final double coth(double x) {
        double y = (Math.exp(x)+Math.exp(-x))/(Math.exp(x)-Math.exp(-x));
        return y;
    }
    public static final double csch(double x) {
        double y = 2/(Math.exp(x)-Math.exp(-x));
        return y;
    }
    public static final double sech(double x) {
        double y = 2/(Math.exp(x)+Math.exp(-x));
        return y;
    }
    public static final double sec(double x) {
        double y = 1.0/Math.cos(x);
        return y;
    }
    public static final double csc(double x) {
        double y = 1.0/Math.sin(x);
        return y;
    }
    public static final double cot(double x) {
        double y = 1.0/Math.tan(x);
        return y;
    }
    public static final double unitstep(double x) {
        if (x >= 0) return 1.0;
        else return 0.0;
    }
    public static final double deltafunc(double x) {
        if (x == 0) return 1.0;
        else return 0.0;
    }
    
    
    // flush out all the values in the different nodes
    public void flush() {
        this.value = Double.NaN;
        this.nodes = null;
        this.nodes = new Vector();
        this.operations = null;
        this.operations = new Vector();
    }
    
    // now calculate out the value, done recursively
    public double getValue(String argument, String[] symbols, double[] values) 
        throws IsInvalidException {
        this.flush(); // first flush out all values here
        
        // check if we have valid symbols and values
        if (symbols.length != values.length) throw new IsInvalidException();
        Formula temp = new Formula();
        boolean nextTokenIsOperator = false;
        boolean isNumber = false;
        boolean isNeg = false;
        int j = 0;
        
        // here we check if the string is a number -- game over
        try { value = Double.parseDouble(argument);}
        catch(NumberFormatException e) { isNumber = false; }
        if (isNumber == true) return value;
        
        // now if not a number then we go through operations
        String line = "";
        String[] subline = new String[12];
        double[] val = new double[12];
        for (int i=0; i<12; i++) subline[i] = "";
        for (int i=0; i<12; i++) val[i] = 0.0;
        
        // initializing FormulaTokenizer, checking that list is distinct
        FormulaTokenizer ft = new FormulaTokenizer(symbols);
        if (ft.numberSymbols() != symbols.length) throw new IsInvalidException();
        ft.tokenize(argument);
        
        StringTokenizer st;
        if (ft.size() == 0) return 0.0;

        while (ft.hasMoreElements()) {
            line = null;
            line = ft.nextElement();
            for (int i=0; i<12; i++) subline[i] = "";
            for (int i=0; i<12; i++) val[i] = 0.0;
            if (!nextTokenIsOperator) {
                if (line.equals(Formula.plus)) throw new IsInvalidException();
                else if (line.equals(Formula.minus)) {
                    if (operations.size() == 0 && !isNeg) {
                        isNeg = true; line = null;
                        line = ft.nextElement();
                    }
                    else throw new IsInvalidException();
                }
                else if (line.equals(Formula.mult)) throw new IsInvalidException();
                else if (line.equals(Formula.div)) throw new IsInvalidException();
                else if (line.equals(Formula.pow)) throw new IsInvalidException();
                
                // 9-letter functions
                if (line.length() >= 10 && !nextTokenIsOperator) {
                    if (line.substring(0,10).equals(Formula.DELTAFUNC+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.DELTAFUNC);
                        val[1] = Formula.deltafunc(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                }
                
                // 8-letter functions
                if (line.length() >= 9 && !nextTokenIsOperator) {
                    if (line.substring(0,9).equals(Formula.UNITSTEP+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.UNITSTEP);
                        val[1] = Formula.unitstep(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                }
                
                // 7-letter functions
                if (line.length() >= 8 && !nextTokenIsOperator) {
                    if (line.substring(0,8).equals(Formula.BESSELI+Formula.lparen)) {
                        nodes.add(new Double(0.0));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,8).equals(Formula.BESSELK+Formula.lparen)) {
                        nodes.add(new Double(0.0));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,8).equals(Formula.BESSELY+Formula.lparen)) {
                        nodes.add(new Double(0.0));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,8).equals(Formula.BESSELJ+Formula.lparen)) {
                        nodes.add(new Double(0.0));
                        nextTokenIsOperator = true;
                    }
                }
                
                // 5-letter functions
                if (line.length() >= 6 && !nextTokenIsOperator) {
                    if (line.substring(0,6).equals(Formula.ASINH+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.ASINH);
                        val[1] = Formula.asinh(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,6).equals(Formula.ACOSH+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.ACOSH);
                        val[1] = Formula.acosh(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,6).equals(Formula.ATANH+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.ATANH);
                        val[1] = Formula.atanh(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                }
                
                // 4-letter functions
                if (line.length() >= 5 && !nextTokenIsOperator) {
                    if (line.substring(0,5).equals(Formula.ACOS+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.ACOS);
                        val[1] = Math.acos(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,5).equals(Formula.ASIN+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.ASIN);
                        val[1] = Math.asin(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,5).equals(Formula.ATAN+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.ATAN);
                        val[1] = Math.atan(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,5).equals(Formula.SECH+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.SECH);
                        val[1] = Formula.sech(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,5).equals(Formula.SINH+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.SINH);
                        val[1] = Formula.sinh(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,5).equals(Formula.COSH+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.COSH);
                        val[1] = Formula.cosh(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,5).equals(Formula.TANH+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.TANH);
                        val[1] = Formula.tanh(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                }

                // 3-letter functions
                if (line.length() >= 4 && !nextTokenIsOperator) {
                    if (line.substring(0,4).equals(Formula.SIN+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.SIN);
                        val[1] = Math.sin(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,4).equals(Formula.COS+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.COS);
                        val[1] = Math.cos(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,4).equals(Formula.TAN+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.TAN);
                        val[1] = Math.tan(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,4).equals(Formula.SEC+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.SEC);
                        val[1] = Formula.sec(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,4).equals(Formula.CSC+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.CSC);
                        val[1] = Formula.csc(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,4).equals(Formula.COT+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.COT);
                        val[1] = Formula.cot(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,4).equals(Formula.EXP+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.EXP);
                        val[1] = Math.exp(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,4).equals(Formula.LOG+Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, Formula.LOG);
                        val[1] = Math.log(temp.getValue(subline[1], symbols, values));
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (line.substring(0,4).equals(Formula.POW+Formula.lparen)) {
                        temp.flush();
                        subline[0] = FormulaTokenizer.strip(line, Formula.POW);
                        st = null; j = 0;
                        st = new StringTokenizer(subline[0], " \n\t\f\r\b,", false);
                        while (st.hasMoreTokens()) {
                            j = j+1;
                            subline[j] = st.nextToken();
                        }
                        if (j != 2) throw new IsInvalidException();
                        temp.flush(); val[1] = temp.getValue(subline[1], symbols, values);
                        temp.flush(); val[2] = temp.getValue(subline[2], symbols, values);
                        val[3] = Math.pow(val[1], val[2]);
                        if (isNeg) {
                            val[3] = -val[3];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[3]));
                        nextTokenIsOperator = true;
                    }
                }
                if (!nextTokenIsOperator) {
                    if (line.substring(0,1).equals(Formula.lparen)) {
                        temp.flush();
                        subline[1] = FormulaTokenizer.strip(line, "");
                        val[1] = temp.getValue(subline[1], symbols, values);
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else if (ft.isSymbol(line)) { // if this is symbol
                        temp.flush();
                        val[1] = FormulaTokenizer.getSymbolValue(line, symbols, values);
                        if (isNeg) {
                            val[1] = -val[1];
                            isNeg = false;
                        }
                        nodes.add(new Double(val[1]));
                        nextTokenIsOperator = true;
                    }
                    else { // is a number, after all previous cases exhausted
                        try { 
                            val[1] = Double.parseDouble(line);
                            if (isNeg) {
                                val[1] = -val[1];
                                isNeg = false;
                            }
                            nodes.add(new Double(val[1]));
                            nextTokenIsOperator = true;
                        }
                        catch(NumberFormatException e) { throw new IsInvalidException();}
                    }
                }
            }
            else {
                if (line.equals(Formula.plus)) operations.add(Formula.plus);
                else if (line.equals(Formula.minus)) operations.add(Formula.minus);
                else if (line.equals(Formula.mult)) operations.add(Formula.mult);
                else if (line.equals(Formula.div)) operations.add(Formula.div);
                else if (line.equals(Formula.pow)) operations.add(Formula.pow);
                else throw new IsInvalidException();
                nextTokenIsOperator = false;
            }
        }
        if (!nextTokenIsOperator) throw new IsInvalidException();
        
        // now calculate the final value, since all we have are a set of double values
        // interleaved with operations. Here, we use PEMDAS operator precedence
        
        // step 1: power
        for (int i=0; i<operations.size(); i++) {
            line = null;
            line = (String)operations.get(i);
            if (line.equals(Formula.pow)) {
                val[1] = ((Double)nodes.get(i)).doubleValue();
                val[2] = ((Double)nodes.get(i+1)).doubleValue();
                val[3] = Math.pow(val[1], val[2]);
                nodes.set(i+1, new Double(val[3]));
                operations.remove(i); nodes.remove(i);
                i = i-1;
            }
        }
        
        // step 2: multiplication
        for (int i=0; i<operations.size(); i++) {
            line = null;
            line = (String)operations.get(i);
            if (line.equals(Formula.mult)) {
                val[1] = ((Double)nodes.get(i)).doubleValue();
                val[2] = ((Double)nodes.get(i+1)).doubleValue();
                val[3] = val[1]*val[2];
                nodes.set(i+1, new Double(val[3]));
                operations.remove(i); nodes.remove(i);
                i = i-1;
            }
        }

        // step 3: division
        for (int i=0; i<operations.size(); i++) {
            line = null;
            line = (String)operations.get(i);
            if (line.equals(Formula.div)) {
                val[1] = ((Double)nodes.get(i)).doubleValue();
                val[2] = ((Double)nodes.get(i+1)).doubleValue();
                val[3] = val[1]/val[2];
                nodes.set(i+1, new Double(val[3]));
                operations.remove(i); nodes.remove(i);
                i = i-1;
            }
        }
        
        // now perform simple addition subtraction
        val[1] = ((Double)nodes.get(0)).doubleValue();
        for (int i=0; i<operations.size(); i++) {
            if (((String)operations.get(i)).equals(Formula.mult)) 
                throw new IsInvalidException();
            else if (((String)operations.get(i)).equals(Formula.div))
                throw new IsInvalidException();
            else if (((String)operations.get(i)).equals(Formula.plus)) {
                val[1] += ((Double)nodes.get(i+1)).doubleValue();
            }
            else if (((String)operations.get(i)).equals(Formula.minus)) {
                val[1] -= ((Double)nodes.get(i+1)).doubleValue();
            }
        }
        return val[1];
    }

    public static void main(String[] args) {
        String[] symbols = {"y", "z"};
        double[] values = { 2000, 1000};
        Formula f = new Formula();
        try {
            System.out.println("value = " + f.getValue("deltafunc(y-2000)", 
                                                       symbols, values));
        } catch(Exception e) { System.out.println("<invalid>");}
    }
}
