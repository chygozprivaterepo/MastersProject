package bruteforce.detection;

import java.util.*;

import exception.GraphFileReaderException;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectTriangle {
	private String time = "-";
	private String found = "found";
	
	public static void main(String[] args) {		
		try{			
			final UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(args[0]);
			final DetectTriangle d = new DetectTriangle();
			long a = System.currentTimeMillis();
			Thread t = new Thread(new Runnable(){
				public void run(){
					List<Graph.Vertex<Integer>> triangle = d.detect(graph);
				}
			});
			t.start();
			while(!t.isInterrupted() && t.isAlive()){
				long timeout = 30000; //timeout of 30 seconds
				long b = System.currentTimeMillis();
				if((b-a)>timeout){
					d.found = "timed out";
					t.interrupt();
				}else{
					Thread.sleep(500);
				}
			}
			
			System.out.print(d.getResult());
			System.exit(0);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("Please provide the graph file as a command line argument");
		}catch(InterruptedException e){}
		catch(GraphFileReaderException e){
			System.out.println(e.getError());
		}
	}
	
	public List<Graph.Vertex<Integer>> detect(UndirectedGraph<Integer,Integer> graph){
		long starttime = System.currentTimeMillis();
		List<Graph.Vertex<Integer>> triangle= find(graph);
		long stoptime = System.currentTimeMillis();
		time = ""+(stoptime-starttime);
		
		if(triangle==null)
			found = "not found";
		
		return triangle;
	}
	
	public static List<Graph.Vertex<Integer>> find(UndirectedGraph<Integer,Integer> graph){
		
		//for each edge in graph, check if each vertex has an edge between it and the 
		//vertices at the edge
		Iterator<Graph.Edge<Integer>> edges = graph.edges();
		while(edges.hasNext()){
			Graph.Edge<Integer> edge = edges.next();
			//get the vertices
			Iterator<Graph.Vertex<Integer>> vertices = graph.vertices();
			
			//check whether an edge exist between vertices at the ends of the edge and
			//the third vertex
			while(vertices.hasNext()){
				Graph.Vertex<Integer> vertex = vertices.next();
				
				//get the end vertices
				Graph.Vertex<Integer> source = edge.getSource();
				Graph.Vertex<Integer> destination = edge.getDestination();
				
				if(graph.containsEdge(source, vertex) && graph.containsEdge(destination, vertex)){
					List<Graph.Vertex<Integer>> triangleVertices = new ArrayList<Graph.Vertex<Integer>>();
					triangleVertices.add(source);
					triangleVertices.add(destination);
					triangleVertices.add(vertex);				
					
					return triangleVertices;					
				}
			}
			
		}
		
		return null;
	}
	
	public String getResult(){
		String result = String.format("%-10s%-10s", time,found);
		return result;
	}
}
