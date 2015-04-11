package assignment2.ex1;

import java.util.concurrent.locks.Lock;

public abstract class Counter {

	Lock lock;

	public Counter(int nThreads, Lock lock) {
		this.lock = lock;
	}

	public void lock() {
		this.lock.lock();
	}

	public abstract long getCount();

	public abstract void increment();
}
