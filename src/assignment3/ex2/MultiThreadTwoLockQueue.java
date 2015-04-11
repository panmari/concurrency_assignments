package assignment3.ex2;

import java.util.concurrent.locks.Lock;

import assignment3.ex1.CCASLock;

public class MultiThreadTwoLockQueue implements IIntQueue {

	private final int QSIZE;
	private final int items[] ;
	private volatile int head = 0, tail = 0;
	private Lock dequeueLock, enqueueLock;

	public MultiThreadTwoLockQueue(int QSIZE) {
		this.enqueueLock = new CCASLock();
		this.dequeueLock = new CCASLock();
		this.QSIZE = QSIZE;
		this.items = new int[QSIZE];
	}
	
	public void enq(int x) {
		enqueueLock.lock();
		while (tail - head == QSIZE) {}
		items[tail % QSIZE] = x;
		tail++;
		enqueueLock.unlock();
	}

	public int deq() {
		dequeueLock.lock();
		while (tail == head) {}
		int item = items[head % QSIZE];
		head++;
		dequeueLock.unlock();
		return item;
	}

	public String toString() {
		return "Head: " + head + ", Tail: " + tail;
	}
}
