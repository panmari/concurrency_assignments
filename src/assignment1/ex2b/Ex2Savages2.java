package assignment1.ex2b;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Ex2Savages2 {
    private static final int NR_SAVAGES = 10;

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Thread> savageThreads = new ArrayList<Thread>(NR_SAVAGES);
        ArrayList<Savage> savages = new ArrayList<Savage>(NR_SAVAGES);

        Pot pot = new Pot();
        Cook cook = new Cook(pot);
        Thread cookThread = new Thread(cook);
        for (int i = 0; i < NR_SAVAGES; i++) {
            Savage s = new Savage(pot, cook, NR_SAVAGES);
            savages.add(s);
            savageThreads.add(new Thread(s));
        }

        // Start threads:
        cookThread.start();
        for (Thread t : savageThreads) {
            t.start();
        }
        // Let them tryToEat/cook for some time.
        for (int i = 0; i < 10; i++) {
            System.out.printf("Cook had to refill %d times.\n", cook.nrRefills);
            Thread.sleep(100);
        }
        for (Savage s: savages) {
            s.hungry = false;
        }
        for (Thread t : savageThreads) {
            t.join();
        }
        // Stop cook:
        cookThread.interrupt();

        System.out.println("Eaten portions per savage:");
        for (Savage s: savages) {
            System.out.println(s.eatCount);
        }
    }
}


class Savage implements Runnable {
    private final Cook cook;
    private final Pot pot;
    private final int nrSavages;
    int eatCount = 0;
    boolean hungry;

    public Savage(Pot pot, Cook cook, int nrSavages) {
        this.nrSavages = nrSavages;
        this.hungry = true;
        this.pot = pot;
        this.cook = cook;
    }

    @Override
    public void run() {
        while(hungry) {
            // Only one savage may access the pot at any time.
            if (tryToEat())
                eatCount++;
        }
    }

    /**
     * @return true, if eating succeeded. False otherwise.
     */
    public boolean tryToEat() {
        try {
            pot.usePot.acquire();
            if (pot.isEmpty()) {
                pot.informCook.release();
            }
            pot.takePortion();
            pot.usePot.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}

class Pot {
    private final int MAX_PORTIONS = 5;
    private Semaphore portions = new Semaphore(MAX_PORTIONS);
    Semaphore usePot = new Semaphore(1);
    Semaphore informCook = new Semaphore(0);

    public void takePortion() throws InterruptedException {
        portions.acquire();
    }

    public boolean isEmpty() {
        return portions.availablePermits() == 0;
    }

    public void fill() {
        portions.release(MAX_PORTIONS);
    }
}

class Cook implements Runnable {

    int nrRefills = 0;
    private final Pot pot;

    public Cook(Pot pot) {
        this.pot = pot;
    }

    @Override
    public void run() {
        while (true) {
            try {
                pot.informCook.acquire();
                pot.fill();
                nrRefills++;
            } catch (InterruptedException e) {
                System.err.println("The cook is dead, go home everyone!");
            }
        }
    }
}