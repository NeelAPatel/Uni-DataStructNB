package linear;

public class DLLapp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DLL<String> dll = new DLL<String>();
		dll.addToFront("blue");
		dll.addToFront("scarlet");
		dll.addToFront("yellow");
		dll.traverse();
		
		dll.front = dll.reverse(dll.front);
		dll.traverse();

	}

}
