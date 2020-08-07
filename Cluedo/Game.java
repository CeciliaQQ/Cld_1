package Cluedo;
import Cluedo.Items.Person;
import Cluedo.Items.Room;
import Cluedo.Items.Weapon;
import Cluedo.Tiles.DoorTile;

import java.util.*;

/**
 * Game class defines the board of the game, and the cards in the game.
 * The number of players is between 3 and 6.
 * Players can move their cards and make accusation.
 */
public class Game {
    /**
     * The board of the game: @ is the partition (wall) (cannot go across it);
     *                        | is the separator of two horizontally adjacent tiles (can go across it);
     *                        --- is the separator of two vertically adjacent tiles (can go across it).
     */
    private Board board;
    /**
     * Six persons on the board.
     */
    private Person[] people;
    /**
     * Nine rooms on the board.
     */
    private Room[] rooms;
    /**
     * Six weapons on the board.
     */
    private Weapon[] weapons;
    /**
     * Three murder cards.
     */
    private Card[] murderCards;
    /**
     * Players in the game (from 3 to 6).
     */
    private Player[] players;
    /**
     * A collection of all cards in the game:
     *    key is the name of the card,
     *    value is the card object.
     */
    private Map<String, Card> cards;

    /**
     * Names of the six characters.
     */
    private static final String[] CHARACTER_NAMES = {"Miss Scarlett", "Professor Plum", "Mrs. Peacock", "Colonel Mustard",
            "Mrs. White", "Mr. Green"};

    /**
     * Names of the nine rooms.
     */
    private static final String[] ROOM_NAMES = {"Kitchen", "Ball Room", "Conservatory", "Billiards Room",
            "Library", "Study", "Hall", "Lounge", "Dining Room"};

    /**
     * Names of the weapons.
     */
    private static final String[] WEAPON_NAMES = {"Candlestick", "Revolver", "Spanner", "Rope", "Dagger", "Lead Pipe"};

    /**
     * Starting rows of the corresponding characters in CHARACTER_NAMES (same order).
     */
    private static final int[] STARTING_ROWS = {0, 0, 7, 24, 6, 19};

    /**
     * Starting columns of the corresponding characters in CHARACTER_NAMES (same order).
     */
    private static final int[] STARTING_COLS = {9, 14, 0, 7, 23, 23};

    /**
     * Initialize the people, rooms, weapons, cards, and assigning murder cards.
     * Also, it asks the number of players, and assigns cards to the players.
     * After all the extra cards have been assigned, initialize the board and add people onto the board.
     */
    public void initialise(){
        // objects and cards
        people = new Person[6];
        for(int i = 0; i < 6; i++)
            people[i] = new Person(CHARACTER_NAMES[i], STARTING_ROWS[i], STARTING_COLS[i]);
        rooms = new Room[9];
        for(int i = 0; i < 9; i++)
            rooms[i] = new Room(ROOM_NAMES[i]);
        weapons = new Weapon[6];
        for(int i = 0; i < 6; i++)
            weapons[i] = new Weapon(WEAPON_NAMES[i]);

        List<Card> characterCards = new ArrayList<>();
        List<Card> weaponCards = new ArrayList<>();
        List<Card> roomCards = new ArrayList<>();

        cards = new HashMap<>();
        for(Person c: people) {
            Card card = new Card(c);
            characterCards.add(card);
            cards.put(c.getName(), card);
        }
        for(Weapon w: weapons) {
            Card c = new Card(w);
            weaponCards.add(c);
            cards.put(w.getName(), c);
        }
        for(Room r: rooms) {
            Card c = new Card(r);
            roomCards.add(c);
            cards.put(r.getName(), c);
        }

        // assigning murder cards
        murderCards = new Card[3];
        Card murderCharacter = characterCards.get((int) (Math.random() * characterCards.size()));
        characterCards.remove(murderCharacter);
        Card murderWeapon = weaponCards.get((int) (Math.random() * weaponCards.size()));
        weaponCards.remove(murderWeapon);
        Card murderRoom = roomCards.get((int) (Math.random() * roomCards.size()));
        roomCards.remove(murderRoom);
        murderCards[0] = murderCharacter;
        murderCards[1] = murderWeapon;
        murderCards[2] = murderRoom;
        List<Card> handCards = new ArrayList<>();
        handCards.addAll(characterCards);
        handCards.addAll(weaponCards);
        handCards.addAll(roomCards);

        // players and hands
        System.out.print("Number of players? ");
        Scanner scan = new Scanner(System.in);
        int playerNum = scan.nextInt();
        while(playerNum < 3 || playerNum > 6){
            System.out.println("Please enter a number between three to six");
            playerNum = scan.nextInt();
        }
        players = new Player[playerNum];
        int cardNum = 18 / playerNum;
        for(int i = 0; i < playerNum; i++) {
            List<Card> hand = new ArrayList<>();
            for (int j = 0; j < cardNum; j++) {
                int index = (int) (Math.random() * handCards.size());
                hand.add(handCards.get(index));
                handCards.remove(index);
            }
            players[i] = new Player(CHARACTER_NAMES[i], hand, people[i]);
        }
        // extra cards
        if(handCards.size() != 0){
            System.out.print("Extra cards: ");
            for(int i = 0; i < handCards.size() - 1; i++){
                System.out.print(handCards.get(i).getName() + ", ");
            }
            System.out.print(handCards.get(handCards.size() - 1).getName());
            System.out.println();
        }

        // initialize a new board
        board = new Board(rooms, weapons);
        board.addPeople(people);
    }

