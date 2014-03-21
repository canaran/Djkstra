import java.util.ArrayList;


public class Graph {
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
