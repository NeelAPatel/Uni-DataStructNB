package apps;

import java.io.*;
import java.util.*;

import structures.Stack;

public class Expression2 {

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
	public Expression2(String expr) {
		this.expr = expr;
	}

	/**
	 * Populates the scalars and arrays lists with symbols for scalar and array
	 * variables in the expression. For every variable, a SINGLE symbol is
	 * created and stored, even if it appears more than once in the expression.
	 * At this time, values for all variables are set to zero - they will be
	 * loaded from a file in the loadSymbolValues method.
	 */
	public void buildSymbols() {
		/** COMPLETE THIS METHOD **/
		//
		// // initialize
		// scalars = new ArrayList<ScalarSymbol>();
		// arrays = new ArrayList<ArraySymbol>();
		//
		// // remove white spaces in the expression
		// expr = expr.trim();
		// expr = expr.replaceAll("\\s", "");
		// StringTokenizer tokens = new StringTokenizer(expr, "*+-/()");
		//
		//
		// while (tokens.hasMoreTokens())
		// {
		// String nexttoken = tokens.nextToken();
		// if (nexttoken.contains("["))
		// {
		// // build arraylist arrays
		// StringTokenizer newtoken = new StringTokenizer (nexttoken, "[]");
		//
		// while (newtoken.hasMoreTokens())
		// {
		// String addtoken = newtoken.nextToken();
		// ArraySymbol temparray = new ArraySymbol (addtoken);
		// if (!isNumeric(addtoken))
		// {
		// if (!arrays.contains(temparray))
		// {
		// arrays.add(temparray);
		// }
		// }
		// }
		// }
		// else
		// {
		// // build arraylist scalars
		// // if the token contains "]", then don't add to scalars
		// if (!nexttoken.contains("]"))
		// {
		// // check if the token is a number
		// if (!isNumeric (nexttoken))
		// {
		// // check whether this token already exists
		// ScalarSymbol tempscalar = new ScalarSymbol(nexttoken);
		// if (!scalars.contains(tempscalar))
		// {
		// scalars.add(tempscalar);
		// }
		// }
		// }
		// }
		// }
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

	// checks whether the string is a number
	private boolean isNumeric(String s) {
		return s.matches("[-+]?\\d*\\.?\\d+");
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
	 * to evaluate array subscript expressions.
	 * 
	 * @return Result of evaluation
	 */
	public float evaluate() {
		/** COMPLETE THIS METHOD **/

		// parse the expression
		int openparentheses = 0;
		int closedparentheses = 0;
		int operatortracker = 0;
		String operatordelims = "+-*/";
		String plusminuoperatordelims = "+-";
		String multiplydivideoperatordelims = "*/";
		float result = 0;

		System.out.println("0. expr:" + expr + ":");
		expr = expr.trim();
		expr = expr.replaceAll("\\s", "");

		// Replace all minus signs with their ASCII equivalent &#045
		expr.replaceAll("\\*\\-", "*&#092&#092&#045");
		expr.replaceAll("\\/\\-", "/&#092&#092&#045");
		expr.replaceAll("\\+\\-", "+&#092&#092&#045");
		expr.replaceAll("\\-\\-", "-&#092&#092&#045");

		// clean out nested () if the expr is contained within it
		while (!expr.isEmpty()) {
			if ((expr.startsWith("(")) && (expr.endsWith(")"))) {
				// check if these ( are not part of same level ()'s
				// count the numbers of ( and ) in the expression before last )
				// - if the sum is odd, then you can remove the ()'s
				int parenthesescount = 0;
				for (int count = 0; count < expr.length(); count++) {
					if ((expr.charAt(count) == '('))
					// if ((expr.charAt(count) == '(' ) || (expr.charAt(count)
					// == ')'))

					{
						parenthesescount++;
					}
				}
				System.out.println("parenthesescount:" + parenthesescount);
				if (((parenthesescount & 1) != 0)) {
					System.out.println("263: expr:" + expr);
					expr = expr.substring(1, expr.length() - 1);
					System.out.println("263: expr:" + expr);
				} else {
					break;
				}
			} else {
				break;
			}
		}

		System.out.println("0+1. expr:" + expr + ":");

		// find the matching expression inside of open/closed parentheses
		while (expr.contains("(") || expr.contains("[")) {
			// if the whole expression is within (), string out the () and
			// evaluate again
			// if ((expr.charAt(0) == '(') && (expr.charAt(expr.length()-1) ==
			// ')'))
			// {
			// expr = expr.substring(1,expr.length()-1);
			// break;
			// }
			if ((expr.startsWith("(")) && (expr.endsWith(")"))) {
				// check if these ( are not part of same level ()'s
				// count the numbers of ( and ) in the expression before last )
				// - if the sum is odd, then you can remove the ()'s
				int parenthesescount = 0;
				for (int count = 0; count < expr.length(); count++) {
					if ((expr.charAt(count) == '('))
					// if ((expr.charAt(count) == '(' ) || (expr.charAt(count)
					// == ')'))

					{
						parenthesescount++;
					}
				}
				System.out.println("parenthesescount:" + parenthesescount);
				if (((parenthesescount & 1) != 0)) {
					System.out.println("263: expr:" + expr);
					expr = expr.substring(1, expr.length() - 1);
					System.out.println("263: expr:" + expr);
				}
			}

			// the following code finds the innermost () or []
			for (int i = 0; i < expr.length(); i++) {
				// if(operatordelims.contains(String.valueOf(expr.charAt(i))))
				// {
				// operatortracker = i;
				// }
				if (expr.charAt(i) == '(' || expr.charAt(i) == '[') {
					openparentheses = i;
				} else if (expr.charAt(i) == ')' || expr.charAt(i) == ']') {
					closedparentheses = i;
					break;
				}
			}

			// find the first operator tracker before the open parentheses
			for (int i = 0; i < expr.length(); i++) {
				if (operatordelims.contains(String.valueOf(expr.charAt(i)))) {
					if (i < openparentheses) {
						operatortracker = i;
						System.out.println("345 operatortracker:" + operatortracker);
					}
				}
			}

			System.out.println("309 openparentheses:" + openparentheses);
			System.out.println("closedparentheses:" + closedparentheses);
			System.out.println("operatortracker:" + operatortracker);

			String parenthesesexpr = expr.substring(openparentheses + 1, closedparentheses);
			System.out.println("parenthesesexpr:" + parenthesesexpr);

			// following code simplified expression inside of []
			if (expr.charAt(closedparentheses) == ']') {
				if (!isNumeric(parenthesesexpr)) {

					System.out.println("parenthesesexpr:" + parenthesesexpr);
					String tempexpr = expr;
					expr = parenthesesexpr;
					System.out.println("226 expr:" + expr);

					float interimresult = evaluate();
					System.out.println("interimresult:" + interimresult);
					// return interimresult;
					expr = tempexpr;

					String expression1 = expr.substring(0, openparentheses);
					String expression3 = expr.substring(closedparentheses + 1, expr.length());
					System.out.println("231: expr before:" + expr);
					expr = expression1 + "[" + (int) interimresult + "]" + expression3;
					System.out.println("233: expr after:" + expr);
					// experiment
					result = evaluate();
					return result;
				} else {
					// if inside of [] is numeric, then lookup the array value
					// and update the expr accordingly
					String expression1;
					String expression2 = "";
					String expression3;
					String arrayindex;
					int cellvalue = 0;

					if (operatortracker != 0) {
						// if there is operator prior to array
						System.out.println("310 expr In Here:" + expr + ":");

						// check if its a nested array
						String checkexpression = expr.substring(0, openparentheses - 1);
						System.out.println("350 checkexpression:" + checkexpression + ":");
						if (!(checkexpression.contains("["))) {

							if (!(operatordelims.contains(checkexpression))) {

								// if the expression is reduced and dont have
								// operators anymore
								if ((expr.length() < operatortracker) || (!((expr.charAt(operatortracker) == '+')
										|| (expr.charAt(operatortracker) == '-')
										|| (expr.charAt(operatortracker) == '*')
										|| (expr.charAt(operatortracker) == '/')))) {
									// Operator doesn't exist
									result = evaluate();
									return result;
								}

								expression1 = expr.substring(0, operatortracker + 1);
								arrayindex = expr.substring(openparentheses + 1, closedparentheses);
								Float temp = Float.parseFloat(arrayindex);
								int intarrayindex = Math.round(temp);
								arrayindex = String.valueOf(intarrayindex);
								System.out.println("temp:" + temp + ": intarrayindex:" + intarrayindex);
								// int integerarrayindex =
								// Integer.parseInt(arrayindex);
								System.out.println("400 expr:" + expr);
								System.out.println(": 401 operatortracker:" + operatortracker + ":openparentheses:"
										+ openparentheses + ":" + ": arrayindex:" + arrayindex);
								expression2 = expr.substring(operatortracker + 1, openparentheses);
								expression3 = expr.substring(closedparentheses + 1, expr.length());
								System.out.println("404 expression1:" + expression1 + ":");
								System.out.println("405 expression2:" + expression2 + ":");
								System.out.println("406 expression3:" + expression3 + ":");

								for (ArraySymbol as : arrays) {
									if (as.name.equals(expression2)) {
										// cellvalue = as.values[intarrayindex];
										cellvalue = as.values[Integer.parseInt(arrayindex)];
										// cellvalue = as.values[(int)
										// Float.parseFloat(arrayindex)];
										System.out.println("cellvalue:" + cellvalue + ":");

										break;
									}
								}

								System.out.println("417 expr before:" + expr + ":");
								expr = expression1 + +cellvalue + expression3;
								System.out.println("417 expr after:" + expr + ":");

							} else {

								arrayindex = expr.substring(openparentheses + 1, closedparentheses);
								System.out.println("343 arrayindex:" + arrayindex + ":");
								System.out.println("344 expr:" + expr + ":");
								String innerarrayname = expr.substring(0, openparentheses);
								System.out.println("344 innerarrayname:" + innerarrayname + ":");

								for (ArraySymbol as : arrays) {
									// if (as.name.equals(expr))
									if (as.name.equals(innerarrayname))

									{
										cellvalue = as.values[Integer.parseInt(arrayindex)];
										System.out.println("cellvalue:" + cellvalue + ":");
										break;
										// System.out.println("cellvalue:" +
										// cellvalue + ":");
									}
								}
								expr = String.valueOf(cellvalue);
							}

							// System.out.println("expr:" + expr + ":");
							// return (revisedexporession.evaluate());
						} else {
							// handle nested array
							// find the inner array and resolve it, then modify
							// the expression and evaluate again
							int innerarraypointer = 0;
							int outerarraypointer = 0;
							boolean add_one = false;

							if (checkexpression.startsWith("(")) {
								add_one = true;
							}

							for (int i = 0; i < expr.length(); i++) {
								if (expr.charAt(i) == '[') {
									if ((innerarraypointer != 0) && (outerarraypointer != 0)) {
										if (add_one) {
											innerarraypointer = i;
										} else {
											innerarraypointer = i;
										}
									} else {
										outerarraypointer = innerarraypointer;
										innerarraypointer = i;
									}
								}
							}
							System.out.println("innerarraypointer:" + innerarraypointer);
							System.out.println("outerarraypointer:" + outerarraypointer);

							String innerarray = "";
							if (add_one) {
								innerarray = expr.substring(outerarraypointer + 1, closedparentheses + 1 + 1);
							} else {
								innerarray = expr.substring(outerarraypointer + 1, closedparentheses + 1);
							}
							System.out.println("491 innerarray:" + innerarray);

							if ((innerarray.startsWith("(")) && (innerarray.endsWith(")"))) {
								String tempexpr = expr;
								expr = innerarray;
								float interimresult = evaluate();
								expr = tempexpr;
								expression1 = expr.substring(0, outerarraypointer + 1);
								expression2 = String.valueOf(interimresult);
								if (innerarray.endsWith(")")) {
									expression3 = expr.substring(closedparentheses + 2, expr.length());
								} else {
									expression3 = expr.substring(closedparentheses + 1, expr.length());
								}
								//
								// String expression4 =
								// expr.substring(closedparentheses+1,
								// expr.length());
								// if (expression4.contains("]"))
								// {
								// for (int u =0; u < expression4.length(); u++)
								// {
								// if (expression4.charAt(u) == "]")
								// {
								// closedparentheses
								// }
								//
								// }
								// }
								System.out.println("515 innerarray :" + innerarray);
								System.out.println("515 expression1 :" + expression1);
								System.out.println("515 expression2 :" + expression2);
								System.out.println("515 expression3 :" + expression3);

								System.out.println("502 expr before:" + expr);
								expr = expression1 + expression2 + expression3;
								System.out.println("502 expr after:" + expr);
								interimresult = evaluate();
								return interimresult;
							}

							StringTokenizer arraytokens = new StringTokenizer(innerarray, "[]");
							String innerarrayname = arraytokens.nextToken();
							arrayindex = arraytokens.nextToken();
							System.out.println("393 innerarrayname:" + innerarrayname + ":");
							System.out.println("393 arrayindex:" + arrayindex + ":");

							for (ArraySymbol as : arrays) {
								if (as.name.equals(innerarrayname)) {
									cellvalue = as.values[Integer.parseInt(arrayindex)];
									break;
								}
							}

							System.out.println("expr before:" + expr + ":");
							expr = expr.substring(0, outerarraypointer + 1) + cellvalue
									+ expr.substring(closedparentheses + 1, expr.length());
							System.out.println("expr after:" + expr + ":");
						}

					} else if ((operatortracker == 0) && (closedparentheses == (expr.length() - 1))) {
						// if there is no operator prior to array and nothing
						// behind the ], then return value
						expression1 = expr.substring(0, openparentheses);
						arrayindex = expr.substring(openparentheses + 1, closedparentheses);

						for (ArraySymbol as : arrays) {
							if (as.name.equals(expression1)) {
								cellvalue = as.values[Integer.parseInt(arrayindex)];
								break;
							}
						}
						return (cellvalue);
					} else {
						// check if operatortracker is zero and if so, does the
						// string after the closedparantheses contain an
						// operator - if so, update the operator value
						if (operatortracker == 0) {
							for (int a = closedparentheses; a < expr.length(); a++) {
								if (operatordelims.contains(String.valueOf(expr.charAt(a)))) {
									operatortracker = a;
									System.out.println("345 operatortracker:" + operatortracker);
								}
							}
						}

						if (operatortracker > openparentheses) {
							// the operator tracker is after the () or []
							// find the array name
							int arraystartindicator = 0;
							for (int h = 0; h < openparentheses; h++) {
								if ((expr.charAt(h) == '[') || (expr.charAt(h) == '(')) {
									arraystartindicator = h + 1;
								}
							}
							String arrayname = expr.substring(arraystartindicator, openparentheses);
							System.out.println("389 arrayname:" + arrayname + ":");

							for (ArraySymbol as : arrays) {
								if (as.name.equals(arrayname)) {
									cellvalue = as.values[Integer.parseInt(parenthesesexpr)];
									break;
									// System.out.println("cellvalue:" +
									// cellvalue + ":");
								}
							}

							System.out.println("389 cellvalue:" + cellvalue + ":");

							String tempexp = expr;
							expression1 = expr.substring(0, arraystartindicator);
							expression2 = String.valueOf(cellvalue);
							expression3 = expr.substring(closedparentheses + 1, expr.length());

							System.out.println("389 expression1:" + expression1 + ":");
							System.out.println("expression2:" + expression2 + ":");
							System.out.println("expression3:" + expression3 + ":");
							System.out.println("392 expr before:" + expr + ":");

							expr = expression1 + expression2 + expression3;
							System.out.println("392 expr after:" + expr + ":");

							result = evaluate();
							return result;

						} else {
							// if there is no operator prior to array

							// check if its a nested array
							String checkexpression = expr.substring(0, openparentheses - 1);
							System.out.println("482 checkexpression:" + checkexpression + ":");
							if (!(checkexpression.contains("["))) {
								expression1 = expr.substring(0, openparentheses);
								arrayindex = expr.substring(openparentheses + 1, closedparentheses);

								for (ArraySymbol as : arrays) {
									if (as.name.equals(expression1)) {
										cellvalue = as.values[Integer.parseInt(arrayindex)];
										break;
									}
								}
								expression2 = expr.substring(closedparentheses + 1, expr.length());
								expr = cellvalue + expression2;
							} else {
								// handle nested array
								// find the inner array and resolve it, then
								// modify the expression and evaluate again
								int innerarraypointer = 0;
								int outerarraypointer = 0;

								for (int i = 0; i < expr.length(); i++) {
									if (expr.charAt(i) == '[') {
										if ((innerarraypointer != 0) && (outerarraypointer != 0)) {
											innerarraypointer = i;
										} else {
											outerarraypointer = innerarraypointer;
											innerarraypointer = i;
										}
									}
								}
								System.out.println("innerarraypointer:" + innerarraypointer);
								System.out.println("outerarraypointer:" + outerarraypointer);

								String innerarray = expr.substring(outerarraypointer + 1, closedparentheses + 1);
								System.out.println("innerarray:" + innerarray);

								StringTokenizer arraytokens = new StringTokenizer(innerarray, "[]");
								String innerarrayname = arraytokens.nextToken();
								arrayindex = arraytokens.nextToken();
								System.out.println("476 innerarrayname:" + innerarrayname + ":");
								System.out.println("477 arrayindex:" + arrayindex + ":");

								for (ArraySymbol as : arrays) {
									if (as.name.equals(innerarrayname)) {
										cellvalue = as.values[Integer.parseInt(arrayindex)];
										break;
									}
								}

								System.out.println("expr before:" + expr + ":");
								expr = expr.substring(0, outerarraypointer + 1) + cellvalue
										+ expr.substring(closedparentheses, expr.length() - 1);
								System.out.println("expr after:" + expr + ":");

							}
						}
					}
				}
			} else if (expr.charAt(closedparentheses) == ')') {
				// the expression doesn't have any array in it
				// Expression expressioninparentheses = new
				// Expression(parenthesesexpr);
				// float subexpressionresult =
				// expressioninparentheses.evaluate();

				String tempexpr = expr;
				expr = parenthesesexpr;
				float interimresult = evaluate();
				expr = tempexpr;

				String expression1 = expr.substring(0, openparentheses);
				String expression3 = expr.substring(closedparentheses + 1, expr.length());
				expr = expression1 + interimresult + expression3;

				// System.out.println("expr1:"+expression1 +
				// ":expr2:"+interimresult+":expr3"+expression3);
			}

		}

		// At this point all of the Arrays and () are resolved - the expression
		// should only have operators in it
		// System.out.println("expr after removing arrays and parentheses:" +
		// expr + ":");

		// process * and /

		while (expr.contains("*") || expr.contains("/")) {
			System.out.println("666 expr:" + expr + ":");

			if (((expr.contains("+")) || (expr.contains("-"))) && (!(((expr.contains("--")))
					|| ((expr.startsWith("-")) || ((expr.contains("\\*\\-")) || ((expr.contains("/\\-")))))))) {
				System.out.println("line 517");
				// mixed case of +-*/
				// simplify expressions between the + and - signs
				StringTokenizer tokens = new StringTokenizer(expr, "+-", true);
				String revisedexpression = "";
				String firstoperand = "";
				String secondoperand = "";
				String Operator = "";

				Stack<String> operatorstack = new Stack<String>();
				// Stack<String> reverseoperatorstack = new Stack<String> ();
				Stack<String> expressstack = new Stack<String>();

				// build the +- and expression stacks
				while (tokens.hasMoreTokens()) {
					String currenttoken = tokens.nextToken();

					if ((currenttoken.equals("+")) || (currenttoken.equals("-"))) {
						System.out.println("pushing Operator:" + currenttoken);
						operatorstack.push(currenttoken);
					} else {
						System.out.println("pushing expressstack:" + currenttoken);

						expressstack.push(currenttoken);
					}
				}

				// Evaluate expression between + and -
				float expressionresult = 0;
				while (!(expressstack.isEmpty())) {
					System.out.println("start of while");
					System.out.println("firstoperand:" + firstoperand);
					System.out.println("secondoperand:" + secondoperand);

					firstoperand = expressstack.pop();
					System.out.println("2. firstoperand:" + firstoperand);

					if (!(expressstack.isEmpty())) {
						secondoperand = expressstack.pop();
						System.out.println("2. secondoperand:" + secondoperand);

					} else {
						// there is only one expression remaining
						String tempexp = expr;
						expr = firstoperand;
						expressionresult = evaluate();
						expr = tempexp;

						revisedexpression = expressionresult + revisedexpression;
						System.out.println("625 revisedexpression:" + revisedexpression);
						if (!(operatorstack.isEmpty())) {
							Operator = operatorstack.pop();
							revisedexpression = Operator + revisedexpression;
							System.out.println("630 revisedexpression:" + revisedexpression);
						}
						tempexp = expr;
						expr = revisedexpression;
						expressionresult = evaluate();
						expr = tempexp;
						return expressionresult;
					}

					if (!(operatorstack.isEmpty())) {
						Operator = operatorstack.pop();
					}

					if ((firstoperand.contains("*")) || (firstoperand.contains("/"))) {
						// simplify first operand
						String tempexp = expr;
						expr = firstoperand;
						float interimresult = evaluate();
						firstoperand = String.valueOf(interimresult);
						expr = tempexp;
					}

					if ((secondoperand.contains("*")) || (secondoperand.contains("/"))) {
						// simplify the second operand
						String tempexp = expr;
						expr = secondoperand;
						float interimresult = evaluate();
						secondoperand = String.valueOf(interimresult);
						expr = tempexp;
					}

					System.out.println("654 firstoperand:" + firstoperand + ": secondoperand:" + secondoperand
							+ ": Operator:" + Operator);
					revisedexpression = secondoperand + Operator + firstoperand + revisedexpression;
					// correct the secondoperand sign right if needed
					if (!(operatorstack.isEmpty())) {
						Operator = operatorstack.pop();
						revisedexpression = Operator + revisedexpression;
					}
					//
					// String tempexp = expr;
					// System.out.println("666 revisedexpression:" +
					// revisedexpression);
					// expr = revisedexpression;
					// expressionresult = evaluate();
					// expr = tempexp;
					// revisedexpression = expressionresult + revisedexpression;
					// // correct the secondoperand sign right if needed
					// if (!(operatorstack.isEmpty()))
					// {
					// Operator = operatorstack.pop();
					// revisedexpression = Operator + revisedexpression;
					// }
				}

				String tempexp = expr;
				expr = revisedexpression;
				expressionresult = evaluate();
				expr = tempexp;
				return expressionresult;
			} else {
				System.out.println("line 597");
				// expression has */ but not +-
				String revisedexpression = "";
				String firstoperand = "";
				String secondoperand = "";
				String Operator = "";

				StringTokenizer tokens = new StringTokenizer(expr, "*/", true);

				Stack<String> operatorstack = new Stack<String>();
				Stack<String> expressstack = new Stack<String>();

				while (tokens.hasMoreTokens()) {
					String currenttoken = tokens.nextToken();

					if ((currenttoken.equals("*")) || (currenttoken.equals("/"))) {
						operatorstack.push(currenttoken);
					} else {
						expressstack.push(currenttoken);
					}
				}

				result = 1;
				while (!(expressstack.isEmpty())) {
					secondoperand = expressstack.pop();
					if (!(expressstack.isEmpty())) {
						firstoperand = expressstack.pop();
					}

					Operator = operatorstack.pop();

					float fo = 0;
					float so = 0;
					if (!isNumeric(firstoperand)) {
						fo = lookupScalar(firstoperand);

					} else {
						fo = Float.parseFloat(firstoperand);
					}

					if (!isNumeric(secondoperand)) {
						so = lookupScalar(secondoperand);

					} else {
						so = Float.parseFloat(secondoperand);
					}

					System.out.println("fo:" + fo + ":");
					System.out.println("so:" + so + ":");

					if (Operator.equals("*")) {
						result = result * fo * so;
						firstoperand = String.valueOf(1);

					} else if (Operator.equals("/")) {
						// validate this
						result = (result * fo) / so;
						firstoperand = String.valueOf(1);
					}
					System.out.println("result :" + result + ":");
				}
				return result;
			}

		} /* while */

		while (expr.contains("+") || expr.contains("-")) {
			System.out.println("1. expr:" + expr + ":");
			// printScalars();

			// check if expr is only one negative value
			if (expr.startsWith("-")) {
				String tempexpr = expr.substring(1, expr.length());
				System.out.println("771. tempexpr:" + tempexpr + ":");

				if (!(tempexpr.contains("+-"))) {
					// then its just one negative digit
					if (!isNumeric(tempexpr)) {
						result = lookupScalar(tempexpr);
					} else {
						result = Float.parseFloat(tempexpr);
					}
					System.out.println("771. result:" + result + ":");

					return (-1 * result);
				}
			}

			// case of +-
			StringTokenizer tokens = new StringTokenizer(expr, "+-", true);
			String revisedexpression = "";
			String firstoperand = "";
			String secondoperand = "";
			String operator1 = "";
			String operator2 = "";
			boolean isnegative = false;

			Stack<String> operatorstack = new Stack<String>();
			Stack<String> reverseoperatorstack = new Stack<String>();
			Stack<String> expressstack = new Stack<String>();

			// build the +- and expression stacks
			while (tokens.hasMoreTokens()) {
				String currenttoken = tokens.nextToken();

				if ((currenttoken.equals("+")) || (currenttoken.equals("-"))) {
					operatorstack.push(currenttoken);
				} else {
					expressstack.push(currenttoken);
				}
			}

			// System.out.println("checking expressstack");
			// while (!(expressstack.isEmpty()))
			// {
			// System.out.println(expressstack.pop());
			// }
			//
			// System.out.println("checking operatorstack");
			// while (!(operatorstack.isEmpty()))
			// {
			// System.out.println(operatorstack.pop());
			// }

			// Evaluate expression between + and -
			result = 0;
			while (!(expressstack.isEmpty())) {
				System.out.println("starting +- while");
				System.out.println("result:" + result);

				firstoperand = expressstack.pop();
				System.out.println("firstoperand:" + firstoperand);

				if (!(expressstack.isEmpty())) {
					secondoperand = expressstack.pop();
					System.out.println("secondoperand:" + secondoperand);
				} else {
					// there is only one expression remaining
					if (!(operatorstack.isEmpty())) {
						operator1 = operatorstack.pop();
					}

					if (!(operatorstack.isEmpty())) {
						// means that the first operand is negative
						operator2 = operatorstack.pop();

					} else {
						operator2 = "+";
					}

					float fo = 0;
					if (!isNumeric(firstoperand)) {
						fo = lookupScalar(firstoperand);
					} else {
						fo = Float.parseFloat(firstoperand);
					}

					if (operator1.equals("+")) {
						if (operator2.equals("-")) {
							// then the first operand is negative
							System.out.println("result before:" + result);
							result = result - fo;
							System.out.println("result after:" + result);
						} else {
							System.out.println("result before:" + result);
							result = result + fo;
							System.out.println("result after:" + result);
						}
					} else {
						// do subtraction
						if (operator2.equals("-")) {
							// then the first operand is negative; therefore,
							// minus minus makes plus
							System.out.println("result before:" + result);
							result = result + fo;
							System.out.println("result after:" + result);
						} else {
							System.out.println("778: result before:" + result);
							result = result + fo;
							System.out.println("780: result after:" + result);
						}

					}
					if (result == 0) {
						// remove -0 and make it 0 without the leading minus
						// sign
						if (1 / result < 0)
							result *= -1;
					}
					return result;
				}

				if (!(operatorstack.isEmpty())) {
					operator1 = operatorstack.pop();
					if (!(operatorstack.isEmpty())) {
						// means that the first operand is negative
						operator2 = operatorstack.pop();
					} else {
						operator2 = "+";
					}

					System.out.println("operator 1:" + operator1);
					System.out.println("operator 2:" + operator2);

					float fo = 0;
					float so = 0;

					if (!isNumeric(firstoperand)) {
						fo = lookupScalar(firstoperand);
						System.out.println("815 fo:" + fo + "firstoperand:" + firstoperand);

					} else {
						fo = Float.parseFloat(firstoperand);
					}

					if (!isNumeric(secondoperand)) {
						// System.out.println("966 printing scalars:");
						// printScalars();
						//
						so = lookupScalar(secondoperand.trim());
						System.out.println("967 L secondoperand:" + secondoperand + ": so:" + so);

					} else {
						so = Float.parseFloat(secondoperand);
						System.out.println("967 L secondoperand:" + secondoperand + ": so:" + so);
					}

					if (operator1.equals("+")) {
						if (operator2.equals("-")) {
							// then the first operand is negative
							System.out.println("result before:" + result);
							System.out.println("L fo:" + fo);
							System.out.println("L so:" + so);
							result = result + fo - so;
							System.out.println("result after:" + result);
						} else {
							System.out.println("result before:" + result);
							System.out.println("L fo:" + fo);
							System.out.println("L so:" + so);
							result = result + fo + so;
							System.out.println("result after:" + result);
						}
					} else {
						if (operator2.equals("-")) {
							// then the first operand is negative
							System.out.println("result before:" + result);
							System.out.println("fo:" + fo);
							System.out.println("so:" + so);
							result = result - fo - so;
							System.out.println("result after:" + result);
						} else {
							System.out.println("866 result before:" + result);
							System.out.println("fo:" + fo);
							System.out.println("so:" + so);
							result = result - fo + so;
							System.out.println("869 result after:" + result);
						}
					}
				}
			}
			if (result == 0) {
				// remove -0 and make it 0 without the leading minus sign
				if (1 / result < 0)
					result *= -1;
			}
			return result;
		}

		// -----
		if (!(delims.contains(expr))) {

			// expression is single value
			if (!isNumeric(expr)) {
				return (lookupScalar(expr));
			} else {
				return Float.parseFloat(expr);
			}
		}

		// System.out.println("expr:" + expr);
		return result;
	}

	/***
	 * look up value for scalars in the scalararray
	 */

	private int lookupScalar(String lookupname) {
		int cellvalue = 0;
		for (ScalarSymbol ss : scalars) {
			if (ss.name.equals(lookupname)) {
				cellvalue = ss.value;
			}
		}
		return cellvalue;
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