package assignment4.ex1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Node implements Comparable<Node> {

	private final int key;
	private final Lock lock;
	public Node next;
	
	public Node(int key) {
		this.key = key;
		this.lock = new ReentrantLock();
	}
	
	public void lock() {
		this.lock.lock();
	}
	
	public void unlock() {
		this.lock.unlock();
	}
	
	@Override
	public int compareTo(Node o) {
		return o.key - this.key;
	}

	public int getKey() {
		return key;
	}
	
}
