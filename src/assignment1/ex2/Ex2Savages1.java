package assignment1.ex2;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Ex2Savages1 {

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
        cook.quitJob();
        cookThread.join();

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
        boolean eaten = false;
        try {
            pot.lock.acquire();
            if (pot.isEmpty()) {
                if (!cook.isCooking())
                    cook.inform();
            } else {
                pot.takePortion();
                eaten = true;
            }
            pot.lock.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return eaten;
    }
}

class Pot {
    private final int MAX_PORTIONS = 5;
    private int portions = 0;
    Semaphore lock = new Semaphore(1);

    public boolean isEmpty() {
        return portions == 0;
    }

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
                try {
                    pot.lock.acquire();
                    pot.fill();
                    nrRefills++;
                    doRefill = false;
                    pot.lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stopRefilling)
                break;
        }
    }
}