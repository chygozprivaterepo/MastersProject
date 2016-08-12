package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import efficientdetection.DetectClaw;
import efficientdetection.DetectDiamond;
import efficientdetection.DetectK4;
import efficientdetection.DetectSimplicialVertex;
import efficientdetection.DetectTriangle;
import general.UndirectedGraph;
import general.Utility;
import listing.ListKL;

public class Evaluator2 {
	static boolean done;
	static int start=0, end=0, no=0;
	
	 public static void main(String[] args) {
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(20);
 
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20,
                                            20, 60000, TimeUnit.MILLISECONDS, blockingQueue);
 
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r,
                    ThreadPoolExecutor executor) {
            	System.out.println(r.toString() + " Rejected");
//            	System.out.print(".");
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Trying "+ r.toString() + " again");
//                System.out.print(".");
                executor.execute(r);
            }
        });
        
        // Let start all core threads initially
        executor.prestartAllCoreThreads();
        
        int i = 1;
        String generateGraphs = "";
        if(args.length>0){
        	start = Integer.parseInt(args[0]);
	        end = Integer.parseInt(args[1]);
	        no = Integer.parseInt(args[2]); 
	        generateGraphs = args[3];
        }
        
        
//        //generate random graphs
//        for(int j=start; j<=end; j+=100){
//        	Runnable r = new GraphGenerator(j,i,no);
//        	executor.execute(r);
//        }
        
        if(generateGraphs.equals("gg")){
	        done = false;
			new Thread(new Runnable(){
	
				@Override
				public void run() {
					generateRandomGraphs(start, end, no);
				}
				
			}).start();
			System.out.print("Generating graphs");
			while(!done){
				System.out.print(".");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Done");
        }
        
        long star = System.currentTimeMillis();
        System.out.println("Reading graphs");
        List<String> graphs = getGeneratedFiles();
        System.out.println("Done");
        
        System.out.println("Running triangle detection");
        //run detect triangle tests
        for(String graph: graphs){
        	Runnable r = new DetectTriangleRunner(graph,i++);
        	executor.execute(r);
        }
        
        i = 1;
        System.out.println("Running diamond detection");
        //run detect diamond tests
        for(String graph: graphs){
        	Runnable r = new DetectDiamondRunner(graph,i++);
        	executor.execute(r);
        }
        
        i = 1;
        System.out.println("Running claw detection");
        //run detect claw tests
        for(String graph: graphs){
        	Runnable r = new DetectClawRunner(graph,i++);
        	executor.execute(r);
        }
        
        i = 1;
        System.out.println("Running k4 detection");
        //run detect K4 tests
        for(String graph: graphs){
        	Runnable r = new DetectK4Runner(graph,i++);
        	executor.execute(r);
        }
        
        //run detect Kl tests
        for(String graph: graphs){
        	Runnable r = new DetectKLRunner(graph,i++);
        	executor.execute(r);
        }
        
        i = 1;
        System.out.println("Running simplicial detection");
        //run detect simplicial tests
        for(String graph: graphs){
        	Runnable r = new DetectSimplicialRunner(graph,i++);
        	executor.execute(r);
        }
        executor.shutdown();
        long stop = System.currentTimeMillis();
        System.out.println("Time taken: "+(stop-star));
    }
	 
	public static class DetectTriangleRunner implements Runnable{
		private String gname;
		private int count; 
		
		DetectTriangleRunner(String f, int i){
			this.gname = f;
			count = i;
		}
		
		public void run(){
			System.out.println("Executing detect triangle on "+count);
//			System.out.print(".");
			String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			DetectTriangle d = new DetectTriangle();
			d.detect(graph);
			String result = d.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			d.resetResult();
			
			saveResultToFile(output, "efficient_triangle_detection_result",name);
		}
		
		public String toString(){
			return count+"";
		}
		
	}
	
	public static class DetectK4Runner implements Runnable{
		private String gname;
		private int count; 
		
		DetectK4Runner(String f, int i){
			this.gname = f;
			count = i;
		}
		
		public void run(){
			System.out.println("Executing detect k4 on "+count);
//			System.out.print(".");
			String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			DetectK4 d = new DetectK4();
			d.detect(graph);
			String result = d.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			d.resetResult();
			
			saveResultToFile(output, "efficient_k4_detection_result",name);
		}
		
		public String toString(){
			return count+"";
		}
		
	}
	
	public static class DetectKLRunner implements Runnable{
		private String gname;
		private int count; 
		
		DetectKLRunner(String f, int i){
			this.gname = f;
			count = i;
		}
		
		public void run(){
			System.out.println("Executing detect kL on "+count);
//			System.out.print(".");
			String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			ListKL d = new ListKL();
			d.detect(graph,5);
			String result = d.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			d.resetResult();
			
			saveResultToFile(output, "efficient_kL_detection_result",name);
		}
		
		public String toString(){
			return count+"";
		}
		
	}
	
	public static class DetectClawRunner implements Runnable{
		private String gname;
		private int count; 
		
		DetectClawRunner(String f, int i){
			this.gname = f;
			count = i;
		}
		
		public void run(){
			System.out.println("Executing detect claw on "+count);
//			System.out.print(".");
			String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			DetectClaw d = new DetectClaw();
			d.detect(graph);
			String result = d.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			d.resetResult();
			
			saveResultToFile(output, "efficient_claw_detection_result",name);
		}
		
		public String toString(){
			return count+"";
		}
		
	}
	
	public static class DetectSimplicialRunner implements Runnable{
		private String gname;
		private int count; 
		
		DetectSimplicialRunner(String f, int i){
			this.gname = f;
			count = i;
		}
		
		public void run(){
			System.out.println("Executing detect simplicial on "+count);
//			System.out.print(".");
			String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			DetectSimplicialVertex d = new DetectSimplicialVertex();
			d.detect(graph);
			String result = d.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			d.resetResult();
			
			saveResultToFile(output, "efficient_simplicial_detection_result",name);
		}
		
		public String toString(){
			return count+"";
		}
		
	}
	
	public static class DetectDiamondRunner implements Runnable{
		private String gname;
		private int count; 
		
		DetectDiamondRunner(String f, int i){
			this.gname = f;
			count = i;
		}
		
		public String toString(){
			return count+"";
		}
		
		public void run(){
			System.out.println("Executing detect diamond on "+count);
//			System.out.print(".");
			String output = String.format("%-30s%-12s%s%n", "File name","Graph size","Result");
			
			UndirectedGraph<Integer,Integer> graph = Utility.makeGraphFromFile(gname);
			DetectDiamond d = new DetectDiamond();
			d.detect(graph);
			String result = d.getResult();
			int index = gname.lastIndexOf("graph");
			String name = gname.substring(index);
			output += String.format("%-30s%-12s%s%n", name,graph.size(),result);
			d.resetResult();
			
			saveResultToFile(output, "efficient_diamond_detection_result",name);
		}
	}
	
	public static class GraphGenerator implements Runnable{
		private int start,name,no;
		
		GraphGenerator(int start,int name,int no){
			this.start = start;
			this.name = name;
			this.no = no;
		}
		
		public String toString(){
			return name+"";
		}
		
		public void run(){
			for(double p=0.1; p<=1; p+=0.1){
				System.out.print(".");
				Utility.generateRandomGraphFile(start, Math.round(p*10)/10.0, no);
			}
		}
	}
	
	public static void saveResultToFile(String result, String filename, String graphname){
		File f = new File("");
		String path = f.getAbsolutePath();
		File dir = new File(path+File.separator+"results");
		dir.mkdir();
		
		File dir2 = new File(path+File.separator+"results"+File.separator+filename);
		dir2.mkdir();
		
		String graphFileName = dir2.getAbsolutePath()+File.separator+graphname;
		try {
			PrintWriter writer = new PrintWriter(graphFileName);
			writer.write(result);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getGeneratedFiles(){
		List<String> graphs = new ArrayList<String>();
		File file = new File("");
		String path = file.getAbsolutePath();
		
		//enter generated graphs folder
		String ggFolder = "generated_graphs";
		File dir = new File(path+File.separator+ggFolder);
		
		String[] graphFolders = dir.list();
		for(String s: graphFolders){
			//enter each graph size folder and print out the graph files in it
			File dir2 = new File(path+File.separator+ggFolder+File.separator+s);
			String[] graphFiles = dir2.list();
			for(String t: graphFiles){
				String graphFileName = dir2.getAbsolutePath()+File.separator+t;
				graphs.add(graphFileName);
			}
		}
		
		return graphs;
	}
	
	/**
	 * method to generate random graphs
	 * @param vmax		the maximum number of vertices 
	 * @param no		the number of graphs for each graph size
	 */
	public static void generateRandomGraphs(int vmin, int vmax, int no){
		for(int v=vmin; v<=vmax; v++){
			for(double p=0.1; p<=1; p+=0.1){
				if(p<=0.9)
					Utility.generateRandomGraphFile(v, Math.round(p*10)/10.0, no);
				else
					Utility.generateRandomGraphFile(v, Math.round(p*10)/10.0, 1);
			}
		}
		done = true;
	}
}
