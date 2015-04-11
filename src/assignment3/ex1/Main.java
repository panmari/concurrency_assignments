package assignment3.ex1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;

import assignment2.ex1.Counter;
import assignment2.ex1.Incrementor;
import assignment2.ex1.NonVolatileCounter;
import assignment2.ex1.PetersonsLock;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		int nThreads = Integer.parseInt(args[0]);
		
		Lock[] locks = new Lock[]{ new PetersonsLock(nThreads), new CASLock(), new CCASLock() };
		System.out.println("#Threads: " + nThreads);

		for (Lock lock: locks) {
			Counter counter = new NonVolatileCounter(nThreads, lock);
	
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
			
			// Print stats
			System.out.println("Statistics for: ");
			System.out.println("Lock: " + lock.getClass().toString());
			System.out.println("Counter end value: " + counter.getCount());
			Arrays.sort(incrementCount);
			System.out.println("Lowest number of increments: " + incrementCount[0]);
			System.out.println("Highest number of increments: "
					+ incrementCount[incrementCount.length - 1]);
			System.out.printf("Execution time: %.2fms\n", duration);
		}
	}

}
