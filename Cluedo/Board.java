package Cluedo;
import Cluedo.Items.*;
import Cluedo.Tiles.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the underlying data structure for a 24 x 25 board.
 * Each square for each position,
 * null if this square is not available.
 *
 * "@" is the partition (wall) (cannot go across it);
 * "|" is the separator of two horizontally adjacent tiles (can go across it);
 * "---" is the separator of two vertically adjacent tiles (can go across it).
 */
public class Board {
    /** Tiles on each square. */
    private Tile[][] tiles;
    /** Horizontal separator lines. */
    private List<String> unmodifiableLines = new ArrayList<String>();
    /**
     *  Using different letters to represent different types of tiles:
     *    numbers: DoorTile
     *    B: BlockedTile
     *    H: HallTile
     *    K: RoomTile - Kitchen
     *    A: RoomTile - Ball Room
     *    C: RoomTile - Conservatory
     *    O: RoomTile - Billiards Room
     *    L: RoomTile - Library
     *    S: RoomTile - Study
     *    X: RoomTile - Hall
     *    U: RoomTile - Lounge
     *    I: RoomTile - Dining Room
     *
     */
    private String initialTiles;
    /**
     * Show the name of the characters for short.
     */
    private List<String> keyStrings = new ArrayList<String>();
    /**
     * "Kitchen", "Ball Room", "Conservatory", "Billiards Room",
     * "Library", "Study", "Hall", "Lounge", "Dining Room"
     */
    private Room[] rooms;
    /**
     * "Candlestick", "Revolver", "Spanner", "Rope", "Dagger", "Lead Pipe"
     */
    private Weapon[] weapons;
    /**
     * Constructor
     * @param rooms 9 rooms in the game
     */
    public Board(Room[] rooms, Weapon[] weapons) {
        this.rooms = rooms;
        this.weapons = weapons;
        this.tiles = new Tile[25][24];
        generateTiles();
        generateLines();
        assignRoomsToDoors();
        placeWeapons();
    }


    /**
     * Move the person following input move direction.
     *
     *
     * @param player the one who is moving
     * @param move   a string (w, a, s, d) that represents the direction
     * @return true if the player can move towards the "move" direction;
     *         false if the player cannot move to the "move" direction.
     */
    public boolean movePerson(Player player, String move){
        Person person = player.getPerson();
        int row = person.row;
        int col = person.col;
        tiles[row][col].setItem(null);
        int dx = 0;
        int dy = 0;
        if(move.equalsIgnoreCase("w")) dy--;
        if(move.equalsIgnoreCase("a")) dx--;
        if(move.equalsIgnoreCase("s")) dy++;
        if(move.equalsIgnoreCase("d")) dx++;
        Tile oldTile = tiles[row][col];
        if((row + dy) < 0 || (row + dy) >= 25 || (col + dx) < 0 || (col + dx) >= 24){
            return false;
        }
        Tile newTile = tiles[row + dy][col + dx];
        if(newTile instanceof DoorTile || newTile instanceof HallTile ||
                (oldTile instanceof DoorTile && newTile instanceof RoomTile)) {
            newTile.setItem(person);
            person.row = row + dy;
            person.col = col + dx;
            return true;
        }else
            return false;

    }

    /**
     * Teleport a person on the board
     *
     * @param player the player needed to be teleported
     * @param door the door the player will be sent to
     */
    public void teleportPerson(Player player, Tile door){
        Person person = player.getPerson();
        int row = person.row;
        int col = person.col;
        tiles[row][col].setItem(null);
        door.setItem(player.getPerson());
    }

    /**
     * Check if a player is in a room or not.
     *
     * @param player a player
     * @return true if the player is in a room;
     *         false otherwise.
     */
    public boolean inRoom(Player player){
        Person person = player.getPerson();
        int row = person.row;
        int col = person.col;
        return tiles[row][col] instanceof RoomTile;
    }

    /**
     * Add people onto the tiles when initialising a new board.
     *
     * @param people 6 people (represented by the name of the character, starting row and column) in the game.
     */
    public void addPeople(Person[] people){
        for(int i = 0; i < people.length; i++){
            Person person = people[i];
            int row = person.row;
            int col = person.col;
            tiles[row][col].setItem(person);
        }
    }

