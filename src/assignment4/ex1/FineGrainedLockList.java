package assignment4.ex1;

public class FineGrainedLockList implements IFineGrainedLockList {

	private Node head;
	private Node tail;

	public FineGrainedLockList() {
		this.head = new Node(Integer.MIN_VALUE);
		this.tail = new Node(Integer.MAX_VALUE);
		this.head.next = this.tail;
	}

	public boolean remove(int key) {
		Node pred = null, curr = null;
		try {
			pred = this.head;
			pred.lock();
			curr = pred.next;
			curr.lock();
			// Searching
			while (curr.getKey() <= key) {
				if (key == curr.getKey()) {
					pred.next = curr.next;
					return true;
				}
				pred.unlock();
				pred = curr;
				curr = curr.next;
				curr.lock();
			}
		} finally {
			curr.unlock();
			pred.unlock();
		}
		return false;
	}

	public boolean add(int key) {
		Node pred = null, curr = null;
		try {
			pred = this.head;
			pred.lock();
			curr = pred.next;
			curr.lock();
			// Searching
			while (curr.getKey() <= key) {
				if (key == curr.getKey()) {
					// Key already present, does not need to be added.
					return false;
				}
				pred.unlock();
				pred = curr;
				curr = curr.next;
				curr.lock();
			}
			// Add new node with key
			Node n = new Node(key);
			pred.next = n;
			n.next = curr;
		} finally {
			curr.unlock();
			pred.unlock();
		}
		return true;
	}

	public boolean contains(int key) {
		Node pred = null, curr = null;
		try {
			pred = this.head;
			pred.lock();
			curr = pred.next;
			curr.lock();
			// Searching
			while (curr.getKey() <= key) {
				if (key == curr.getKey()) {
					// present
					return true;
				}
				pred.unlock();
				pred = curr;
				curr = curr.next;
				curr.lock();
			}
		} finally {
			curr.unlock();
			pred.unlock();
		}
		return false;
	}
}
