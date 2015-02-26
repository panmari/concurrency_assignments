package assignment1.ex1;

public class Ex1ReentrantLock {

    public static void main(String[] args) throws InterruptedException {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        new ThreadedCountRunner(n, m, ComputationMode.REENTRANT_LOCKED);
    }
}
