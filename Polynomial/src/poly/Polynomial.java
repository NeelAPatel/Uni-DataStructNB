package poly;

import java.io.*;
import java.util.StringTokenizer;

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
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	public Polynomial add(Polynomial p)
	{
		//NullFileTest
		if(this.poly == null)
			return this;
		else if(p.poly == null)
			return p;
		
		
		Polynomial sum = new Polynomial(); 
		Node sumHead = null;
		Node thisptr = this.poly;
		Node thatptr = p.poly;

		while (thisptr != null || thatptr != null)   // MAIN LOOP: Monitors ends when both loops have been traversed to null. 
		{
			if (thisptr == null && thatptr == null)  // STATE 1: Both are null = Both loops traversed = kill the while loop. 
				break;
			else if ((thisptr == null && thatptr != null)   || (thisptr != null && thatptr == null) )     // STATE 2: One of them is null = other is done traversing.  Takes care of situations when one polynomial has more terms than the other
			{
				// Current: Null | NotNull
				if (thisptr == null && thatptr != null)
					 if(thatptr != null && thatptr.next == null) // When this poly is at the last node
					{
						sumHead = new Node(thatptr.term.coeff, thatptr.term.degree, sumHead);  // Add the remaining to sumHead
						thatptr = thatptr.next;
					}
				
				// Current: NotNull | Null
				if (thisptr != null && thatptr == null)
					if (thisptr != null && thisptr.next == null)   // When this poly is at the last node
					{
						sumHead = new Node(thisptr.term.coeff, thisptr.term.degree, sumHead);  // Add the remaining to sumHead
						thisptr = thisptr.next;
					}
			}
			else   // STATE 3 : Neither are null = terms available to compare
			{
				if(thisptr != null && thatptr != null)
				{
				    if (thisptr.term.degree == thatptr.term.degree) // Degrees match = add them up
					{
				    	if (thisptr.term.coeff + thatptr.term.coeff != 0)
				    		sumHead = new Node(thisptr.term.coeff + thatptr.term.coeff, thisptr.term.degree, sumHead);
						thatptr = thatptr.next;
						thisptr = thisptr.next;
					}
				    else
					if (thisptr.term.degree < thatptr.term.degree) // One is less than other 
					{
						sumHead = new Node(thisptr.term.coeff, thisptr.term.degree, sumHead);
						thisptr = thisptr.next;
					}
					else if (thisptr.term.degree > thatptr.term.degree)// One is less than other
					{
						sumHead = new Node(thatptr.term.coeff, thatptr.term.degree, sumHead);
						thatptr = thatptr.next;
					}
				} 
			} // State 3
		}// While Loop
		sum.poly = reverse(sumHead);
		return sum;
		
	}
	
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		
		//NullFileTest
		if(this.poly == null)
			return this;
		else if(p.poly == null)
			return p;
		
		
		Polynomial product = new Polynomial();
		Node roughProduct = null;
		
		Node thisptr = poly;
		Node thatptr = p.poly;
		
		for(thisptr = poly; thisptr != null; thisptr = thisptr.next) // Loop through both, multiply everything. FOIL method.
			for(thatptr = p.poly; thatptr != null; thatptr = thatptr.next)
				if (thisptr.term.coeff * thatptr.term.coeff != 0)
					roughProduct = new Node(thisptr.term.coeff * thatptr.term.coeff,thisptr.term.degree + thatptr.term.degree , roughProduct);
		
		product.poly = reverse(fix(simplify(roughProduct)));
		return product;
	}
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) {
		//NullFileTest
		if(this.poly == null)
			return 0;

		float sum = 0;
		for(Node pointer = poly; pointer != null;pointer = pointer.next){
			Term t = pointer.term;
			sum += t.coeff * (Math.pow(x, t.degree));
		}
		return sum;
	}
		
	/**
	 * Reverses the LinkedList around Ex A => Z becomes Z=>A upon return
	 * @param list Head of the linked list intended to reverse
	 * @return Reversed LinkedList's head
	 */
	private Node reverse(Node list) {
        Node prev = null;
        Node current = list;
        Node next = null;
        while (current != null) { // Swaps pointers around 
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        list = prev;
        return list;
    }
	
	/**
	 * After Multiplying, the answer becomes a list of unsimplified terms 
	 * which can be paired up and matched to bring it down to simplest form.
	 * @param roughProduct the head of the linked list with unsimplified terms
	 * @return Simplified terms
	 */
	private Node simplify(Node roughProduct) {
		Node pointer = roughProduct;
		while (pointer != null)
		{
			for (Node search = pointer.next,returnpoint = pointer; search != null ; search = search.next)  // loops through the list, returnpoint simply allows 
			{																							   // the pointer to return to its default place
				if (pointer.term.degree == search.term.degree)
				{
					pointer.term.coeff = pointer.term.coeff + search.term.coeff;
					returnpoint.next = search.next;
					
					if(returnpoint.next == null)
						break;
					else
						search = returnpoint.next;
				}
				returnpoint = returnpoint.next;
			}
			pointer = pointer.next;
		}
		return roughProduct;
		
	}
	
	/**
	 * After simplification, the terms can be out of order by degree. 
	 * This method sorts them to give a proper answer
	 * @param unsorted list of terms
	 * @return sorted list of terms
	 */
	private Node fix(Node unsorted)
	{
		Node ptr = unsorted;
		int highestDegree = 0;
		Node head = null; 
		
		// find highest degree
		for(Node x = unsorted; x != null; x = x.next)
			if(x.term.degree > highestDegree)
				highestDegree = x.term.degree;		
		//Loops from degree 0 to highest degree in the unsorted list, and reorders them 
		for(int x = 0; x <= highestDegree; x++)
			for (Node search = ptr; search != null; search = search.next)
				if (search.term.degree == x)
					head = new Node(search.term.coeff, x, head);
		
		return head;
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