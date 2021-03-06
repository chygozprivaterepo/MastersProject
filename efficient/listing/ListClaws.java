package efficient.listing;
import java.util.*;

import exception.GraphFileReaderException;
import general.Graph;
import general.Graph.Vertex;
import general.UndirectedGraph;
import general.Utility;

/**
 * class to check for presence of claws in a graph and lists the vertices of the claws found if any
 * @author Chigozie Ekwonu
 *
 */
public class ListClaws {
	
	//instance variables
	private  String p1time; //records time taken for listing 
	private  int found; //records number of claws found
	
	/**
	 * constructor to initialize the instance variables
	 */
	public ListClaws(){
		this.p1time = "-";
		this.found = 0;
	}
	
	/**
	 * main method which allows direct access to the class via the command line terminal.
	 * @param args		command line arguments
	 */
	public static void main(String [] args){		
		try{
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);//create graph from file
			//run the listing operation
			ListClaws d = new ListClaws();
			List<List<Vertex<Integer>>> claws = d.detect(graph);
			String out = "";
			if(!claws.isEmpty()){
				for(List<Graph.Vertex<Integer>> claw: claws){
					out += Utility.printList(claw)+"\n";
				}
				out = String.format("Claw found%nVertices:%n%s", out);
				out += String.format("Number of claws found: %d%n", claws.size());
				out += String.format("CPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}else{
				out = String.format("Claw not found%nCPU time taken: %d milliseconds", Utility.getTotalTime(d.getResult()));
			}
			//print out results
			System.out.println(out);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}	
	
	/**
	* method that lists claws in a given graph. Uses the helper method "find" to achieve that and 
	* computes time taken
	* @param graph 		the graph to be checked for a claw
	* @return			the vertices of each of the claws if found
	*/
	public List<List<Vertex<Integer>>> detect(UndirectedGraph<Integer,Integer> graph){
		long start = System.currentTimeMillis();
		List<List<Vertex<Integer>>> claws = find(graph);
		long stop = System.currentTimeMillis();
		p1time = ""+(stop-start); //calculate time taken to run the listing operation
		found = claws.size(); //store number of claws found
		return claws;
	}

	/**
	* method that lists claws in a given graph. 
	* @param graph 		the graph to be checked for a claw
	* @return			the vertices of each of the claws if found
	*/
	public List<List<Vertex<Integer>>> find(UndirectedGraph<Integer,Integer> graph){
		//for each vertex, check if the complement of its neighbour contains a triangle
		List<List<Graph.Vertex<Integer>>> claws = new ArrayList<List<Graph.Vertex<Integer>>>();//list to store claws found
		
		Iterator<Graph.Vertex<Integer>> vertices = graph.vertices(); //get graph vertices
		
		//for each vertex, get its neighbourhood graph and check the complement of the neighbourhood for a triangle
		while(vertices.hasNext()){
			//get neighbourhood graph
			Graph.Vertex<Integer> v = vertices.next();
			UndirectedGraph<Integer,Integer> vNeighGraph = Utility.getNeighbourGraph(graph, v);
			
			if(vNeighGraph.size()<3)
				continue;
			
			//check the neighbourhood's complement for presence of a triangles
			List<List<Graph.Vertex<Integer>>> cls = getClawVerticesFromNeighbourGraph(vNeighGraph);
			if(!cls.isEmpty()){ //if triangles are found, create claws
				for(List<Graph.Vertex<Integer>> claw: cls){
					//add v to the collection
					claw.add(v);
					claws.add(claw);
				}
			}
			
		}
		return claws;
	}
	
	/**
	 * method to check if a neighbourhood contains non-central vertices of a claw by checking
	 * if the complement of the neighbourhood contains a triangle
	 * @param vNeighGraph		the neighbourhood graph to be checked
	 * @return					the vertices if found
	 */
	private List<List<Graph.Vertex<Integer>>> getClawVerticesFromNeighbourGraph(UndirectedGraph<Integer,Integer> vNeighGraph){
		//get the complement matrix of the neighbour graph
		List<List<Graph.Vertex<Integer>>> claws = new ArrayList<List<Graph.Vertex<Integer>>>();
		int[][] vncomp = vNeighGraph.getComplementMatrix();
		
		//map the neighbour graph vertices to the complement matrix indices
		List<Graph.Vertex<Integer>> vimap = new ArrayList<Graph.Vertex<Integer>>();
		Iterator<Graph.Vertex<Integer>> vnIt = vNeighGraph.vertices();
		while(vnIt.hasNext()){
			vimap.add(vnIt.next());
		}
		
		//create complement graph from complement matrix
		UndirectedGraph<Integer,Integer> vncompgraph = Utility.makeGraphFromAdjacencyMatrix(vncomp); 
		
		//look for a triangle in the complement graph. Such a triangle forms the remaining vertices
		//of the claw
		List<List<Graph.Vertex<Integer>>> tris =  new ListTriangles().detect(vncompgraph);
		if(!tris.isEmpty()){
			for(List<Graph.Vertex<Integer>> tri: tris){
				//get the vertices of the main graph that correspond to the vertices of the triangle found
				List<Graph.Vertex<Integer>> claw = new ArrayList<Graph.Vertex<Integer>>();
				
				for(Graph.Vertex<Integer> v: tri){
					claw.add(vimap.get(v.getElement()));
				}
				
				claws.add(claw);
			}
		}
		
		return claws;
	}
	
	/**
	*	method to return the time taken to run the listing	
	*	and the number of claws found
	*/
	public String getResult(){
		String result = String.format("%-10s%10d", p1time,found);
		return result;
	}

}
