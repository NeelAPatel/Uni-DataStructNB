package Linear;

public class StringLL {

	StringNode front; //beginning of the LL
	
	
	//Constructor initializes the LinkedList
	StringLL()
	{
		front = null;
		//size=0;
	}
	
	
	//methods of the LL object
	public void addToFront(String newData){
		front = new StringNode(newData, front);
		//size++;
	}
	
	public void traverse()
	{
		if(front == null)
		{
			System.out.println("List is empty");
		}
		
		for (StringNode pointer = front; pointer != null; pointer = pointer.next)
		{
			System.out.print(pointer.data + " -> ");
		}
		System.out.println("\\");
	}
	
	public void delete (String target)
	{
		StringNode pointer = front;
		StringNode prev = null;
		while (pointer != null && pointer.data.equals(target))
		{
			prev = pointer;
			pointer = pointer.next;
		}
		if(pointer == null)
		{
			System.out.println(target + "not");
		}
		else if (pointer == front)
		{
			front = front.next;
			//size --;
		}
		else
		{
			prev.next = pointer.next;
			//size--;
		}
		

	}
	
	
	public boolean isEmpty()
	{
		return front == null;
	}
	
	
	/*Testing our String LL object*/
	public static void main(String[]args)
	{
		StringLL ll = new StringLL();
		ll.addToFront("Friday");
		ll.addToFront("Wednesday");
		
	}
}
