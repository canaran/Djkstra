

/*
	 * The node of Fibonacci Heap. It holds the necessary info.
	 * - left and right pointers for doubly linkedlist
	 * - degree, childCut for concatanate cut
	 * - pointers for child and parent nodes
	 */
public class Node {
		Node child;
		Node parent;

		Node left;
		Node right;

		boolean childCut;
		int degree;
		int key;
		Vertex v;

		
		public int getKey() {
			return key;
		}

		public Vertex getVertex() {
			return v;
		}

		public Node(int key, Vertex v) {
			this.key = key;
			this.v = v;
		}
}
