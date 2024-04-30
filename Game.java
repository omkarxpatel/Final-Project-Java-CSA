import java.util.*;

public class Game {
    private Board board; 
    private Player player; 
    private Map map; 

    public Game(Board board,
                Player player,
                Map map
                ) {
        this.board = board;
        this.player = player;
        this.map = map;
    }

    public void keyInput() {

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Render r = new Render();
        while (true) {
            
        }
    }
}

