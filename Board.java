import java.util.*;

public class Board {
    private int health;
    private Stack<Card> deck;
    private Card[][] board;
    private Player currentPlayer;
    private int bones;
    private ArrayList<Card> hand;
    private int turn = 0;
    private int battleID = 0;
    private TreeMap<String, Card> cards = Card.cards;

    public static final String[][][] BATTLES = new String[][][] {
        {{"coyote"}},
        {{"wolf cub"}, {"bat"}},
        {{"coyote", "bat"}},
        {{}, {"grizzly"}}
    };

    public Board(Player currentPlayer1, int id) {
        health = 0;
        board = new Card[3][4];
        currentPlayer = currentPlayer1;
        deck = shuffle(currentPlayer.getCards());
        bones = 0; 
        hand = new ArrayList<Card>();
        for (int i = 0; i < 3; i++) {
            hand.add(deck.pop());
        }
        this.battleID = id;

        String[] firstTurnCards = BATTLES[id][0];
        if (firstTurnCards != null && !firstTurnCards[0].isEmpty()) {
            ArrayList<Card> newCards = new ArrayList<Card>();
            for (int i = 0; i < BATTLES[battleID][turn].length; i++) {
                newCards.add(cards.get(BATTLES[battleID][turn][i]).clone());
            }
            ArrayList<Integer> newPos = ncrex(4, newCards.size(), null);
            for (int i = 0; i < newPos.size(); i++) {
                board[2][newPos.get(i)] = newCards.get(i);
            }
        }
    }

    /**
     * The function `shuffle` takes an ArrayList of Card objects, shuffles the cards
     * randomly, and
     * returns a Stack of shuffled cards.
     * 
     * @param deck1 The parameter `deck1` is an `ArrayList` of `Card` objects
     *              representing a deck of
     *              cards that you want to shuffle. The method `shuffle` takes this
     *              deck as input, shuffles the
     *              cards randomly, and returns a `Stack` of `Card` objects
     *              representing the shuffled deck.
     * @return The `shuffle` method returns a `Stack` of `Card` objects after
     *         shuffling the input
     *         `ArrayList` of `Card` objects.
     */
    public static Stack<Card> shuffle(ArrayList<Card> deck1) {
        int index = 0;
        ArrayList<Card> deckCopy = new ArrayList<Card>(deck1);
        Stack<Card> returnDeck = new Stack<Card>();
        while (!deckCopy.isEmpty()) {
            index = (int) (Math.random() * deckCopy.size());
            returnDeck.add(deckCopy.remove(index));
        }
        return returnDeck;
    }

    /**
     * The update function iterates through a 2D board of cards, applying various
     * abilities and attacks
     * based on card properties and conditions, and returns different values based
     * on health
     * conditions.
     * 
     * @return The method `update()` returns an integer value. If the condition
     *         `health >= 5` is met,
     *         it returns `1` which indicates to return to the map. If the condition
     *         `health <= -5` is met, it
     *         returns `-1` which also indicates to return to the map. If neither of
     *         these conditions is met,
     *         it returns `0`.
     */
    public int update() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Card thisCard = board[i][j];
                if (thisCard == null) continue;
                Card lCard = j == 0 ? null : board[i][j - 1];
                Card rCard = j == 3 ? null : board[i][j + 1];
                Card oCard = board[1 - i][j];
                Card loCard = j == 0 ? null : board[1 - i][j - 1];
                Card roCard = j == 3 ? null : board[1 - i][j + 1];
                
                int count = 0;
                int power = thisCard.getPower();
                ArrayList<String> abilities = thisCard.getAbilities();
                if (abilities == null)
                    abilities = new ArrayList<String>();

