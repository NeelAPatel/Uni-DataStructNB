package ProblemSet2;

public class StringLL {

	static StringNode front; //beginning of the LL
	
	
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
	
	public static int numberOfOccurences(StringNode front, String target)
	{
		int count = 0;
		
		for (StringNode pointer = front; pointer != null; pointer = pointer.next){
			if (target.equals(pointer.toString()))
			{
				count++;
			}
			System.out.println(pointer.toString());
		}
		//traverse target count ++
		return count;
	}
	
	
//	
//	public static StringNode deleteAllOccurences(StringNode front, String target)
//	{
//		StringNode trailCurrent = front;
//		StringNode current = front;
//		
//		
//		 if (front == null) // Case 1; the list is empty
//		        System.err.println("Cannot delete from an empty " + "list.");
//		    else {
//		        if (front.data.equals(target)) // Case 2
//		        {
//		            front = front.next;
////
////		            if (front == null) // the list had only one node
////		                last = null;
//		        }
//		    else{
//		        trailCurrent = front;
//		        current = front.next;
//
//		        while(current != null){
//		            if(current.data.equals(target)){
//		            trailCurrent.next = current.next;
//		            }
//		            else
//		                {
//		                    trailCurrent = current;
//		                    current = current.next;
//		                }
//		            }
//
//		        }
//		        }
//		return front;
//	}
//	
	
	
	public StringNode deleteAllOccurences(StringNode front, String target)
	{
		
		
		if (front == null)
		{
			return null;
		}
	
		StringNode prev = null;
		StringNode pointer = front;

		while(pointer != null)
			{
				if (pointer.data.equals(target))
				{
					if (prev == null)
						front = pointer.next; 
					else
						prev.next = pointer.next;
					
				}
				else
				{
					prev = pointer; 
					
				}
				pointer = pointer.next;
				
			}
		
		
		traverse();
		
		
		return front;
		
	}
	/*Testing our String LL object*/
	public static void main(String[]args)
	{
		StringLL ll = new StringLL();
		ll.addToFront("Sun");
		ll.addToFront("Wed");
		ll.addToFront("Sun");
		ll.addToFront("Mon");
		ll.addToFront("Sun");
		ll.addToFront("Thu");
		ll.addToFront("Wed");
		ll.addToFront("Sat");
		ll.addToFront("Sun");
		ll.addToFront("Tue");
		ll.addToFront("Mon");
		ll.addToFront("Fri");
		ll.addToFront("Thu");
		ll.addToFront("Sun");
		ll.addToFront("Sun");
		ll.addToFront("Wed");
		ll.addToFront("Mon");
		ll.addToFront("Wed");
		ll.addToFront("Tue");
		ll.addToFront("Wed");
		ll.addToFront("Tue");

		ll.addToFront("Sun");
		
		ll.traverse();
		ll.deleteAllOccurences(front, "Sun");
		//ll.delete("Sun");
		ll.traverse();
		
		//System.out.println(ll.numberOfOccurences(ll.front, "Wed"));
		
	}
}
