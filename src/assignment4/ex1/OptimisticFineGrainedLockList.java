package assignment4.ex1;

public class OptimisticFineGrainedLockList implements IFineGrainedLockList {

	private Node head;
	private Node tail;

	public OptimisticFineGrainedLockList() {
		this.head = new Node(Integer.MIN_VALUE);
		this.tail = new Node(Integer.MAX_VALUE);
		this.head.next = this.tail;
	}

	public boolean remove(int key) {
		// Outer loop does retry if validate fails.
		while (true) {
			Node pred = this.head;
			Node curr = pred.next;
			while (curr.getKey() <= key) {
				if (key == curr.getKey()) {
					break;
				}
				pred = curr;
				curr = curr.next;
			}
			try {
				pred.lock();
				curr.lock();
				if (validate(pred, curr)) {
					if (curr.getKey() == key) {
						pred.next = curr.next;
						return true;
					} else {
						return false;
					}
				}
			} finally {
				pred.unlock();
				curr.unlock();
			}
		}
	}

	public boolean add(int key) {
		// Outer loop does retry if validate fails.
		while (true) {
			Node pred = this.head;
			Node curr = pred.next;
			while (curr.getKey() <= key) {
				pred = curr;
				curr = curr.next;
			}
			try {
				pred.lock();
				curr.lock();
				if (validate(pred, curr)) {
					if (pred.getKey() == key)
						return false;
					Node n = new Node(key);
					n.next = curr;
					pred.next = n;
					return true;
				}
			} finally {
				pred.unlock();
				curr.unlock();
			}
		}
	}

	public boolean contains(int key) {
		// Outer loop does retry if validate fails.
		while (true) {
			Node pred = this.head;
			Node curr = pred.next;
			while (curr.getKey() <= key) {
				pred = curr;
				curr = curr.next;
			}
			try {
				pred.lock();
				curr.lock();
				if (validate(pred, curr)) {
					if (key == pred.getKey()) {
						return true;
					} else {
						return false;
					}
				}
			} finally {
				pred.unlock();
				curr.unlock();
			}
		}
	}

	/**
	 * @param pred
	 * @param curr
	 * @return true, if pred.next is actually curr.
	 */
	private boolean validate(Node pred, Node curr) {
		Node node = this.head;
		while (node.getKey() <= pred.getKey()) {
			if (node == pred) {
				return pred.next == curr;
			}
			node = node.next;
		}
		return false;
	}
}
