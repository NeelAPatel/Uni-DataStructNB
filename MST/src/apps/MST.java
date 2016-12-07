package apps;

import structures.*;
import java.util.ArrayList;

import apps.PartialTree.Arc;

public class MST {
	
	/**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
	
		/* COMPLETE THIS METHOD */
		
		PartialTreeList ptl = new PartialTreeList();
		
		
		for (int i = 0; i < graph.vertices.length; i++)
		{	
			// Names of each vertex (A~I)
			//System.out.println("graph.verticies[i]" + graph.vertices[i]);
			PartialTree pt = new PartialTree(graph.vertices[i]);
			//MinHeap heap = new MinHeap();
			for (Vertex.Neighbor n = graph.vertices[i].neighbors; n != null; n = n.next) {
				
				//Create a new line with two vertexes 
				Vertex v1 = graph.vertices[i]; 
				Vertex v2 = n.vertex;
				int weight = n.weight;
				PartialTree.Arc arc = new PartialTree.Arc(v1, v2, weight);
				
				
				
				//gain access to arcs list and add new arc
				//System.out.println("arc: " + arc);
				pt.getArcs().insert(arc);
				
				
				
				
			}
		
			//System.out.println(pt.getArcs());
			ptl.append(pt);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/**
		 * -Create PartialTreeList
		 * - for all verticies in a graph {
		 * --- create new partial tree with vertex
		 * --- for every neighbour AT VERTEX {
		 * ------ create new arc (edge w/ two vertexes) 
		 * ------ add it to a gets arc list
		 * --- }
		 * --- add to tree list 
		 */
		
		/**
		 * TLDR; 
		 * make a new arc from the neighbours of every vertex and add to partial tree list..?
		 * 
		 */
		
		return ptl;
	}

	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<PartialTree.Arc> execute(PartialTreeList ptlist) {
		
		/* COMPLETE THIS METHOD */
		PartialTreeList ptl = ptlist; // PTL = L
		ArrayList<PartialTree.Arc> arcArr = new ArrayList<PartialTree.Arc>();
		while(ptl.size() > 1)
		{
			
			//Step 3
			PartialTree PTX = ptl.remove();  //firstPartialTree = ptx
			System.out.println("S3: " + PTX);
			//Step 4
			PartialTree.Arc PQX = PTX.getArcs().deleteMin();  
			Vertex v1 = PQX.v1; // belongs to ptx
			Vertex v2 = PQX.v2;
			
			
			//step 5 - could belong and not contain?
			boolean v2BelongsPTX = true;
			boolean PTXcontainsV2 = false;
			while (v2BelongsPTX)
			{
				PTXcontainsV2 = false;
				MinHeap<Arc> minHeap = PTX.getArcs();
				// sets up contains
				for (PartialTree.Arc arc: minHeap)
				{
					if ((arc.v1.name).equals(PQX.v2.name))
						PTXcontainsV2 = true;
				}
				
				
				
				/**
				 * if PTX does contain v2
				 * --- Go back to Step 4 | Pick next highestPriorityArc
				 * else
				 * --- go to step 6 (break loop)
				 */
				if (PTXcontainsV2) 
				{	PQX = PTX.getArcs().deleteMin();
				v2BelongsPTX = true;
				continue;
				}
				else
				{
					v2BelongsPTX = false;
					break;
				}
					
			} //end while v2Belong
			
			
		
			
		//Step7
			
			
			
			
			
		}//end while
			
		/*
		 //Step 3
		PartialTree firstPartialtree = ptl.remove();  //firstPartialTree = ptx
		//Step 4
		PartialTree.Arc highestPriorityArc = firstPartialtree.getArcs().deleteMin();  
		Vertex v1 = highestPriorityArc.v1; // belongs to ptx
		Vertex v2 = highestPriorityArc.v2;
		
		
		//step 5
		boolean v2Belong = true;
		while (v2Belong)
		{
			MinHeap<Arc> minHeap = firstPartialtree.getArcs();
			for (PartialTree.Arc arc: minHeap)
			{
				if ()
			}
			
			
		} //end while v2Belong
		
		 */
		
		/**
		 * arcs merge
		 */
		
		
		
		return null;
	}
}
