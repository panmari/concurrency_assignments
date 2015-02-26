package assignment1.ex1;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

enum ComputationMode {NAIVE, SYNCHRONIZED, REENTRANT_LOCKED};

public class ThreadedCountRunner {

    private static final boolean verbose = false;
    public final Counter counter;

    public ThreadedCountRunner(int n, int m, ComputationMode computationMode) throws InterruptedException {
        counter = new Counter();

        ArrayList<Thread> threads = new ArrayList<Thread>(n + m);
        // Create incrementors.
        for (int i = 0; i < n; i++) {
            threads.add(new Thread(new Incrementor(counter, computationMode)));
        }
        // Create decrementors.
        for (int i = 0; i < m; i++) {
            threads.add(new Thread(new Decrementor(counter, computationMode)));
        }
        // Start threads
        for (Thread t: threads) {
            t.start();
        }
        // Join threads
        for (Thread t: threads) {
            t.join();
        }

        if (verbose) {
            System.out.printf("Using %d incrementors and %d decrementors.\n", n, m);
            System.out.println("Final value on counter: " + counter.count);
        }
    }
}

// Shared among all threads.
class Counter {
    final ReentrantLock lock = new ReentrantLock();
    long count = 0;
}

abstract class CounterActor implements Runnable {
    private final ComputationMode computationMode;

    protected Counter counter;
    private final static int ITERATION_COUNT = 100000;

    public CounterActor(Counter counter, ComputationMode computationMode) {
        this.counter = counter;
        this.computationMode = computationMode;
    }

    public abstract void act();

    public void run() {
        for (int i = 0; i < ITERATION_COUNT; i++) {
            switch (computationMode) {
                case NAIVE:
                    act();
                    break;
                case SYNCHRONIZED:
                    synchronized(counter) {
                        act();
                    }
                    break;
                case REENTRANT_LOCKED:
                    counter.lock.lock();
                    act();
                    counter.lock.unlock();
                    break;
            }
        }
    }

}

class Incrementor extends CounterActor {

    public Incrementor(Counter counter, ComputationMode computationMode) {
        super(counter, computationMode);
    }

    public void act() {
        long c = counter.count;
        c++;
        counter.count = c;
    }
}

class Decrementor extends CounterActor {

    public Decrementor(Counter counter, ComputationMode computationMode) {
        super(counter, computationMode);
    }

    public void act() {
        long c = counter.count;
        c--;
        counter.count = c;
    }
}
