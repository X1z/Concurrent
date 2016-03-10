import entity.Port;
import entity.Ship;

import java.io.*;
import java.util.concurrent.BrokenBarrierException;

/**
 * Created by X1z on 29.02.2016.
 */
public class Runner {

    public static void main(String[] args) throws InterruptedException, IOException, BrokenBarrierException {
        Thread thread;
        for (int i = 0; i < 5; i++) {
            thread = new Thread(new Ship(5,1));
            thread.start();
        }

    }
}
