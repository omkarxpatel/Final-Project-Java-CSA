import java.util.*;

public class Board {
    private int health;
    private Stack<Card> deck;
    private Card[][] board;
    private Player currentPlayer;
    private int bones;
    private ArrayList<Card> hand;

    public Board(Player currentPlayer1) {
        health = 0;
        board = new Card[3][4];
        currentPlayer = currentPlayer1;
        deck = shuffle(currentPlayer.getCards());
        bones = 0; 
        hand = new ArrayList<Card>();
        for (int i = 0; i < 3; i++) {
            hand.add(deck.pop());
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
        Stack<Card> returnDeck = new Stack<Card>();
        while (!deck1.isEmpty()) {
            index = (int) (Math.random() * deck1.size());
            returnDeck.add(deck1.remove(index));
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
                        if (board[i][k].getAbilities().contains("ant")) {
                            count++;
                        }
                    }
                    power += count - 1;
                }
                if (lCard != null && lCard.getAbilities().contains("alpha")) {
                    power++;
                }
                if (rCard != null && rCard.getAbilities().contains("alpha")) {
                    power++;
                }
                if (oCard != null && oCard.getAbilities().contains("stinky")) {
                    power--;
                }

                if (thisCard != null) {
                    ArrayList<Card> targets = new ArrayList<Card>();
                    boolean doublestrike = abilities.contains("doublestrike");
                    boolean triplestrike = abilities.contains("triplestrike");
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
                    int[] results = board[i][j].attackCard((Card[]) targets.toArray(), power);
                    if (oCard.getAbilities().contains("beeswithin")) {
                        currentPlayer.addCards(Card.cards.get("bee"));
                    }
                    for (int r : results) {
                        health += r;
                    }

                    for (Card c : targets) {
                        if (c == null || c.getHealth() <= 0) {
                            if (c.getAbilities().contains("unkillable") && i == 1) {
                                currentPlayer.addCards(c);
                            }
                            c = null;
                            bones++; 
                        }
                    }
                }

                if (health >= 5) {
                    return 1; // return to map
                }
                if (health <= -5) {
                    return -1; // return to map
                }
            }
        }
        for (int j = 0; j < board[0].length; j++) {
            if (board[1][j] == null && board[2][j] != null) {
                board[1][j] = board[2][j];
                board[2][j] = null;
            }
        }
        return 0;
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
        board[0][location] = null;
    }

    public static void main(String[] args) {
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

    public void setBones(int i) {
        bones = i;
    }
}
