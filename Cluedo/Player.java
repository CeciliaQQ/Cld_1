package Cluedo;
import Cluedo.Items.Person;
import java.util.List;

/**
 * The Player class defines the player in the game.
 * A player has his name, a list of cards in his hand, and the character he is playing.
 */
public class Player {
    // name of the player
    private String name;
    // the cards that are assigned to the player
    private List<Card> hand;
    // the character the player is playing
    private Person person;

    /**
     * Constructing a player using his name, the cards in his hand, and the character he is playing.
     *
     * @param name name of the player
     * @param hand cards the player has
     * @param person the character the player is playing
     */
    public Player(String name, List<Card> hand, Person person) {
        this.name = name;
        this.hand = hand;
        this.person = person;
    }

    /**
     * Get the cards the player has.
     *
     * @return the cards in the player's hand
     */
    public List<Card> getHand(){
        return hand;
    }

    /**
     * Get the character of the player.
     * @return the character of the player
     */
    public Person getPerson(){ return person;}

    /**
     * Get the name of the player
     * @return the name of the player
     */
    public String getName(){ return person.getName(); }

    /**
     * Get the names of the cards the player has.
     * @return a string of the names of the cards.
     */
    public String getHandString(){
        String string = hand.get(0).getName();
        for(int i = 1; i < hand.size(); i++)
            string += ", " + hand.get(i).getName();
        return string;
    }
}