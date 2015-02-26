package assignment1.ex1;

import java.util.ArrayList;

public class Ex1NoSync {
    public static void main(String[] args) throws InterruptedException {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);
        System.out.printf("Using %d incrementors and %d decrementors.\n", n, m);

        Counter counter = new Counter();

        ArrayList<Thread> threads = new ArrayList<Thread>(n + m);
        // Create incrementors.
        for (int i = 0; i < n; i++) {
            threads.add(new Thread(new Incrementor(counter)));
        }
        // Create decrementors.
        for (int i = 0; i < m; i++) {
            threads.add(new Thread(new Decrementor(counter)));
        }
        // Start threads
        for (Thread t: threads) {
            t.start();
        }
        // Join threads
        for (Thread t: threads) {
            t.join();
        }

        System.out.println("Final value on counter: " + counter.count);
    }


}

class Counter {
    long count = 0;
}

abstract class CounterActor implements Runnable {
    protected Counter counter;
    private final static int ITERATION_COUNT = 100000;

    public CounterActor(Counter counter) {
        this.counter = counter;
    }

    public abstract void act();

    public void run() {
        for (int i = 0; i < ITERATION_COUNT; i++) {
            act();
        }
    }

}

class Incrementor extends CounterActor {

    public Incrementor(Counter counter) {
        super(counter);
    }

    public void act() {
        long c = counter.count;
        c++;
        counter.count = c;
    }
}

class Decrementor extends CounterActor {

    public Decrementor(Counter counter) {
        super(counter);
    }

    public void act() {
        long c = counter.count;
        c--;
        counter.count = c;
    }
}
