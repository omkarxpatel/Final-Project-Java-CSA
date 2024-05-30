import java.util.*;

public class Game {
    private Board board;
    private Player player;
    private Map map;
    private Render render;
    private int screen;
    private int selected;
    private CursorPosition[] cursorPositions;
    private String messageBuffer = "";
    private boolean firstNode = true;
    private boolean firstCampfire = true;

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
        cursorPositions = Render.cursorPairs[screen];
    }

    /**
     * The `processInput` function in Java handles different input cases to perform
     * actions such as
     * navigating screens, initializing player cards, and displaying text.
     * 
     * @param input The `processInput` method you provided seems to handle different
     *              actions based on
     *              the input provided. The input parameter is a String that
     *              represents the user's input, such as
     *              "a", "d", or "s". The method uses a switch statement to
     *              determine the action to take based on
     *              the input
     */
    public void processInput(String input) {
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
                        render.setLastCursorChar('─');
                        render.displayText("STARTING DECK. ", 7, 25, 30);
                        render.displayText("<HEALTH ", 15, 4, 30);
                        render.displayText("POWER>", 16, 14, 30);
                        messageBuffer = "YOU WERE LOST DEEP IN THE FOREST... ";
                        render.displayMessage(messageBuffer,screen);
                        break;
                    }
                    case "exitDeck": {
                        if (firstNode) {
                            switch (map.getChapter()) {
                                case 1: {
                                    messageBuffer = "The sun rose over the firs... Birds fluttered across the paths of beasts... You were embarking upon... the Woodlands.".toUpperCase();
                                    break;
                                }
                                default: {
                                    messageBuffer = "";
                                    break;
                                }
                            }
                            firstNode = false;
                        }
                        gotoScreen(3);
                        render.displayMap(map);
                        cursorPositions = Render.cursorPairs[screen];
                        render.displayCursor(screen, selected);
                        render.displayMessage(messageBuffer, screen);
                        break;
                    }
                    case "openDeck": {
                        gotoScreen(1);
                        deckDrawCards();
                        hoverAction();
                        render.setLastCursorChar('─');
                        break;
                    }
                    case "node": {
                        openNode(selected);
                    }
                }
            }
            default:
                break;
        }
    }

    public void openNode(int selected) {
        MapNode[][] nodes = map.getNodes();
        int progress = map.getProgress();
        int pos = map.getPos();
        MapNode selectedNode = nodes[progress + 1][selected];
        map.setProgress(progress + 1);
        map.setPos(selected);
        switch (selectedNode.event()) {
            case ("campfire"): {
                
                if (firstCampfire) {
                    firstCampfire = false;
                }
            }
        }
    }

    /**
     * The `hoverAction` function checks for a specific action related to hovering
     * over a card in a deck
     * and displays the card if the action is valid.
     */

    public void hoverAction() {
        String action = Render.cursorPairs[screen][selected].hover();
        if (action == null || action.equals(""))
            return;
        switch (action) {
            case "showCardInDeck": {
                ArrayList<Card> deck = player.getCards();
                if (selected >= deck.size()) {
                    render.displayCardBig(null);
                } else {
                    render.displayCardBig(player.getCards().get(selected));
                }

            }
        }
    }

    /**
     * The `deckDrawCards` function displays a player's cards on the screen in a row
     * if the player has less
     * than 7 cards.
     */

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

    /**
     * The `gotoScreen` function sets the current screen, loads the screen content,
     * displays the cursor,
     * and updates cursor positions.
     * 
     * @param screenIndex The `screenIndex` parameter in the `gotoScreen` method
     *                    represents the index of
     *                    the screen that you want to navigate to. This method sets
     *                    the `screen` variable to the specified
     *                    `screenIndex`, resets the `selected` variable to 0, loads
     *                    the screen content using the `render.load
     */

    public void gotoScreen(int screenIndex) {
        screen = screenIndex;
        selected = 0;
        render.loadScreen(screenIndex);
        render.displayCursor(screen, selected);
        cursorPositions = Render.cursorPairs[screen];
    }

    /**
     * The `flush` function calls the `flush` method of the `render` object.
     */

    public void flush() {
        render.flush();
    }

    /**
     * The main function initializes game components and continuously processes user
     * input to interact
     * with the game world.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Render r = new Render();
        Player p = new Player();
        Board b = new Board(p);
        Map m = new Map(1);
        Game game = new Game(b, p, m, r); // TODO
        String input = null;

        int progress = m.getProgress();
        int pos = m.getPos();

        game.flush();
        while (true) {
            input = sc.nextLine();
            game.processInput(input);
            game.flush();
            // System.out.println("𐀛𐀗𐀂𐀭𐘃𐡗");
        }
    }
}
