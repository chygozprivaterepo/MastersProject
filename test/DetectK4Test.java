package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import efficient.detection.DetectK4;
import exception.GraphFileReaderException;
import general.Graph;
import general.Graph.Vertex;
import general.UndirectedGraph;
import general.Utility;

public class DetectK4Test {
	
	UndirectedGraph<Integer,Integer> graph;
	List<Graph.Vertex<Integer>> lowDegreeVertices, highDegreeVertices;

	@Before
	public void before() throws GraphFileReaderException{
		String fileName = "test"+File.separator+"testdata"+File.separator+"k4testdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
		lowDegreeVertices = new DetectK4().partitionVertices(graph)[0];
		highDegreeVertices = new DetectK4().partitionVertices(graph)[1];
	}
	
	@Test
	public void testDetect(){
		List<Graph.Vertex<Integer>> d = (List<Vertex<Integer>>) new DetectK4().detect(graph);
		
		List<Integer> actualResult = new ArrayList<Integer>();
		for(Graph.Vertex<Integer> v: d){
			actualResult.add(v.getElement());
		}
		
		int[] expectedResult = {0,1,2,3}; //expected result should have one diamond 
															//with the specified vertex elements
		
		for(int i=0; i<expectedResult.length;i++){
			assertTrue(actualResult.contains(expectedResult[i]));
			
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

}
