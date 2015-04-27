package assignment4.ex1;

import java.util.ArrayList;
import java.util.Random;

public class BenchmarkListImplementations {

	private static int nrRuns = 3;
	/** Number of integers that are added/removed altogether 
	(is divided for adders/removers and threads) */
	private static int nrIntegers = 100000;
	private static long randomSeed = 42;

	public static void main(String[] args) throws InterruptedException {
		Random rng = new Random(randomSeed);
		int[] nThreadsOptions = new int[] { 2, 4, 8 };
		for (int nThreads : nThreadsOptions) {
			ArrayList<IFineGrainedLockList> lists = new ArrayList<IFineGrainedLockList>(2);
			lists.add(new FineGrainedLockList());

			System.out.println("#Threads: " + nThreads);
			for (IFineGrainedLockList queue : lists) {
				double duration = 0;
				// Use first round for warmup.
				for (int r = 0; r < nrRuns + 1; r++) {
					ArrayList<Thread> threads = new ArrayList<Thread>(nThreads);					
					
					// Create enqueuers and dequeuers.
					for (int i = 0; i < nThreads / 2; i++) {
						// Generate numbers to add/remove
						ArrayList<Integer> addList = new ArrayList<Integer>();
						ArrayList<Integer> removeList = new ArrayList<Integer>();
						for (int j = 0; j < nrIntegers / nThreads / 2; j++) {
							// Add numbers in range [0 - 100)
							// (exclusive upper bound).
							addList.add(rng.nextInt(100));
							removeList.add(rng.nextInt(100));
						}
						threads.add(new Thread(new Adder(queue, addList)));
						threads.add(new Thread(new Remover(queue, removeList)));
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
				System.out.println("List type: " + queue.getClass().toString());
				System.out.printf("Execution time: %.2fms\n", duration / nrRuns);
			}
		}
	}

	}

