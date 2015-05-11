package assignment5.ex3;

import java.util.ArrayList;

import assignment3.ex2.Dequeuer;
import assignment3.ex2.Enqueuer;
import assignment3.ex2.IIntQueue;

public class Main {

	private static int nrRuns = 3;
	
	public static void main(String[] args) throws InterruptedException {

		int[] nThreadsOptions = new int[] { 2, 4, 8 };
		for (int nThreads : nThreadsOptions) {
			ArrayList<IIntQueue> queues = new ArrayList<IIntQueue>(2);
			queues.add(new UnboundLockQueue());
			queues.add(new UnboundLockFreeQueue());

			System.out.println("#Threads: " + nThreads);
			for (IIntQueue queue : queues) {
				double duration = 0;
				// Use first round for warmup.
				for (int r = 0; r < nrRuns + 1; r++) {
					ArrayList<Thread> threads = new ArrayList<Thread>(nThreads);

					// Create enqueuers and dequeuers.
					int workPerThread = 100000 / nThreads;
					for (int i = 0; i < nThreads / 2; i++) {
						Enqueuer enc = new Enqueuer(queue, workPerThread);
						threads.add(new Thread(enc));
						Dequeuer deq = new Dequeuer(queue, workPerThread);
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
					// Skip first run, which ins only for warmup.
					if (r > 0) {
						duration += (end - start) / 1e6;
					}
				}
				// Print stats
				System.out.println("Queue type: " + queue.getClass().toString());
				System.out.printf("Execution time: %.2fms\n", duration / nrRuns);
			}
		}
	}
}
