package ProblemSet2;

// created "AddBeforeLast"Method
//Integer LinkedList
public class IntLL {
	
	
	public static IntNode addToFront(IntNode front, int newData){
		// replace // IntNode newIntNode = new IntNode (newData, null);
		return new IntNode(newData,front);	
	}
	
	public static void traverse(IntNode front)
	{
		if (front == null)
		{
			System.out.println("List is empty");
			return;
		}
		
		for(IntNode pointer = front; pointer != null ; pointer = pointer.next){
			System.out.print(pointer.data + "-");
		}
		System.out.println();
	}
		
	public static void search(IntNode front, int target)
	{
		for(IntNode pointer = front; pointer != null ; pointer = pointer.next){
			if(pointer.data == target){
				System.out.println(target+ "found");
				return;
			}
			
		}
		System.out.println(target+"not found");
	}
	
	public static boolean addAfter(IntNode front, int target, int newData)
	{
		for(IntNode pointer = front; pointer != null ; pointer = pointer.next){
			if(pointer.data == target){
				IntNode newIntNode = new IntNode(newData, pointer.next); //Points to the next IntNode after 4
				pointer.next = newIntNode;
				return true;
			}
			
		}
		return false;
	}
	
	public static IntNode delete(IntNode front, int targetToDelete)
	{
		IntNode prev = null;
		IntNode ptr = front;
		
		while(ptr!=null && ptr.data != targetToDelete)
		{
			prev = ptr;
			ptr = ptr.next;
		}
		
		if(ptr == null)
		{
			return front;
		}
		else if(ptr == front){
			return ptr.next;
		} else {
			prev.next = ptr.next;
			return front;
		}
	}
	
	public static IntNode addBeforeLast(IntNode front, int item)
	{
		
		if (front == null)
			return null;
		
		// Find number of nodes
		int count= 0;
		IntNode x = front;
		while(x != null)
		{
			count++;
			x = x.next;
		}
		
		System.out.println(count);
		
		
		int target = count -2;
		int pos = 0;
		for(IntNode pointer = front; pointer != null ; pointer = pointer.next){
//			if(pointer.data == target){
//				IntNode newIntNode = new IntNode(item, pointer.next); //Points to the next IntNode after 4
//				pointer.next = newIntNode;
////				return true;
//			}
			if (pos == target)
			{
				IntNode newIntNode = new IntNode(item, pointer.next); //Points to the next IntNode after 4
				pointer.next = newIntNode;
			}
			
			
			pos++;
		}
		
		
		
//		IntNode secondLast = null;
//		IntNode thirdLast = null;
//		
//		
//		for(IntNode pointer = front; pointer != null ; pointer = pointer.next)
//		{
//			if(pointer.next == null)
//			{
//				IntNode newIntNode = new IntNode(item, pointer); //Points to the next IntNode after 4
//				pointer.next = newIntNode;
//			}
////				beforeLast = pointer;
//		}
		return front;
		
	}
	
	
	
	public static void deleteEveryOther(IntNode front){
		IntNode prev = front;
		IntNode ptr = front.next;

		
		while (prev != null && ptr != null)
		{
			// Change .next of previous node to one ahead
			prev.next = ptr.next;
			
			
			ptr = null;
			
			prev = prev.next;
			if (prev != null)
				ptr = prev.next;
		}
		
//		for (IntNode x = front; x != null; x = x.next)
//		{
//			if (count % 2 == 0)
//			{
//				x = x.next;
//			}
//			count++;
//		}
//		

	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IntNode L = null;
		L = addToFront(L,7);
		L = addToFront(L,6);
		L = addToFront(L,5);
		L = addToFront(L,4);
		L = addToFront(L,3);
		L = addToFront(L,2);
		L = addBeforeLast(L,1000);
		traverse(L);
		deleteEveryOther(L);
		traverse(L);
//		search(L,5);
//		search(L,8);
//		System.out.println(addAfter(L, 4, 8));
//		traverse(L);
//		
//		
//		System.out.println(delete(L,4));
//		traverse(L);
	}

}

/*
 * 
 * Big O
 * Assignments are constant-time
 */
