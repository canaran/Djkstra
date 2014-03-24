

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/*
 * Implements the Vertex class
 * Every vertex has its neighbor list (edge list) and a variable called distFromSource which
 * gives the shortest distance from source after djkstra algorithm is executed.
 * It also implements the Comparable class to compare two vertices according to
 * their distances from source.
 */

class Vertex implements Comparable<Vertex>
{
	public int id;
	public ArrayList<Edge> neighbors; 					// neighbor list (edge)
	public int distFromSource = Integer.MAX_VALUE; 		// distance from source
	public Vertex(int id) {
		this.id = id;
		neighbors = new ArrayList<Edge>();
	}
	public int getId() {
		return id;
	}

	@Override
	public int compareTo(Vertex v) {					// compares two vertices according to their distances from source
		if(distFromSource > v.distFromSource)
			return 0;
		else
			return 1;
	}
}

/*
 * Implements the Edge Class
 * Each edge has a destination and a weight
 * Source is the vertex which has this edge in its neighbor list.
 */

class Edge
{
	public Vertex destination;
	public int weight;
	public Edge(Vertex destination, int weight) {
		this.destination = destination;
		this.weight = weight;
	}
}

class Graph {
	ArrayList<Vertex> vertices;
	ArrayList<Edge> edges;

	Graph() {
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	}

	Graph(ArrayList<Vertex> v, ArrayList<Edge> e) {
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
		setVertices(v);
		setEdges(e);
	}

	private void setVertices(ArrayList<Vertex> a) {
		for(int i=0; i<a.size(); i++) {
			vertices.add(a.get(i));
		}
	}

	private void setEdges(ArrayList<Edge> a) {
		for(int i=0; i<a.size(); i++) {
			edges.add(a.get(i));
		}
	}

	ArrayList<Vertex> getVertices() {
		return vertices;
	}

	ArrayList<Edge> getEdges() {
		return edges;
	}

}

public class Djkstra {

	/*
	 * Executes the Djkstra algorithm using Fibonacci Heap.
	 */
	public static void executeFHeap(Graph g, Vertex source) throws Exception {
		source.distFromSource = 0;
		FibonacciHeap unvisited = new FibonacciHeap();					// create a fibonacci heap for unvisited vertices
		Map<Integer,Node> uMap = new HashMap<Integer,Node>();			// create a HashMap to hold the pointers to the vertices that are in the f-heap.
		for(Vertex v:g.getVertices()) {									// put all the vertices inside the f-heap and in the hashmap.
			Node n = new Node(v.distFromSource, v);						// Fibonacci node has the vertex's distance from source as key and vertex itself as the object
			unvisited.insert(n);
			uMap.put(v.getId(), n);
		}
		unvisited.decreaseKey(uMap.get(source.getId()), 0);				// Decrease the key of source to 0.
		while(unvisited.getSize() != 0) {
			Node n = unvisited.removeMin();								// Extract the vertex, which has the minimum distance from source, from f-heap.
			Vertex v = n.v;
			int weight = n.key;
			for(Edge e : v.neighbors) {									// for each neighbor of that vertex
				Vertex d = e.destination;
				int edge_weight = e.weight;
				int dist = edge_weight + weight;						// Calculate and update(by decreasing keys) neighbor's distances from source if necessary
				if(d.distFromSource > dist) {
					unvisited.decreaseKey(uMap.get(d.getId()), dist);
					d.distFromSource = dist;
				}
			}
		}
	}

	/*
	 * Executes the Djktra Algorithm using simple heap/array.
	 */

	public static void executeArray(Vertex source) {
		source.distFromSource = 0;
		ArrayList<Vertex> unvisited = new ArrayList<Vertex>();			// Create an arrayList for unvisited vertices
		unvisited.add(source);											// Add source to that list
		while(unvisited.size() != 0) {
			Vertex v = findMin(unvisited);								// Extract the vertex, which has the minimum distance from source, from the arraylist.
			for(Edge e : v.neighbors) {									// for each neighbor of that vertex
				Vertex d = e.destination;
				int weight = e.weight;
				int dist = weight + v.distFromSource;					// calculate and update neighbor's distances from source if necessary
				if(d.distFromSource > dist) {
					remove(unvisited, d);
					d.distFromSource = dist;
					unvisited.add(d);
				}
			}
		}
	}

	/*
	 * Returns the vertex, which has the minimum distance from source
	 */

	public static Vertex findMin(ArrayList<Vertex> a) {
		Vertex min = a.get(0);
		int index =0;
		for(int i=0; i< a.size(); i++) {
			Vertex v = a.get(i);
			if(min.distFromSource > v.distFromSource)  {
				min = v;
				index = i;
			}
		}
		a.remove(index);
		return min;
	}

