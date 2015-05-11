package assignment5.ex3;

import java.util.concurrent.locks.Lock;

import assignment3.ex1.CCASLock;
import assignment3.ex2.IIntQueue;

public class UnboundLockQueue implements IIntQueue {

	private Lock dequeueLock, enqueueLock;
	private Node head, tail;

	public UnboundLockQueue() {
		this.enqueueLock = new CCASLock();
		this.dequeueLock = new CCASLock();
		// create sentinel
		Node sentinel = new Node(Integer.MIN_VALUE);
		head = sentinel;
		tail = sentinel;
	}

	@Override
	public void enq(int value) {
		enqueueLock.lock();
		try {
			Node newNode = new Node(value);
			tail.next = newNode;
			tail = newNode;
		} finally {
			enqueueLock.unlock();
		}
	}

	@Override
	public int deq() {
		int result;
		dequeueLock.lock();
		try {
			if (head.next == null) {
				throw new RuntimeException("Queue is empty.");
			}
			result = head.next.value;
			head = head.next;
		} finally {
			dequeueLock.unlock();
		}
		return result;
	}

}
