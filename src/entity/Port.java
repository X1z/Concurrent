package entity;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by X1z on 29.02.2016.
 */
public class Port {

    private static Port myPort = new Port();
    private static ArrayList<Item> items = new ArrayList<Item>();
    private static LinkedList<Berth> berthArray = new LinkedList<Berth>();
    private static int berthCount=3;
    private static int maxCapacity=300;

    static {
        for (int i = 0; i < 50; i++) {
            items.add(new Item("Item#"+i, i+100));
        }
    }

    public static Port getMyPort(){
        Port.buildPort();
        return myPort;
    }

    private Port() {
    }

    public static int getBerthCount() {
        return berthCount;
    }

    public static Berth pollBerth(){
        return berthArray.poll();
    }

    public static void addBerth(Berth berth){
        berthArray.add(berth);
    }

    private static void buildPort(){
        int size=items.size();
        int berthCapacity;
        int emptyBuckets;
        int fullBuckets;
        ArrayList<Item> tmp;
        Berth berth;

        for (int i = 0; i < berthCount; i++) {
            berthCapacity = (i != berthCount - 1
                    ? maxCapacity / berthCount
                    : maxCapacity / berthCount + maxCapacity % berthCount);

            fullBuckets = (i != berthCount - 1
                    ? size / berthCount
                    : size / berthCount + size % berthCount);

            emptyBuckets = berthCapacity - fullBuckets;
            tmp = new ArrayList<Item>(items.subList(0, fullBuckets));
            berth=new Berth(emptyBuckets, fullBuckets, tmp);
            berth.buildBerth();
            berthArray.add(berth);
            items.removeAll(tmp);
        }
    }

    public static void rebuildPort(){
        for (Berth berth : berthArray) {
            items.addAll(berth.rebuildBerth());
        }
        berthArray.clear();
        Port.buildPort();
    }

    public static ArrayList<Item> getItems() {
        return items;
    }
}
