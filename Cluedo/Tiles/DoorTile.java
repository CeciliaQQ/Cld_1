package Cluedo.Tiles;

public class DoorTile extends Tile {

    private int doorNumber;

    public DoorTile(int x) {
        this.doorNumber = x;
    }

    public int getDoorNumber() {
        return doorNumber;
    }

    public String toString() {
        return "" + this.doorNumber;
    }

}