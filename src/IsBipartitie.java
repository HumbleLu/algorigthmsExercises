import java.util.*;

public class IsBipartitie {
		
	public static void main(String[] args){		
		Graph g = new Graph();

		g.addEdge(new Edge(new Node(2), new Node(1)));
		g.addEdge(new Edge(new Node(2), new Node(3)));
		g.addEdge(new Edge(new Node(3), new Node(4)));
		g.addEdge(new Edge(new Node(5), new Node(2)));
		g.addEdge(new Edge(new Node(6), new Node(7)));
		g.addEdge(new Edge(new Node(2), new Node(4)));
		
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
			nodeNames = new HashSet<Integer>();
			
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
			adjMtx = new HashSet<String>();
			nodes = new HashSet<Integer>();
		}
		
		public AdjMtx(Edge e){
			adjMtx = new HashSet<String>();
			nodes = new HashSet<Integer>();
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
			HashSet<Integer> nodeList = new HashSet<Integer>(nodeNames);
			HashSet<Integer> nodeListFull = new HashSet<Integer>(nodeNames);
			ArrayList<Set<Integer>> BFS;
			Set nodeInBFS;
			
			//Current Node
			for(int n : nodeNames){
				BFS = BFS(n);			
				nodeInBFS = new HashSet<Integer>();

				//For every layer
				for(int i = 0; i < BFS.size(); i++){
					//For every edge
					for(Edge e : edges){
						boolean edgeInsameLayer = BFS.get(i).contains(e.getNode1().getName()) & BFS.get(i).contains(e.getNode2().getName());
						if(edgeInsameLayer){
							return false;
						}
					}
				}
				
				//remove nodes already in current BFS
				for(int i = 0; i < BFS.size(); i++){
					nodeInBFS.addAll((Collection) BFS.get(i));
				}
				
				for(int m : nodeListFull){
					if(nodeInBFS.contains(m)){
						nodeList.remove(m);
					}
				}
				nodeListFull = new HashSet<Integer>(nodeList);
			}
						
			return true;
		}
		
		public void printIsBipartitie(){
			if (isBipartitie()){
				System.out.println("Not Yet");
			}else{
				System.out.print("Not bipartite!");
			}
		}
		
		public Graph(){
			edges = new ArrayList<Edge>();
			nodeNames = new HashSet<Integer>();
		}
		
		public void addEdge(Edge e){			
			edges.add(e);
			nodeNames.add(e.getNode1().getName());
			nodeNames.add(e.getNode2().getName());
		}
		
		
		public ArrayList<Set<Integer>> BFS(int start){
			ArrayList L = new ArrayList<Set<Integer>>();
			
			//First Layer
			Set lastLayer = new HashSet<Integer>(Arrays.asList(start));
			L.add(lastLayer);
			
			//Second layer
			ArrayList<Edge> edgesCloneFull = new ArrayList<Edge>(edges);
			ArrayList<Edge> edgesClone = new ArrayList<Edge>(edges);

			Set nextLayer = new HashSet<Integer>();
			
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
					L.add(new HashSet(nextLayer));
					edgesCloneFull = new ArrayList<Edge>(edgesClone);
					lastLayer = new HashSet(nextLayer);
					nextLayer.clear();
				}else{
					break;
				}
			} 
			
			return L;
		}

	}	
}



