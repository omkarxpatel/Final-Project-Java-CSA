import java.util.*;

public class Card {
    private String name;
    private int health;
    private int power;
    private int cost;
    private int costType;
    private ArrayList<String> abilities;
    private String desc;
    public static TreeMap<String,Card> cards = new TreeMap<String,Card>(); 

    static {
        cards.put("adder", new Card("adder", 1, 1, 2, 1, null));
        cards.put("stoat", new Card("stoat", 2, 1, 1, 1, null));
        cards.put("wolf", new Card("wolf", 2, 3, 2, 1, null));
        cards.put("bullfrog", new Card("bullfrog", 2, 1, 1, 1, new ArrayList<String>()));
    }

    public Card(String name,
            int health,
            int power,
            int cost,
            int costType,
            ArrayList<String> abilities) {
        this.name = name;
        this.health = health;
        this.power = power;
        this.cost = cost;
        this.costType = costType;
        this.abilities = abilities;
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

    public int getCostType() {
        return costType;
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