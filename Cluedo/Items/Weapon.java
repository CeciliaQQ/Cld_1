
package Cluedo.Items;

public class Weapon extends Item{
    private String name;

    public Weapon(String name) {
        this.name = name;
    }

    public String getName(){ return name; }

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