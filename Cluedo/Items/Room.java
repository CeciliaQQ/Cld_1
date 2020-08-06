package Cluedo.Items;

import Cluedo.Tiles.DoorTile;
import Cluedo.Tiles.RoomTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room extends Item{
    private String name;
    private List<RoomTile> roomTiles = new ArrayList<RoomTile>();
    private Map<Integer, DoorTile> doorTiles;

    public Room(String name) {
        doorTiles = new HashMap<>();
        this.name = name;
    }

    public void addRoomTile(RoomTile t){
        roomTiles.add(t);
    }

    public void addDoorTile(int num, DoorTile d){
        doorTiles.put(num, d);
    }

    @Override
    public String getName() {
        return name;
    }
}

