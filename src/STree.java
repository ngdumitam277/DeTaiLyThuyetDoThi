import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class STree {

	private State root;

	static boolean JUSTKEEPSWIMMING = true;

	public STree(State r){
		root = r;

	}

	private void cleanLog() {
		try {
			FileWriter fw = new FileWriter("log.txt");
			fw.write(new Date().toString() + '\n');
			fw.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}

	public State getRoot() {
		return root;
	}

	
	private String[] solution(State solution) {
		JUSTKEEPSWIMMING = false;
		
		
		
		String[] result = {solution.getPath()};
		return result;
	}

	private String[] solution(State solution, int generated, int repeated, int fringe, int seen, long start) {
		JUSTKEEPSWIMMING = false;
		
		
		

		
		double time = (System.nanoTime() - start) / 1000000000.0;
		String[] data = {
				solution.getPath(), 
				"\n",
				"Số lần tạo: " + Integer.toString(generated), 
				"Số lần lặp: " + Integer.toString(repeated), 
				"Ngăn Fringe: " + Integer.toString(fringe), 
				"Số lược thăm dò: " + Integer.toString(seen), 
				"Thời gian chạy: " + String.valueOf((time)) 
		};
		return data;
	}

	private String[] failure() {
		String[] fail = {"Qúa trình tìm kếm đường hoàn thành và chưa tìm được đường"};
		return fail;
	}

	public String[] BFS() {
		JUSTKEEPSWIMMING = true;
		
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		
		State node = root;
		HashSet<String> explored = new HashSet<String>();
		Queue<State> frontier = new LinkedList<State>();
		frontier.add(node);

		if(node.isGoal())
			return solution(node, gen, rep, frontier.size(), explored.size(), start);

		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.poll();
			explored.add(node.getStateString());

			for(char c : node.getValidMoves()) {
				State child = new State(node, c);
				gen++;
				
				
				
				if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
					
					if(child.isGoal())
						return solution(child, gen, rep, frontier.size(), explored.size(), start);
					frontier.add(child);
				}
				else if(explored.contains(child.getStateString()))
					rep++;
			}
		}
		return new String[0];
	}
	
	public String[] DFS() {
		JUSTKEEPSWIMMING = true;
		
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		State node = root;
		HashSet<String> explored = new HashSet<String>();
		Stack<State> frontier = new Stack<State>();
		
		frontier.push(node);
		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.pop();
			if( !explored.contains(node.getStateString()) ) {
				explored.add(node.getStateString());
				if(node.isGoal())
					return solution(node, gen, rep, frontier.size(), explored.size(), start);
				for(char c : node.getValidMoves()) {
					State child = new State(node, c);
					gen++;
					if( !explored.contains(child.getStateString()) ) {
						frontier.add(child);
					}
					else
						rep++;
				}
			}
			else
				rep++;
		}
		return new String[0];
	}
	

	private int getCostFromPQ(PriorityQueue<State> pq, State comp) {
		for(Object orig : pq.toArray()) {
			if( ((State) orig).equals(comp) )
				return ((State) orig).getCost();
		}
		return -1;
	}

	public String[] UCS() {
		
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		
		State node = root;
		PriorityQueue<State> frontier = new PriorityQueue<State>();
		HashSet<String> explored = new HashSet<String>();
		frontier.add(node);

		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.poll();
			if(node.isGoal())
				return solution(node, gen, rep, frontier.size(), explored.size(), start);
			explored.add(node.getStateString());

			for(char c : node.getValidMoves()) {
				State child = new State(node, c);
				gen++;
				if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
					frontier.add(child);
				}
				else if(frontier.contains(child) && getCostFromPQ(frontier, child) > child.getCost()){
					frontier.remove(child);
					frontier.add(child);
				}
				else if(explored.contains(child.getStateString()))
					rep++;
			}
		}
		return new String[0];
	}
	

	private int getOGFromPQ(PriorityQueue<State> pq, State comp) {
		for(Object orig : pq.toArray()) {
			if( ((State) orig).equals(comp) )
				return ((State) orig).openGoals();
		}
		return -1;
	}

	public String[] GreedyOG() {
		
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		
		State node = root;
		PriorityQueue<State> frontier = new PriorityQueue<State>(11, new OpenGoals());
		HashSet<String> explored = new HashSet<String>();
		frontier.add(node);

		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.poll();
			if(node.isGoal())
				return solution(node, gen, rep, frontier.size(), explored.size(), start);
			explored.add(node.getStateString());

			for(char c : node.getValidMoves()) {
				State child = new State(node, c);
				gen++;
				if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
					frontier.add(child);
				}
				else if(frontier.contains(child) && getOGFromPQ(frontier, child) > child.openGoals()){
					frontier.remove(child);
					frontier.add(child);
				}
				else if(explored.contains(child.getStateString()))
					rep++;
			}
		}
		return new String[0];
	}
	

	private int getMDFromPQ(PriorityQueue<State> pq, State comp) {
		for(Object orig : pq.toArray()) {
			if( ((State) orig).equals(comp) )
				return ((State) orig).manhDist();
		}
		return -1;
	}

	public String[] GreedyMD() {
		
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		
		State node = root;
		PriorityQueue<State> frontier = new PriorityQueue<State>(11, new ManhDist());
		HashSet<String> explored = new HashSet<String>();
		frontier.add(node);

		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.poll();
			if(node.isGoal())
				return solution(node, gen, rep, frontier.size(), explored.size(), start);
			explored.add(node.getStateString());

			for(char c : node.getValidMoves()) {
				State child = new State(node, c);
				gen++;
				if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
					frontier.add(child);
				}
				else if(frontier.contains(child) && getMDFromPQ(frontier, child) > child.manhDist()){
					frontier.remove(child);
					frontier.add(child);
				}
				else if(explored.contains(child.getStateString()))
					rep++;
			}
		}
		return new String[0];
	}
	

	private int getStarMDFromPQ(PriorityQueue<State> pq, State comp) {
		for(Object orig : pq.toArray()) {
			if( ((State) orig).equals(comp) )
				return ((State) orig).manhDist() + ((State) orig).getCost();
		}
		return -1;
	}

	public String[] AStarMD() {
		
		long start = System.nanoTime();
		int rep = 0;
		int gen = 1;

		
		State node = root;
		PriorityQueue<State> frontier = new PriorityQueue<State>(11, new StarMD());
		HashSet<String> explored = new HashSet<String>();
		frontier.add(node);

		while(JUSTKEEPSWIMMING) {
			if(frontier.peek() == null)
				return failure();
			node = frontier.poll();
			if(node.isGoal())
				return solution(node, gen, rep, frontier.size(), explored.size(), start);
			explored.add(node.getStateString());

			for(char c : node.getValidMoves()) {
				State child = new State(node, c);
				gen++;
				if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
					frontier.add(child);
				}
				else if(frontier.contains(child) && getStarMDFromPQ(frontier, child) > (child.manhDist() + child.getCost())){
					frontier.remove(child);
					frontier.add(child);
				}
				else if(explored.contains(child.getStateString()))
					rep++;
			}
		}
		return new String[0];
	}
	

		private int getStarOGFromPQ(PriorityQueue<State> pq, State comp) {
			for(Object orig : pq.toArray()) {
				if( ((State) orig).equals(comp) )
					return ((State) orig).openGoals() + ((State) orig).getCost();
			}
			return -1;
		}

		public String[] AStarOG() {
			
			long start = System.nanoTime();
			int rep = 0;
			int gen = 1;

			
			State node = root;
			PriorityQueue<State> frontier = new PriorityQueue<State>(11, new StarOG());
			HashSet<String> explored = new HashSet<String>();
			frontier.add(node);

			while(JUSTKEEPSWIMMING) {
				if(frontier.peek() == null)
					return failure();
				node = frontier.poll();
				if(node.isGoal())
					return solution(node, gen, rep, frontier.size(), explored.size(), start);
				explored.add(node.getStateString());

				for(char c : node.getValidMoves()) {
					State child = new State(node, c);
					gen++;
					if( !explored.contains(child.getStateString()) && !frontier.contains(child) ) {
						frontier.add(child);
					}
					else if(frontier.contains(child) && getStarOGFromPQ(frontier, child) > (child.openGoals() + child.getCost())){
						frontier.remove(child);
						frontier.add(child);
					}
					else if(explored.contains(child.getStateString()))
						rep++;
				}
			}
			return new String[0];
		}

}



