package apps;

import java.io.*;
import java.util.*;

import structures.Stack;

public class Expression {

	/**
	 * Expression to be evaluated
	 */
	String expr;                
    
	/**
	 * Scalar symbols in the expression 
	 */
	ArrayList<ScalarSymbol> scalars;   
	
	/**
	 * Array symbols in the expression
	 */
	ArrayList<ArraySymbol> arrays;
    
    /**
     * String containing all delimiters (characters other than variables and constants), 
     * to be used with StringTokenizer
     */
    public static final String delims = " \t*+-/()[]";
    
    /**
     * Initializes this Expression object with an input expression. Sets all other
     * fields to null.
     * 
     * @param expr Expression
     */
    public Expression(String expr) {
        this.expr = expr;
        
        System.out.println("Neel: " + expr);
    }

    /**
     * Populates the scalars and arrays lists with symbols for scalar and array
     * variables in the expression. For every variable, a SINGLE symbol is created and stored,
     * even if it appears more than once in the expression.
     * At this time, values for all variables are set to
     * zero - they will be loaded from a file in the loadSymbolValues method.
     * (varx + vary*varz[(vara+varb[(a+b)*33])])/55
     */
    public void buildSymbols() {
    	System.out.println("\n\n ====== MY CODE ========");
    	//Initialize ArrayLists
    	scalars = new ArrayList<ScalarSymbol>();
    	arrays = new ArrayList<ArraySymbol>();
    	
    	String myexpr = expr;
    	StringTokenizer st = new StringTokenizer(myexpr,"\t*/+-() ");
    	// Only Square Brackets remain
    	
        while (st.hasMoreTokens()){
        	String vars = st.nextToken();
            if (!(vars.contains("["))){
            	StringTokenizer inside = new StringTokenizer(vars);
				while (inside.hasMoreTokens()){
					String insideToken = inside.nextToken();
					if ((Character.isLetter(insideToken.charAt(0))))
						if (!(scalars.contains(new ScalarSymbol(insideToken))))
							scalars.add(new ScalarSymbol(insideToken));
				}
    		}
            else
            {
            	vars = vars.replaceAll("\\[", "\\|");  // Signifies where Array variables end  ex: arrVar|????]
            	StringTokenizer inSquareBrackets = new StringTokenizer(vars, "]"); // no need for ]
            	while (inSquareBrackets.hasMoreTokens()){
            		String iSBTokenized = inSquareBrackets.nextToken();
					System.out.println(iSBTokenized);
					if (iSBTokenized.contains("|")){
						iSBTokenized = iSBTokenized.replaceAll("\\|", "");
						if (Character.isLetter(iSBTokenized.charAt(0)))
							if (!(arrays.contains(new ArraySymbol(iSBTokenized))))
								arrays.add(new ArraySymbol(iSBTokenized));
					}
					else
						if (Character.isLetter(iSBTokenized.charAt(0))) 
							if (!(scalars.contains(new ScalarSymbol(iSBTokenized))))
								scalars.add(new ScalarSymbol(iSBTokenized));
            	}
            }
        }//While Loop
        
//        
//        
//        System.out.println();
//        System.out.println(arrays);
//        System.out.println(scalars);
    }
    
