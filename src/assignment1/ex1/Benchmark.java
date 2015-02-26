package assignment1.ex1;

public class Benchmark {

    public static void main(String[] args) throws InterruptedException {
        int[] different_nr_threads = new int[] {1, 2, 4, 8};
        for (int nr_threads: different_nr_threads) {
            for (ComputationMode cm: ComputationMode.values()) {
                // Manually call gc before benchmarking.
                System.gc();
                long start = System.currentTimeMillis();
                ThreadedCountRunner t = new ThreadedCountRunner(nr_threads, nr_threads, cm);
                long stop = System.currentTimeMillis();
                System.out.printf("%d\t%s\t%d\t%d\n", nr_threads, cm, t.counter.count, stop - start);
            }
        }
    }
}
