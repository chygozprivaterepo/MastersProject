/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import efficientdetection.*;
import general.*;
import general.Graph.Vertex;

/**
 * @author Chigozie Ekwonu
 *
 */
public class DetectClawTest {
	UndirectedGraph<Integer,Integer> graph;

	@Before
	public void before(){
		String fileName = "test\\testdata\\clawtestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
	}
	
	/**
	 * Test method for {@link efficientdetection.DetectTriangle#detect(general.UndirectedGraph)}.
	 */
	@Test
	public void testDetect() {
		List<Graph.Vertex<Integer>> actualResult = (List<Vertex<Integer>>) DetectClaw.detect(graph);
		int[] expectedResult = {0,1,2,3}; //expected result should have 1 claw 
													//with the specified vertex elements
		List<Integer> vList = new ArrayList<Integer>();
		for(Graph.Vertex<Integer> v: actualResult)
			vList.add(v.getElement());
		for(int j=0; j<expectedResult.length; j++){
			assertTrue(vList.contains(expectedResult[j]));
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

	/**
	 * Test method for {@link efficientdetection.DetectTriangle#phaseOne(general.UndirectedGraph, java.util.List)}.
	 */
	@Test
	public void testPhaseOne() {
		List<Graph.Vertex<Integer>> actualResult = (List<Vertex<Integer>>) DetectClaw.phaseOne(graph);
		assertTrue(actualResult==null);			
	}

	/**
	 * Test method for {@link efficientdetection.DetectTriangle#phaseTwo(general.UndirectedGraph, java.util.List)}.
	 */
	@Test
	public void testPhaseTwo() {
		List<Graph.Vertex<Integer>> actualResult = (List<Vertex<Integer>>) DetectClaw.phaseTwo(graph);
		int[] expectedResult = {0,1,2,3}; //expected result should have 1 claw 
													//with the specified vertex elements
		List<Integer> vList = new ArrayList<Integer>();
		for(Graph.Vertex<Integer> v: actualResult)
			vList.add(v.getElement());
		for(int j=0; j<expectedResult.length; j++){
			assertTrue(vList.contains(expectedResult[j]));
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}

}
