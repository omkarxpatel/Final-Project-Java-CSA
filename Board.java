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

    public void update() {
        for (int j = 0; j < board[0].length; j++) {
            if (board[0][j] != null && board[1][j] != null) {
                board[0][j].attackCard(board[1][j], board[0][j].getPower());
            }
            if (board[0][j] != null && board[1][j] == null) {
                health += board[0][j].getPower();
                if (health >= 5) {
                    // return to the map
                }
            }
        }
        for (int j = 0; j < board[0].length; j++) {
            if (board[1][j] != null && board[0][j] != null) {
                board[1][j].attackCard(board[0][j], board[1][j].getPower());
            }
            if (board[1][j] != null && board[0][j] == null) {
                health -= board[1][j].getPower();
                if (health <= 5) {
                    // return to game screen
                }
            }
        }

        
    }

    public void changeBoard() {
        
    }
    public static void main(String[] args) {
    }
}