                if (abilities.contains("antspawner")) {
                    count = 0;
                    for (int k = 0; k < 4; k++) {
                        if (board[i][k].getAbilities() != null && board[i][k].getAbilities().contains("ant")) {
                            count++;
                        }
                    }
                    power += count - 1;
                }
                if (lCard != null && 
                    lCard.getAbilities() != null &&
                    lCard.getAbilities().contains("alpha")) {
                        power++;
                }
                if (rCard != null && 
                    rCard.getAbilities() != null &&
                    rCard.getAbilities().contains("alpha")) {
                        power++;
                }
                if (oCard != null && 
                    oCard.getAbilities() != null &&
                    oCard.getAbilities().contains("stinky")) {
                        power--;
                }

                if (thisCard != null) {
                    ArrayList<Card> targets = new ArrayList<Card>();
                    boolean doublestrike = (abilities == null) ? false : abilities.contains("doublestrike");
                    boolean triplestrike = (abilities == null) ? false : abilities.contains("triplestrike");
                    if (!doublestrike &&
                            !triplestrike) {
                        targets.add(oCard);
                    } else {
                        if (doublestrike) {
                            if (!(j == 0))
                                targets.add(loCard);
                            if (!(j == 3))
                                targets.add(roCard);
                        }
                        if (triplestrike) {
                            if (!(j == 0))
                                targets.add(loCard);
                            targets.add(loCard);
                            if (!(j == 3))
                                targets.add(roCard);
                        }
                    }
                    int[] results = board[i][j].attackCard(targets, power);
                    if (!(oCard == null) && 
                        !(oCard.getAbilities() == null) &&
                        oCard.getAbilities().contains("beeswithin")) {
                            currentPlayer.addCards(Card.cards.get("bee"));
                    }
                    for (int r : results) {
                        if (i == 0) health += r;
                        else health -= r;
                    }

                    for (int k = 0; k < 2; k++) {
                        for (int l = 0; l < 4; l++) {
                            Card c = board[k][l];
                            if (c != null && c.getHealth() <= 0) {
                                if (!(c.getAbilities() == null) && 
                                    c.getAbilities().contains("unkillable") && 
                                    i == 1) {
                                        currentPlayer.addCards(c);
                                }
                                board[k][l] = null;
                                bones++; 
                            }
                        }
                    }
                }

