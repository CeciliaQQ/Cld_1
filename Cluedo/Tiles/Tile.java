package Cluedo.Tiles;
import Cluedo.Items.Item;

/**
 * Superclass of the different types of tiles.
 */
public class Tile {
    // Item on the tile
    private Item item;

    public Tile() {
        this.item = null;
    }

    /**
     * Set item on the tile
     * @param item the item on the tile
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * Get the item on the tile
     * @return the item object on the tile
     */
    public Item getItem(){
        return this.item;
    }

    public String toString(){
        if(item != null) return item.toString();
        else return " ";
    }

}