package assignment3.ex2;

public class TwoThreadNoLockQueue implements IIntQueue {
	private final int QSIZE;
	private final int items[] ;
	private volatile int head = 0, tail = 0;

	public TwoThreadNoLockQueue(int QSIZE) {
		this.QSIZE = QSIZE;
		items = new int[QSIZE];
	}
	
	public void enq(int x) {
		while (tail - head == QSIZE) {}
		items[tail % QSIZE] = x;
		tail++;
	}

	public int deq() {
		while (tail == head) {}
		int item = items[head % QSIZE];
		head++;
		return item;
	}
	
	public String toString() {
		return "Head: " + head + ", Tail: " + tail;
	}
}