    /**
     * Run the game: Roll the two dice and sum up the two integers.
     * Implement a move and then update the board.
     * Print the number of remaining moves, ask for a move if there is any move remaining;
     * or turn to the next player.
     * When a player makes a correct accusation, stop running and return.
     */
    public void run(){
        while(true){
            for(int i = 0; i < players.length; i++) {

                board.drawBoard();
                System.out.println(players[i].getName() + "'s turn");

                if(board.inRoom(players[i])){
                    if(askAccusation(i)) {
                        if (makeAccusation(i)) return;
                    }else{
                        Room room = board.getRoom(players[i]);
                        Map<Integer, DoorTile> doorTiles = room.getDoorTiles();
                        System.out.print("What door would you like to exit from (integer between 1-" +
                                doorTiles.size() + ")?");
                        int doorNum = getNumber(doorTiles.size());
                        board.teleportPerson(players[i], doorTiles.get(doorNum));
                    }
                }else {
                    int diceOne = (int) (Math.random() * 6) + 1;
                    int diceTwo = (int) (Math.random() * 6) + 1;
                    System.out.println("You rolled a " + diceOne + " and a " + diceTwo);
                    for (int j = diceOne + diceTwo; j >= 1; j--) {
                        System.out.println("You have " + j + " moves remaining");
                        String move = getMove();
                        while (!board.movePerson(players[i], move)) {
                            System.out.println("You can't move in that direction");
                            move = getMove();
                        }
                        board.drawBoard();
                        if (board.inRoom(players[i])) {
                            if (askAccusation(i)) {
                                if (makeAccusation(i)) return;
                                else break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Ask the player if he wants to make an accusation
     * @param playerNum the index of the player in the player list
     * @return true if the player wants to make an accusation; otherwise return false.
     */
    public boolean askAccusation(int playerNum){
        System.out.println("You are in the " + board.getRoom(players[playerNum]).getName());
        System.out.print("Would you like to make an accusation? ");
        return getAnswer();
    }

    /**
     * Press w a s d to move the card.
     * w: up;
     * a: left;
     * s: down;
     * d: right.
     *
     * @return w, a, s, or d.
     */
    public String getMove(){
        System.out.print("Please enter in your move (w a s d): ");
        Scanner scan = new Scanner(System.in);
        String move = scan.next();
        while (!(move.equalsIgnoreCase("w") || move.equalsIgnoreCase("a") ||
                move.equalsIgnoreCase("s") || move.equalsIgnoreCase("d"))) {
            System.out.println("Please enter any of the following characters: w, a, s, d");
            move = scan.next();
        }
        return move;
    }

    /**
     * Make an accusation.
     *
     * @param playerNum index of the player
     * @return true if the player makes a correct accusation;
     *         otherwise return false.
     */
    public boolean makeAccusation(int playerNum){
        Scanner scan = new Scanner(System.in);
        System.out.println("Make an accusation (refer to key for how to write an answer)");
        System.out.println("Here is your hand: " + players[playerNum].getHandString());
        Card roomCard = cards.get(board.getRoom(players[playerNum]).getName());
        Card weaponCard = getCard("weapon", WEAPON_NAMES);
        Card personCard = getCard("character", CHARACTER_NAMES);

        for(int i = playerNum + 1; i != playerNum; i++){
            if(i == players.length) i = 0;
            List<Card> hand = players[i].getHand();
            Card card = null;
            if(hand.contains(weaponCard)) card = weaponCard;
            else if(hand.contains(roomCard)) card = roomCard;
            else if(hand.contains(personCard)) card = personCard;
            if(card != null){
                System.out.println("Your accusation has been disputed: " +
                        players[i].getName() + " has the " + card.getName());
                System.out.println("Enter any key to continue");
                scan.next();
                return false;
            }
        }
        System.out.println("Nobody can dispute this guess. Would you like to check the envelope?");
        boolean answer = getAnswer();
        if(!answer) return false;
        // accusation successful, win
        if(murderCards[0] == personCard && murderCards[1] == weaponCard && murderCards[2] == roomCard){
            System.out.println(players[playerNum].getName() + " wins");
            return true;
        }else{
            System.out.println("These were not the correct cards");
            return false;
        }
    }

    /**
     * Get the answer entered by the user.
     *
     * @return true if the user inputs "yes";
     *         false if the user input "no".
     */
    public boolean getAnswer(){
        Scanner sc = new Scanner(System.in);
        String answer = sc.next();
        while(!(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no"))){
            System.out.println("Please input yes or no");
            answer = sc.next();
        }
        if(answer.equalsIgnoreCase("yes")) return true;
        else return false;
    }

    /**
     * Get a integer from the user.
     *
     * @return int between 1 and upper bound (inclusive).
     */
    public int getNumber(int upperBound){
        Scanner scan = new Scanner(System.in);
        while(!scan.hasNextInt()){
            System.out.println("Please input an integer between 1 and " + "upper bound:");
        }
        return scan.nextInt();
    }

    /**
     * Get the card based on the type or the corresponding array.
     *
     * @param type weapon, room, or character
     * @param names WEAPON_NAMES, ROOM_NAMES, or CHARACTER_NAMES
     * @return the card that matches the type and name (input by the user)
     */
    public Card getCard(String type, String[] names){
        Scanner scan = new Scanner(System.in);
        for(int i = 0; i < names.length; i++){
            System.out.println((i + 1) + " " + names[i]);
        }
        System.out.print("Enter a number to choose a "  + type + " (1 - " + names.length + "): ");
        int num = scan.nextInt();
        while(num < 1 || num >= names.length){
            System.out.print("Please enter a valid number (1 - " + names.length + "): ");
            num = scan.nextInt();
        }
        return cards.get(names[num - 1]);
    }

    /**
     * Initialise game and run it.
     * @param args
     */
    public static void main(String args[]){
        Game g = new Game();
        g.initialise();
        g.run();
        g.makeAccusation(1);

    }
}