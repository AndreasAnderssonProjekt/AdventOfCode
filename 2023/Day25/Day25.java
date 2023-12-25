package AdventOfCode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Day25{
	
	private class Node{
		String name;
		String[] neighbours;
		
		public Node(String name, String[] neighbours) {
			this.name = name;
			this.neighbours = neighbours;
		}
		
	}
	private class Network_Flow {
	    private int[][] network;
	    private int source;
	    private int sink;
	
	    public Network_Flow(int[][] network, int s, int t) {
	        assert is_valid_network(network, s, t);
	        this.network = network;
	        this.source = s;
	        this.sink = t;
	    }
	
	    public boolean is_valid_network(int[][] network, int s, int t) {
	        int n = network.length;
	        for (int i = 0; i < network.length; i++) {
	            if (network[i][s] != 0) {
	                return false;
	            }
	            if (network[t][i] != 0) {
	                return false;
	            }
	        }
	        return true;
	    }
	
	    public List<int[]> Ford_Fulkerson() {
	    	List<int[]> connectionsToRemove = new ArrayList<>();
	        List<Integer> path = new ArrayList<>();
	        int max_flow = 0;
	        
	        int[][] residual = new int[network.length][network[0].length];
	        for(int i = 0; i < residual.length; i++) {
	        	for(int j = 0; j < residual[0].length; j++) {
	        		residual[i][j] = network[i][j];
	        	}
	        }
	        
	        while (BFS(residual, path)) {
	            int b = bottleneck(path);
	            for (int i = 0; i < path.size() - 1; i++) {
	                int u = path.get(i);
	                int v = path.get(i + 1);
	
	                residual[u][v] -= b;
	                residual[v][u] += b;
	                if (u == source) {
	                    max_flow += b;
	                }
	                if (v == source) {
	                    max_flow -= b;
	                }
	            }
	            path = new ArrayList<>();
	        }
	        
	        boolean[] isVisited = new boolean[residual.length]; 
	        dfs(residual, source , isVisited);
	        System.out.println(Arrays.toString(isVisited));
	        for (int i = 0; i < residual.length; i++) {
	            for (int j = 0; j < residual[0].length; j++) {
	                if (network[i][j] == 1 && isVisited[i] && !isVisited[j]) {
	                	connectionsToRemove.add(new int[] {i, j});
	                    System.out.println(i + " - " + j);
	                }
	            }
	        }
	        
	        return connectionsToRemove;
	    }
	
	    private boolean BFS(int[][] network, List<Integer> path) {
	        int[] parent = new int[network.length];
	        Arrays.fill(parent, -1);
	        boolean[] visited = new boolean[network.length];
	        visited[source] = true;
	
	        Queue<Integer> queue = new LinkedList<>();
	        queue.add(source);
	
	        while (!queue.isEmpty()) {
	            int u = queue.poll();
	
	            for (int v = 0; v < network.length; v++) {
	                if (!visited[v] && network[u][v] > 0) {
	                    queue.add(v);
	                    visited[v] = true;
	                    parent[v] = u;
	                }
	            }
	        }
	
	        if (visited[sink]) {
	            int v = sink;
	            while (v != source) {
	                path.add(v);
	                v = parent[v];
	            }
	            path.add(source);
	            Collections.reverse(path);
	            return true;
	        }
	
	        return false;
	    }
	    
	    private static void dfs(int[][] residual, int source, boolean[] visited) {
			visited[source] = true;
			for (int i = 0; i < residual.length; i++) {
			if (residual[source][i] > 0 && !visited[i]) {
			    dfs(residual, i, visited);
				}
			}
	    }
	
	    private int bottleneck(List<Integer> path) {
	        int b = Integer.MAX_VALUE;
	        for (int i = 0; i < path.size() - 1; i++) {
	            int u = path.get(i);
	            int v = path.get(i + 1);
	            b = Math.min(b, network[u][v]);
	        }
	        return b;
	    }
	    
	   
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("C:\\Users\\Andre\\eclipse-workspace\\AdventOfCOde\\src\\testExempel");
		Scanner sc = new Scanner(file);
		Day25 d = new Day25();
		Map<String, Integer> nodes = new HashMap<>();
		Map<Integer, String> inverse = new HashMap<>();
		Map<String, List<String>> map = new HashMap<>();
 		int nodeNr = 0;
		List<String> lines = new ArrayList<>();
		while(sc.hasNext()) {
			String line = sc.nextLine();
			lines.add(line);
		}
		
		for(String line : lines) {
			String[] components = line.split(": ");
			if(!nodes.containsKey(components[0])) {
				nodes.put(components[0], nodeNr);
				inverse.put(nodeNr, components[0]);
				nodeNr += 1;
			}
			if(!map.containsKey(components[0])) map.put(components[0], new ArrayList<String>());
			
			
			String[] neighbours = components[1].split(" ");
			
			for(int i = 0; i < neighbours.length; i++) {
				if(!nodes.containsKey(neighbours[i])) {
					nodes.put(neighbours[i], nodeNr);
					
					inverse.put(nodeNr, neighbours[i]);
					nodeNr += 1;
				}
				if(!map.containsKey(neighbours[i])) map.put(neighbours[i], new ArrayList<String>());
				map.get(neighbours[i]).add(components[0]);
				map.get(components[0]).add(neighbours[i]);
			}
			
			
			
		}
		
		
		
		int[][] connections = new int[nodes.size()][nodes.size()];
		
		for(String line : lines) {
			String[] components = line.split(": ");
			String[] neighbours = components[1].split(" ");
			for(int i = 0; i < neighbours.length; i++) {
				connections[nodes.get(components[0])][nodes.get(neighbours[i])] = 1;
				connections[nodes.get(neighbours[i])][nodes.get(components[0])] = 1;
			}
		}
		
		
		Network_Flow nf = d.new Network_Flow(connections, 0, connections.length - 1);
		List<int[]> connectionsToRemove = nf.Ford_Fulkerson();
		System.out.println(nodes);
		
		System.out.println("MAP: " + map);
		for(int[] i : connectionsToRemove) {
			map.get(inverse.get(i[0])).remove(inverse.get(i[1]));
			map.get(inverse.get(i[1])).remove(inverse.get(i[0]));
		}
		
		int start1 = connectionsToRemove.get(0)[0];
		int start2 = connectionsToRemove.get(0)[1];
		System.out.println("MAP: " + map);
		Queue<String> q = new LinkedList<>();
		Set<String> set1 = new HashSet<>();
		
		
			q.add(inverse.get(start1));
			set1.add(inverse.get(start1));
		
		
		
		
		System.out.println("INV : " + inverse);
		System.out.println(inverse.get(start1));
		
		while(!q.isEmpty()) {
			String c = q.poll();
			System.out.println("HEJ : " + c);
			for(String s : map.get(inverse.get(nodes.get(c)))){
				if(!set1.contains(s)) {
					set1.add(s);
					q.add(s);
				}
			}
		}
		
		//Map not correct.
		
		q = new LinkedList<>();
		Set<String> set2 = new HashSet<>();
		if(map.get(inverse.get(start2)) != null) {
		for(String s : map.get(inverse.get(start2))) {
			q.add(inverse.get(start2));
			set2.add(inverse.get(start2));
		}
		}
		System.out.println("INV : " + inverse);
		System.out.println(inverse.get(start1));
		
		while(!q.isEmpty()) {
			String c = q.poll();
			System.out.println("HEJ : " + c);
			for(String s : map.get(inverse.get(nodes.get(c)))){
				if(!set2.contains(s)) {
					set2.add(s);
					q.add(s);
				}
			}
		}
		
		System.out.println(set1);
		System.out.println(set2);
		System.out.println(set1.size() * set2.size());
		
		
	}
	
	 
	
}

