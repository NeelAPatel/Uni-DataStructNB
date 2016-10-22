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
	 * String containing all delimiters (characters other than variables and
	 * constants), to be used with StringTokenizer
	 */
	public static final String delims = " \t*+-/()[]";

	/**
	 * Initializes this Expression object with an input expression. Sets all
	 * other fields to null.
	 * 
	 * @param expr
	 *            Expression
	 */
	public Expression(String expr) {
		this.expr = expr;
		System.out.println("Input: " + expr);
	}

	/**
	 * Populates the scalars and arrays lists with symbols for scalar ad array
	 * variables in the expression. For every variable, a SINGLE symbol is
	 * created and stored, even if it appears more than once in the expression.
	 * At this time, values for all variables are set to zero - they will be
	 * loaded from a file in the loadSymbolValues method. (varx +
	 * vary*varz[(vara+varb[(a+b)*33])])/55
	 */
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
	 * Loads values for symbols in the expression
	 * 
	 * @param sc
	 *            Scanner for values input
	 * @throws IOException
	 *             If there is a problem with the input
	 */
	public void loadSymbolValues(Scanner sc) throws IOException {
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
					StringTokenizer stt = new StringTokenizer(tok, " (,)");
					int index = Integer.parseInt(stt.nextToken());
					int val = Integer.parseInt(stt.nextToken());
					asymbol.values[index] = val;
				}
			}
		}
	}

	private String removeExtraSpaces() {
		StringTokenizer split = new StringTokenizer(expr, " ");
		String exp = "";
		while (split.hasMoreTokens()) {
			String x = split.nextToken();
			exp += x;
		}
		// exp = "(" + expr + ")";
		return exp;
	}

	/**
	 * Evaluates the expression, using RECURSION to evaluate subexpressions and
	 * to evaluate array subscript expressions.
	 * 
	 * @return Result of evaluation
	 */
	public float evaluate() {
		// Expression w/o extra spaces
		expr = removeExtraSpaces();
		System.out.println(expr);

		System.out.println("\n\nNO SPACES: " + expr);
		Stack<Integer> LBRIndex = new Stack<Integer>();
		Stack<Integer> RBRIndex = new Stack<Integer>();
		Stack<Integer> depth = new Stack<Integer>();
		// Find First Occurance of ) or ]
		int rBracketFirstOccurence = 0;
		for (int i = 0; i < expr.length(); i++)
			if (expr.charAt(i) == ')' || expr.charAt(i) == ']') {
				rBracketFirstOccurence = i;
				break;
			}
		System.out.println("first occurence at [" + rBracketFirstOccurence + "]");

		// Left stacks
		for (int i = rBracketFirstOccurence - 1; i >= 0; i--) {
			if (expr.charAt(i) == '(') {

				LBRIndex.push(i);
				System.out.println("Pushed LBR index: " + i);
			}
		}

		for (int i = rBracketFirstOccurence; i < expr.length(); i++) {
			if (expr.charAt(i) == ')') {
				RBRIndex.push(i);
				System.out.println("Pushed RBR index: " + i);
			}
		}

		StringTokenizer st = new StringTokenizer(expr, delims, true);
		ArrayList<String> tempvars = new ArrayList<String>();
		while (st.hasMoreTokens())
			tempvars.add(st.nextToken());
		String[] vars = new String[tempvars.size()];
		int a = 0;
		for (String x : tempvars) {
			vars[a] = x;
			a++;
		}

		for (int index = 0; index < vars.length; index++) {
			for (ScalarSymbol x : scalars) {
				if (vars[index].equals(x.name)) {
					vars[index] = "" + x.value;
				}
			}
		}
		expr = "";
		for (int index = 0; index < vars.length; index++) {
			expr += vars[index];
		}

		System.out.println(expr);
		String modifiedExpr = expr;
		return calculate(modifiedExpr, LBRIndex, RBRIndex);
		// return 0;
	}

	private float calculate(String inExp, Stack<Integer> LBRIndex, Stack<Integer> RBRIndex) {
		System.out.println("========== Calculate ======== ");

		float finalAnswer = 0;
		int lbr, rbr;
		
		if (inExp.contains("(") && (!(LBRIndex.isEmpty()))) {

			lbr = LBRIndex.pop();
			rbr = RBRIndex.pop();
			System.out.println("popped" + lbr + " | " + rbr);
			inExp = inExp.substring(lbr+1, rbr);
		} else {
			lbr = 0;
			rbr = expr.length();
			System.out.println(inExp);
			//inExp = inExp.substring(lbr, rbr-1);
		}

		System.out.println("expr   : " + expr);
		System.out.println("inExp  : " + inExp);
		
		
		
		
		
		
		
		
		
		
		
		//TEST CASES
		
		
		if (isNumeric(expr))
			return Float.parseFloat(expr);
		
		if (inExp.indexOf("(") == -1 && inExp.indexOf("[") == -1)  // FALSE FALSE
		{
			System.out.println("===Case 1");
			System.out.println("1expr   : " + expr);
			System.out.println("1inExp  : " + inExp);
			finalAnswer = solve(inExp);
			
			System.out.println("Returned from first TestCase");
			return finalAnswer;
		}
		else if (!(inExp.indexOf("[") == -1) && inExp.indexOf("(") == -1)
		{
			
			System.out.println("===  Case 2");
			String oriinExp = inExp;
			System.out.println("3expr   : " + expr);
			System.out.println("3inExp  : " + inExp);
			System.out.println("3OriInExp"  + oriinExp);
			
			//int leftbracket = inExp.indexOf("[");
			inExp = fixSquareBrackets(inExp);
			System.out.println(inExp);
			int left = 0;
			if (expr.indexOf(oriinExp)-1 < 0)
			{
				left = 0;
			}
			else
			{
				left = expr.indexOf(oriinExp)-1;
			}
			
			//int right = 0;
			
			
//			System.out.println(expr.charAt(expr.indexOf(oriinExp)-1));
//			System.out.println(expr.substring(expr.indexOf(oriinExp)+oriinExp.length()+1));
//			System.out.println(left + inExp + expr.substring(expr.indexOf(oriinExp)+oriinExp.length()));
//			
			
			inExp = "" + solve(inExp);
			
			String newinExp = expr.substring(0, left) + inExp;
			newinExp = newinExp + expr.substring(newinExp.length()+1);
			
			
			System.out.println("inExp in ThirdTestCase = " + inExp);
			return calculate(newinExp,LBRIndex,RBRIndex);
		}
		else if (inExp.contains("(") ||inExp.contains(")"))    // TRUE 
 
		{
			System.out.println("===Case 3");
			System.out.println("2expr   : " + expr);
			System.out.println("2inExp  : " + inExp);
			//ystem.out.println("SecondCase : inExp : "+inExp);
			
			inExp = inExp.substring(lbr+1, rbr);
			System.out.println("Returned from second TestCase");
			return calculate(inExp, LBRIndex, RBRIndex);
		}
		
		// Does not contain brackets
		System.out.println(">>>======End of No brackets ==========");
		System.out.println("================== END OF CALCULATION ========");
		
		return finalAnswer;

	}

	private String fixSquareBrackets(String inExp)
	{
		while (inExp.contains("[") || inExp.contains("]")) // Array value
			// exists.
		{
		String originalinExp = inExp;
		int indexInnerMostArrayName = 0;
		int indexInnerMostLSB = 0;
		int indexValueOfSB = 0;
		int indexInnerMostRSB = 0;
		boolean difference = false;
		
		for (int i = 0; i < inExp.length(); i++)
		if (inExp.charAt(i) == '[') {
		indexInnerMostLSB = i;
		for (int j = i; j < inExp.length(); j++)
		if (inExp.charAt(j) == ']') {
		indexInnerMostRSB = j;
		break;
		}
		}
		
		if ((indexInnerMostRSB - indexInnerMostLSB) > 2) {
		difference = true;
		}
		if (difference) {
		
		String result;
		// System.out.println("UH OHH");
		// //System.out.println(inExp.substring(indexInnerMostLSB+1,
		// indexInnerMostRSB));
		// System.out.println("Original inExp" + originalinExp);
		
		float ans = solve(inExp.substring(indexInnerMostLSB + 1, indexInnerMostRSB));
		String temp = inExp.substring(0, indexInnerMostLSB + 1) + ans + inExp.charAt(indexInnerMostRSB);
		// System.out.println("temp " + temp );
		// System.out.println("UH OHHOVER");
		// System.out.println("Original inExp" + originalinExp);
		inExp = temp;
		// break;
		// Solve expression inside
		// fix inExp with result
		
		}
		
		indexInnerMostArrayName = indexInnerMostLSB - 1;
		indexValueOfSB = indexInnerMostLSB + 1;
		// System.out.println(inExp.substring(indexInnerMostLSB-1,indexInnerMostRSB+1));
		
		// Convert token ==> Array
		StringTokenizer st = new StringTokenizer(inExp, delims, true);
		ArrayList<String> stAL = new ArrayList<String>();
		while (st.hasMoreTokens())
		stAL.add(st.nextToken());
		String[] inExprArr = new String[stAL.size()];
		int a = 0;
		for (String x : stAL) {
		inExprArr[a] = x;
		a++;
		}
		
		int arrayAns = 0;
		// System.out.println("Name: " +
		// inExprArr[indexInnerMostArrayName]);
		for (ArraySymbol x : arrays) {
		// System.out.println(x.name);
		if ((x.name).equals(inExprArr[indexInnerMostArrayName])) {
		// System.out.println("WORKS");
		arrayAns = x.values[(int) Float.parseFloat((inExprArr[indexValueOfSB]))];
		}
		}
		
		// At this point arrayAns = value of B[2]
		// System.out.println("Array ans of"
		// +inExp.substring(indexInnerMostLSB-1,indexInnerMostRSB+1) + " = "
		// + arrayAns );
		String newinExpr = "";
		int a1 = 0;
		for (a1 = 0; a1 < inExprArr.length; a1++) {
			if (a1 == (indexInnerMostArrayName)) {
					newinExpr += arrayAns;
					a1 += 3;
				} 
				else
				newinExpr += inExprArr[a1];
				
		}
//		System.out.println(newinExpr);
//		
//		if(inExprArr.length > 1){
//			float answer  = solve(newinExpr);
//			inExp = "" +answer; 
//		}
		inExp = newinExpr;
		
		} // END OF WHILE
		
		return inExp;
	}
	
	
	public static boolean isNumeric(String str) {
		try {
			float d = Float.parseFloat(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	private float solve(String inExp) {

		System.out.println("           ========== Solve ============");

		StringTokenizer st = new StringTokenizer(inExp, delims, true);
		ArrayList<String> tempvars = new ArrayList<String>();
		while (st.hasMoreTokens())
			tempvars.add(st.nextToken());
		String[] vars = new String[tempvars.size()];
		int a = 0;
		for (String x : tempvars) {
			vars[a] = x;
			a++;
		}

		for (int index = 0; index < vars.length; index++) {
			for (ScalarSymbol x : scalars) {
				if (vars[index].equals(x.name)) {
					vars[index] = "" + x.value;
				}
			}
		}

		float answer = 0;
		// boolean squarebracket = expr.charAt(expr.indexOf(inExp)-2) == '[';
		System.out.println("inExp: " + inExp);
		System.out.println(expr);

		System.out.println("inExp: " + inExp + "\nVARS: ");
		for (int x1 = 0; x1 < vars.length; x1++) {
			System.out.print(vars[x1]);
		}
		System.out.println();

		String calcExp = inExp;
		System.out.println("Before everything: " + inExp);
		System.out.println("Before Multiply/Divide : " + calcExp);
		// Calculate w/ order of ops
		// ==================================================== MULTIPLY DIVIDE
		// ============
		while (calcExp.contains("*") || calcExp.contains("/")) {
			System.out.println("multiply " + calcExp);
			float singleAns = 0;
			boolean isStartAffected = false;
			int symbolIndex = 0;

			for (int index = 0; index < vars.length; index++) {
				if (vars[index].equals("*")) {

					singleAns = (Float.parseFloat(vars[index - 1]) * Float.parseFloat(vars[index + 1]));
					System.out
							.println("Multiply " + vars[index - 1] + vars[index] + vars[index + 1] + " = " + singleAns);
					symbolIndex = index;
					if (index - 1 == 0)
						isStartAffected = true;
					break;
				} else if (vars[index].equals("/")) {
					singleAns = (Float.parseFloat(vars[index - 1]) / Float.parseFloat(vars[index + 1]));
					System.out.println("Divide " + vars[index - 1] + vars[index] + vars[index + 1] + " = " + singleAns);
					symbolIndex = index;
					if (index - 1 == 0)
						isStartAffected = true;
					break;
				}
			}

			String[] newArr = new String[vars.length - 2];

			if (isStartAffected) {
				newArr[0] = "" + singleAns;
				System.arraycopy(vars, symbolIndex + 2, newArr, 1, vars.length - 3);
			} else {
				System.arraycopy(vars, 0, newArr, 0, symbolIndex - 1);
				newArr[symbolIndex - 1] = "" + singleAns;
				System.arraycopy(vars, symbolIndex + 2, newArr, symbolIndex, newArr.length - symbolIndex);
			}

			// singleAns ==> full
			answer = singleAns;

			// convert array to calcExp
			calcExp = "";
			for (int i = 0; i < newArr.length; i++) {
				calcExp += "" + newArr[i];
			}

			vars = newArr;

		}
		System.out.println("After Multiply/Divide : " + calcExp);
		System.out.println("Before Add/Substract : " + calcExp);
		while (calcExp.contains("+") || calcExp.contains("-")) {

			float singleAns = 0;
			boolean isStartAffected = false;
			int symbolIndex = 0;

			for (int index = 0; index < vars.length; index++) {
				if (vars[index].equals("+")) {
					// calculate answer
					singleAns = (Float.parseFloat(vars[index - 1]) + Float.parseFloat(vars[index + 1]));
					System.out.println("Add " + vars[index - 1] + vars[index] + vars[index + 1] + " = " + singleAns);
					symbolIndex = index;
					if (index - 1 == 0)
						isStartAffected = true;
					break;
				} else if (vars[index].equals("-")) {
					singleAns = (Float.parseFloat(vars[index - 1]) - Float.parseFloat(vars[index + 1]));
					System.out
							.println("Subtract " + vars[index - 1] + vars[index] + vars[index + 1] + " = " + singleAns);
					symbolIndex = index;
					if (index - 1 == 0)
						isStartAffected = true;
					break;
				}
			}

			String[] newArr = null;
			if (vars.length - 2 > 0) {
				newArr = new String[vars.length - 2];

				if (isStartAffected) {
					newArr[0] = "" + singleAns;
					if (newArr.length >= 3)
						System.arraycopy(vars, symbolIndex + 2, newArr, 1, vars.length - 3);
				} else {
					newArr[symbolIndex - 1] = "" + singleAns;
					if (newArr.length >= 3) {
						System.arraycopy(vars, 0, newArr, 0, symbolIndex - 1);
						System.arraycopy(vars, symbolIndex + 2, newArr, symbolIndex, newArr.length - symbolIndex);
					}

				}

				// convert array to calcExp
				calcExp = "";
				for (int i = 0; i < newArr.length; i++) {
					calcExp += "" + newArr[i];
				}
			} else {
				newArr = new String[1];
				newArr[0] = "" + singleAns;
			}

			answer = singleAns;
			System.out.println(calcExp);

			vars = newArr;
			
			
			if (isNumeric(calcExp))
			{
				break;
			}

		}

		System.out.println("aftereverything: " + inExp);

		System.out.println("Answer of calcExp = " + answer + "\n           =====EndofSolve====");

		return answer;
	}

//	private boolean isNegativeNumber(String str) {
//		try {
//			float d = Float.parseFloat(str);
//		} catch (NumberFormatException nfe) {
//			return false;
//		}
//		return true;
//		return false;
//	}

	/**
	 * Utility method, prints the symbols in the scalars list
	 */
	public void printScalars() {
		for (ScalarSymbol ss : scalars) {
			System.out.println(ss);
		}
	}

	/**
	 * Utility method, prints the symbols in the arrays list
	 */
	public void printArrays() {
		for (ArraySymbol as : arrays) {
			System.out.println(as);
		}
	}

}