    /**
     * Generate the horizontal separator lines (generate one line every two lines of the board)
     */
    private void generateLines() {
        unmodifiableLines.add("                                    @@@@@               @@@@@                                    \n");
        unmodifiableLines.add("@@@@@@@@@@@@@@@@@@@@@@@@@   @@@@@@@@@---@@@@@@@@@@@@@@@@@---@@@@@@@@@   @@@@@@@@@@@@@@@@@@@@@@@@@       *************************\n");
        unmodifiableLines.add("@        KITCHEN        @@@@@---@@@@@@@@@   BALLROOM    @@@@@@@@@---@@@@@      CONSERVATORY     @       *************************\n");
        unmodifiableLines.add("@                       @-------@                               @-------@                       @       *  M - Colonel Mustard  *\n");
        unmodifiableLines.add("@                       @-------@                               @-------@                       @       *  G - Mr. Green        *\n");
        unmodifiableLines.add("@                       @-------@                               @-------@@@@@               @@@@@       *  P - Professor Plum   *\n");
        unmodifiableLines.add("@@@@@                   @-------@                               @-----------@@@@@@@@@@@@@@@@@@@@@       *  D - Dagger           *\n");
        unmodifiableLines.add("@@@@@@@@@@@@@@@@@---@@@@@-------@                               @---------------------------@@@@@       *  R - Revolver         *\n");
        unmodifiableLines.add("@@@@@---------------------------@@@@@---@@@@@@@@@@@@@@@@@---@@@@@-------@@@@@@@@@@@@@@@@@@@@@@@@@       *  A - Spanner          *\n");
        unmodifiableLines.add("@@@@@@@@@@@@@@@@@@@@@---------------------------------------------------@     BILLARD ROOM      @\n");
        unmodifiableLines.add("@                   @@@@@@@@@@@@@-------@@@@@@@@@@@@@@@@@@@@@-----------@                       @\n");
        unmodifiableLines.add("@         DINING ROOM           @-------@                   @-----------@                       @\n");
        unmodifiableLines.add("@                               @-------@                   @-----------@                       @\n");
        unmodifiableLines.add("@                               @-------@                   @-----------@@@@@@@@@@@@@@@@@---@@@@@\n");
        unmodifiableLines.add("@                               @-------@                   @-----------@@@@@@@@@---@@@@@@@@@    \n");
        unmodifiableLines.add("@                               @-------@                   @-------@@@@@      LIBRARY      @@@@@\n");
        unmodifiableLines.add("@@@@@@@@@@@@@@@@@@@@@@@@@---@@@@@-------@                   @-------@                           @\n");
        unmodifiableLines.add("@@@@@-----------------------------------@@@@@@@@@@@@@@@@@@@@@-------@                           @\n");
        unmodifiableLines.add("@@@@@-------------------------------@@@@@@@@@-------@@@@@@@@@-------@@@@@                   @@@@@\n");
        unmodifiableLines.add("@@@@@@@@@@@@@@@@@@@@@@@@@---@-------@          HALL         @-----------@@@@@@@@@@@@@@@@@@@@@@@@@\n");
        unmodifiableLines.add("@          LOUNGE           @-------@                       @-------------------------------@@@@@\n");
        unmodifiableLines.add("@                           @-------@                       @-------@---@@@@@@@@@@@@@@@@@@@@@@@@@\n");
        unmodifiableLines.add("@                           @-------@                       @-------@            STUDY          @\n");
        unmodifiableLines.add("@                           @-------@                       @-------@                           @\n");
        unmodifiableLines.add("@                       @@@@@---@@@@@                       @@@@@---@@@@@                       @\n");
        unmodifiableLines.add("@@@@@@@@@@@@@@@@@@@@@@@@@   @@@@@   @@@@@@@@@@@@@@@@@@@@@@@@@   @@@@@   @@@@@@@@@@@@@@@@@@@@@@@@@\n");

        keyStrings.add(" @       *          KEY          *\n");
        keyStrings.add(" @       *  S - Miss Scarlett    *\n");
        keyStrings.add(" @       *  W - Mrs. White       *\n");
        keyStrings.add(" @       *  E - Mrs. Peacock     *\n");
        keyStrings.add("         *  C - Candlestick      *\n");
        keyStrings.add(" @       *  L - Lead Pipe        *\n");
        keyStrings.add("         *  O - Rope             *\n");
        keyStrings.add(" @       *************************\n");

    }

