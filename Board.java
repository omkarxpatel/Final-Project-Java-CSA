import java.io.*;
import java.util.*;

public class Board {
    private int health;
    private Stack<Card> deck;
    private Card[][] board;

    public Board() {
        health = 0;
        board = new Card[2][4];

    }

    public void shuffle() {

    }

    public void update() {
        for (int j = 0; j < board[0].length; j++) {
            if (board[0][j] != null && board[1][j] != null) {
                board[0][j].attackCard(board[1][j], board[0][j].getPower());
            }
            if (board[0][j] != null && board[1][j] == null) {
                health += board[0][j].getPower();
            }
        }
        for (int j = 0; j < board[0].length; j++) {
            if (board[1][j] != null && board[0][j] != null) {
                board[1][j].attackCard(board[0][j], board[1][j].getPower());
            }
            if (board[1][j] != null && board[0][j] == null) {
                health -= board[1][j].getPower();
            }
        }
    }
}
