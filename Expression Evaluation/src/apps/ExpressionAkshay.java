package apps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

import structures.Stack;

public class ExpressionAkshay {

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
	 * Positions of opening brackets
	 */
	ArrayList<Integer> openingBracketIndex;

	/**
	 * Positions of closing brackets
	 */
	ArrayList<Integer> closingBracketIndex;

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
	public ExpressionAkshay(String expr) {
		this.expr = expr;
		scalars = null;
		arrays = null;
		openingBracketIndex = null;
		closingBracketIndex = null;
	}

	/**
	 * Matches parentheses and square brackets. Populates the
	 * openingBracketIndex and closingBracketIndex array lists in such a way
	 * that closingBracketIndex[i] is the position of the bracket in the
	 * expression that closes an opening bracket at position
	 * openingBracketIndex[i]. For example, if the expression is:
	 * 
	 * <pre>
	 * (a + (b - c)) * (d + A[4])
	 * </pre>
	 * 
	 * then the method would return true, and the array lists would be set to:
	 * 
	 * <pre>
	 * openingBracketIndex: [0 3 10 14] closingBracketIndex: [8 7 17 16] </pe>
	 *
	 * See the FAQ in project description for more details.
	 *
	 * @return True if brackets are matched correctly, false if not
	 */
	public boolean isLegallyMatched() {
		Stack<Bracket> stk = new Stack<Bracket>();
		openingBracketIndex = new ArrayList<Integer>();
		closingBracketIndex = new ArrayList<Integer>();

		for (int i = 0; i < expr.length(); i++) {
			Bracket ch1 = new Bracket(expr.charAt(i), i);
			if (ch1.ch == '(' || ch1.ch == '[') {
				stk.push(ch1);
			} else if (ch1.ch == ')' || ch1.ch == ']') {
				closingBracketIndex.add(ch1.pos);
				try {
					Bracket ch2 = stk.pop();
					if (ch2.ch == '(' && ch1.ch == ')') {
						openingBracketIndex.add(ch2.pos);
						continue;
					}
					if (ch2.ch == '[' && ch1.ch == ']') {
						openingBracketIndex.add(ch2.pos);
						continue;
					}
					return false;
				} catch (NoSuchElementException e) {
					return false;
				}
			}
		}

		for (int x = 0; x < openingBracketIndex.size(); x++) {
			for (int y = 0; y < openingBracketIndex.size() - 1 - x; y++) {
				if ((openingBracketIndex.get(y).compareTo(openingBracketIndex.get(y + 1)) > 0)) {
					int swapInteger = openingBracketIndex.get(y);
					openingBracketIndex.set(y, openingBracketIndex.get(y + 1));
					openingBracketIndex.set(y + 1, swapInteger);
					int swapInteger2 = closingBracketIndex.get(y);
					closingBracketIndex.set(y, closingBracketIndex.get(y + 1));
					closingBracketIndex.set(y + 1, swapInteger2);
				}
			}
		}
		return stk.isEmpty();
	}

	/**
	 * Populates the scalars and arrays lists with symbols for scalar and array
	 * variables in the expression. For every variable, a SINGLE symbol is
	 * created and stored, even if it appears more than once in the expression.
	 * At this time, the constructors for ScalarSymbol and ArraySymbol will
	 * initialize values to zero and null, respectively. The actual values will
	 * be loaded from a file in the loadSymbolValues method.
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

	/**
	 * Evaluates the expression, using RECURSION to evaluate subexpressions and
	 * to evaluate array subscript expressions. (Note: you can use one or more
	 * private helper methods to implement the recursion.)
	 *
	 * @return Result of evaluation
	 */
	public float evaluate() {
		int bracketCount = 0;
		return evaluate(expr, expr.length() - 1, bracketCount);
	}

	public float evaluate(String expression, int endIndex, int bracketCount) {
		Stack<String> operators = new Stack<String>();
		Stack<Float> numbers = new Stack<Float>();
		StringTokenizer breakup = new StringTokenizer(expression, delims, true);
		String token;
		float constant = 0;
		float scalarVal = 0;
		float bracketVal = 0;
		float parenSol = 0;
		float arrayLoc = 0;
		String letters = "";

		while (breakup != null) {
			if (!(breakup.hasMoreTokens())) {
				break;
			}

			token = breakup.nextToken();
			if (token.equals("(") || token.endsWith("[")) {
				int startingBracketIndex = openingBracketIndex.get(bracketCount);
				int endingBracketIndex = closingBracketIndex.get(bracketCount);
				bracketCount++;
				parenSol = evaluate(expr.substring(startingBracketIndex + 1, endingBracketIndex),
						endingBracketIndex - 1, bracketCount);
				if (token.equals("[")) {
					for (int i = 0; i < arrays.size(); i++) {
						if (arrays.get(i).name.equals(letters)) {
							arrayLoc = i;
						}
					}
					int[] arrayvalues = arrays.get((int) arrayLoc).values;
					bracketVal = arrayvalues[(int) parenSol];
					numbers.push(bracketVal);
				} else {
					numbers.push(parenSol);
				}
				if (endingBracketIndex == endIndex) {
					breakup = null;
				} else {
					breakup = new StringTokenizer(expr.substring(endingBracketIndex + 1, endIndex + 1), delims, true);
				}
			} else if (Character.isLowerCase(token.charAt(0))) {
				letters = token;
				ScalarSymbol newScalarSymbol = new ScalarSymbol(token);
				int scalarSymbolIndex = scalars.indexOf(newScalarSymbol);
				if ((scalarSymbolIndex != -1)) {
					scalarVal = scalars.get(scalarSymbolIndex).value;
					numbers.push(scalarVal);
					checkDivMult(operators, numbers);
				}
			} else if (Character.isUpperCase(token.charAt(0))) {
				letters = token;
			} else if (token.equals("+") || token.equals("-") || token.equals("/") || token.equals("*")) {
				operators.push(token);
			} else if (!(token.equals(" "))) {
				constant = Integer.parseInt(token);
				numbers.push(constant);
				checkDivMult(operators, numbers);
			}
		}
		if (operators.isEmpty()) {
			return numbers.pop();
		}

		Stack<Float> reverseNum = new Stack<Float>();
		Stack<String> reverseOp = new Stack<String>();

		while (!(operators.isEmpty())) {
			reverseOp.push(operators.pop());
		}
		while (!(numbers.isEmpty())) {
			reverseNum.push(numbers.pop());
		}
		while (!(reverseOp.isEmpty())) {
			processStack(reverseOp, reverseNum, false);
		}

		return reverseNum.peek();
	}

	private void checkDivMult(Stack<String> operators, Stack<Float> numbers) {
		if (!operators.isEmpty()) {
			String firstOperator = operators.peek();
			if (firstOperator.equals("/") || firstOperator.equals("*")) {
				processStack(operators, numbers, true);
			}
		}
	}

	private void processStack(Stack<String> operators, Stack<Float> numbers, boolean inOrder) {
		String firstOperator = operators.pop();
		float temp1 = 0;
		float temp2 = 0;
		float solution = 0;

		if (inOrder) {
			temp2 = numbers.pop();
			temp1 = numbers.pop();
		} else {
			temp1 = numbers.pop();
			temp2 = numbers.pop();
		}

		if (firstOperator.equals("/")) {
			solution = temp1 / temp2;
		} else if (firstOperator.equals("*")) {
			solution = temp1 * temp2;
		} else if (firstOperator.equals("+")) {
			solution = temp1 + temp2;
		} else if (firstOperator.equals("-")) {
			solution = temp1 - temp2;
		}

		numbers.push(solution);
	}

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