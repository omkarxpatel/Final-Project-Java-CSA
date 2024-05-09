import java.util.*;

public class Game {
    private Board board; 
    private Player player; 
    private Map map;
    private Render render; 
    private int screen;
    private int selected;
    private CursorPosition[] cursorPositions;

    public Game(Board board,
                Player player,
                Map map,
                Render render) 
                {
        this.board = board;
        this.player = player;
        this.map = map;
        this.render = render;
        screen = 0;
        selected = 0;
        cursorPositions = Render.CURSOR_PAIRS[screen];
    }

    public void processInput(String input) {
        //TODO
        switch (input) {
            case "a": {
                selected--;
                if (selected < 0) selected = cursorPositions.length - 1;
                render.displayCursor(screen, selected);
                break;
            }
            case "d": {
                selected++;
                if (selected > cursorPositions.length - 1) selected = 0;
                render.displayCursor(screen, selected);
                break;
            }
            case "s": {

            }
            
            default: break;
        }
    }

    public void gotoScreen(int screenIndex) {
        screen = screenIndex;
        selected = 0;
        render.loadScreen(screenIndex);
        render.displayCursor(screen, selected);
        render.flush();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Render r = new Render();
        Game game = new Game(null, null, null, r); //TODO
        String input = null;
        r.flush();
        while (true) {
            input = sc.nextLine();
            game.processInput(input);
            r.flush();
        }
    }
}