    /**
     * Tiles on the board.
     */
    public void generateTiles() {
        initialTiles = "BBBBBBBBBHBBBBHBBBBBBBBB" +
                "KKKKKKBHHHAAAAHHHBCCCCCC" +
                "KKKKKKHHAAAAAAAAHHCCCCCC" +
                "KKKKKKHHAAAAAAAAHHCCCCCC" +
                "KKKKKKHHAAAAAAAAHHCCCCCC" +
                "KKKKKKH1AAAAAAAA4H1CCCCB" +
                "BKKKKKHHAAAAAAAAHHHHHHHH" +
                "HHHH1HHHAAAAAAAAHHHHHHHB" +
                "BHHHHHHHH2HHHH3HHHOOOOOO" +
                "IIIIIHHHHHHHHHHHH1OOOOOO" +
                "IIIIIIIIHHBBBBBHHHOOOOOO" +
                "IIIIIIIIHHBBBBBHHHOOOOOO" +
                "IIIIIIII1HBBBBBHHHOOOOOO" +
                "IIIIIIIIHHBBBBBHHHHH1H2B" +
                "IIIIIIIIHHBBBBBHHHLLLLLB" +
                "IIIIIIIIHHBBBBBHHLLLLLLL" +
                "BHHHHH2HHHBBBBBH2LLLLLLL" +
                "HHHHHHHHHHH12HHHHLLLLLLL" +
                "BHHHHH1HHXXXXXXHHHLLLLLB" +
                "UUUUUUUHHXXXXXXHHHHHHHHH" +
                "UUUUUUUHHXXXXXX3H1HHHHHB" +
                "UUUUUUUHHXXXXXXHHSSSSSSS" +
                "UUUUUUUHHXXXXXXHHSSSSSSS" +
                "UUUUUUUHHXXXXXXHHSSSSSSS" +
                "UUUUUUBHBXXXXXXBHBSSSSSS";
        int index = 0;
        char currentChar;
        for(int i = 0; i< 25; i++) {
            for(int j = 0; j<24; j++) {
                currentChar = initialTiles.charAt(index);
                if(currentChar == 'B') {
                    tiles[i][j] = new BlockedTile();
                }
                else if(currentChar == 'H') {
                    tiles[i][j] = new HallTile();
                }
                else if(currentChar == '1' || currentChar == '2' || currentChar == '3' || currentChar == '4') {
                    tiles[i][j] = new DoorTile(Integer.parseInt(String.valueOf(currentChar)));
                }
                else{
                    if(currentChar == 'K') {
                        tiles[i][j] = new RoomTile(rooms[0]);
                        rooms[0].addRoomTile((RoomTile)tiles[i][j]);
                    }
                    if(currentChar == 'A') {
                        tiles[i][j] = new RoomTile(rooms[1]);
                        rooms[1].addRoomTile((RoomTile)tiles[i][j]);
                    }
                    if(currentChar == 'C') {
                        tiles[i][j] = new RoomTile(rooms[2]);
                        rooms[2].addRoomTile((RoomTile)tiles[i][j]);
                    }
                    if(currentChar == 'O') {
                        tiles[i][j] = new RoomTile(rooms[3]);
                        rooms[3].addRoomTile((RoomTile)tiles[i][j]);
                    }
                    if(currentChar == 'L') {
                        tiles[i][j] = new RoomTile(rooms[4]);
                        rooms[4].addRoomTile((RoomTile)tiles[i][j]);
                    }
                    if(currentChar == 'S') {
                        tiles[i][j] = new RoomTile(rooms[5]);
                        rooms[5].addRoomTile((RoomTile)tiles[i][j]);
                    }
                    if(currentChar == 'X') {
                        tiles[i][j] = new RoomTile(rooms[6]);
                        rooms[6].addRoomTile((RoomTile)tiles[i][j]);
                    }
                    if(currentChar == 'U') {
                        tiles[i][j] = new RoomTile(rooms[7]);
                        rooms[7].addRoomTile((RoomTile)tiles[i][j]);
                    }
                    if(currentChar == 'I') {
                        tiles[i][j] = new RoomTile(rooms[8]);
                        rooms[8].addRoomTile((RoomTile)tiles[i][j]);
                    }
                }
                index = index + 1;
            }
        }
    }

    /**
     * Assign each door tile to a specific room
     */
    public void assignRoomsToDoors() {
        for(int i = 0; i<25; i++) {
            for(int j = 0; j <24; j++) {
                if(tiles[i][j] instanceof DoorTile) {
                    if(tiles[i][j+1] instanceof RoomTile) {
                        ((DoorTile) tiles[i][j]).setRoom(((RoomTile) tiles[i][j+1]).getRoom());
                        ((RoomTile)(tiles[i][j+1])).getRoom().removeTile(tiles[i][j+1]);
                    }
                    else if(tiles[i][j-1] instanceof RoomTile) {
                        ((DoorTile) tiles[i][j]).setRoom(((RoomTile) tiles[i][j-1]).getRoom());
                        ((RoomTile)tiles[i][j-1]).getRoom().removeTile(tiles[i][j+1]);
                    }
                    else if(tiles[i+1][j] instanceof RoomTile) {
                        ((DoorTile) tiles[i][j]).setRoom(((RoomTile) tiles[i+1][j]).getRoom());
                        ((RoomTile) tiles[i+1][j]).getRoom().removeTile(tiles[i][j+1]);
                    }
                    else if(tiles[i-1][j] instanceof RoomTile) {
                        ((DoorTile) tiles[i][j]).setRoom(((RoomTile) tiles[i-1][j]).getRoom());
                        ((RoomTile) tiles[i-1][j]).getRoom().removeTile(tiles[i][j+1]);
                    }
                }
            }
        }
        ((DoorTile) tiles[13][20]).setRoom(((RoomTile) tiles[14][20]).getRoom());
        ((DoorTile) tiles[13][22]).setRoom(((RoomTile) tiles[12][22]).getRoom());
    }

