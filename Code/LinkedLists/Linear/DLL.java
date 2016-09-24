package Linear;	

public class DLL<T> {

	NodeDLL <T> front;
	int size;
	DLL ()
	{
		front = null;
		size = 0;
	}
	
	
	void addToFront(T data)
	{
		NodeDLL <T> node = new NodeDLL <T>  (data, null, front); // prev = null next = front;
		if (front != null)
			front.previous = node;
		
		front = node;
		size++;
		
	}
	
//	void addAfter(T target, T data)
//	{
//		for (NodeDLL <T> ptr = front; ptr != null ; ptr = ptr.next)
//		{
//			if (ptr.data.equals(target))  // target found;
//			{
//				NodeDLL <T> node = new NodeDLL <T> (data, ptr, ptr.next); // fixed 2  references for new node
//				ptr.next.previous = node;
//				ptr.next = node;
//				size++;
//			}
//		}
//	}
	
	void addAfter (T target, T data) {
		NodeDLL<T> ptr = front;
		while (ptr != null && !ptr.data.equals(target)) {
			ptr = ptr.next;
		}
		if (ptr == null) {
			return;
		}
		NodeDLL<T> node = new NodeDLL<T>(data, ptr, ptr.next);
		ptr.next = node;
		if (node.next != null) {
			node.next.previous = node;
		}
		size++;
	}
	void traverse(){
		for (NodeDLL <T> ptr = front;ptr != null ; ptr = ptr.next)
		{
			System.out.print(ptr.data + " -> ");
		}
		
		System.out.println();
	}	void delete (T target) {
		NodeDLL<T> ptr = front;
		while(ptr != null && !ptr.data.equals(target)) {
			ptr = ptr.next;
		}
		if (ptr == null) {
			return;
		} else {
			if (ptr.previous != null) {
				ptr.previous.next = ptr.next;
			} else {
				front = ptr.next;
			}
			if (ptr.next != null) {
				ptr.next.previous = ptr.previous;
			}
		}
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
