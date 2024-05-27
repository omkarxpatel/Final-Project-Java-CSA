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
                if (cursorPositions.length == 1) {
                    hoverAction();
                    break;
                }
                selected--;
                if (selected < 0)
                    selected = cursorPositions.length - 1;
                render.displayCursor(screen, selected);
                hoverAction();
                break;
            }
            case "d": {
                if (cursorPositions.length == 1) {
                    hoverAction();
                    break;
                }
                selected++;
                if (selected > cursorPositions.length - 1)
                    selected = 0;
                render.displayCursor(screen, selected);
                hoverAction();
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
                    case "play": {
                        gotoScreen(1);
                        ArrayList<Card> initCards = new ArrayList<Card>();
                        initCards.add(Card.cards.get("stoat"));
                        initCards.add(Card.cards.get("wolf"));
                        initCards.add(Card.cards.get("bullfrog"));
                        player = new Player(initCards);
                        deckDrawCards();
                        hoverAction();
                        render.displayText("THIS IS YOUR STARTING DECK. ", 7, 25, 30);
                        break;
                    }
                    case "exitDeck": {
                        gotoScreen(3);

                        break;
                    }
                }
            }
            default:
                break;
        }
    }

    public void hoverAction() {
        String action = Render.CURSOR_PAIRS[screen][selected].hover();
        if (action == null || action.equals("") ) return;
        switch (action) {
            case "showCardInDeck": {
                ArrayList<Card> deck = player.getCards();
                if (selected >= deck.size()) {
                    render.displayCardBig(null);
                }
                else {
                    render.displayCardBig(player.getCards().get(selected));
                }
                
            }
        }
    }

    public void deckDrawCards() {
        ArrayList<Card> cards = player.getCards();
        int row = 1;
        int col = 25;
        if (cards.size() < 7) {
            for (Card c : cards) {
                render.displayCard(1, col, c);
                col += 5;
            }
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
        Map m = new Map(1);
        Game game = new Game(b, p, m, r); // TODO
        String input = null;
        m.initNodes(); 
        m.randNodes();
        // display the map here or something
        
        game.flush();
        while (true) {
            input = sc.nextLine();
            game.processInput(input);
            game.flush();
            // System.out.println("𐀛𐀗𐀂𐀭𐘃𐡗");
        }
    }
}
