package assignment3.ex2;

import java.util.concurrent.locks.Lock;

import assignment3.ex1.CCASLock;

public class MultiThreadOneLockQueue implements IIntQueue {

	private final int QSIZE;
	private final int items[] ;
	private volatile int head = 0, tail = 0;
	private Lock lock;

	public MultiThreadOneLockQueue(int QSIZE) {
		this.lock = new CCASLock();
		this.QSIZE = QSIZE;
		this.items = new int[QSIZE];
	}
	public void enq(int x) {
		while(true) {
			lock.lock();
			if (tail - head == QSIZE)
				lock.unlock();
			else
				break;
		}
		items[tail % QSIZE] = x;
		tail++;
		lock.unlock();
	}

	public int deq() {
		while(true) {
			lock.lock();
			if (tail == head)
				lock.unlock();
			else
				break;
		}
		int item = items[head % QSIZE];
		head++;
		lock.unlock();
		return item;
	}
	
	public String toString() {
		return "Head: " + head + ", Tail: " + tail;
	}
}
