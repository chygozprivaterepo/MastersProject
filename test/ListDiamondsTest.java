/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import efficient.listing.ListDiamonds;
import exception.GraphFileReaderException;
import general.*;
import general.Graph.Vertex;

/**
 * @author Chigozie Ekwonu
 *
 */
public class ListDiamondsTest {
	UndirectedGraph<Integer,Integer> graph;

	@Before
	public void before() throws GraphFileReaderException{
		String fileName = "test"+File.separator+"testdata"+File.separator+"diamondtestdata.txt";
		graph = Utility.makeGraphFromFile(fileName);
	}
	
	/**
	 * Test method for {@link efficient.detection.DetectTriangle#detect(general.UndirectedGraph)}.
	 */
	@Test
	public void testDetect() {
		List<List<Vertex<Integer>>> actualResult = new ListDiamonds().detect(graph);
		int[][] expectedResult = {{0,1,2,4},{0,2,3,4}}; //expected result should have 3 triangles 
															//with the specified vertex elements
		
		for(int i=0; i<expectedResult.length;i++){
			List<Integer> vList = new ArrayList<Integer>();
			Collection<Graph.Vertex<Integer>> aRIt = actualResult.get(i);
			for(Graph.Vertex<Integer> v: aRIt)
				vList.add(v.getElement());
			for(int j=0; j<expectedResult[i].length; j++){
				assertTrue(vList.contains(expectedResult[i][j]));
			}
		}
		assertEquals(actualResult.size(),expectedResult.length);
	}
}
