

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Vertex implements Comparable<Vertex>
{
	public int id;
	public ArrayList<Edge> neighbors;
	public int distFromSource = Integer.MAX_VALUE;
	public Vertex(int id) {
		this.id = id;
		neighbors = new ArrayList<Edge>();
	}
	public int getId() {
		return id;
	}
	public int compareTo(Vertex v) {
		if(distFromSource > v.distFromSource)
			return 0;
		else
			return 1;
	}

	public boolean equals(Vertex v) {
		if(id == v.id)
			return true;
		else
			return false;
	}
}

class Edge
{
	public Vertex destination;
	public int weight;
	public Edge(Vertex destination, int weight) {
		this.destination = destination;
		this.weight = weight;
	}
}

public class Djkstra {

	/*
	 * Executes the Djkstra algorithm using Fibonacci Heap.
	 */
	public static void executeFHeap(Graph g, Vertex source) throws Exception {
		source.distFromSource = 0;
		FibonacciHeap unvisited = new FibonacciHeap();
		Map<Integer,Node> uMap = new HashMap<Integer,Node>();
		for(Vertex v:g.getVertices()) {
			Node n = new Node(v.distFromSource, v);
			unvisited.insert(n);
			uMap.put(v.getId(), n);
		}
		unvisited.decreaseKey(uMap.get(source.getId()), 0);
		while(unvisited.getSize() != 0) {
			Node n = unvisited.removeMin();
			Vertex v = n.v;
			int weight = n.key;
			//System.out.println(v.distFromSource);
			for(Edge e : v.neighbors) {
				Vertex d = e.destination;
				int edge_weight = e.weight;
				int dist = edge_weight + weight;
				if(d.distFromSource > dist) {
					unvisited.decreaseKey(uMap.get(d.getId()), dist);
					d.distFromSource = dist;
				}
			}
		}
	}

	public static void executeArray(Vertex source) {
		source.distFromSource = 0;
		ArrayList<Vertex> unvisited = new ArrayList<Vertex>();
		unvisited.add(source);
		while(unvisited.size() != 0) {
			Vertex v = findMin(unvisited);
			//System.out.println(v.distFromSource);
			for(Edge e : v.neighbors) {
				Vertex d = e.destination;
				int weight = e.weight;
				int dist = weight + v.distFromSource;
				if(d.distFromSource > dist) {
					remove(unvisited, d);
					d.distFromSource = dist;
					unvisited.add(d);
				}
			}
		}
	}

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

	public static void remove(ArrayList<Vertex> list, Vertex v) {
		for(int i=0; i<list.size(); i++) {
			if(list.get(i).equals(v)) {
				list.remove(i);
			}
		}
	}

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
		if(args[0].compareTo("-s") == 0) {
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
			System.out.println("*****");

		} else if (args[0].compareTo("-f") == 0) {
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
		} else if(args[0].compareTo("-r") == 0) {
			Map<Integer, boolean[]> map = new HashMap<Integer, boolean[]>();
			int v_size = Integer.parseInt(args[1]);
			double density = Double.parseDouble(args[2]);
			int source_id = Integer.parseInt(args[3]);
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
					break;
				}
			} while (!isConnected(vertices.get(source_id), v_size));

			/*while(!isConnected(vertices.get(source_id), v_size)) {
				int source = (int)(Math.random()*v_size);
				int dest =0;

				do {
					dest = (int)(Math.random()*v_size);
				} while(map.get(source)[dest]);

				map.get(source)[dest] = true;
				int weight = ((int)(Math.random()*1000))+1;
				Edge e = new Edge(vertices.get(dest),weight);
				vertices.get(source).neighbors.add(e);
				edges.add(e);
				edge_num++;
			}*/
			System.out.println("Edge number: " + edge_num);
			Graph g = new Graph(vertices, edges);
			long startTime = System.currentTimeMillis();


			executeArray(vertices.get(source_id));
			long endTime   = System.currentTimeMillis();
			long totalTime = (endTime - startTime);
			System.out.println(totalTime);
			/*for(int i=0; i<vertices.size(); i++) {
				System.out.println("Weigth for source" + source_id +" to "+ vertices.get(i).getId() + " is " +
						vertices.get(i).distFromSource);
			}*/
			startTime = System.currentTimeMillis();
			executeFHeap(g, vertices.get(source_id));
			endTime   = System.currentTimeMillis();
			totalTime = (endTime - startTime);
			System.out.println(totalTime);

			/*for(int i=0; i<vertices.size(); i++) {
				System.out.println(vertices.get(i).distFromSource+" //cost from node " + source_id +" to "+ vertices.get(i).getId());
			}*/
			/*for(int i=0; i<vertices.size(); i++) {
				System.out.println("Weigth for source" + source_id +" to "+ vertices.get(i).getId() + " is " +
						vertices.get(i).distFromSource);
			}*/



		} else {
				System.out.println("Error in syntax");
		}
	}

}
