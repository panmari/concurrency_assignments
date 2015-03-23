package assignment2.ex1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		int nThreads = Integer.parseInt(args[0]);

		if (args[1].equals("true")) {
			setSolarisAffinity();
			System.out.println("Set affinity to only one processor.");
		}

		Counter counter;
		if (args[2].equals("true")) {
			counter = new VolatileCounter(nThreads);
		} else {
			counter = new NonVolatileCounter(nThreads);
		}

		ArrayList<Thread> threads = new ArrayList<Thread>(nThreads);

		// Create incrementors.
		int[] incrementCount = new int[nThreads];
		for (int i = 0; i < nThreads; i++) {
			Incrementor inc = new Incrementor(counter, incrementCount);
			threads.add(new Thread(inc));
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
		System.out.println("Statistics for: ");
		System.out.println("#Threads: " + nThreads);
		System.out.println("Affinity to only one process: " + args[1]);
		System.out.println("Volatile counter: " + args[2]);
		System.out.println("Counter end value: " + counter.getCount());
		Arrays.sort(incrementCount);
		System.out.println("Lowest number of increments: " + incrementCount[0]);
		System.out.println("Highest number of increments: "
				+ incrementCount[incrementCount.length - 1]);
		System.out.printf("Execution time: %.2fms\n", duration);
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
			System.out.println("Trying to lock Processor #" + processor);
			System.out.println("By executing");
			String cmd = "/usr/sbin/pbind -b " + processor + " " + pid;
			System.out.println(cmd);
			// Set process affinity to one processor ( on Solaris )
			Process p = Runtime.getRuntime().exec(cmd);
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
		// These are implicitly initialized to 0.
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

	private Counter counter;
	private int[] incrementorCount;
	private static final int MAX_COUNT = 300000;

	Incrementor(Counter counter, int[] incrementorCount) {
		this.counter = counter;
		this.incrementorCount = incrementorCount;
	}

	@Override
	public void run() {
		long threadId = Thread.currentThread().getId();
		while (true) {
			counter.lock.lock(threadId);
			if (counter.getCount() < MAX_COUNT) {
				counter.increment();
				counter.lock.unlock(threadId);
				incrementorCount[(int) (threadId % incrementorCount.length)]++;
			} else {
				counter.lock.unlock(threadId);
				break;
			}
		}
	}
}
