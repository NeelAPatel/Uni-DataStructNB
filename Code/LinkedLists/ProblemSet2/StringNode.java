package ProblemSet2;


public class StringNode {
	/* Fields of the object*/
	String data; // holds the data of the node
	StringNode next; // refers to the next node
	
	/* Construct */	
	StringNode (String data, StringNode next){
		this.data = data;
		this.next = next;
	}
	
	public String toString()
	{
		return data;
	}
}
