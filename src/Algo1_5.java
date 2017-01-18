import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Algo1_5 {
		
	public static void main(String[] args){		
		Graph g = new Graph();

		try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	g.addEdge(new Edge(new Node(Integer.valueOf(line.split(" ")[0])), new Node(Integer.valueOf(line.split(" ")[1]))));
		    }
		}catch (IOException e) {
			e.printStackTrace();
		}
				
		g.printIsBipartitie();
	}
	
	public static class Node{
		int name;
		
		public Node(int name){
			this.name = name;
		}
		
		public void setName(int name){
			this.name = name;
		}
		
		public int getName(){
			return name;
		}
	}
	
	public static class Edge{
		ArrayList<Node> nodes;
		Set<Integer> nodeNames;
		
		public Edge(Node n1, Node n2){
			nodes = new ArrayList<Node>();
			nodeNames = new TreeSet<Integer>();
			
			nodeNames.add(n1.getName());
			nodeNames.add(n2.getName());
			
			if(n1.getName() < n2.getName()){
				nodes.add(n1);
				nodes.add(n2);
			}
			
			if(n2.getName() < n1.getName()){
				nodes.add(n2);
				nodes.add(n1);
			}
		}
		
		public Node getNode1(){
			return nodes.get(0);
		}
		
		public Node getNode2(){
			return nodes.get(1);
		}
		
		public boolean contains(int n){
			return nodeNames.contains(n);
		}
		
		public int getNext(int n){
			
			ArrayList<Integer> names = new ArrayList<Integer>();
			for (int m : nodeNames){
				if(m != n){
					names.add(m);
				}
			}
				
			return names.get(0);
		}
		
		public void print(){
			System.out.println(nodes.get(0).getName() + "-" + nodes.get(1).getName());
		}
		
		public String toString(){
			return String.valueOf(nodes.get(0).getName()) + "-" + String.valueOf(nodes.get(1).getName());
		}
	}
	
	public static class AdjMtx{
		
		public Set<String> adjMtx;
		public Set<Integer> nodes;
		
		public AdjMtx(){
			adjMtx = new TreeSet<String>();
			nodes = new TreeSet<Integer>();
		}
		
		public AdjMtx(Edge e){
			adjMtx = new TreeSet<String>();
			nodes = new TreeSet<Integer>();
			addEdge(e);
		}
		
		public void addEdge(Edge e){
			adjMtx.add(e.toString());
			
			nodes.add(e.getNode1().getName());
			nodes.add(e.getNode2().getName());
		}
		
		public int isAdj(int a, int b){
			Edge e = new Edge(new Node(a), new Node(b));
			
			if (adjMtx.contains(e.toString())){
				return 1;
			}
			else{
				return 0;
			}
		}
		
		public int isAdj(Node a, Node b){
			Edge e = new Edge(a, b);
			
			if (adjMtx.contains(e.toString())){
				return 1;
			}
			else{
				return 0;
			}
		}
		
		public int isAdj(Edge e){
			
			if (adjMtx.contains(e.toString())){
				return 1;
			}
			else{
				return 0;
			}
		}
				
	}
	
	public static class Graph{
		public ArrayList<Edge> edges;
		public Set<Integer> nodeNames;
		boolean isBipartitie = true;
		
		public boolean isBipartitie(){
			List<Integer> nodeList = new ArrayList<Integer>(nodeNames);
			List<Integer> nodeListFull = new ArrayList<Integer>(nodeNames);
			ArrayList<Set<Integer>> BFS;
			Set nodeInBFS;
			
			//Current Node
			for(int i = 0; i < nodeList.size(); i++){
				BFS = BFS(nodeList.get(i));			
				nodeInBFS = new TreeSet<Integer>();

				//For every layer
				for(int k = 0; k < BFS.size(); k++){
					//For every edge
					for(Edge e : edges){
						boolean edgeInsameLayer = BFS.get(k).contains(e.getNode1().getName()) & BFS.get(k).contains(e.getNode2().getName());
						if(edgeInsameLayer){
							return false;
						}
					}
				}
				
				//remove nodes already in current BFS
				for(int j = 0; j < BFS.size(); j++){
					nodeInBFS.addAll((Collection) BFS.get(j));
				}
				
				for(int m : nodeListFull){
					if(nodeInBFS.contains(m)){
						nodeList.remove(new Integer(m));
					}
				}
				nodeListFull = new ArrayList<Integer>(nodeList);
			}
						
			return true;
		}
		
		public ArrayList BFSList(){
			
			ArrayList BFSList = new ArrayList<ArrayList<Set>>();
			
			List<Integer> nodeList = new ArrayList<Integer>(nodeNames);
			List<Integer> nodeListFull = new ArrayList<Integer>(nodeNames);
			ArrayList<Set<Integer>> BFS;
			Set nodeInBFS;
			
			//Current Node			
			while(!nodeList.isEmpty()){
				BFS = BFS(nodeList.get(0));
				
				nodeInBFS = new HashSet<Integer>();
				
				//remove nodes already in current BFS
				for(int j = 0; j < BFS.size(); j++){
					nodeInBFS.addAll((Collection) BFS.get(j));
				}
				
				for(int m : nodeListFull){
					if(nodeInBFS.contains(m)){
						nodeList.remove(new Integer(m));
					}
				}
				nodeListFull = new ArrayList<Integer>(nodeList);
							
				BFSList.add(BFS);
			}
			
			return BFSList;
		}
		
		public void printIsBipartitie(){
			if (isBipartitie()){
				ArrayList<ArrayList<Set<Integer>>> BFSList = BFSList();
				
				TreeSet<Integer> outsetA = new TreeSet<Integer>();
				TreeSet<Integer> outsetB = new TreeSet<Integer>();
				
				
				for (int i = 0; i < BFSList.size(); i++){
					Set<Integer> setA = new TreeSet<Integer>();
					Set<Integer> setB = new TreeSet<Integer>();
					ArrayList<Set<Integer>> BFS = BFSList.get(i);
					for(int j = 0; j < BFS.size(); j+=2){
						setA.addAll(BFS.get(j));
						if(j + 1 < BFS.size()){
							setB.addAll(BFS.get(j+1));
						}
					}
					
					//sort
					int firstInA = (int) new ArrayList(setA).get(0);
					int firstInB = (int) new ArrayList(setB).get(0);
					
					if(firstInA < firstInB){
						outsetA.addAll(setA);
						outsetB.addAll(setB);
					}else{
						outsetB.addAll(setA);
						outsetA.addAll(setB);
					};
					
					
				}
				
				System.out.println(outsetA.toString().replaceAll("[,\\[\\]]", ""));
				System.out.println(outsetB.toString().replaceAll("[,\\[\\]]", ""));
				
			}else{
				System.out.print("Not bipartite!");
			}
		}
		
		public Graph(){
			edges = new ArrayList<Edge>();
			nodeNames = new TreeSet<Integer>();
		}
		
		public void addEdge(Edge e){			
			edges.add(e);
			nodeNames.add(e.getNode1().getName());
			nodeNames.add(e.getNode2().getName());
		}
		
		
		public ArrayList<Set<Integer>> BFS(int start){
			ArrayList L = new ArrayList<Set<Integer>>();
			
			//First Layer
			Set lastLayer = new TreeSet<Integer>(Arrays.asList(start));
			L.add(lastLayer);
			
			//Second layer
			ArrayList<Edge> edgesCloneFull = new ArrayList<Edge>(edges);
			ArrayList<Edge> edgesClone = new ArrayList<Edge>(edges);

			Set nextLayer = new TreeSet<Integer>();
			
			//Second layer and on and on
			while(!edgesClone.isEmpty()){
				//A certain layer
				Iterator iter = lastLayer.iterator();
				int numOfNext = 0;
				while (iter.hasNext()) {
				    start = (int) iter.next();
				    for(int i = 0; i < edgesCloneFull.size(); i ++){
						if(edgesCloneFull.get(i).contains(start)){
							numOfNext++;
							nextLayer.add(edgesCloneFull.get(i).getNext(start));
							edgesClone.remove(edgesCloneFull.get(i));
						}
					}
				}
				if(!nextLayer.isEmpty()){
					L.add(new TreeSet(nextLayer));
					edgesCloneFull = new ArrayList<Edge>(edgesClone);
					lastLayer = new TreeSet(nextLayer);
					nextLayer.clear();
				}else{
					break;
				}
			} 
			
			return L;
		}

	}	
}



