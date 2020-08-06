package Cluedo.Tiles;

import Cluedo.Items.*;

public class RoomTile extends Tile {
    private Room room;

    public RoomTile(Room room) {
        this.room = room;
    }


    public Room getRoom() {
        return this.room;
    }

}