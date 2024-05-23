import java.util.*;

@SuppressWarnings("unchecked")
public class Card {
    private String name;
    private int health;
    private int power;
    private int cost;
    private int costType;
    private ArrayList<String> abilities;
    public static TreeMap<String, Card> cards = new TreeMap<String, Card>();
    public static TreeMap<String, String> descriptions = new TreeMap<String, String>();

    static {
        ArrayList<String> leap = new ArrayList<String>();
        leap.add("leap");
        ArrayList<String> deathTouch = new ArrayList<String>();
        deathTouch.add("deathtouch");
        ArrayList<String> airborne = new ArrayList<String>();
        airborne.add("airborne");
        ArrayList<String> doubleStrike = new ArrayList<String>();
        airborne.add("doublestrike");

        cards.put("squirrel", new Card("squirrel", 1, 0, 0, 0, null));
        cards.put("adder", new Card("adder", 1, 1, 2, 1, (ArrayList<String>) deathTouch.clone()));
        cards.put("stoat", new Card("stoat", 2, 1, 1, 1, null));
        cards.put("wolf", new Card("wolf", 2, 3, 2, 1, null));
        cards.put("bullfrog", new Card("bullfrog", 2, 1, 1, 1, (ArrayList<String>) leap.clone()));
        cards.put("sparrow", new Card("sparrow", 1, 2, 1, 1, (ArrayList<String>) airborne.clone()));
        cards.put("mantis", new Card("mantis", 1, 1, 1, 1, (ArrayList<String>) doubleStrike.clone()));
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

    public ArrayList<String> getAbilities() {
        return abilities;
    }

    public void modifyHealth(int modifier) {
        health += modifier;
    }

    public int[] attackCard(Card[] opponents, int power) {
        int[] rVals = new int[opponents.length];
        for (int i = 0; i < opponents.length; i++) {
            int r = attackCard(opponents[i], power);
            rVals[0] = r;
        }
        return rVals;
    }

    public int attackCard(Card opponent, int power) {
        if (opponent == null) {
            return power;
        }

        ArrayList<String> A = getAbilities();
        ArrayList<String> opA = opponent.getAbilities();
        if (A.contains("airborne")) {
            if (opA.contains("leap")) {
                attack(opponent, power);
            } else {
                return power;
            }
        } else if (A.contains("deathtouch")) {
            attack(opponent, opponent.getHealth());
        } else {
            attack(opponent, power);
        }

        // System.out.println(name + " has dealt " + String.valueOf(power) + " damage to
        // " + opponent.getName());
        return 0;
    }

    private void attack(Card opponent, int cardPower) {
        opponent.modifyHealth(-cardPower);
    }

    public void useAbility(Card opponent, String ability) {
        if (ability.equals("Insta Kill")) {
            attackCard(opponent, opponent.getHealth());
        }

    }

}