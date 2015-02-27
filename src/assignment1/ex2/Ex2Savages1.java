package assignment1.ex2;

import assignment1.ex1.ThreadedCountRunner;

import java.util.ArrayList;

public class Ex2Savages1 {

    private static final int NR_SAVAGES = 101;

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Thread> savageThreads = new ArrayList<Thread>(NR_SAVAGES);

        Pot pot = new Pot();
        Cook cook = new Cook(pot);
        Thread cookThread = new Thread(cook);
        for (int i = 0; i < NR_SAVAGES; i++) {
            savageThreads.add(new Thread(new Savage(pot, cook)));
        }

        // Start threads:
        cookThread.start();
        for (Thread t : savageThreads) {
            t.start();
        }
        for (Thread t : savageThreads) {
            t.join();
        }
        // Stop cook:
        cook.quitJob();
        cookThread.join();

        System.out.printf("Cook had to refill %d times.\n", cook.nrRefills);
    }
}

class Savage implements Runnable {
    private final Cook cook;
    private final Pot pot;
    private boolean hungry;

    public Savage(Pot pot, Cook cook) {
        this.hungry = true;
        this.pot = pot;
        this.cook = cook;
    }

    @Override
    public void run() {
        while(hungry) {
            // Only one savage may access the pot at any time.
            synchronized (pot) {
                eat();
            }
        }
    }

    public void eat() {
        try {
            pot.takePortion();
            hungry = false;
        } catch (PotException e) {
            cook.inform();
            // Wait while cook is cooking.
            while(cook.isCooking()) {}
        }
    }
}

class Pot {
    private final int MAX_PORTIONS = 10;
    private int portions = 0;

    public void takePortion() {
        if (portions == 0)
            throw new PotException("No more portions available!");
        portions--;
    }

    public void fill() {
        if (portions != 0)
            throw new PotException("Pot was not empty!");
        portions = MAX_PORTIONS;
    }
}

class PotException extends RuntimeException {

    public PotException(String s) {
        super(s);
    }
}

class Cook implements Runnable {

    int nrRefills = 0;
    private final Pot pot;
    private boolean doRefill;
    private boolean stopRefilling;

    public Cook(Pot pot) {
        this.pot = pot;
    }

    public void inform() {
        doRefill = true;
    }

    public boolean isCooking() {
        return doRefill;
    }

    // Can be used once all the savages have eaten enough.
    public void quitJob() {
        stopRefilling = true;
    }

    @Override
    public void run() {
        while (true) {
            if (doRefill) {
                pot.fill();
                nrRefills++;
                doRefill = false;
            }
            if (stopRefilling)
                break;
        }
    }
}