                if (health >= 5) {
                    return 1; // return to map
                }
                if (health <= -5) {
                    return -1; // return to map
                }
                if (!(thisCard.getAbilities() == null) && thisCard.getAbilities().contains("fledgling")) {
                    if (thisCard.getName().equals("wolf cub")) {
                        board[i][j] = cards.get("wolf");
                    }
                    else {
                        thisCard.modifyHealth(2);
                        thisCard.modifyPower(1);
                    }
                    thisCard.getAbilities().remove("fledgling");
                }
            }
            if (i == 0) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[1][j] == null && board[2][j] != null) {
                        board[1][j] = board[2][j];
                        board[2][j] = null;
                    }
                }
                nextCards();
            }
        }
        

        turn++;
        return 0;
    }

    private void nextCards() {
        ArrayList<Integer> occupiedOList = new ArrayList<Integer>();
        for (int i = 0; i < board[0].length; i++) {
            if (board[2][i] != null) {
                occupiedOList.add(i);
            }
        }
        if (turn > 0 && turn < BATTLES[battleID].length && occupiedOList.size() < 4) {
            ArrayList<Card> newCards = new ArrayList<Card>();
            for (int i = 0; i < BATTLES[battleID][turn].length && newCards.size() < 4 - occupiedOList.size(); i++) {
                newCards.add(cards.get(BATTLES[battleID][turn][i]));
            }
            ArrayList<Integer> newPos = ncrex(4, newCards.size(), occupiedOList);
            for (int i = 0; i < newPos.size(); i++) {
                board[2][newPos.get(i)] = newCards.get(i);
            }
        }
    }

    // location is index in the array, 0 for very last and 4 for last
    // side true for player false forAI
    /**
     * The function `changeBoard` updates a card at a specific location on the game
     * board based on the
     * side provided.
     * 
     * @param side     The `side` parameter in the `changeBoard` method represents
     *                 the side of the board
     *                 where the new card `new1` will be placed. It is an integer
     *                 value that determines whether the
     *                 card will be placed on the top side (side 0), bottom side
     *                 (side 1), or
     * @param location The `location` parameter in the `changeBoard` method
     *                 represents the position
     *                 within the board where the new card (`new1`) will be placed.
     *                 It is an integer value that
     *                 specifies the column index in the board array where the card
     *                 will be updated.
     * @param new1     `new1` is a parameter of type `Card` that represents the new
     *                 card that will be
     *                 placed on the board at the specified location and side.
     */
    public void changeBoard(int side, int location, Card new1) {
        if (new1.getAbilities().contains(""))

            if (side == 0) {
                board[0][location] = new1;
            } else if (side == 1) {
                board[1][location] = new1;
            } else {
                board[2][location] = new1;
            }

    }

    /**
     * The draw function draws a card from the deck and assigns it to the current
     * player.
     */
    public void draw() {
        currentPlayer.draw(deck.pop());
    }

    /**
     * The `sacrifice` function in Java removes a piece from the specified location
     * on the board.
     * 
     * @param location The `location` parameter in the `sacrifice` method represents
     *                 the column index
     *                 in a 2D array called `board`. The method sets the value at
     *                 the first row and the specified
     *                 column index to `null`, essentially removing the piece at
     *                 that location on the board.
     */
    public void sacrifice(int location) {
        Card sac = board[0][location];
        if (sac.getAbilities() == null || !sac.getAbilities().contains("manylives")) {
            board[0][location] = null;
        }
    }

    /**
     * The `playCard` function takes a list of sacrifices, a new card, and a
     * location, sacrifices the
     * specified cards, and then changes the board by placing the new card at the
     * specified location.
     * 
     * @param sacrifices The `sacrifices` parameter is an ArrayList of Integers that
     *                   represents the
     *                   cards being sacrificed in order to play the new card. The
     *                   `playCard` method iterates through
     *                   each Integer in the `sacrifices` list and calls the
     *                   `sacrifice` method for each card
     * @param new1       `new1` is an object of type `Card` that represents the new
     *                   card being played on the
     *                   board.
     * @param location   The `location` parameter in the `playCard` method
     *                   represents the position on the
     *                   board where the new card will be played. It is an integer
     *                   value that specifies the index or
     *                   position where the `new1` card will be placed on the board.
     */
    public void playCard(ArrayList<Integer> sacrifices, Card new1, int location) {
        for (int i : sacrifices) {
            if (board[0][i].getAbilities().contains("cagedwolf")) {
                changeBoard(0, i, Card.cards.get("wolf"));
            }
            if (board[0][i].getAbilities().contains("manylives")) {
                changeBoard(0, i, Card.cards.get("cat"));
            } else {
                sacrifice(i);
            }
        }
        if (new1.getAbilities().contains("antspawner")) {
            currentPlayer.addCards(Card.cards.get("worker ant"));
        }
        if (new1.getAbilities().contains("damBuilder")) {
            if (board[0][location - 1] == null) {
                changeBoard(0, location - 1, Card.cards.get("dam"));
            }
            if (board[0][location + 1] == null) {
                changeBoard(0, location + 1, Card.cards.get("dam"));
            }
        }
        changeBoard(0, location, new1);
    }

    public Card[][] getBoard() {
        return board;
    }

    public int getHealth() {
        return health;
    }

    public int getBones() {
        return bones;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public Stack<Card> getDeck() {
        return deck;
    }

    public void setBones(int i) {
        bones = i;
    }

    private static ArrayList<Integer> ncrex(int n, int r, List<Integer> e) {
        int i = 0;
        int rand = (int)(n * Math.random());
        ArrayList<Integer> out = new ArrayList<Integer>();
        while (i < r) {
            while (out.contains(rand) && (e == null || !e.contains(rand))) {
                rand = (int)(n * Math.random());
            }
            out.add(rand);
            i++;
        }
        return out;
    }
}
