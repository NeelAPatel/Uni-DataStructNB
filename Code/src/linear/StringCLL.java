package linear;

public class StringCLL {
	StringNode rear;
	int size;
	
	StringCLL()
	{
		rear = null;
		size = 0;
	}

	void addToFront(String data)
	{
		StringNode node = new StringNode (data, null                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        );
		if (rear == null) // List is empty
		{
			node.next = node; // points to itself
			rear = node; // rear points to the end of the list  therefore: rear.next = front of list
		}
		else
		{
		node.next = rear.next;
		rear.next = node;
		}
		
		size++;
	}
	
	
//	void deleteFront()
//	{
//		if (rear == null)
//			System.out.println("List is empty");
//		else
//			if (rear == rear.next)
//			{
//				size = 0;
//				rear = null;
//			}
//			else
//			{
//				rear.next = rear.next.next;
//			size--;
//			}
//	}

	void deleteFront() {
		if (rear == null || rear.next == rear) {
			rear = null;
			size = 0;
		} else {
			rear.next = rear.next.next;
			size--;
		}
	}
	
	
	void search (String target) {
		if (rear == null) {
			System.out.println("List is empty");
			return;
		}
		StringNode ptr = rear.next;
		do {
			if (ptr.data.equals(target)) {
				System.out.println("Found it");
				return;
			}
			ptr = ptr.next;
		} while (ptr != rear.next);
		System.out.println(target + " not found");
	}
	
	
	
	void traverse () {
		StringNode ptr = rear.next;
		do {
			System.out.print(ptr.data + " -> ");
			ptr = ptr.next;
		} while (ptr != rear.next);
		System.out.println();
	}
	
	
	void removeFront()
	{
		if (rear == rear.next)
		{
			rear = null;
			size--;
		}
		else
		{
			rear.next = rear.next.next;  //rear.next = front of list, rear.next.next = second in the list.
		}
	}
}
