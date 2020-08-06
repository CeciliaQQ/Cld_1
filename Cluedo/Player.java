package Cluedo;

import Cluedo.Items.Person;

import java.util.List;

public class Player {
    private String name;
    private List<Card> hand;
    private Person person;

    public Player(String name, List<Card> hand, Person person) {
        this.name = name;
        this.hand = hand;
        this.person = person;
    }


    public List<Card> getHand(){
        return hand;
    }

    public Person getPerson(){ return person;}

    public String getName(){ return person.getName(); }

    public String getHandString(){
        String string = hand.get(0).getName();
        for(int i = 1; i < hand.size(); i++)
            string += ", " + hand.get(i).getName();
        return string;
    }

}