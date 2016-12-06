package apps;

import java.io.IOException;

import structures.Graph;

public class MSTDriver {
	public static void main(String[] args) throws IOException {
		 
		//Create graph
		Graph g = new Graph ("graph1.txt");
		
		PartialTreeList PTL = MST.initialize(g);
		System.out.println("Main: PTL.toString: " + PTL.toString());
;//		int size = PTL.size();
//		
//		for(int i = 0; i < size; i++)
//		{
//			PartialTree pt = PTL.remove();
//			pt.toString();
//		}
		
		//Call methods
		
	}
}
