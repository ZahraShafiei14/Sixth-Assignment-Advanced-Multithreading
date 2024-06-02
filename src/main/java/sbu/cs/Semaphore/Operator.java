package sbu.cs.Semaphore;

import java.util.concurrent.Semaphore;

public class Operator extends Thread {

    private final Semaphore semaphore;

    public Operator(String name, Semaphore semaphore) {
        super(name);
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            semaphore.acquire();
            System.out.println(getName() + " accessed to resource");
            for (int i = 0; i < 20; i++) {
                Resource.accessResource();         // critical section - a Maximum of 2 operators can access the resource concurrently
                sleep(500);
            }
            System.out.println(getName() + " exited from resource");
            semaphore.release();
        }catch (InterruptedException e){
            e.getMessage();
        }
    }
}
