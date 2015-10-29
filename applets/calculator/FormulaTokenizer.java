import java.util.*;

public class FormulaTokenizer {
    private Vector strings = new Vector();
    private int currIndex = 0;
    private static String delimsimple = " \n\f\b\r\t";
    private static String delims = " \n\f\b\r\t+-*/";
    private static String delimsparen = " \n\f\b\r\t()+-*/^";
    private static String delimslparen = " \n\f\b\r\t(";
    private static String delimsrparen = " \n\f\b\r\t)";

    private String[] symbols = null;
    
    public int size() { return strings.size();}
    public int numberSymbols() { return symbols.length;}
    
    public boolean hasMoreElements() {
        if (this.currIndex < strings.size() && strings.size() != 0) return true;
        else return false;
    }

    public boolean isSymbol(String line) {
        for (int i=0; i<symbols.length; i++)
            if (line.equals(symbols[i])) return true;
        return false;
    }
    public static double getSymbolValue(String line, String[] symbols, double[] values) 
        throws IsInvalidException {
        if (values.length != symbols.length) throw new IsInvalidException();
        boolean isSymbol = false;
        for (int i=0; i<symbols.length; i++) {
            if (line.equals(symbols[i])) {
                isSymbol = true;
                return values[i];
            }
        }
        if (!isSymbol) throw new IsInvalidException();
        return Double.NaN;
    }

    public String nextElement() {
        if (!this.hasMoreElements()) return "";
        else {
            this.currIndex = this.currIndex+1;
            return (String)this.strings.get(this.currIndex-1);
        }
    }
    public int index() { return this.currIndex;}
    
    public static boolean isFunction(String line) {
        if (line.equals(Formula.SIN)) return true;
        else if (line.equals(Formula.COS)) return true;
        else if (line.equals(Formula.TAN)) return true;
        else if (line.equals(Formula.SINH)) return true;
        else if (line.equals(Formula.COSH)) return true;
        else if (line.equals(Formula.TANH)) return true;
        else if (line.equals(Formula.LOG)) return true;
        else if (line.equals(Formula.EXP)) return true;
        else if (line.equals(Formula.POW)) return true;
        else if (line.equals(Formula.ACOS)) return true;
        else if (line.equals(Formula.ASIN)) return true;
        else if (line.equals(Formula.ATAN)) return true;
        else if (line.equals(Formula.ACOSH)) return true;
        else if (line.equals(Formula.ASINH)) return true;
        else if (line.equals(Formula.ATANH)) return true;
        else if (line.equals(Formula.CSC)) return true;
        else if (line.equals(Formula.SEC)) return true;
        else if (line.equals(Formula.COT)) return true;
        else if (line.equals(Formula.CSCH)) return true;
        else if (line.equals(Formula.SECH)) return true;
        else if (line.equals(Formula.COTH)) return true;
        else if (line.equals(Formula.BESSELI)) return true;
        else if (line.equals(Formula.BESSELJ)) return true;
        else if (line.equals(Formula.BESSELY)) return true;
        else if (line.equals(Formula.BESSELK)) return true;
        else if (line.equals(Formula.UNITSTEP)) return true;
        else if (line.equals(Formula.DELTAFUNC)) return true;
        else return false;
    }
    
    public static boolean isInvalid(String line) {
        if (line.equals(Formula.plus)) return true;
        else if (line.equals(Formula.minus)) return true;
        else if (line.equals(Formula.mult)) return true;
        else if (line.equals(Formula.div)) return true;
        else if (line.equals(Formula.lparen)) return true;
        else if (line.equals(Formula.rparen)) return true;
        else if (line.equals(" ")) return true;
        else if (line.equals("\n")) return true;
        else if (line.equals("\f")) return true;
        else if (line.equals("\r")) return true;
        else if (line.equals("\t")) return true;
        else if (line.equals("\b")) return true;
        else if (line.equals(".")) return true;
        else if (line.equals(",")) return true;
        else return false;
    }
    
    public FormulaTokenizer(String[] setsymbols) throws IsInvalidException {
        Vector tempSymbols = new Vector();
        boolean isSame = false;
        for (int i=0; i<setsymbols.length; i++) {
            isSame = false;
            if (FormulaTokenizer.isInvalid(setsymbols[i])) throw new IsInvalidException();
            if (FormulaTokenizer.isFunction(setsymbols[i])) throw new IsInvalidException();
            for (int j=0; j<i; j++) if (setsymbols[j].equals(setsymbols[i])) isSame = true;
            if (isSame == false) tempSymbols.add(setsymbols[i]);
        }
        this.symbols = new String[tempSymbols.size()];
        for (int i=0; i<tempSymbols.size(); i++) 
            this.symbols[i] = (String)(tempSymbols.get(i));
    }

