package poly;

import java.io.*;
import java.util.StringTokenizer;

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

		while (thisptr != null || thatptr != null)
		{
			if (thisptr == null && thatptr == null)  //Current : Null|Null
				break;
			else if ((thisptr == null && thatptr != null)   || (thisptr != null && thatptr == null) )
			{
				// Current: Null | NotNull
				if (thisptr == null && thatptr != null)
				{
					 if(thatptr != null && thatptr.next == null)
					{
						sumHead = new Node(thatptr.term.coeff, thatptr.term.degree, sumHead);
						thatptr = thatptr.next;
					}
				}
				
				// Current: NotNull | Null
				if (thisptr != null && thatptr == null)
				{
					if (thisptr != null && thisptr.next == null)   // When this poly is at the last node
					{
						sumHead = new Node(thisptr.term.coeff, thisptr.term.degree, sumHead);
						thisptr = thisptr.next;
					}
				}
			}
			else
			{
				if(thisptr != null && thatptr != null)
				{
				    if (thisptr.term.degree == thatptr.term.degree)
					{
				    	if (thisptr.term.coeff + thatptr.term.coeff != 0)
				    		sumHead = new Node(thisptr.term.coeff + thatptr.term.coeff, thisptr.term.degree, sumHead);
						thatptr = thatptr.next;
						thisptr = thisptr.next;
					}
				    else
					if (thisptr.term.degree < thatptr.term.degree)
					{
						sumHead = new Node(thisptr.term.coeff, thisptr.term.degree, sumHead);
						thisptr = thisptr.next;
					}
					else if (thisptr.term.degree > thatptr.term.degree)
					{
						sumHead = new Node(thatptr.term.coeff, thatptr.term.degree, sumHead);
						thatptr = thatptr.next;
					}
				} 
			}
		} // while
		
		sum.poly = reverse(sumHead);
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
		
		Polynomial product = new Polynomial();
		Node completeProduct = null;
		Node roughProduct = null;
		
		Node thisptr = poly;
		Node thatptr = p.poly;
		Polynomial tempPoly = new Polynomial();
		
		for(thisptr = poly; thisptr != null; thisptr = thisptr.next)
		{
			
			for(thatptr = p.poly; thatptr != null; thatptr = thatptr.next)
			{
				roughProduct = new Node(thisptr.term.coeff * thatptr.term.coeff,thisptr.term.degree + thatptr.term.degree , roughProduct);
				
			}
		}
		
		
		
		
		System.out.print("ROUGH PRODUCT: ");
		for (Node x = roughProduct; x != null; x = x.next)
			System.out.print(x.term +" + ");
		System.out.println();
		

		System.out.print("His code: ");
		Node a = reverse(reduce(reverse(roughProduct)));
		for (Node x = a; x != null; x = x.next)
			System.out.print(x.term +" + ");
		System.out.println();
		
		System.out.print("My code: ");
		Node b = fix(reverse(simplify(reverse(roughProduct))));
		for (Node x = b; x != null; x = x.next)
			System.out.print(x.term +" + ");
		System.out.println();
		
		
		
		//completeProduct = simplify(roughProduct);		
		product.poly = reverse(roughProduct);
		product.poly = simplify(reverse(roughProduct));
		return product;
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
	
	
	
	//helper method
	private Node reverse(Node node) {
        Node prev = null;
        Node current = node;
        Node next = null;
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        node = prev;
        return node;
    }
	
	private Node fix(Node unsorted)
	{
		Node ptr = unsorted;
		Node prev = null;
		int targetDegree = 0;
		
		int highestDegree = 0;
		Node head = null; 
		
		
		// find highest degree
		for(Node x = unsorted; x != null; x = x.next)
		{
			if(x.term.degree > highestDegree)
				highestDegree = x.term.degree;
		}
		
		
		
		for(int x = 0; x <= highestDegree; x++)
		{
			
			for (Node search = ptr; search != null; search = search.next)
			{
				if (search.term.degree == x)
				{
					head = new Node(search.term.coeff, x, head);
				}
			}
		}
		return head;
	}

	public Node reduce(Node ptr) 
		{
			for(Node p1 = ptr; p1 != null; p1 = p1.next)
			{
				for(Node p2 = p1.next,p3 = p1; p2 != null; p2 = p2.next)
				{
					if(p1.term.degree == p2.term.degree)
					{
						p1.term.coeff = p1.term.coeff + p2.term.coeff;
						p3.next = p2.next;
						
						if(p3.next != null)
						{
							p2 = p3.next;
						}
						else
						{
							break;
						}
					}
					p3 = p3.next;
				}
			}
			return ptr;
		}



	private Node simplify(Node roughProduct) {
		// TODO Auto-generated method stub

		Node pointer = roughProduct;
		//Node returnpoint = null;
	//	System.out.println("simplify executed");
		while (pointer != null)
		{
			//System.out.println("While pass");
			for (Node search = pointer.next,returnpoint = pointer; search != null ; search = search.next)
			{
				if (pointer.term.degree == search.term.degree)
				{
					//System.out.println("Yes");
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
		
	}
	private Node simplify2(Node roughProduct){
		
		Node pointer = roughProduct;
		boolean found= false;
		
		while(pointer != null)
		{
			System.out.println("Current term: " +pointer.term);
			found = false;
			for(Node p = pointer.next; p != null; p = p.next)
			{
				System.out.print("    search term: " + p.term);
				f = pointer.term.coeff;
				if(pointer.term.degree == p.term.degree)
				{
					
					f += p.term.coeff; 
					System.out.print(" ---- MATCH!   " + (pointer.term.coeff + p.term.coeff));
					
					
					roughProduct = delete(roughProduct, p.term );
					found = true;
				}
				System.out.println();
			}
			 
			if (found == false){
				ans = new Node(pointer.term.coeff, pointer.term.degree,ans);
			}
			else{
				ans = new Node(f,pointer.term.degree,ans);
			}
			
			System.out.println("\n\n\n");
			pointer = pointer.next;
		}
		
	}
	
	
//		for(Node p = roughProduct; p != null; p = p.next)
//		{
//			for (Node p2 = roughProduct; p2 != null; p2 = p2.next)
//			{
//				if(p.term.degree == p2.term.degree){
//					hold = new Node(p.term.coeff + p2.term.coeff, p.term.degree, hold);
//
//					roughProduct = delete(roughProduct,p.term);
//					p = p.next;
//					System.out.print(p.term + " + ");
//					found = true;
//				}
//				else
//					System.out.print(p.term + " + ");
//			}
//			if (found != true)
//				hold = new Node(p.term.coeff, p.term.degree, hold);
//			
//			
//		}
			
		
		
		
		
		
		
		return roughProduct;
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
