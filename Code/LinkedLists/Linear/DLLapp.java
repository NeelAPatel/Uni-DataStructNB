package Linear;

public class DLLapp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DLL<String> dll = new DLL<String>();
		dll.addToFront("blue");
		dll.addToFront("scarlet");
		dll.addToFront("yellow");
		dll.traverse();
		dll.addAfter("scarlet", "green");
		dll.traverse();
		dll.delete("green");
		dll.traverse();
		dll.delete("scarlet");
		dll.traverse();
		dll.delete("yellow");
		dll.traverse();

	}

}
