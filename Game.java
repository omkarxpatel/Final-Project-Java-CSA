import java.util.*;

public class Game {
    private Board board; 
    private Player player; 
    private Map map;
    private Render render; 
    private int selected;

    public Game(Board board,
                Player player,
                Map map,
                Render render) 
                {
        this.board = board;
        this.player = player;
        this.map = map;
        this.render = render;
        selected = -1;
    }

    public static void processInput(String input) {
        //TODO
        switch (input) {
            case "a": 
            case "s":
            case "d":
            default: break;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Render r = new Render();
        Game game = new Game(null, null, null, r); //TODO
        String input = null;
        r.loadPreset(0);
        r.flush();
        while (true) {
            input = sc.nextLine();
            processInput(input);
            r.flush();
        }
    }
}

