import java.util.*;

public class Card {
    private String name;
    private int health;
    private int power;
    private int cost;
    private ArrayList<String> abilities;
    private String desc;

    public Card(String name,
            int health,
            int power,
            int cost,
            ArrayList<String> abilities,
            String desc) {
        this.health = health;
        this.power = power;

        this.abilities = abilities;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getPower() {
        return power;
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

    public void attackCard(Card opponent, int cardPower) {
        opponent.modifyHealth(-cardPower);
        System.out.println(name + " has dealt " + String.valueOf(power) + " damage to " + opponent.getName());
    }

    public void useAbility(Card opponent, String ability) {
        if (ability.equals("Insta Kill")) {
            attackCard(opponent, opponent.getHealth());
        }
    }

}