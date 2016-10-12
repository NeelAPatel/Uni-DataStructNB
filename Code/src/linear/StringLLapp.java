package linear;

public class StringLLapp {

	public static void main(String[] args) {
		StringLL linkedlist = new StringLL();
		linkedlist.addToFront("Friday");
		linkedlist.traverse();
		linkedlist.addToFront("Wednesday");
		linkedlist.traverse();
		linkedlist.addToFront("Tuesday");
		linkedlist.traverse();
		linkedlist.addToFront("Monday");
		
		
		StringLL ll2 = new StringLL();
		ll2.addToFront ("cs111");
		ll2.addToFront("cs112");
	
	}

}