    public FormulaTokenizer(FormulaTokenizer ft) throws IsInvalidException {
        if (ft.symbols == null) throw new IsInvalidException();
        int rows = ft.symbols.length;
        this.symbols = new String[rows];
        for (int i=1; i<rows; i++) this.symbols[i] = ft.symbols[i];
    }

    
    public void tokenize(String argument)
        throws IsInvalidException {
        
        boolean issymb = false;
        String line = "";
        String subline = "";
        int lparens = 0; double val = 0.0;
        StringTokenizer fi = new StringTokenizer(argument, delimsimple, false);
        while (fi.hasMoreTokens()) line = line + fi.nextToken();
        StringTokenizer st = new StringTokenizer(line, delimsparen, true);
        while (st.hasMoreTokens()) {
            line = null;
            line = st.nextToken();
            
            // if we have arithmetic operators
            if (line.equals(Formula.plus) && lparens == 0)
                strings.add(Formula.plus);
            else if (line.equals(Formula.minus) && lparens == 0) 
                strings.add(Formula.minus);
            else if (line.equals(Formula.mult) && lparens == 0) 
                strings.add(Formula.mult);
            else if (line.equals(Formula.div) && lparens == 0) 
                strings.add(Formula.div);
            else if (line.equals(Formula.pow) && lparens == 0)
                strings.add(Formula.pow);
            
            // else if we have a symbol
            else if (this.isSymbol(line)) strings.add(line);
            
            // if sine, cosine, etc...we treat as an atomic unit with "()" 
            // expression
            else if (FormulaTokenizer.isFunction(line) && lparens == 0) {
                subline = null;
                subline = st.nextToken();
                if (!subline.equals(Formula.lparen))
                    throw new IsInvalidException();
                lparens = 1;
                line = line+subline;
                while (st.hasMoreTokens() && lparens != 0) {
                    subline = null;
                    subline = st.nextToken();
                    line = line+subline;
                    if (subline.equals(Formula.lparen)) lparens = lparens+1;
                    else if (subline.equals(Formula.rparen)) 
                        lparens = lparens-1;
                }
                if (lparens != 0) throw new IsInvalidException();
                else strings.add(line);
            }
            
            // if we begin parenthetic expression, treat as atomic unit
            else if (line.equals(Formula.lparen) && lparens == 0) {
                lparens = 1;
                while (st.hasMoreTokens() && lparens != 0) {
                    subline = null;
                    subline = st.nextToken();
                    line = line+subline;
                    if (subline.equals(Formula.lparen)) lparens = lparens+1;
                    else if (subline.equals(Formula.rparen)) 
                        lparens = lparens-1;
                }
                if (lparens != 0) throw new IsInvalidException();
                else strings.add(line);
            }
            
            // for everything else, including possible symbols
            else {
                try { val = Double.parseDouble(line);} // MUST be a number if not above
                catch(NumberFormatException e) { throw new IsInvalidException();}
                strings.add(line);
            }
        }
    }

    public static boolean hasSymbol(String argument, String symbol) {
        if (symbol.length() > argument.length()) return false;
        for (int i=0; i<=argument.length()-symbol.length(); i++) {
            if (argument.substring(i,i+symbol.length()).equals(symbol)) return true;
        }
        return false;
    }
    
    // this strips a string into its final arguments
    public static String strip(String superarg, String subarg) 
        throws IsInvalidException {
        String output = null;
        if (subarg.length() >= superarg.length()) 
            throw new IsInvalidException();
        if (superarg.charAt(subarg.length()) != '(') 
            throw new IsInvalidException();
        if (superarg.charAt(superarg.length()-1) != ')') 
            throw new IsInvalidException();
        if (!superarg.substring(0,subarg.length()).equals(subarg))
            throw new IsInvalidException();
        else {
            output = superarg.substring(subarg.length()+1, superarg.length()-1);
            return output;
        }
    }

    public static void main(String[] args) {
        String argument = "8+9*7";
        String[] setsymbols = new String[4];
        FormulaTokenizer ft;
        setsymbols[1] = "x";
        setsymbols[2] = "y";
        setsymbols[3] = "z";
        try { ft = new FormulaTokenizer(setsymbols);}
        catch(IsInvalidException e) { return;}
        try { ft.tokenize(argument);}
        catch(IsInvalidException e) { return;}
        System.out.println("argument = "+argument);
        System.out.println("size = "+ft.size());
        while (ft.hasMoreElements()) System.out.println(ft.nextElement());
    }
}
