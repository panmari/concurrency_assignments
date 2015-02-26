package assignment1.ex1;

// Computes the mean runtime of five runs for different locking methods and different number of threads.
public class Benchmark {

    private static int NR_RUNS = 5;

    public static void main(String[] args) throws InterruptedException {
        //int[] different_nr_threads = new int[] {8, 4, 2, 1};
        int[] different_nr_threads = new int[] {1, 2, 4, 8};
        for (int nr_threads: different_nr_threads) {
            long runtime = 0L;
            for (ComputationMode cm: ComputationMode.values()) {
                // Manually call gc before benchmarking.
                for (int i = 0; i < NR_RUNS + 1; i++) {
                    System.gc();
                    long start = System.nanoTime();
                    ThreadedCountRunner t = new ThreadedCountRunner(nr_threads, nr_threads, cm);
                    long stop = System.nanoTime();
                    // First run is only warmup.
                    if (i > 0)
                        runtime += stop - start;
                }
                System.out.printf("%d\t%s\t%f\n", nr_threads, cm, runtime/NR_RUNS/1000000.f);
            }
        }
    }
}
