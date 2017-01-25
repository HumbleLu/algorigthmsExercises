import java.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Algo2_5 {
	public static void main(String args[]){
		
		Schedual s = new Schedual();
						
		try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	s.addReq(new Interval(Integer.valueOf(line.split(" ")[0]), Integer.valueOf(line.split(" ")[1])));
		    }
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		s.printSchedual();
	}
	
	public static class Interval implements Comparable<Interval>{
		int start;
		int finish;
		public int length;
		
		public Interval(int start, int finish){
			this.start = start;
			this.finish = finish;
			this.length = finish - start;
		}
		
		public void setStart(int start){
			this.start = start;
			this.length = finish - start;
		}
		
		public void setEnd(int finish){
			this.finish = finish;
			this.length = finish - start;
		}
		
		public int getStart(){
			return start;
		}
		
		public int getFinish(){
			return finish;
		}
		
		public int getLength(){
			return length;
		}

		public boolean compatible(Interval other){
			if(this.start == other.getStart() || this.finish == other.getFinish()){
				return false;
			}
			if(this.start >= other.getStart() & this.start < other.getFinish()){
				return false;
			}
			if(this.start < other.getStart() & this.finish > other.getStart() & this.finish < other.getFinish()){
				return false;
			}
			if(this.start < other.getStart() & this.finish > other.getFinish()){
				return false;
			}
			
			
			
			if(this.start == other.getFinish()){
				return true;
			}
			
			return true;
		}
		
		@Override
	    public int hashCode() {
			return Objects.hash(this.start,this.finish);
	    }
		
		@Override
		public boolean equals(Object other){
			if (other instanceof Interval){
				Interval that = (Interval) other;
				return this.start == that.getStart() & this.finish == that.getFinish();
			}
			return false;
		}
		
		@Override
		public String toString(){
			return String.valueOf(this.start) + "-" + String.valueOf(this.finish);
		}
		
		@Override
		public int compareTo(Interval other) {
			if(this.start == other.getStart()){
				return this.finish - other.getFinish();
			}else{
				return this.start - other.start;
			}
	        
	    }
	}
	
	public static class Schedual{
		ArrayList<Interval> requests;
		
		public Schedual(){
			requests = new ArrayList<Interval>();
		}
		
		public void addReq(Interval interval){
			requests.add(interval);
			Collections.sort(requests);
		}
		
		public void printSchedual(){
			int totalRoom = 0;
			
			ArrayList<ArrayList<Interval>> ret = new ArrayList<ArrayList<Interval>>();

			ArrayList<Interval> totalReq = new ArrayList<Interval>(requests);
			ArrayList<Interval> totalReqChk = new ArrayList<Interval>(requests);
			
			while(totalReq.size() != 0){
				ArrayList<Interval> rSet = new ArrayList<Interval>(totalReq);
				ArrayList<Interval> rSetChk = new ArrayList<Interval>(totalReq);
				ArrayList<Interval> aSet = new ArrayList<Interval>();
			
				while(rSetChk.size() != 0){
					//find the req with minimum length
					Interval minReq = rSetChk.get(0);
					
					//add to set A
					aSet.add(minReq);
					
					//remove not compatible intervals in R set
					for(Interval rReq : rSet){
						if(!minReq.compatible(rReq)){
							rSetChk.remove(rReq);
						}
					}
				}
				
				//sorting
				Collections.sort(aSet);
				ret.add(aSet);
				for(Interval aReq: aSet){
					totalReq.remove(aReq);
				}
			}
			
			System.out.println(ret.size());
			for(ArrayList<Interval> A: ret){
				System.out.println(A.toString().replaceAll("[\\]\\[,]",""));
			}
		}
	}
}
