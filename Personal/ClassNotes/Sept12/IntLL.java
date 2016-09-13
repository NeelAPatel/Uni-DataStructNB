package Sept12;
//Integer LinkedList
public class IntLL {
	
	
	public static Node addToFront(Node front, int newData){
		// replace // Node newNode = new Node (newData, null);
		return new Node(newData,front);	
	}
	
	public static void traverse(Node front)
	{
		if (front == null)
		{
			System.out.println("List is empty");
			return;
		}
		
		for(Node pointer = front; pointer != null ; pointer = pointer.next){
			System.out.print(pointer.data + "-");
		}
		System.out.println();
	}
	
	
	public static void search(Node front, int target)
	{
		for(Node pointer = front; pointer != null ; pointer = pointer.next){
			if(pointer.data == target){
				System.out.println(target+ "found");
				return;
			}
			
		}
		System.out.println(target+"not found");
	}
	public static boolean addAfter(Node front, int target, int newData)
	{
		for(Node pointer = front; pointer != null ; pointer = pointer.next){
			if(pointer.data == target){
				Node newNode = new Node(newData, pointer.next); //Points to the next node after 4
				pointer.next = newNode;
				return true;
			}
			
		}
		return false;
	}
	
	public static Node delete(Node front, int targetToDelete)
	{
		Node prev = null;
		Node ptr = front;
		
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Node L = null;
		L = addToFront(L,9);
		L = addToFront(L,4);
		L = addToFront(L,3);
		traverse(L);
		
		search(L,5);
		search(L,8);
		System.out.println(addAfter(L, 4, 8));
		traverse(L);
		
		
		System.out.println(delete(L,4));
		traverse(L);
	}

}

/*
 * 
 * Big O
 * Assignments are constant-time
 */
