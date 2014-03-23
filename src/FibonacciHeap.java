import java.util.ArrayList;


public class FibonacciHeap {


	// Variables for FibonacciHeap class

	Node min;	// holds the min key node

	int size;		// number of nodes in the heap
	private static final double oneOverLogPhi = //
            1.0 / Math.log((1.0 + Math
                .sqrt(5.0)) / 2.0);
	//Constructor
	FibonacciHeap() {
		min = null;
	}

	/*
	 * Returns the list of nodes in the doubly linked list
	 */
	private ArrayList<Node> getListofNodeInDb (Node list) {
		ArrayList<Node> l = new ArrayList<Node>();
		Node next = list.right;
		l.add(list);
		while(next!=list) {
			l.add(next);
			next = next.right;
		}
		return l;
	}

	// remove a node from doubly linked list
	private Node removeNodeFromDbList(Node list, Node n) {
		// if n is pointing itself, which means there is one element in the list that is Node n, return null
		if(n.left == n) {
			return null;
		} else {
			n.right.left = n.left;
			n.left.right = n.right;
			return list;
		}
	}

	// inserts a node from
	private Node insertNodeToDbList(Node list, Node n) {
		if(list == null) {
			n.right = n;
			n.left = n;
			return n;
		} else {
			n.right = list.right;
			n.left = list;
			list.right.left = n;
			list.right = n;
			return list;
		}
	}

	/*
	 * Decreases the key of a node to the specified key
	 */
	void decreaseKey(Node n, int key) throws Exception{

		if(key > n.key) { 	// if new key is greater than current key, throw an exception
			throw new Exception("New key is greater than current key!");
		} else {
			n.key = key;
			//check if n has a parent (not a root node) and if new key is less than its parent's key
			if((n.parent != null) && (n.key < n.parent.key)) {
				Node z = n.parent;
				separate(n, n.parent);
				cascadingCut(z);
			}

			//check if new key is less than min key. If so, update min.
			if(n.key < min.key) {
				min = n;
			}
		}
	}

	/*
	 * if there is parent of the node and if it has lost a child before (n.childCut= true), separate
	 * n from its parent and make a recursive call with its parent. if childCut is false, update it
	 */
	private void cascadingCut(Node n) {
		if(n.parent != null) {
			if(n.childCut) {
				separate(n, n.parent);
				cascadingCut(n.parent);
			} else {
				n.childCut = true;
			}
		}
	}

	/* Cut the link between two nodes and put the cut node back to heap
	 * n1 is in the child list of n2
	 * n2 is the parent of n1
	 */
	private void separate(Node n1, Node n2) {
		n2.child = removeNodeFromDbList(n2.child, n1);
		n2.degree--;

		min = insertNodeToDbList(min, n1);
		n1.parent = null;
		n1.childCut = false;
	}

	/*
	 * Removes a specified node from the heap
	 */
	void removeNodeFromHeap(Node n) throws Exception{
		decreaseKey(n, Integer.MIN_VALUE);
		removeMin();
	}

	/*
	 * Extracts the minimum node of the heap according to node's key
	 */
	Node removeMin() {
		Node m = min;
		if(m != null) {
			if(m.child != null) {
				for(Node n : getListofNodeInDb(m.child)) {
					min = insertNodeToDbList(min, n);
					n.parent = null;
				}
			}

			min = removeNodeFromDbList(min, m);

			if(m==m.right) {
				min = null;
			} else {
				min = m.right;
				meld();
			}
			size--;

		}
		return m;
	}

	/*
	 * Returns the size
	 */
	public int getSize() {
		return size;
	}

	/*
	 * inserts the node in to the heap
	 */
	public void insert(Node n) {
		n.parent = null;
		n.child = null;
		n.left = n;
		n.right = n;
		n.degree = 0;
		n.childCut = false;
		min = insertNodeToDbList(min, n);
		if(n.key <= min.key) {
			min = n;
		}

		size++;

	}

	/*
	 * Melds the trees in the heap according to their degrees
	 */
	private void meld() {
		int degreeArrSize = ((int) Math.floor(Math.log(size)* oneOverLogPhi)) + 1;   // Determine the degree array size using the size of the heap
		Node degreeArr[] = new Node[degreeArrSize];
		if(min != null) {

			for(Node n : getListofNodeInDb(min)) {									// for each root node of the trees
				int ndeg = n.degree;												// Get degree of that node
				while(degreeArr[ndeg] != null) {									// while there is another tree with same degree, meld them
					Node n2 = degreeArr[ndeg];

					if(n2.key < n.key) {
						// swap n and n2
						Node temp = n;
						n = n2;
						n2 = temp;
					}
					min = removeNodeFromDbList(min, n2);							// Remove n2 from the list and make n2 child of n
					link(n2, n);													// Link n and n2 (n parent, n2 child)

					degreeArr[ndeg] = null;
					ndeg++;
				}
				degreeArr[ndeg] = n; 												// Update degree
			}
		}

		// put the nodes/trees in the degree array into the list and determine min.
		min = null;
		for(int i=0; i<degreeArrSize; i++) {
			if(degreeArr[i] != null) {
				min = insertNodeToDbList(min, degreeArr[i]);
				if(degreeArr[i].key <= min.key) {
					min = degreeArr[i];
				}
			}
		}

	}

	/*
	 * Links two nodes
	 */
	private void link(Node n1, Node n2) {
		n1.parent = n2;
		n1.right = n1;
		n1.left = n1;
		n2.child = insertNodeToDbList(n2.child, n1);

		n1.childCut = false;
		n2.degree++;

	}



}
