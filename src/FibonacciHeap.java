
import java.util.ArrayList;
import java.util.List;


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
		//System.out.println(next.left.v.getId());
		//System.out.println(next.right.v.getId());
		//System.out.println(list.v.getId() + " " + list.left.v.getId() );
		l.add(list);
		//73 247
		while(next!=list) {
			//System.out.println(next.v.getId() + " vs " + list.v.getId());
			l.add(next);
			next = next.right;
		}
		return l;
		/*Node next = list.right;
		int res = 1;
		while(next.v.getId()!=list.v.getId()) {
			next = next.right;
			res++;
		}
		System.out.println("size of the list"+res);
		System.out.println("can");
		Node[] l = new Node[res];
		l[0] = next;
		next = list.right;
		int i = 1;
		while(next.v.getId()!=list.v.getId()) {
			l[i] = next;
			next = next.right;
			i++;
		}
		return l;*/
	}

	// remove a node from doubly linked list
	private Node removeNodeFromDbList(Node list, Node n) {
		// if n is pointing itself, which means there is one element in the list that is Node n, return null
		if(n.left == n) {
			return null;
		} else {
			n.right.left = n.left;
			n.left.right = n.right;
			return n.right;
		}
	}

	// inserts a node to the doubly linked list
	private Node insertNodeToDbList(Node list, Node n) {
		if(list == null) {
			n.right = n;
			n.left = n;
			return n;
		} else {
			n.left = list;
			n.right = list.right;
			list.right = n;
			n.right.left = n;

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
			Node z = n.parent;
			if((z != null) && (n.key < z.key)) {
				separate(n, z);
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
		Node z = n.parent;
		if(z != null) {
			if(n.childCut) {
				separate(n, z);
				cascadingCut(z);
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
		
		if(n2.degree == 0) 
			n2.child = null;
		
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
		//System.out.println("in remove min");
		//System.out.println(min.key);
		//System.out.println(min.v.getId());
		Node m = min;
		//System.out.println("min: " + m.v.getId());
		if(m != null) {
			//System.out.println("m is not null");
			if(m.child != null) {
				//System.out.println("m.child is not null");
				ArrayList<Node> arr =getListofNodeInDb(m.child);
				//System.out.println("1 finished");
				for (Node n:arr) {
					m.child = removeNodeFromDbList(m.child, n);
					min = insertNodeToDbList(min, n);
					n.parent = null;
					//System.out.println("in r");
				}
				//System.out.println("out");
			}
			//System.out.println("done");

			min = removeNodeFromDbList(min, m);
			//System.out.println(min.key);
			if(m==m.right) {
				min = null;
			} else {

				min = m.right;
				//System.out.println("meld");
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
			ArrayList<Node> arr = getListofNodeInDb(min);
			for(Node n : arr) {									// for each root node of the trees
				int ndeg = n.degree;												// Get degree of that node
				Node x = n;
				while(degreeArr[ndeg] != null) {									// while there is another tree with same degree, meld them
					Node n2 = degreeArr[ndeg];

					if(n2.key < x.key) {
						// swap n and n2
						Node temp = x;
						x = n2;
						n2 = temp;
					}
												// Remove n2 from the list and make n2 child of n
					link(n2, x);													// Link n and n2 (n parent, n2 child)

					degreeArr[ndeg] = null;
					ndeg++;
				}
				degreeArr[ndeg] = x; 												// Update degree
			}
		}

		// put the nodes/trees in the degree array into the list and determine min.
		min = null;
		for(int i=0; i<degreeArrSize; i++) {
			if(degreeArr[i] != null) {
				min = insertNodeToDbList(min, degreeArr[i]);
				if(min == null || degreeArr[i].key < min.key) {
					min = degreeArr[i];
				}
			}
		}
		//System.out.println("Meld" + min.child.v.getId() + min.child.right.v.getId() + min.child.left.v.getId());

	}

	/*
	 * Links two nodes
	 */
	private void link(Node n1, Node n2) {
		min = removeNodeFromDbList(min, n1);
		n1.parent = n2;
		n1.right = n1;
		n1.left = n1;
		n2.child = insertNodeToDbList(n2.child, n1);

		n1.childCut = false;
		n2.degree++;

	}



}
