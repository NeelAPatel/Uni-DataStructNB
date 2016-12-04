package apps;

import java.io.IOException;

import structures.Graph;

public class MSTDriver {
	public static void main(String[] args) throws IOException {
		 
		//Create graph
		Graph g = new Graph ("graph1.txt");
		
		PartialTreeList PTL = MST.initialize(g);
		
		
		
		//Call methods
		
	}
}
