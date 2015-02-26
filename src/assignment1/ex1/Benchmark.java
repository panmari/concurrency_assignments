package assignment1.ex1;

// Computes the mean runtime of five runs for different locking methods and different number of threads.
public class Benchmark {

    private static int NR_RUNS = 5;

    public static void main(String[] args) throws InterruptedException {
        //int[] different_nr_threads = new int[] {8, 4, 2, 1};
        int[] different_nr_threads = new int[] {1, 2, 4, 8};
        for (int nr_threads: different_nr_threads) {
            long runtime = 0L;
            long[] runtimes = new long[ComputationMode.values().length];
            for (ComputationMode cm: ComputationMode.values()) {
                for (int i = 0; i < NR_RUNS + 1; i++) {
                    // Manually call gc before benchmarking.
                    System.gc();
                    long start = System.nanoTime();
                    ThreadedCountRunner t = new ThreadedCountRunner(nr_threads, nr_threads, cm);
                    long stop = System.nanoTime();
                    // First run is only warmup.
                    if (i > 0) {
                        runtimes[cm.ordinal()] += stop - start;
                    }
                }
            }
            long referenceRuntimePerRun = runtimes[ComputationMode.NAIVE.ordinal()] / NR_RUNS;
            for (ComputationMode cm: ComputationMode.values()) {
                long runtimePerRun = runtimes[cm.ordinal()] / NR_RUNS;
                float runtimeMs = runtimePerRun  / 1000000.f;
                float speedup = (float) referenceRuntimePerRun / runtimePerRun;
                System.out.printf("%d\t%s\t%f\t%f\n", nr_threads, cm, runtimeMs, speedup);
            }
        }
    }
}
