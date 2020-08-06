package Cluedo.Tiles;
import Cluedo.Items.Item;

public class Tile {
    private Item item;

    public Tile() {
        this.item = null;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem(){
        return this.item;
    }

    public String toString(){
        if(item != null) return item.toString();
        else return " ";
    }

}