package assignment1.ex1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

enum ComputationMode {NAIVE, SYNCHRONIZED, REENTRANT_LOCKED};

public class ThreadedCountRunner {

    private final boolean verbose;
    public final Counter counter;
    private Collection<Thread> threads;
    public ThreadedCountRunner(int n, int m, ComputationMode computationMode) throws InterruptedException {
        this(n, m, computationMode, true);
    }

    public ThreadedCountRunner(int n, int m, ComputationMode computationMode, boolean verbose) {
        this.verbose = verbose;
        counter = new Counter();

        threads = new ArrayList<Thread>(n + m);
        // Create incrementors.
        for (int i = 0; i < n; i++) {
            threads.add(new Thread(new Incrementor(counter, computationMode)));
        }
        // Create decrementors.
        for (int i = 0; i < m; i++) {
            threads.add(new Thread(new Decrementor(counter, computationMode)));
        }

        if (verbose) {
            System.out.printf("Using %d incrementors and %d decrementors.\n", n, m);
        }
    }

    public void run() throws InterruptedException {
        // Start threads
        for (Thread t: threads) {
            t.start();
        }
        // Join threads
        for (Thread t: threads) {
            t.join();
        }
    }
}

// Shared among all threads.
class Counter {
    final Lock lock = new ReentrantLock();
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
