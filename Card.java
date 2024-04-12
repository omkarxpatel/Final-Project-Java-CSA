import java.util.*;

public class Card {
    private int health;
    private int damage;
    private int cost;
    private ArrayList<String> abilities;

    public Card(int health, 
        int damage, 
        int cost, 
        ArrayList<String> abilities ) {
            this.health = health;
            this.damage = damage;
            this.cost = cost;
            this.abilities = abilities;
    }

    public int getHealth() {return health;}
    public int getDamage() {return damage;}
    public int getCost() {return cost;}
    public ArrayList<String> getAbilities() {return abilities;}



}