package assignment2.ex1;

import java.util.concurrent.locks.Lock;

public class VolatileCounter extends Counter {
	private volatile long count = 0;

	public VolatileCounter(int nThreads, Lock lock) {
		super(nThreads, lock);
	}

	public void increment() {
		count++;
	}

	public long getCount() {
		return count;
	}

}