    /**
     *
     */
    public void placeWeapons() {
        List<Integer> usedRooms = new ArrayList<Integer>();
    }

    /**
     * Get the room that a specific player is in
     * @param player a specific player
     * @return the room the player is in
     */
    public Room getRoom(Player player){
        Person person = player.getPerson();
        int row = person.row;
        int col = person.col;
        return ((RoomTile) tiles[row][col]).getRoom();
    }

    /**
     * Draw the board line by line: one horizontal separator line, followed by one line of tiles (looping in this way).
     */
    public void drawBoard() {
        String boardOutput = "";
        int i = 0;
        while(i<25) {
            boardOutput = boardOutput + unmodifiableLines.get(i);
            String arrayLine = "";
            for(int j = 0; j<24; j++) {

                //Special cases - on either end of the board
                if(j == 0) {
                    if(tiles[i][j] instanceof RoomTile) {
                        arrayLine = arrayLine + "@ ";
                    }
                    else if(tiles[i][j] instanceof HallTile) {
                        arrayLine = arrayLine + "@ ";
                    }
                    else if(tiles[i][j] instanceof BlockedTile) {
                        arrayLine = arrayLine + "  ";
                    }
                }

                arrayLine = arrayLine + tiles[i][j].toString(); //put a key to represent what is actually in the tile

                if(j == 23) {
                    if(i > 0 && i < 9) {
                        arrayLine = arrayLine + keyStrings.get(i-1);
                    }
                    else {
                        if(tiles[i][j] instanceof RoomTile) {
                            arrayLine = arrayLine + " @\n";
                        }
                        else if(tiles[i][j] instanceof HallTile) {
                            arrayLine = arrayLine + " @\n";
                        }
                        else if(tiles[i][j] instanceof BlockedTile) {
                            arrayLine = arrayLine + "  \n";
                        }
                    }
                }
                else {
                    if(tiles[i][j] instanceof RoomTile && tiles[i][j+1] instanceof RoomTile) {
                        arrayLine = arrayLine + "   ";
                    }
                    else if(tiles[i][j] instanceof HallTile && tiles[i][j+1] instanceof HallTile) {
                        arrayLine = arrayLine + " | ";
                    }
                    else if(tiles[i][j] instanceof BlockedTile && tiles[i][j+1] instanceof BlockedTile){
                        arrayLine = arrayLine + "   ";
                    }
                    else if(tiles[i][j] instanceof DoorTile && tiles[i][j+1] instanceof DoorTile){
                        arrayLine = arrayLine + " | ";
                    }
                    else if(tiles[i][j] instanceof RoomTile && tiles[i][j+1] instanceof HallTile || tiles[i][j] instanceof HallTile && tiles[i][j+1] instanceof RoomTile){
                        arrayLine = arrayLine + " @ ";
                    }
                    else if(tiles[i][j] instanceof BlockedTile && tiles[i][j+1] instanceof HallTile || tiles[i][j] instanceof HallTile && tiles[i][j+1] instanceof BlockedTile){
                        arrayLine = arrayLine + " @ ";
                    }
                    else if(tiles[i][j] instanceof RoomTile && tiles[i][j+1] instanceof BlockedTile || tiles[i][j] instanceof BlockedTile && tiles[i][j+1] instanceof RoomTile){
                        arrayLine = arrayLine + " @ ";
                    }
                    else if(tiles[i][j] instanceof DoorTile && tiles[i][j+1] instanceof HallTile || tiles[i][j] instanceof HallTile && tiles[i][j+1] instanceof DoorTile){
                        arrayLine = arrayLine + " | ";
                    }
                    else if(tiles[i][j] instanceof BlockedTile && tiles[i][j+1] instanceof DoorTile || tiles[i][j] instanceof DoorTile && tiles[i][j+1] instanceof BlockedTile){
                        arrayLine = arrayLine + " @ ";
                    }
                    else if(tiles[i][j] instanceof RoomTile && tiles[i][j+1] instanceof DoorTile || tiles[i][j] instanceof DoorTile && tiles[i][j+1] instanceof RoomTile){
                        arrayLine = arrayLine + " | ";
                    }
                }
            }
            boardOutput = boardOutput + arrayLine;
            i = i + 1;
        }
        boardOutput = boardOutput + unmodifiableLines.get(25);
        System.out.println(boardOutput);
    }
}