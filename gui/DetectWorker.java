package gui;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JEditorPane;
import javax.swing.SwingWorker;

import efficientdetection.DetectClaw;
import efficientdetection.DetectDiamond;
import efficientdetection.DetectK4;
import efficientdetection.DetectKL;
import efficientdetection.DetectSimplicialVertex;
import efficientdetection.DetectTriangle;
import general.Graph;
import general.UndirectedGraph;
import general.Utility;

public class DetectWorker extends SwingWorker<String,Void>{

	private String type;
	private String filename;
	private int l;
	private JEditorPane outputArea;
	
	public DetectWorker(String type, String filename, int l, JEditorPane a){
		this.type = type;
		this.filename = filename;
		this.l = l;
		this.outputArea = a;
	}
	
	@Override
	protected String doInBackground() throws Exception {			
		UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(filename);
		if(graph==null)
			return null;
		
		String out = "";
		if(type.equals("Diamond")){
			DetectDiamond d = new DetectDiamond();
			List<Graph.Vertex<Integer>> diamond = d.detect(graph);
			if(diamond!=null){
				out = printList(diamond);
				out = String.format("Diamond found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}else{
				out = String.format("Diamond not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}else if(type.equals("Claw")){
			
			DetectClaw d = new DetectClaw();
			List<Graph.Vertex<Integer>> claw = d.detect(graph);
			if(claw!=null){
				out = printList(claw);
				out = String.format("Claw found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}else{
				out = String.format("Claw not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}else if(type.equals("K4")){
			DetectK4 d = new DetectK4();
			List<Graph.Vertex<Integer>> k4 = d.detect(graph);
			if(k4!=null){
				out = printList(k4);
				out = String.format("K4 found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}else{
				out = String.format("K4 not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}else if(type.equals("Simplicial Vertex")){
			DetectSimplicialVertex d = new DetectSimplicialVertex();
			Graph.Vertex<Integer> simpVertex = d.detect(graph);
			if(simpVertex!=null){
				out = String.format("Simplicial vertex found%nOne such vertex: %s%n", simpVertex.getElement());
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
				return out;
			}else{
				out = String.format("Simplicial vertex not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}else if(type.equals("Triangle")){
			DetectTriangle d = new DetectTriangle();
			List<Graph.Vertex<Integer>> triangle = d.detect(graph);
			if(triangle!=null){
				out = printList(triangle);
				out = String.format("Triangle found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}else{
				out = String.format("Triangle not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}else if(type.equals("KL")){
			DetectKL d = new DetectKL();
			List<Graph.Vertex<Integer>> kl = d.detect(graph, l);
			if(kl!=null){
				out = printList(kl);
				out = String.format("K"+l+" found%nVertices: %s%n", out);
				out += String.format("CPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}else{
				out = String.format("K"+l+" not found%nCPU time taken: %d milliseconds", getTotalTime(d.getResult()));
			}
		}
		return out;
	}

	@Override
	protected void done() {
		String out = "";
		try {
			out = get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		outputArea.setText(outputArea.getText()+out+"\n\n");
	}	
	
	public String printList(List<Graph.Vertex<Integer>> list){
		String out = "";

		for(Graph.Vertex<Integer> v:list){
			out+=v.getElement()+","; 
		}
		out = out.substring(0,out.length()-1);
		return out;
	}
	
	public int getTotalTime(String s){
		String[] tokens = s.split("[ ]+");
		int time = 0;
		int limit = tokens[tokens.length-2].equals("not") ? tokens.length-2: tokens.length-1;
		for(int i=0; i<limit; i++){
			if(!tokens[i].equals("-"))
				time += Integer.parseInt(tokens[i]);
		}
		return time;
	}
}
