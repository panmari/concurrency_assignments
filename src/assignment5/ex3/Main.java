package assignment5.ex3;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

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
				// Add some elements to queue, so no EmptyQueueException is thrown.
				for (int i = 0; i < 10000; i++) {
					queue.enq(i);
				}
				double duration = 0;
				// Use first round for warmup.
				for (int r = 0; r < nrRuns + 1; r++) {
					ArrayList<Thread> threads = new ArrayList<Thread>(nThreads);

					CyclicBarrier barrier = new CyclicBarrier(nThreads);
					// Create enqueuers and dequeuers.
					int workPerThread = 100000 / nThreads;
					for (int i = 0; i < nThreads / 2; i++) {
						Enqueuer enc = new Enqueuer(barrier, queue, workPerThread);
						threads.add(new Thread(enc));
						Dequeuer deq = new Dequeuer(barrier, queue, workPerThread);
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
