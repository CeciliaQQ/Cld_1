package Cluedo.Items;

/**
 * This class defines the weapons in the game.
 */
public class Weapon extends Item{
    // the name of the weapon.
    private String name;

    /**
     * Constructing a Weapon using its name
     * @param name name of the weapon
     */
    public Weapon(String name) {
        this.name = name;
    }

    /**
     * Get the name of the weapon.
     * @return the name of the weapon.
     */
    public String getName(){ return name; }

    /**
     * Get the letter representing the name of the weapon.
     * @return the letter representing the name of the weapon
     */
    public String toString() {
        if(this.name.equals("Candlestick")){
            return "C";
        }else if(this.name.equals("Dagger")){
            return "D";
        }else if(this.name.equals("Lead Pipe")){
            return "L";
        }else if(this.name.equals("Revolver")){
            return "R";
        }else if(this.name.equals("Rope")){
            return "O";
        }else if(this.name.equals("Spanner")){
            return "A";
        }else{
            throw new Error("Invalid Weapon Name");
        }
    }
}