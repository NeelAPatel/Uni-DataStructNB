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

    
    public void buildSymbols() {
		StringTokenizer variables = new StringTokenizer(expr, " \t*+-/()");
		scalars = new ArrayList<ScalarSymbol>();
		arrays = new ArrayList<ArraySymbol>();

		while (variables.hasMoreTokens()) {
			String token = variables.nextToken();
			if (token.contains("[")) {
				token = token.replaceAll("\\[", "\\&\\[");
				StringTokenizer inside = new StringTokenizer(token, "][");
				while (inside.hasMoreTokens()) {
					String insideToken = inside.nextToken();
					if (insideToken.contains("&")) {
						insideToken = insideToken.replaceAll("\\&", "");
						if (Character.isLetter(insideToken.charAt(0))) {
							ArraySymbol newArraySymbol = new ArraySymbol(insideToken);
							if (!(arrays.contains(newArraySymbol))) {
								arrays.add(newArraySymbol);
							}
						}
					} else {
						if (Character.isLetter(insideToken.charAt(0))) {
							ScalarSymbol newScalarSymbol = new ScalarSymbol(insideToken);
							if (!(scalars.contains(newScalarSymbol))) {
								scalars.add(newScalarSymbol);
							}
						}
					}
				}
			} else {
				StringTokenizer inside = new StringTokenizer(token, "[]");
				while (inside.hasMoreTokens()) {
					String insideToken = inside.nextToken();
					if ((Character.isLetter(insideToken.charAt(0)))) {
						ScalarSymbol newScalarSymbol = new ScalarSymbol(insideToken);
						if (!(scalars.contains(newScalarSymbol))) {
							scalars.add(newScalarSymbol);
						}
					}
				}
			}
		}
	}
    
    
    
    
    
    
    
    /**
     * Populates the scalars and arrays lists with symbols for scalar ad array
     * variables in the expression. For every variable, a SINGLE symbol is created and stored,
     * even if it appears more than once in the expression.
     * At this time, values for all variables are set to
     * zero - they will be loaded from a file in the loadSymbolValues method.
     * (varx + vary*varz[(vara+varb[(a+b)*33])])/55
     */
    public void buildSymbols2() {
    	//System.out.println("\n\n ====== MY CODE ========");
    	//Initialize ArrayLists
    	scalars = new ArrayList<ScalarSymbol>();
    	arrays = new ArrayList<ArraySymbol>();
    	
    	String myexpr = expr;
    	StringTokenizer st = new StringTokenizer(myexpr,"\t*/+-() ]");
    	// Only Square Brackets remain
    	
        while (st.hasMoreTokens()){
        	String vars = st.nextToken();
            if (!(vars.contains("["))){
            	StringTokenizer inside = new StringTokenizer(vars);
				while (inside.hasMoreTokens()){
					String insideToken = inside.nextToken();
					if ((Character.isLetter(insideToken.charAt(0))))
						if (!(scalars.contains(new ScalarSymbol(insideToken))))
						{
							//System.out.println(insideToken);
							scalars.add(new ScalarSymbol(insideToken));
						}
				}
    		}
            else
            {
            	vars = vars.replaceAll("\\[", "\\|");  // Signifies where Array variables end  ex: arrVar|????]
            	StringTokenizer inSquareBrackets = new StringTokenizer(vars, "]"); // no need for ]
            	while (inSquareBrackets.hasMoreTokens()){
            		String iSBTokenized = inSquareBrackets.nextToken();
					//System.out.println(iSBTokenized);
					if (iSBTokenized.contains("|")){
						iSBTokenized = iSBTokenized.replaceAll("\\|", "");
						if (Character.isLetter(iSBTokenized.charAt(0)))
							if (!(arrays.contains(new ArraySymbol(iSBTokenized))))
								arrays.add(new ArraySymbol(iSBTokenized));
					}
					else
						if (Character.isLetter(iSBTokenized.charAt(0))) 
							if (!(scalars.contains(new ScalarSymbol(iSBTokenized))))
							{
								//System.out.println(iSBTokenized);
								scalars.add(new ScalarSymbol(iSBTokenized));
							}
            	}
            }
        }

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
    	expr = removeExtraSpaces();
    	
    	
    	System.out.println("\n\nNO SPACES: " + expr);
    	Stack <Integer> LBRIndex = new Stack<Integer>();
    	Stack <Integer> RBRIndex = new Stack<Integer>();
    	
    	//Find First Occurance of ) or ]
    	int rBracketFirstOccurence = 0;
    	for (int i = 0; i < expr.length(); i++)
    		if (expr.charAt(i) == ')' || expr.charAt(i) == ']'){
    			rBracketFirstOccurence = i;
    			break;
    		}
    	System.out.println("first occurence at [" + rBracketFirstOccurence +"]");
    	// add to stacks
    	
    	//Left stacks
    	for (int i = rBracketFirstOccurence-1 ; i >= 0; i--)
    	{
    		if (expr.charAt(i) == '(' || expr.charAt(i) == '['){
    			
    			LBRIndex.push(i);
    			System.out.println("Pushed LBR index: " + i);
    		}
    	}
    	
    	for (int i = rBracketFirstOccurence ; i < expr.length(); i++)
    	{
    		if (expr.charAt(i) == ')' || expr.charAt(i) == ']'){
    			RBRIndex.push(i);
    			System.out.println("Pushed RBR index: " + i);
    		}
    	}

    	
    	
    	
    	
    	System.out.println();
    	String modifiedExpr = expr;
    	return calculate (modifiedExpr,LBRIndex, RBRIndex);
    }
    
    
    
    private float calculate (String modExpr, Stack <Integer> LBRIndex, Stack <Integer> RBRIndex)
    {
    	System.out.println("========== Calculate ============ ");
    	
    	String inExp = "";
    	float finalAnswer = 0;
    	//(a + A[a*2-b])
    	if (!(LBRIndex.isEmpty())){
    	int lbr = LBRIndex.pop();
    	int rbr = RBRIndex.pop();
    	//int prev 
    	
    	inExp = expr.substring(lbr+1, rbr);
    	System.out.println("expr: " + expr);
    	System.out.println("inExp: " + inExp);
    	
    	}
    	else
    	{
    		inExp = expr;
    	}
    	if (inExp.contains("(") || inExp.contains("[") || inExp.contains("]") || inExp.contains(")")) 
    	{ 
    		 
    		 finalAnswer += calculate(modExpr,LBRIndex,RBRIndex);
    		 System.out.println("   ===== Answer from inner ====");
    		 System.out.println(expr);
    		 modExpr = modExpr.replace(inExp, ""+finalAnswer);
    		 System.out.println(modExpr);
    		 System.out.println(inExp);
    		 
    	}
    	// Does not contain brackets
    		System.out.println("        ============No Brackets==================");
    		
    		StringTokenizer st = new StringTokenizer(inExp,delims, true);
    		
    		String[] vars = tokenizerToArray(st);
    		
    		finalAnswer = solve(inExp, vars);
    		System.out.println("Solution of " + inExp + " is " + finalAnswer);
    		
    		System.out.println("expr : " + expr);
    		modExpr = modExpr.replace(inExp, ""+finalAnswer);
    		System.out.println("modExpr: " + modExpr);
    		System.out.println("inExp : " + inExp);
    		//replace inExp PART in expr with finalAnswer
    		String newExpr = expr.substring(0, expr.indexOf(inExp)) + finalAnswer;
    		
    		newExpr = newExpr + expr.substring((newExpr.indexOf(""+finalAnswer) + ("" + finalAnswer).length() +2));
    		System.out.println(newExpr);
    		//expr = newExpr;
    		
	   		 if (modExpr.charAt(modExpr.indexOf(""+finalAnswer)-1) == '[')
	   		 {
	   			 StringTokenizer tokens = new StringTokenizer(modExpr,delims,true);
	   			
	   			 
	   			ArrayList<String> tempvars = new ArrayList<String>();
	   			while (tokens.hasMoreTokens())
	   				tempvars.add(tokens.nextToken());
	   			String[] vars2 = new String[tempvars.size()];
	   			int a = 0;
	   			for (String x : tempvars)
	   			{
	   				vars2[a] = x;
	   				a++;
	   			}
	   			 
	   			 
	      		
	   			modExpr = arrayFix(vars2,finalAnswer);
	   			finalAnswer += calculate(modExpr,LBRIndex,RBRIndex);
	   		 }
    		System.out.println(modExpr);
    		
    		System.out.println("        =============End of No brackets ==========");
    		return finalAnswer;
    	
    }

    
    private String arrayFix(String[] vars,float finalAnswer)
    {
    	
    	System.out.println("======= Array Fix =========");
    	 // returns one character behind final answer expr.charAt(expr.indexOf("" + finalAnswer)-1)
    	//int sbFinalAnswer = expr.indexOf("" + finalAnswer) - 1;
    	
    	int finalIndex = 0;
    	
    	for(int i = 0; i < vars.length ; i++)
    	{
    		if(vars[i].equals("" + finalAnswer))
    		{
    			finalIndex = i;
    			break;
    		}
    	}
    	System.out.println(finalIndex);
    	boolean isStartAffected= false;
    	if (finalIndex-2 == 0)
    		isStartAffected = true;
    	
    	
    	System.out.println(vars[finalIndex-2]);
    	
    	
    	int truncFinalIndex = (int) finalAnswer;
    	
    	int arrayAns = 0;
			for (ArraySymbol x : arrays)
			{
				System.out.println(x.name);
				if ((x.name).equals(vars[finalIndex-2]))
				{
					System.out.println("WORKS");
					arrayAns = x.values[truncFinalIndex];
				}
			}
			
			
			
			System.out.println(arrayAns);
			
			
			String[] newArr = new String[vars.length-3];
    		
    		if(isStartAffected)
    		{
    			newArr[0] = "" + arrayAns;
    			System.arraycopy(vars, finalIndex+2, newArr, 1,vars.length-3);	
    		}
    		else
    		{
    			System.arraycopy(vars, 0, newArr, 0, finalIndex-2);
    			newArr[finalIndex-2] = "" + arrayAns;
    			System.arraycopy(vars, finalIndex+2, newArr, finalIndex-1, newArr.length-finalIndex +1);
    		} 
			
			
    		vars = newArr;
			String ansExp = "";
			// PRINTS VALUES WITH VALUE
			System.out.print("Vars w/ value: ");
			for (int x = 0; x < vars.length; x++)
			{
				System.out.print(vars[x]);
				ansExp += vars[x];
			}
				
			System.out.println();
    	
    	
    	
			System.out.println("======= End Array Fix =========");
    	return ansExp;
    }
    
    
    private String[] tokenizerToArray(StringTokenizer st) {
		// TODO Auto-generated method stub
    	
    	//Make an array of strings  and store each value
		ArrayList<String> tempvars = new ArrayList<String>();
		while (st.hasMoreTokens())
			tempvars.add(st.nextToken());
		String[] vars = new String[tempvars.size()];
		int a = 0;
		for (String x : tempvars)
		{
			vars[a] = x;
			a++;
		}
		
		// PRINTS CURRENT VARIABLES
		System.out.print("Current vars : ");
		for (int x = 0; x < vars.length; x++)
			System.out.print(vars[x]);
		System.out.println();
	
	
			// Assign values to vars string array
			for (int index = 0; index < vars.length; index++)
			{
				for (ScalarSymbol x : scalars)
				{
					if (vars[index].equals(x.name))
					{
						vars[index] = "" + x.value;
					}
				}
			}
		
		
		// PRINTS VALUES WITH VALUE
		System.out.print("Vars w/ value: ");
		for (int x = 0; x < vars.length; x++)
			System.out.print(vars[x]);
		System.out.println();
		
		return vars;
	}

	private float solve (String inExp, String[] x)
    {
    	
    	System.out.println("           ========== Solve ============");
    	String[] vars = x;
    	float answer = 0;
    	//boolean squarebracket = expr.charAt(expr.indexOf(inExp)-2) == '[';
    	System.out.println(inExp);
    	System.out.println(expr);
    	int leftIndex = expr.indexOf(inExp) - 1;
    	int rightIndex = expr.indexOf(inExp) + inExp.length();
    	System.out.println("Left Index = " + leftIndex + "    " + expr.charAt(leftIndex));
    	System.out.println("Right Index = " + rightIndex + "    "+ expr.charAt(rightIndex));
    	
    	//Calculate w/ order of ops
		// ==================================================== MULTIPLY DIVIDE ============
		while (inExp.contains("*") || inExp.contains("/"))
		{
			float singleAns = 0;
			boolean isStartAffected = false;
			int symbolIndex = 0;
			
    		for (int index = 0; index < vars.length; index ++)
    		{
    			if (vars[index].equals("*")){
    				// calculate answer
    				//System.out.print(vars[index-1] + " | " + vars[index+1] + " error \n");
    				singleAns = (Float.parseFloat(vars[index-1]) * Float.parseFloat(vars[index+1]));
    				System.out.println("Multiply " + vars[index-1] + vars[index] + vars[index+1] + " = " + singleAns);
    				symbolIndex = index;
    				if (index-1 == 0)
    					isStartAffected = true;
    				break;
    			} else if (vars[index].equals("/"))
    			{
    				singleAns = (Float.parseFloat(vars[index-1]) / Float.parseFloat(vars[index+1]));
    				System.out.println("Divide " + vars[index-1] + vars[index] + vars[index+1] + " = " + singleAns);
    				symbolIndex = index;
    				if (index-1 == 0)
    					isStartAffected = true;
    				break;
    			}
    		}
    		
    		String[] newArr = new String[vars.length-2];
    		
    		if(isStartAffected)
    		{
    			newArr[0] = "" + singleAns;
    			System.arraycopy(vars, symbolIndex+2, newArr, 1,vars.length-3);	
    		}
    		else
    		{
    			System.arraycopy(vars, 0, newArr, 0, symbolIndex-1);
    			newArr[symbolIndex-1] = "" + singleAns;
    			System.arraycopy(vars, symbolIndex+2, newArr, symbolIndex, newArr.length-symbolIndex);
    		} 
    		
    		//singleAns ==> full
    		answer = singleAns;
    		
    		//convert array to inExp
    		inExp = "";
    		for(int i = 0; i < newArr.length; i++)
    		{
    			inExp += "" + newArr[i];
    		}
    		
    		vars = newArr;
    		
		}
		
		
		while (inExp.contains("+") || inExp.contains("-"))
		{
			float singleAns = 0;
			boolean isStartAffected = false;
			int symbolIndex = 0;
			
    		for (int index = 0; index < vars.length; index ++)
    		{
    			if (vars[index].equals("+")){
    				// calculate answer
    				singleAns = (Float.parseFloat(vars[index-1]) + Float.parseFloat(vars[index+1]));
    				System.out.println("Add " + vars[index-1] + vars[index] + vars[index+1] + " = " + singleAns);
    				symbolIndex = index;
    				if (index-1 == 0)
    					isStartAffected = true;
    				break;
    			} else if (vars[index].equals("-"))
    			{
    				singleAns = (Float.parseFloat(vars[index-1]) - Float.parseFloat(vars[index+1]));
    				System.out.println("Subtract " + vars[index-1] + vars[index] + vars[index+1] + " = " + singleAns);
    				symbolIndex = index;
    				if (index-1 == 0)
    					isStartAffected = true;
    				break;
    			}
    		}
    		
    		String[] newArr = new String[vars.length-2];
    		
    		
    		if(isStartAffected)
    		{
    			newArr[0] = "" + singleAns;
    			if(newArr.length >= 3)
    				System.arraycopy(vars, symbolIndex+2, newArr, 1,vars.length-3);	
    		}
    		else
    		{
    			newArr[symbolIndex-1] = "" + singleAns;
    			if(newArr.length >= 3) 
    			{
    				System.arraycopy(vars, 0, newArr, 0, symbolIndex-1);
	    			System.arraycopy(vars, symbolIndex+2, newArr, symbolIndex, newArr.length-symbolIndex);	
    			}
    			
    			
    		}
    		//singleAns ==> full
    		answer = singleAns;
    		
    		//convert array to inExp
    		inExp = "";
    		for(int i = 0; i < newArr.length; i++)
    		{
    			inExp += "" + newArr[i];
    		}
    		
    		
    		
    		vars = newArr;
    		
		}
		
		
		System.out.println("Answer of inExp = " + answer + "\n           =====EndofSolve====");
		//expr = expr.substring(0, leftIndex+1) + answer + expr.substring(rightIndex);
		//System.out.println("expr inside solve: " + expr);
		

		
		//System.out.println("Possible Replacement: " +expr.substring(0, expr.expr.indexOf(vars[0])) + answer);
		
		
		
		return answer;
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
