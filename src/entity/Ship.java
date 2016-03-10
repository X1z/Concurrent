package entity;

import java.util.LinkedList;
import java.util.concurrent.*;

/**
 * Created by X1z on 29.02.2016.
 */
public class Ship implements Runnable {

    static {
        Port.getMyPort();
    }
    private static final Semaphore SEMAPHORE = new Semaphore(Port.getBerthCount(),true);
    private static int countToRebuild;
    private int neededToLoad=10;
    private int neededToUnload=10;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private LinkedList<Item> items= new LinkedList<Item>();
    private Berth berth;

    {
        for (int i = 0; i <30 ; i++) {
            items.addLast(new Item("ItemShip#"+i,i+150));
        }
    }

    public Ship(Integer neededToLoad, Integer neededToUnload) {
        this.neededToLoad = neededToLoad;
        this.neededToUnload = neededToUnload;
    }

    public int getNeededToLoad() {
        return neededToLoad;
    }

    public int getNeededToUnload() {
        return neededToUnload;
    }

    @Override
    public void run() {
        try {
            countToRebuild++;
            if (countToRebuild==4){
                SEMAPHORE.acquire(Port.getBerthCount());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("REBUILD");
                        Port.rebuildPort();
                        SEMAPHORE.release(Port.getBerthCount());
                    }
                });
                thread.start();
                countToRebuild=0;
            }
            SEMAPHORE.acquire();
            System.out.println("Ship "+Thread.currentThread().getName()+" in berth");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        berth=Port.pollBerth();
        System.out.println(berth.getClass()+"Berth size getItemsEmpty before: "+berth.getItemsEmpty().size());
        System.out.println(berth.getClass().getName()+"Berth size getItemsFull before: "+berth.getItemsFull().size());

        executorService.execute(this.loadItem());
        executorService.execute(this.unloadItem());
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Port.addBerth(berth);
        SEMAPHORE.release();

        System.out.println("Ship" +Thread.currentThread().getName()+" go to sea");
        System.out.println("Berth size after: "+berth.getItemsEmpty().size());
        System.out.println("Berth size after: "+berth.getItemsFull().size());
    }

    public Thread loadItem(){               //загрузка в кораблик
        return new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    for (;neededToUnload >0;){
                        System.out.println("Load " + Thread.currentThread().getId());
                        Thread.sleep(100);
                        items.addFirst(berth.unloadItem());   //тут if (null) на переполнение в порту
                        neededToUnload--;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Thread unloadItem(){              //загрузка в порт
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (;neededToLoad >0;){
                        System.out.println("UnLoad " + Thread.currentThread().getId());
                        Thread.sleep(150);
                        berth.loadItem(items.removeLast());   //тут if на переполнение в порту
                        neededToLoad--;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
