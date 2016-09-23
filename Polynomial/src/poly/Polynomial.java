package poly;

import java.io.*;
import java.util.StringTokenizer;

//import Linear.Node;

//import Linear.Node;
// ======================================================= CLASS TERM=====
/**
 * This class implements a term of a polynomial.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}
// ================================================== CLASS NODE
/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked list. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}
//============================================= CLASS POLYNOMIAL
/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	
	//===================================================================  COMPLETE ADD METHOD
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	
	
	
	
	
	public Polynomial add(Polynomial p)
	{
		
		Polynomial sum = new Polynomial(); 
		Node sumHead = null;
		Node thisptr = this.poly;
		Node thatptr = p.poly;
		
		int thisPolyLength = 0;
		int thatPolyLength = 0;
	
		for (Node ptr = this.poly; ptr != null; ptr = ptr.next)
			thisPolyLength++;
		
		for (Node pptr = p.poly; pptr!=null; pptr = pptr.next)
			thatPolyLength++;
		
		
		int count_this = 0;
		int count_that = 0;
		
		//sum.poly = new Node(5,2,null);
		while (thisptr != null || thatptr != null)
		{
			if (thisptr == null && thatptr == null)
				break;
			else if (thisptr != null && thisptr.next == null)
			{
				sumHead = new Node(thisptr.term.coeff, thisptr.term.degree, sumHead);
				count_this++;
				thisptr = thisptr.next;
			}
			else if(thatptr != null && thatptr.next == null)
			{
				sumHead = new Node(thatptr.term.coeff, thatptr.term.degree, sumHead);
				count_that++;
				thatptr = thatptr.next;
			}
			else if(thisptr != null && thatptr != null)
			{
			    if (thisptr.term.degree == thatptr.term.degree)
				{

					sumHead = new Node(thisptr.term.coeff + thatptr.term.coeff, thisptr.term.degree, sumHead);
					thatptr = thatptr.next;
					thisptr = thisptr.next;
					count_this++;
					count_that++;
				}
				if (thisptr.term.degree < thatptr.term.degree)
				{
					sumHead = new Node(thisptr.term.coeff, thisptr.term.degree, sumHead);
					count_this++;
					thisptr = thisptr.next;
				}
				if (thisptr.term.degree > thatptr.term.degree)
				{
					sumHead = new Node(thatptr.term.coeff, thatptr.term.degree, sumHead);
					count_that++;
					thatptr = thatptr.next;
				}
			} 
			if (count_this == thisPolyLength)
				thisptr = null;
			if (count_that == thatPolyLength)
				thatptr = null;			
		}
		
		sum.poly = sumHead;
		return sum;
		
	}

	//===================================================================  COMPLETE MULTIPLY METHOD
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		/**TODO COMPLETE THIS METHOD **/
		return null;
	}
	
	//===================================================================  COMPLETE EVALUATE METHOD
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) {
		
		
		float sum = 0;
		for(Node pointer = poly; pointer != null;pointer = pointer.next){
			Term t = pointer.term;
			sum += t.coeff * (Math.pow(x, t.degree));
		}
		
		return sum;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
