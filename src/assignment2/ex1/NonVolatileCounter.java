package assignment2.ex1;

import java.util.concurrent.locks.Lock;

public class NonVolatileCounter extends Counter {
	private long count = 0;

	public NonVolatileCounter(int nThreads, Lock lock) {
		super(nThreads, lock);
	}

	public void increment() {
		count++;
	}

	public long getCount() {
		return count;
	}
}

