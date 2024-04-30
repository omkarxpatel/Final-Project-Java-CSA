import java.util.*;

public class Card {
    private String name;
    private int health;
    private int damage;
    private int cost;
    private ArrayList<String> abilities;
    private String desc;

    public Card(String name,
            int health,
            int damage,
            int cost,
            ArrayList<String> abilities,
            String desc) {
        this.health = health;
        this.damage = damage;

        this.abilities = abilities;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getCost() {
        return cost;
    }

    public String getDesc() {
        return desc;
    }

    public ArrayList<String> getAbilities() {
        return abilities;
    }

    public void modifyHealth(int modifier) {
        health += modifier;
    }

    public void attackCard(Card opponent, int cardDamage) {
        opponent.modifyHealth(-cardDamage);
        System.out.println(name + " has dealt " + String.valueOf(damage) + " damage to " + opponent.getName());
    }

    public void useAbility(Card opponent, String ability) {
        if (ability.equals("Insta Kill")) {
            attackCard(opponent, opponent.getHealth());
        }
    }

}