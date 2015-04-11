package assignment2.ex1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		int nThreads = Integer.parseInt(args[0]);

		if (args[1].equals("true")) {
			setSolarisAffinity();
			System.out.println("Set affinity to only one processor.");
		}

		Counter counter;
		Lock lock = new PetersonsLock(nThreads);
		if (args[2].equals("true")) {
			counter = new VolatileCounter(nThreads, lock);
		} else {
			counter = new NonVolatileCounter(nThreads, lock);
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
			String cmd = "/usr/sbin/pbind -b " + processor + " " + pid;
			// Set process affinity to one processor ( on Solaris )
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
}

