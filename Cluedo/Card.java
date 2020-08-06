package Cluedo;

import Cluedo.Items.Item;

public class Card {
    Item item;

    public Card(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public String getName(){
        return item.getName();
    }

}