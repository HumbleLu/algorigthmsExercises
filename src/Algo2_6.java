import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Algo2_6 {
	public static void main(String[] args){		
		DirGraph g = new DirGraph();
		
		try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	g.addEdge(new Edge(Integer.valueOf(line.split(" ")[0]), Integer.valueOf(line.split(" ")[1])));
		    }
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		g.printTopoOrder();
	}
	
	public static class Edge{
		int start;
		int end;
		
		public Edge(int start, int end){
			this.start = start;
			this.end = end;
		}
		
		public int getStart(){
			return start;
		}
		
		public int getEnd(){
			return end;
		}
		
		public boolean contains(int node){
			if((start == node) || (end == node)){
				return true;
			}
			return false;
		}
		
		public String toString(){
			return "(" + start + " -> " + end + ")";
		}
	}
	
	public static class DirGraph{
		public Set<Edge> edges;
		public Set<Integer> nodeList;
		
		public DirGraph(){
			edges = new HashSet<Edge>(); // don't compare the nodes
			nodeList = new TreeSet<Integer>();
		}
				
		public void addEdge(Edge e){
			// update edges
			edges.add(e); 
			
			// update nodeList
			if(!nodeList.contains(e.getStart())){
				nodeList.add(e.getStart());
			}
			if(!nodeList.contains(e.getEnd())){
				nodeList.add(e.getEnd());
			}
			
		}
		
		public void printTopoOrder(){
			ArrayList<Integer> ret = new ArrayList<Integer>();
			ArrayList<Integer> prevRet = new ArrayList<Integer>(); // for comparison

			// make check sets
			HashSet<Edge> edgesChk = new HashSet<Edge>(edges);
			TreeSet<Integer> nodeListChk = new TreeSet<Integer>(nodeList);
			
			// loop through nodes and edges
			int currentNode;
			Edge currentEdge;
			Edge delEdge;
			boolean acyclic = true;
			boolean hasIncoming;
			Iterator nodeIter;
			Iterator edgeIter;
			int incomCnt;
			
			while(!nodeListChk.isEmpty()){
				
				nodeIter = nodeList.iterator();
				while (nodeIter.hasNext()) {
					currentNode = (int) nodeIter.next();
					
					if(!nodeListChk.contains(currentNode)){
						continue;
					}
					hasIncoming = false;
					//check if it has incoming edges
					for (Edge e : edges) {
						currentEdge = e;						
						if(edgesChk.contains(currentEdge) && currentEdge.getEnd() == currentNode){
							hasIncoming = true;
							break;
						}
					}
					if(hasIncoming == false){
						// add to output ArrayList
						ret.add(currentNode);
						// remove from node list
						nodeListChk.remove(currentNode);
						// remove related edges
						Iterator delIter = edges.iterator();
						while (delIter.hasNext()) {
							delEdge = (Edge) delIter.next();
							if(delEdge.contains(currentNode)){
								edgesChk.remove(delEdge);
							}
						}
						break;
					}
				}
				// check it acyclic, it is not acyclic the previous ret will be the same
				if(prevRet.toString().equals(ret.toString())){
					acyclic = false;
					break;
				}else{
					prevRet = new ArrayList<Integer>(ret);
				}
			}
			if(acyclic == true){
				System.out.println(ret.toString().replaceAll("[,\\[\\]]", ""));
			}else{
				System.out.println("The graph is not acyclic");
			}
		}
	}
}
