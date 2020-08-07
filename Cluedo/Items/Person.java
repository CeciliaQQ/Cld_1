package Cluedo.Items;

/**
 * The Person class defines the characters in the game.
 * Because "Character" cannot be used here (class Character existsalready),
 * we call the characters in the game "Person".
 */
public class Person extends Item{
    // name of the character
    private String name;
    // position of the character
    public int row, col;

    /**
     * Construct a Person object by its name, and original position (row and column).
     *
     * @param name name of the character/person
     * @param row  original row (at the start of the game)
     * @param col  original column (at the start of the game)
     */
    public Person(String name, int row, int col) {
        this.name = name;
        this.row = row;
        this.col = col;
    }

    /**
     * Get the name of the character.
     * @return the name of the character
     */
    public String getName(){ return name; }

    /**
     * A letter to represent the name of a character for short.
     * @return A letter representing the name of a character.
     */
    public String toString() {
        if(this.name.equals("Miss Scarlett")){
            return "S";
        }else if(this.name.equals("Colonel Mustard")){
            return "M";
        }else if(this.name.equals("Mrs. White")){
            return "W";
        }else if(this.name.equals("Mr. Green")){
            return "G";
        }else if(this.name.equals("Mrs. Peacock")){
            return "E";
        }else if(this.name.equals("Professor Plum")){
            return "P";
        }else{
            throw new Error("Invalid Player Name.");
        }
    }

}