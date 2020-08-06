
package Cluedo.Items;

public class Person extends Item{
    private String name;
    public int row, col;

    public Person(String name, int row, int col) {
        this.name = name;
        this.row = row;
        this.col = col;
    }

    public String getName(){ return name; }

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