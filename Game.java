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
    private Card[] choiceBuffer;
    private boolean sacrificing = false;
    private int sacrificesNeeded = 0;
    private ArrayList<Card> sacrificeBuffer = new ArrayList<Card>();
    private boolean drawCard = true;
    private Card cardBuffer;
    private int battlesWon = 0;

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
                        battlesWon = 0;
                        player = new Player();
                        deckDrawCards();
                        hoverAction();
                        render.setLastCursorChar('─');
                        render.displayCursor(screen,selected);
                        messageBuffer = "YOU WERE LOST DEEP IN THE FOREST... ";
                        render.displayMessage(messageBuffer,screen);
                        break;
                    }
                    case "exitDeck": {
                        if (firstNode) {
                            switch (map.getChapter()) {
                                case 1: {
                                    messageBuffer = "THE WOODLANDS";
                                    break;
                                }
                                default: {
                                    messageBuffer = "";
                                    break;
                                }
                            }
                            firstNode = false;
                        }
                        openMap();
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
                        break;
                    }
                    case "chooseCardCampfire": {
                        if (selected >= player.getCards().size()) {
                            selected = 0;
                            render.displayCursor(screen, selected);
                            break;
                        }
                        if (Math.random() < 0.5) {
                            player.getCards().get(selected).modifyHealth(2);
                        }
                        else {
                            player.getCards().get(selected).modifyPower(1);
                        }
                        openMap();
                        break;
                    }
                    case "chooseCardChoice": {
                        player.addCards(choiceBuffer[selected]);
                        openMap();
                        break;
                    }
                    case "openDeckBattle": {
                        gotoScreen(6);
                        handDrawCards();
                        hoverAction();
                        render.setLastCursorChar('─');
                        break;
                    }
                    case "exitDeckBattle": {
                        gotoScreen(5);
                        render.displayBoard(board);
                        render.displayCursor(screen, selected);
                        break;
                    }
                    case "chooseCardOnBoard": {
                        if (selected > 5) {
                            break;
                        }
                        if (cardBuffer != null && !sacrificing) {
                            if (cardBuffer.getCostType() == 2) {
                                board.setBones(board.getBones() - cardBuffer.getCost());
                            }
                            board.getBoard()[0][selected] = cardBuffer;
                            board.getHand().remove(cardBuffer);
                            cardBuffer = null;
                            render.displayBoard(board);
                            break;
                        }
                        if (sacrificing && 
                            !sacrificeBuffer.contains(board.getBoard()[0][selected])) {
                                if (board.getBoard()[0][selected] == null) {
                                    render.displayMessage("INVALID SACRIFICE", screen);
                                    break;
                                }
                                sacrificeBuffer.add(board.getBoard()[0][selected]);
                                board.sacrifice(selected);
                                render.displayMessage("SACRIFICE MADE", screen);
                                sacrificesNeeded--;
                                if (sacrificesNeeded <= 0) {
                                    sacrificing = false;
                                    sacrificeBuffer = new ArrayList<Card>();
                                }
                                render.displayBoard(board);
                        }
                        break;
                    }
                    case "playCard": {
                        if (drawCard) {
                            render.displayMessage("DRAW A CARD FIRST", 6);
                            break;
                        }
                        if (selected < board.getHand().size()) {
                            cardBuffer = board.getHand().get(selected);

                            if (cardBuffer.getCostType() == 2 && board.getBones() < cardBuffer.getCost()) {
                                render.displayMessage("NOT ENOUGH BONES", 6);
                                cardBuffer = null;
                                break;
                            }

                            if (cardBuffer.getCostType() == 1) {
                                sacrificing = true;
                                sacrificesNeeded = cardBuffer.getCost();
                            }
                            else {
                                sacrificing = false;
                                sacrificesNeeded = 0;
                            }

                            if (sacrificing) {
                                render.displayMessage("CARD SELECTED, " + cardBuffer.getCost() + " SACRIFICES NEEDED", 6);
                            }
                            else {
                                render.displayMessage("CARD SELECTED", 6);
                            }
                            
                        }
                        break;
                    }
                    case "battleBell": {
                        drawCard = true;
                        int result = board.update();
                        if (result == -1) {
                            gotoScreen(0);
                            render.displayText("LOST", 7, 29, 4);
                            break;
                        }
                        if (result == 1) {
                            openMap();
                            render.displayMessage("VICTORY", 3);
                            battlesWon++;
                            break;
                        }
                        render.displayBoard(board);
                        break;
                    }
                    case "drawFromDeck": {
                        if (!drawCard) {
                            render.displayMessage("ALREADY DREW CARD", screen);
                            break;
                        }
                        if (board.getDeck().isEmpty()) {
                            render.displayMessage("NO MORE CARDS", screen);
                            break;
                        }
                        drawCard = false;
                        board.getHand().add(board.getDeck().pop());
                        handDrawCards();
                        render.displayMessage("CARD DRAWN", screen);
                        break;
                    }
                    case "drawSquirrel": {
                        if (!drawCard) {
                            render.displayMessage("ALREADY DREW CARD", screen);
                            break;
                        }
                        drawCard = false;
                        board.getHand().add(Card.cards.get("squirrel"));
                        handDrawCards();
                        render.displayMessage("CARD DRAWN", screen);
                        break;
                    }
                }
            }
            default:
                break;
        }
    }

    private void openMap() {
        gotoScreen(3);
        render.displayMap(map);
        cursorPositions = Render.cursorPairs[screen];
        render.displayCursor(screen, selected);
        render.displayMessage(messageBuffer, screen);
    }

    public void openNode(int selected) {
        MapNode[][] nodes = map.getNodes();
        int progress = map.getProgress();
        int pos = map.getPos();
        MapNode selectedNode = nodes[progress + 1][selected];
        map.setProgress(progress + 1);
        map.setPos(selected);
        switch (selectedNode.event()) {
            case "campfire": {
                CursorPosition[] cursorPositionsGeneric = new CursorPosition[21];
                for (int i = 0; i < 21; i++) {
                    cursorPositionsGeneric[i] = new CursorPosition(4, 2 + 5 * (i / 7), 9 + 7 * (i % 7), "chooseCardCampfire", null);
                }
                Render.cursorPairs[4] = cursorPositionsGeneric;
                gotoScreen(4);
                render.setLastCursorChar('─');
                render.displayMessage("You came across a dim campfire.".toUpperCase(), 4);
                campfireDrawCards();
                break;
            }
            case "choice": {
                CursorPosition[] cursorPositionsGeneric = new CursorPosition[3];
                for (int i = 0; i < 3; i++) {
                    cursorPositionsGeneric[i] = new CursorPosition(4, 7, 23 + 7 * (i % 7), "chooseCardChoice", null);
                }
                Render.cursorPairs[4] = cursorPositionsGeneric;
                gotoScreen(4);
                render.setLastCursorChar('─');
                render.displayMessage("You came across a group of beasts. One may join your deck".toUpperCase(), 4);
                choiceBuffer = choiceDrawCards();
                break;
            }
            case "battle": {
                gotoScreen(5);
                board = new Board(player, battlesWon);
                render.displayBoard(board);
                render.setLastCursorChar('─');
                break;
            }
        }
        render.displayCursor(screen, selected);
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
                ArrayList<Card> deck;
                if (screen == 6) {
                    deck = board.getHand();
                }
                else {
                    deck = player.getCards();
                }
                
                if (selected >= deck.size()) {
                    render.displayCardBig(null);
                } else {
                    render.displayCardBig(deck.get(selected));
                }
                break;
            }
            case "showCardInBattle": {
                Card[][] b = board.getBoard();
                if (selected <= 3) {
                    render.displayCardBig(b[0][selected]);
                    break;
                }
                if (selected >= 6 && selected <= 9) {
                    render.displayCardBig(b[1][selected - 6]);
                    break;
                }
                if (selected >= 10) {
                    render.displayCardBig(b[2][selected - 10]);
                    break;
                }
                render.displayCardBig(null);
                break;
            }
        }
    }

    /**
     * The `deckDrawCards` function displays a player's cards on the screen
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
        else {
            for (int i = 0; i < 7; i++) {
                render.displayCard(1, col, cards.get(i));
                col += 5;
            }
            col = 25;
            if (cards.size() < 14) {
                for (int i = 7; i < cards.size(); i++) {
                    render.displayCard(6, col, cards.get(i));
                    col += 5;
                }
            }
            else {
                for (int i = 7; i < 14; i++) {
                    render.displayCard(6, col, cards.get(i));
                    col += 5;
                }
                col = 25;
                for (int i = 14; i < cards.size() && i < 21; i++) {
                    render.displayCard(11, col, cards.get(i));
                    col += 5;
                }
            }
        }
    }

    public void handDrawCards() {
        ArrayList<Card> cards = board.getHand();
        int row = 1;
        int col = 25;
        if (cards.size() < 7) {
            for (Card c : cards) {
                render.displayCard(1, col, c);
                col += 5;
            }
        }
        else {
            for (int i = 0; i < 7; i++) {
                render.displayCard(1, col, cards.get(i));
                col += 5;
            }
            col = 25;
            if (cards.size() < 14) {
                for (int i = 7; i < cards.size(); i++) {
                    render.displayCard(6, col, cards.get(i));
                    col += 5;
                }
            }
            else {
                for (int i = 7; i < 14; i++) {
                    render.displayCard(6, col, cards.get(i));
                    col += 5;
                }
                col = 25;
                for (int i = 14; i < cards.size() && i < 21; i++) {
                    render.displayCard(11, col, cards.get(i));
                    col += 5;
                }
            }
        }
    }

    public void campfireDrawCards() {
        ArrayList<Card> cards = player.getCards();
        int row = 3;
        int col = 7;
        if (cards.size() < 7) {
            for (Card c : cards) {
                render.displayCard(2, col, c);
                col += 7;
            }
        }
        else {
            for (int i = 0; i < 7; i++) {
                render.displayCard(2, col, cards.get(i));
                col += 7;
            }
            col = 7;
            if (cards.size() < 14) {
                for (int i = 7; i < cards.size(); i++) {
                    render.displayCard(7, col, cards.get(i));
                    col += 7;
                }
            }
            else {
                for (int i = 7; i < 14; i++) {
                    render.displayCard(12, col, cards.get(i));
                    col += 7;
                }
                col = 7;
                for (int i = 14; i < cards.size() && i < 21; i++) {
                    render.displayCard(12, col, cards.get(i));
                    col += 7;
                }
            }
        }
    }

    private Card[] choiceDrawCards() {
        Card[] cards = new Card[3];
        Card[] cardPool = Card.CARDS_COMMON;
        for (int i = 0; i < 3; i ++) {
            cards[i] = cardPool[(int)(cardPool.length * Math.random())];
            render.displayCard(7, 21 + 7 * i, cards[i]);
        }
        return cards;
    }

    private static int battleID() {

        return 0;
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
        Map m = new Map(1);
        Game game = new Game(null, null, m, r); 
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
