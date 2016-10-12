package linear;

public class StringLL {

	StringNode front; //beginning of the LL
	int size;
	
	//Constructor initializes the LinkedList
	StringLL()
	{
		front = null;
		//size=0;
	}
	
	void clear() {
		front = null;
//		size = 0;
	}
	//methods of the LL object
	public void addToFront(String newData){
		front = new StringNode(newData, front);
		//size++;
	}
	
	boolean search (String target) {
		for (StringNode ptr = front; ptr != null; ptr = ptr.next) {
			if (ptr.data.equals(target)) {
				return true;
			}
		}
		return false;
	}
	
	boolean addAfter (String target, String data) {
		for (StringNode ptr = front; ptr != null; ptr = ptr.next) {
			if (ptr.data.equals(target)) {
				StringNode node = new StringNode(data, ptr.next);
				ptr.next = node;
//				size++;
				return true;
			}
		}
		return false;
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
