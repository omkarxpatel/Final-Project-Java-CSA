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
            Render render) {
        this.board = board;
        this.player = player;
        this.map = map;
        this.render = render;
        screen = 0;
        selected = 0;
        cursorPositions = Render.CURSOR_PAIRS[screen];
    }

    public void processInput(String input) {
        // TODO
        switch (input) {
            case "a": {
                if (cursorPositions.length == 1)
                    break;
                selected--;
                if (selected < 0)
                    selected = cursorPositions.length - 1;
                render.displayCursor(screen, selected);
                break;
            }
            case "d": {
                if (cursorPositions.length == 1)
                    break;
                selected++;
                if (selected > cursorPositions.length - 1)
                    selected = 0;
                render.displayCursor(screen, selected);
                break;
            }
            case "s": {
                String action = cursorPositions[selected].action();
                switch (action) {
                    case "title": {
                        gotoScreen(0);
                        break;
                    }
                    case "credits": {
                        gotoScreen(2);
                        break;
                    }
                }
            }
            default:
                break;
        }
    }

    public void gotoScreen(int screenIndex) {
        screen = screenIndex;
        selected = 0;
        render.loadScreen(screenIndex);
        render.displayCursor(screen, selected);
        cursorPositions = Render.CURSOR_PAIRS[screen];
    }

    public void flush() {
        render.flush();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Render r = new Render();
        Player p = new Player();
        Board b = new Board(p);
        Map m = new Map();
        Game game = new Game(b, p, m, r); // TODO
        String input = null;
        game.flush();
        while (true) {
            input = sc.nextLine();
            game.processInput(input);
            game.flush();
        }
    }
}
