import java.util.*;

public class Card {
    private int health;
    private int damage;
    private int cost;
    private ArrayList<String> abilities;
    private String desc;

    public Card(int health, 
        int damage, 
        int cost, 
        ArrayList<String> abilities, String desc ) {
            this.health = health;
            this.damage = damage;
            this.cost = cost;
            this.abilities = abilities;
            this.desc = desc;
    }

    public int getHealth() {return health;}
    public int getDamage() {return damage;}
    public int getCost() {return cost;}
    public ArrayList<String> getAbilities() {return abilities;}



}