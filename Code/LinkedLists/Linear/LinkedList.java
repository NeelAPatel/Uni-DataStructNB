package Linear;

import java.util.NoSuchElementException;

public class LinkedList <T> // "<T> means generic type
{
	Node <T> front;
	int size;
	
	LinkedList() 
	{
		front = null;
		size = 0;
	}
	
	void addToFront(T data)
	{
		front = new Node <T> (data, front);
		size++;
	}
	 
	void traverse() 
	{
		for (Node <T> ptr = front; ptr != null; ptr = ptr.next)
		{
			System.out.print(ptr.data + " -> ");
		}
		System.out.println("\\");
	}
	
	T deleteFront(){
		if (front == null)
			throw new NoSuchElementException("List is empty");
		T temp = front.data;
		front = front.next;
		size --;
		return temp;
			
	}
	
}
