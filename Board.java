import com.sun.tools.javac.Main;
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
        for (int j = 0; j < board[0].length; j++) {
            if (board[0][j] != null && board[1][j] != null) {
                board[0][j].attackCard(board[1][j], board[0][j].getPower());
                if (board[1][j].getHealth() <= 0) {
                    board[1][j] = null;
                }
            }
            if (board[0][j] != null && board[1][j] == null) {
                health += board[0][j].getPower();
                if (health >= 5) {
                    return -1; // return to map
                }
            }
        }
        for (int j = 0; j < board[0].length; j++) {
            if (board[1][j] != null && board[0][j] != null) {
                board[1][j].attackCard(board[0][j], board[1][j].getPower());
                if (board[0][j].getHealth() <= 0) {
                    board[0][j] = null;
                }
            }
            if (board[1][j] != null && board[0][j] == null) {
                health -= board[1][j].getPower();
                if (health <= 5) {
                    return 1; // return to game screen
                }
            }
        }
        return 0;
    }

    // location is index in the array, 0 for very last and 4 for last
    // side true for player false forAI
    public void changeBoard(boolean side, int location, Card new1) {
        if (side == true) {
            board[0][location] = new1;
        }
        else {
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
}
