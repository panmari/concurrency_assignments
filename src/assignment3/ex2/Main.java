package assignment3.ex2;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws InterruptedException {

		int nThreads = Integer.parseInt(args[0]);
		if (nThreads % 2 != 0) {
			System.err.println("Number of threads must be even.");
			System.exit(1);
		}
		
		int QSIZE = 20;
		IIntQueue[] queues = new IIntQueue[]{new TwoThreadNoLockQueue(QSIZE), 
				new MultiThreadOneLockQueue(QSIZE), 
				new MultiThreadTwoLockQueue(QSIZE)};

		System.out.println("#Threads: " + nThreads);
		for (IIntQueue queue: queues) {
			ArrayList<Thread> threads = new ArrayList<Thread>(nThreads);
	
			// Create incrementors.
			int[] incrementCount = new int[nThreads];
			for (int i = 0; i < nThreads/2; i++) {
				Enqueuer enc = new Enqueuer(queue);
				threads.add(new Thread(enc));
				Dequeuer deq = new Dequeuer(queue);
				threads.add(new Thread(deq));
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
			System.out.println("Queue type: " + queue.getClass().toString());
			System.out.println("Final queue state: " + queue.toString());
			Arrays.sort(incrementCount);
			System.out.println("Lowest number of increments: " + incrementCount[0]);
			System.out.println("Highest number of increments: "
					+ incrementCount[incrementCount.length - 1]);
			System.out.printf("Execution time: %.2fms\n", duration);
		}
	}

}