    /**
     * Loads values for symbols in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     */
    public void loadSymbolValues(Scanner sc) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String sym = st.nextToken();
            ScalarSymbol ssymbol = new ScalarSymbol(sym);
            ArraySymbol asymbol = new ArraySymbol(sym);
            int ssi = scalars.indexOf(ssymbol);
            int asi = arrays.indexOf(asymbol);
            if (ssi == -1 && asi == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                scalars.get(ssi).value = num;
            } else { // array symbol
            	asymbol = arrays.get(asi);
            	asymbol.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    String tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    asymbol.values[index] = val;              
                }
            }
        }
    }

    private String removeExtraSpaces()
    {
    	StringTokenizer split = new StringTokenizer(expr," ");
    	String exp = "";
    	while(split.hasMoreTokens())
    	{
    		String x = split.nextToken();
    		exp += x;
    	}
    	
    	return exp;
    }
    
    /**
     * Evaluates the expression, using RECURSION to evaluate subexpressions and to evaluate array 
     * subscript expressions.
     * 
     * @return Result of evaluation
     */
    public float evaluate() {
    	//Expression w/o extra spaces
    	String exp = removeExtraSpaces();
    	
    	
    	System.out.println(exp);
    	
    	StringTokenizer entries = new StringTokenizer (exp,delims,true);  //Recieved hint: using ,true = getting symbols back such as + ( as indiv tokens
    	
    	
    	
    	Stack <String> opps = new Stack<String>();  // Will contain + - / *
    	Stack <Double> nums = new Stack<Double>(); // Will contain constants + solved values
    	
    	
    	
    	
    	
    	
    	
    	
    	return 0;
    }
    
    /**
     * Evaluates the expression, using RECURSION to evaluate subexpressions and to evaluate array 
     * subscript expressions.
     * 
     * @return Result of evaluation
     */
    public float evaluate2() {
    		/** COMPLETE THIS METHOD **/
    		// following line just a placeholder for compilation
    String exp = expr;
    
    
    int length = exp.length();
    int index = 0;
    char hold = '|', hold2 = '|';
    int holdIndex = -1, hold2Index = -1;
       
    while (index < length)
    {
    	if (exp.charAt(index) == '(' || exp.charAt(index) == '[')
    	{
    		hold = exp.charAt(index);
    		holdIndex = index;
    		index++;
    		
    		//System.out.println("\n\nIndex: [" + index + "] \n HoldIndex = [" + holdIndex + "]");
    	}
    	else if (hold == '(' && exp.charAt(index) == ')')
    	{
    		hold2 = ')';
    		hold2Index = index;
    		
    		
    		//System.out.println("\n\n A = Index: [" + index + "] \n Hold2Index = [" + hold2Index + "]");
    		String innerExpression = exp.substring(holdIndex+1, hold2Index);
    		System.out.println("Inner Expression: " + innerExpression);
    		// calculate here
    		StringTokenizer st = new StringTokenizer(innerExpression, "\t*/+-");
    		while (st.hasMoreTokens())
    		{
    			String vars = st.nextToken();
    			if (!(vars.contains("(")))
				{
    				int symbolIndex = vars.length();
    				char operation = innerExpression.charAt(symbolIndex);
				}
    		}
    		
    		
    		exp = exp.substring(0, holdIndex) + exp.substring(hold2Index+1); // deletes substring from the main string
    		System.out.println(exp);
    	}
    	else if (hold == '[' && exp.charAt(index) == ']')
    	{
    		hold2 = ']';
    		hold2Index = index;
    		System.out.println("B = Index: [" + index + "] \n Hold2Index = [" + hold2Index + "]");
    		String innerExpression = exp.substring(holdIndex+1, hold2Index);
    		System.out.println(innerExpression);
    		// calculate here
    		StringTokenizer st = new StringTokenizer(innerExpression, "\t*/+-");

    		exp = exp.substring(0, holdIndex) + exp.substring(hold2Index+1); // deletes substring from the main string
    		System.out.println(exp); // deletes substring from the main string
    	}
    	else
    	{
    		
    		
    		index ++;
    	}
    	
    	
    	
    	
    }
    
    	
    		/**
    		 * Find First bracket and hold position  ( , ) , [ , ]
    		 * Find second bracket
    		 * > If  ( or [
    		 * 		then replace First Hold with new position 
    		 * 			hold variable prior to parentheses i.e A*(??)  or A[??]
    		 * 			repeat method
    		 * > else if ) and matches firstHold  OR     ] and matches firstHold
    		 * > 	then Substring expr from First hold to secondHold(Matches first hold)
    		 * >		remove substring from expr  i.e ~~~~<deleted>~~~~
    		 * 			Calculate
    		 * 			repeat method
    		 * 		
    		 */

    		return 0;
    }

    /**
     * Utility method, prints the symbols in the scalars list
     */
    public void printScalars() {
        for (ScalarSymbol ss: scalars) {
            System.out.println(ss);
        }
    }
    
    /**
     * Utility method, prints the symbols in the arrays list
     */
    public void printArrays() {
    		for (ArraySymbol as: arrays) {
    			System.out.println(as);
    		}
    }

}
