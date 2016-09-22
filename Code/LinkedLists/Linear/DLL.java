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
	
	void addAfter(T target, T data)
	{
		for (NodeDLL <T> ptr = front; ptr != null ; ptr = ptr.next)
		{
			if (ptr.data.equals(target))  // target found;
			{
				NodeDLL <T> node = new NodeDLL <T> (data, ptr, ptr.next); // fixed 2  references for new node
				ptr.next.previous = node;
				ptr.next = node;
				size++;
			}
		}
	}
	
	
	void traverse(){
		for (NodeDLL <T> ptr = front;ptr != null ; ptr = ptr.next)
		{
			System.out.print(ptr.data + " -> ");
		}
		
		System.out.println();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DLL<String> dll = new DLL<String>();
		dll.addToFront("Real Madrid");
		dll.addToFront("Barcelona");
		dll.addToFront("Chelsea");
		dll.traverse();
		dll.addAfter("Barcelona", "Liverpool");
		dll.traverse();
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
