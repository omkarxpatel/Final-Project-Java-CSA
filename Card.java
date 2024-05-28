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
        doubleStrike.add("doublestrike");
        ArrayList<String> tripleStrike = new ArrayList<String>();
        tripleStrike.add("triplestrike");
        ArrayList<String> antSpawner = new ArrayList<String>();
        antSpawner.add("antspawner");

        cards.put("squirrel", new Card("squirrel", 1, 0, 0, 0, null));
        cards.put("adder", new Card("adder", 1, 1, 2, 1, (ArrayList<String>) deathTouch.clone()));
        cards.put("stoat", new Card("stoat", 2, 1, 1, 1, null));
        cards.put("wolf", new Card("wolf", 2, 3, 2, 1, null));
        cards.put("bullfrog", new Card("bullfrog", 2, 1, 1, 1, (ArrayList<String>) leap.clone()));
        cards.put("sparrow", new Card("sparrow", 1, 2, 1, 1, (ArrayList<String>) airborne.clone()));
        cards.put("mantis", new Card("mantis", 1, 1, 1, 1, (ArrayList<String>) doubleStrike.clone()));
        cards.put("antqueen", new Card("ant queen", 3, 1, 2, 1, (ArrayList<String>) antSpawner.clone())); 
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

    /**
     * The `getName` function in Java returns the value of the `name` variable.
     * 
     * @return The `name` variable is being returned.
     */
    public String getName() {
        return name;
    }

    /**
     * The function `getHealth` returns the current health value.
     * 
     * @return The method `getHealth()` is returning the value of the `health`
     *         variable.
     */
    public int getHealth() {
        return health;
    }

    /**
     * This Java function returns the value of the "power" variable.
     * 
     * @return The method `getPower()` is returning the value of the variable
     *         `power`.
     */
    public int getPower() {
        return power;
    }

    /**
     * The `getCost` function in Java returns the cost value.
     * 
     * @return The `cost` variable is being returned.
     */
    public int getCost() {
        return cost;
    }

    /**
     * The function `getCostType` returns the cost type as an integer.
     * 
     * @return The method `getCostType()` is returning the value of the variable
     *         `costType`.
     */
    public int getCostType() {
        return costType;
    }

    /**
     * The function `getAbilities()` returns an ArrayList of Strings representing
     * abilities.
     * 
     * @return An ArrayList of Strings containing abilities is being returned.
     */
    public ArrayList<String> getAbilities() {
        return abilities;
    }

    /**
     * The `modifyHealth` function in Java increases the health variable by the
     * specified modifier.
     * 
     * @param modifier The `modifier` parameter in the `modifyHealth` method is an
     *                 integer value that is
     *                 used to adjust the `health` variable by adding or subtracting
     *                 its value.
     */
    public void modifyHealth(int modifier) {
        health += modifier;
    }

    /**
     * The function `attackCard` takes an array of `Card` objects and a power value,
     * and returns an array
     * of integers representing the result of attacking each opponent with the given
     * power.
     * 
     * @param opponents It looks like you are trying to create a method `attackCard`
     *                  that takes an array of
     *                  `Card` objects and an integer `power` as parameters. The
     *                  method is supposed to attack each opponent
     *                  card with the given power and return an array of results.
     * @param power     It looks like there is a mistake in the `attackCard` method.
     *                  The `rVals[0] = r;`
     *                  statement inside the loop should be `rVals[i] = r;` to store
     *                  the result for each opponent correctly.
     * @return The `attackCard` method is returning an array of integers `rVals`,
     *         which contains the result
     *         of attacking each opponent card with the specified power.
     */
    public int[] attackCard(Card[] opponents, int power) {
        int[] rVals = new int[opponents.length];
        for (int i = 0; i < opponents.length; i++) {
            int r = attackCard(opponents[i], power);
            rVals[0] = r;
        }
        return rVals;
    }

    /**
     * The `attackCard` function in Java determines the outcome of a card attack
     * based on the abilities
     * of the attacking and defending cards.
     * 
     * @param opponent The `opponent` parameter in the `attackCard` method
     *                 represents the card that is
     *                 being attacked. It is of type `Card`, which likely contains
     *                 information about the opponent card
     *                 such as its abilities, health, and name. The method checks
     *                 the abilities of the attacking card
     *                 and the opponent card to
     * @param power    Power is the amount of damage that the attacking card can
     *                 deal to the opponent card.
     * @return The method `attackCard` is returning an integer value. The specific
     *         value being returned
     *         depends on the conditions within the method. If the opponent is null,
     *         the method returns the
     *         `power` parameter. Otherwise, the method may return 0 after
     *         performing certain actions based on
     *         the abilities of the cards involved in the attack.
     */
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

    /**
     * The `attack` function decreases the health of the opponent card by the
     * specified power.
     * 
     * @param opponent  The `opponent` parameter is a reference to the `Card` object
     *                  representing the
     *                  opponent that is being attacked.
     * @param cardPower The `cardPower` parameter represents the amount of power or
     *                  damage that will be
     *                  inflicted on the opponent's health when the `attack` method
     *                  is called.
     */
    private void attack(Card opponent, int cardPower) {
        opponent.modifyHealth(-cardPower);
    }

    /**
     * The function `useAbility` checks if the ability is "Insta Kill" and attacks
     * the opponent card with
     * their remaining health if it is.
     * 
     * @param opponent The `opponent` parameter in the `useAbility` method
     *                 represents the card that the
     *                 player wants to target with the ability. This card is passed
     *                 as an argument to the method so that
     *                 the player can use the specified ability on it.
     * @param ability  The `ability` parameter in the `useAbility` method represents
     *                 the special ability
     *                 that the player wants to use on the opponent's card. In the
     *                 provided code snippet, if the
     *                 `ability` is "Insta Kill", it will call the `attackCard`
     *                 method on the opponent's card and
     */
    public void useAbility(Card opponent, String ability) {
        if (ability.equals("Insta Kill")) {
            attackCard(opponent, opponent.getHealth());
        }

    }

}