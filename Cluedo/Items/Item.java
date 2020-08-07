package Cluedo.Items;

/**
 * Superclass of Person, Room, and Weapon.
 */
public class Item {
    // Name of the Item
    public String name;

    // Get the name of the Item (Person, Room, or Weapon)
    public String getName() {
        return name;
    }
}