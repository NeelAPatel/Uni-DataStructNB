package ProblemSet2;

public class StringLLapp {

	public static void main(String[] args) {
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
		ll.addToFront("Sun");
		
		ll.traverse();
		//ll.deleteAllOccurences(front, "Sun");
		//ll.delete("Sun");
		ll.traverse();
	
	}

}