	/*
	 * Removes the specified vertex from the list of vertices
	 */
	public static void remove(ArrayList<Vertex> list, Vertex v) {
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).equals(v)) {
				list.remove(i);
			}
		}
	}

	/*
	 * Checks whether graph is connected or not
	 */
	public static boolean isConnected(Vertex source, int size) {
		boolean[] arr = new boolean[size];

		ArrayList<Vertex> unvisited = new ArrayList<Vertex>();
		unvisited.add(source);
		while(unvisited.size() != 0) {
			Vertex v = unvisited.get(0);
			unvisited.remove(0);
			arr[v.id] = true;
			for(Edge e : v.neighbors) {
				Vertex d = e.destination;
				if(!arr[d.id]) {
					arr[d.id] = true;
					unvisited.add(d);
				}

			}
		}
		for (boolean b:arr) {
			if(!b)
				return false;
		}
		return true;
	}

	/*
	 * checks if a vertex has an edge to all of the other vertices in the graph
	 */

	public static boolean allEdges(boolean[] arr) {
		for(boolean i: arr) {
			if(!i)
				return false;
		}
		return true;
	}

	public static void main(String[] args) throws Exception{
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		Map<Integer, int[]> m = new HashMap<Integer, int[]>();
		/*
		 * Djkstra -s file_name
		 * Gets graph information from a file and executes the Djkstra Algorithm using simple array.
		 */
		if(args[0].compareTo("-s") == 0) {
			System.out.println("Running Djkstra Algorithm using simple array");
			BufferedReader reader = new BufferedReader(new FileReader(args[1]));
			String line = null;
			int source_id = Integer.parseInt(reader.readLine());
			line = reader.readLine();
			String[] s = line.split(" ");
			int v_size = Integer.parseInt(s[0]);
			int e_size = Integer.parseInt(s[1]);
			for(int i=0; i<v_size; i++) {
				Vertex v = new Vertex(i);
				vertices.add(v);
			}
			for(int i=0; i<e_size; i++) {
				line = reader.readLine();
				s = line.split(" ");
				Edge e = new Edge(vertices.get(Integer.parseInt(s[1])), Integer.parseInt(s[2]));
				vertices.get(Integer.parseInt(s[0])).neighbors.add(e);
				edges.add(e);
			}
			Graph g = new Graph(vertices, edges);
			executeArray(vertices.get(source_id));
			for(int i=0; i<vertices.size(); i++) {
				System.out.println(vertices.get(i).distFromSource+" //cost from node " + source_id +" to "+ vertices.get(i).getId());
			}

		}
		/*
		 * Djkstra -f file_name
		 * Gets graph information from a file and executes the Djkstra Algorithm using fibonacci heap.
		 */
		else if (args[0].compareTo("-f") == 0) {
			System.out.println("Running Djkstra Algorithm using fibonnacci heap");
			BufferedReader reader = new BufferedReader(new FileReader(args[1]));
			String line = null;
			int source_id = Integer.parseInt(reader.readLine());
			line = reader.readLine();
			String[] s = line.split(" ");
			int v_size = Integer.parseInt(s[0]);
			int e_size = Integer.parseInt(s[1]);
			for(int i=0; i<v_size; i++) {
				Vertex v = new Vertex(i);
				vertices.add(v);
			}
			for(int i=0; i<e_size; i++) {
				line = reader.readLine();
				s = line.split(" ");
				Edge e = new Edge(vertices.get(Integer.parseInt(s[1])), Integer.parseInt(s[2]));
				vertices.get(Integer.parseInt(s[0])).neighbors.add(e);
				edges.add(e);
			}
			Graph g = new Graph(vertices, edges);
			executeFHeap(g,vertices.get(source_id));
			for(int i=0; i<vertices.size(); i++) {
				System.out.println(vertices.get(i).distFromSource+" //cost from node " + source_id +" to "+ vertices.get(i).getId());
			}
		}
		/*
		 *  Djkstra -r n d x
		 * 	Run in a random	connected graph	with n vertices	and d% of density.
		 *	the	source node	number is x.
		 */
		else if(args[0].compareTo("-r") == 0) {
			Map<Integer, boolean[]> map = new HashMap<Integer, boolean[]>();
			int v_size = Integer.parseInt(args[1]);
			double density = Double.parseDouble(args[2]);
			int source_id = Integer.parseInt(args[3]);
			if(source_id >= v_size || source_id < 0) {
				System.out.println("Source should be in the range of 0 and " + v_size);
				System.exit(0);
			}
			int edge_num;
			do{
				vertices.clear();
				edges.clear();
				map.clear();
				for(int i=0; i<v_size; i++) {
					Vertex v = new Vertex(i);
					vertices.add(v);
					boolean[] bitMap = new boolean[v_size];
					bitMap[i] = true;
					map.put(v.getId(), bitMap);
				}
				edge_num = (int) ((v_size*(v_size-1)/2)*(density/100));
				if(!(edge_num < v_size-1)) {
					for(int i=0; i<edge_num; i++) {
						int source = (int)(Math.random()*v_size);
						int dest =0;

						do {
							if(allEdges(map.get(source))) {
								source = (int)(Math.random()*v_size);
							}
							dest = (int)(Math.random()*v_size);
						} while(map.get(source)[dest]);
						map.get(dest)[source] = true;
						map.get(source)[dest] = true;
						int weight = ((int)(Math.random()*1000))+1;
						Edge e = new Edge(vertices.get(dest),weight);
						vertices.get(source).neighbors.add(e);
						edges.add(e);
					}
				} else {
					System.out.println("Connected graph cannot be formed with this density!");
					System.exit(0);
				}
			} while (!isConnected(vertices.get(source_id), v_size));

			Graph g = new Graph(vertices, edges);
			long startTime = System.currentTimeMillis();
			executeArray(vertices.get(source_id));
			long endTime   = System.currentTimeMillis();
			long totalTime = (endTime - startTime);

			startTime = System.currentTimeMillis();
			executeFHeap(g, vertices.get(source_id));
			endTime   = System.currentTimeMillis();
			long totalTime_fheap = (endTime - startTime);
			for(int i=0; i<vertices.size(); i++) {
				System.out.println(vertices.get(i).distFromSource+" //cost from node " + source_id +" to "+ vertices.get(i).getId());
			}
			System.out.println("\n**************\n");
			System.out.println("Executing Djkstra Algorithm using simple array with " + v_size +" vertices and "+ density + "% of edge density takes " + totalTime + "ms");
			System.out.println("Executing Djkstra Algorithm using fibonacci heap with " + v_size +" vertices and "+ density + "% of edge density takes " + totalTime_fheap + "ms");


		} else {
				System.out.println("Error in syntax");
		}
	}

}
