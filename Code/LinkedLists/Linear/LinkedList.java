package Linear;

public class LinkedList <T> // "<T> means generic type
{
	
	//================================= GENERIC NODE CLASS=================
	public class Node <T>
	{
		T data;  
		Node <T> next;
		Node (T data, Node <T> next)
		{
			this.data = data;
			this.next = next;
		}
	}
	
	
	//================================ GENERIC LINKED LIST =================
	
	
}
