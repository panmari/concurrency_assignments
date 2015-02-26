package assignment1.ex2;

import assignment1.ex1.ThreadedCountRunner;

import java.util.ArrayList;

public class Ex2Savages1 {

    private static final int NR_SAVAGES = 20;

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<Thread>(NR_SAVAGES);

        Pot pot = new Pot();
        Cook cook = new Cook(pot);
        for (int i = 0; i < NR_SAVAGES; i++) {
            threads.add(new Thread(new Savage(pot, cook)));
        }

        for (Thread t: threads) {
            t.start();
        }
        for (Thread t: threads) {
            t.join();
        }
    }
}

class Savage implements Runnable {
    private final Cook cook;
    private final Pot pot;

    public Savage(Pot pot, Cook cook) {
        this.pot = pot;
        this.cook = cook;
    }

    @Override
    public void run() {
        eat();
    }

    public synchronized void eat() {
        try {
            pot.takePortion();
        } catch (RuntimeException e) {
            cook.run();
            eat();
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

    private final Pot pot;

    public Cook(Pot pot) {
        this.pot = pot;
    }

    @Override
    public void run() {
        pot.fill();
    }}