package entity;

import java.util.ArrayList;

/**
 * Created by X1z on 09.03.2016.
 */
public class Berth {

    private int emptyBuckets;
    private int fullBuckets;
    private ArrayList<Item> arrayForBuild;
    private ArrayList<Item> itemsFull = new ArrayList<Item>();
    private ArrayList<Item> itemsEmpty = new ArrayList<Item>();

    public Berth() {
    }

    public Berth(int emptyBuckets, int fullBuckets, ArrayList<Item> arrayForBuild) {
        this.emptyBuckets = emptyBuckets;
        this.fullBuckets = fullBuckets;
        this.arrayForBuild = arrayForBuild;
    }

    public ArrayList<Item> getItemsFull() {
        return itemsFull;
    }

    public ArrayList<Item> getItemsEmpty() {
        return itemsEmpty;
    }

    public void buildBerth (){
        for (Item item : arrayForBuild) {
            itemsFull.add(item);
        }
    }

    public ArrayList<Item> rebuildBerth(){
        arrayForBuild.clear();
        arrayForBuild.addAll(itemsFull);
        arrayForBuild.addAll(itemsEmpty);
        return arrayForBuild;
    }

    public boolean loadItem(Item item){
        boolean res= false;
        if (emptyBuckets!=0){
            itemsEmpty.add(item);
            emptyBuckets--;
            res=true;
        }
        return res;
    }

    public Item unloadItem(){
        Item res=null;
        if (!(itemsFull.size()<=0)){
            res=itemsFull.get(0);
            itemsFull.remove(0);
        }
        return res;
    }

}
