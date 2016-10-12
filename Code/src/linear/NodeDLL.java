package linear;

public class NodeDLL <T>{

	T data;
	NodeDLL <T> previous;
	NodeDLL <T> next;
	
	NodeDLL (T data, NodeDLL <T> previous, NodeDLL <T> next)
	{
		this.data = data;
		this.previous = previous;
		this.next = next;
	}
}
