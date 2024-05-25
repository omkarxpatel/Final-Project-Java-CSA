import java.util.*;

public class Board {
    private int health;
    private Stack<Card> deck;
    private Card[][] board;
    private Player currentPlayer;

    public Board(Player currentPlayer1) {
        health = 0;
        board = new Card[2][4];
        currentPlayer = currentPlayer1;
        deck = shuffle(currentPlayer.getCards());

    }

    public static Stack<Card> shuffle(ArrayList<Card> deck1) {
        int index = 0;
        Stack<Card> returnDeck = new Stack<Card>();
        while (!deck1.isEmpty()) {
            index = (int) (Math.random() * deck1.size());
            returnDeck.add(deck1.remove(index));
        }
        return returnDeck;
    }

    public int update() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < board[0].length; j++) {

                
                Card thisCard = board[i][j];
                Card lCard = j == 0 ? null : board[i][j-1];
                Card rCard = j == 3 ? null : board[i][j+1];
                Card oCard = board[1-i][j];
                Card loCard = j == 0 ? null : board[1-i][j-1];
                Card roCard = j == 3 ? null : board[1-i][j+1];

                int power = thisCard.getPower();
                ArrayList<String> abilities = thisCard.getAbilities();
    
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
                    }
                    else {
                        if (doublestrike) {
                            if (!(j == 0)) targets.add(loCard);
                            if (!(j == 3)) targets.add(roCard);
                        }
                        if (triplestrike) {
                            if (!(j == 0)) targets.add(loCard);
                            targets.add(loCard);
                            if (!(j == 3)) targets.add(roCard);
                        }
                    }
                    int[] results = board[i][j].attackCard((Card[])targets.toArray(), power);
                    for (int r : results) {
                        health += r;
                    }
    
                    for (Card c : targets) {
                        if (c == null || c.getHealth() <= 0) {
                            c = null;
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
    public void changeBoard(boolean side, int location, Card new1) {
        if (side == true) {
            board[0][location] = new1;
        } else {
            board[1][location] = new1;
        }
    }

    public void draw() {
        currentPlayer.draw(deck.pop());
    }

    public void sacrifice(int location) {
        board[0][location] = null;
    }

    public static void main(String[] args) {
    }

    public void playCard(ArrayList<Integer> sacrifices, Card new1, int location) {
        for (int i : sacrifices) {
            sacrifice(i);
        }
        changeBoard(true, location, new1);
    }
}
