package assignment2.ex1;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		
		int nThreads = Integer.parseInt(args[0]);

		if (args.length > 1 && args[1].equals("true"))
			setSolarisAffinity();
		
		Counter counter;
		if (args.length > 2 && args[2].equals("true")) {
			System.out.println("Using volatile counter.");
			counter = new VolatileCounter(nThreads);
		} else {
			System.out.println("Using non-volatile counter.");
			counter = new NonVolatileCounter(nThreads);
		}
		
		ArrayList<Thread> threads = new ArrayList<Thread>(nThreads);
		// Create incrementors.
		for (int i = 0; i < nThreads; i++) {
			threads.add(new Thread(new Incrementor(counter)));
		}

		long start = System.nanoTime();
		// Start threads
		for (Thread t : threads) {
			t.start();
		}

		// Join threads
		for (Thread t : threads) {
			t.join();
		}
		long end = System.nanoTime();
		double duration = (end - start) / 1e6;
		System.out.printf("Counted to %d in %.2fms\n", counter.getCount(),
				duration);
	}

	public static void setSolarisAffinity() {
		try {
			// retrieve process id
			String pid_name = java.lang.management.ManagementFactory
					.getRuntimeMXBean().getName();
			String[] pid_array = pid_name.split("@");
			int pid = Integer.parseInt(pid_array[0]);
			// random processor
			int processor = new java.util.Random().nextInt(32);
			// Set process affinity to one processor ( on Solaris )
			Process p = Runtime.getRuntime().exec(
					"/usr/sbin/pbind -b" + processor + " " + pid);
			p.waitFor();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
}

abstract class Counter {
	
	PetersonsLock lock;
	public Counter(int nThreads) {
		this.lock = new PetersonsLock(nThreads);
	}

	public void lock(long threadId) {
		this.lock.lock(threadId);
	}
	
	public abstract long getCount();
	
	public abstract void increment();
}

class VolatileCounter extends Counter {
	private volatile long count = 0;

	public VolatileCounter(int nThreads) {
		super(nThreads);
	}

	public void increment() {
		count++;
	}

	public long getCount() {
		return count;
	}

}

class NonVolatileCounter extends Counter {
	private long count = 0;

	public NonVolatileCounter(int nThreads) {
		super(nThreads);
	}

	public void increment() {
		count++;
	}

	public long getCount() {
		return count;
	}
}

class PetersonsLock {
	volatile AtomicIntegerArray level;
	volatile AtomicIntegerArray victim;
	private int nThreads;

	public PetersonsLock(int nThreads) {
		// These are implicitely initialized to 0.
		level = new AtomicIntegerArray(nThreads);
		victim = new AtomicIntegerArray(nThreads);
		this.nThreads = nThreads;
	}

	public void lock(long threadId) {
		int id = threadIdToId(threadId);
		for (int L = 1; L < nThreads; L++) {
			level.set(id, L);
			victim.set(L, id);
			while (canNotAdvance(id, L)) {
			}
		}
	}

	public void unlock(long threadId) {
		int id = threadIdToId(threadId);
		level.set(id, 0);
	}

	private int threadIdToId(long threadId) {
		return (int) threadId % nThreads;
	}

	/**
	 * Returns false if this thread can advance.
	 * 
	 * @param id
	 * @param currentLevel
	 * @return
	 */
	private boolean canNotAdvance(int id, int currentLevel) {
		boolean threadWithHigherLevelExists = false;
		for (int k = 0; k < nThreads; k++) {
			if (k == id)
				continue;
			// Return immediately if there is another thread with higher
			// level/priority.
			if (level.get(k) >= currentLevel) {
				threadWithHigherLevelExists = true;
				break;
			}
		}
		return threadWithHigherLevelExists && victim.get(currentLevel) == id;
	}
}

class Incrementor implements Runnable {

	private int lockCount = 0;
	private Counter counter;
	private static final int MAX_COUNT = 300000;

	Incrementor(Counter counter) {
		this.counter = counter;
	}

	@Override
	public void run() {
		long threadId = Thread.currentThread().getId();
		while (true) {
			counter.lock.lock(threadId);
			if (counter.getCount() < MAX_COUNT) {
				counter.increment();
				lockCount++;
				counter.lock.unlock(threadId);
			} else {
				System.out.println("Thread " + threadId + " incremented "
						+ lockCount + " times.");
				counter.lock.unlock(threadId);
				break;
			}
		}
	}
}
