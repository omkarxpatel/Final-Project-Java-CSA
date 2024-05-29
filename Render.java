import java.util.*;
import java.lang.StringBuilder;
import java.lang.StringBuffer;

public class Render {
    private int maxLines = 20;
    private String[] display = new String[maxLines];
    private ArrayList<StringBuffer> displayBuffer = new ArrayList<StringBuffer>();
    private int currentScreen = 0;
    private int currentSelected = 0;
    private int lastCursorRow = -1;
    private int lastCursorCol = -1;
    private char lastCursorChar = (char) 0;
    private static char emptyChar = '.';
    private static String emptyLine = null;
    private static char[] cursorChars = new char[] { '✦', };
    private static int cursor = 0;
    private static int lineLength = 61;

    static {
        StringBuilder newLine = new StringBuilder();
        for (int i = 0; i < lineLength; i++)
            newLine.append(emptyChar);
        emptyLine = newLine.toString();
    }

    private static final String COLOR_WHITE = "\u001B[0m";
    private static String[] screenTitle = new String[] {
            "-------------------------------------------------------------",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            "......................┳┏┓┏┏┏┓┓┏┏┓╋┓┏┓┏┓......................",
            "......................┻┛┗┛┗┛ ┗┫┣┛┗┗┗┛┛┗......................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            "...........................┏┓┓┏┓┓┏...........................",
            "...........................┣┛┗┗┻┗┫...........................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            "-------------------------------------------------------------",
    };
    private static String[] screenDeck = new String[] {
            "-------------------------------------------------------------",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│....................................>",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│.....................................",
            "│......................│.....................................",
            "-------------------------------------------------------------"
    };
    private static String[] screenCredits = new String[] {
            "-------------------------------------------------------------",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            "....................Lynbrook High School.....................",
            ".............................................................",
            "........................2023 - 2024..........................",
            ".............................................................",
            "...............AP CS A Period 3, Mark Kwong..................",
            ".............................................................",
            ".........Omkar Patel, Sriman Ratnapu, Jianyu Wang............",
            ".............................................................",
            "............................BACK.............................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            "-------------------------------------------------------------"
    };
    private static String[] screenMap = new String[] {
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
    };
    private static String[][] screens = new String[][] {
            screenTitle,
            screenDeck,
            screenCredits,
            screenMap
    };

    public static CursorPosition[] cursorPositionsDeck = new CursorPosition[22];
    static {
        for (int i = 0; i < 21; i++) {
            cursorPositionsDeck[i] = new CursorPosition(1, 1 + 5 * (i / 7), 27 + 5 * (i % 7), "card", "showCardInDeck");
        }
        cursorPositionsDeck[21] = new CursorPosition(1, 6, 60, "exitDeck", null);
    }
    public static CursorPosition[][] cursorPairs = new CursorPosition[][] {
            {
                    new CursorPosition(0, 8, 30, "play", null),
                    new CursorPosition(0, 2, 30, "credits", null),
            },
            cursorPositionsDeck,
            {
                    new CursorPosition(2, 12, 26, "title", null)
            },
            {
                    new CursorPosition(3, 0, 0, "empty", null),
            }
    };

    public Render() {

        loadScreen(0);
        for (int i = 0; i < displayBuffer.size(); i++) {
            display[i] = displayBuffer.get(i).toString();
        }
        currentScreen = 0;
        currentSelected = 0;
        displayCursor(currentScreen, currentSelected);
    }

    /**
     * This function colors a specified range of text in a display buffer with the
     * given color.
     * 
     * @param color       The `color` parameter in the `colorText` method represents
     *                    the color that you want to
     *                    apply to the text within the specified range. This color
     *                    can be specified using color codes or color
     *                    names depending on the context in which you are working.
     *                    For example, you can use color names like
     *                    "RED
     * @param startPosRow The `startPosRow` parameter represents the starting row
     *                    index where you want to
     *                    begin coloring the text in the display buffer. If the
     *                    provided `startPosRow` is less than 0, the
     *                    method sets it to 1 before proceeding with coloring the
     *                    text.
     * @param startPosCol The `startPosCol` parameter in the `colorText` method
     *                    represents the starting
     *                    column position where the color should be applied within
     *                    the specified row.
     * @param endPosRow   The `endPosRow` parameter represents the ending row
     *                    position for the text color
     *                    change in the `colorText` method. It specifies the row
     *                    number where the color change operation
     *                    should stop.
     * @param endPosCol   The `endPosCol` parameter in the `colorText` method
     *                    represents the ending column
     *                    position where the color should be applied in the text.
     *                    The method will apply the specified color to
     *                    the text starting from the `startPosCol` up to (but not
     *                    including) the `endPosCol`
     */
    public void colorText(String color,
            int startPosRow,
            int startPosCol,
            int endPosRow,
            int endPosCol) {
        if (startPosRow < 0)
            startPosRow = 1;
        for (int i = startPosRow; i < endPosRow; i++) {
            String displayLine = displayBuffer.get(i).toString();
            displayLine = displayLine.substring(0, startPosCol) + color + displayLine.substring(startPosCol, endPosCol)
                    + COLOR_WHITE + displayLine.substring(endPosCol);
            displayBuffer.set(i, new StringBuffer(displayLine));
        }
    }

    /**
     * The formatText function in Java applies formatting to a specified range of
     * text within a display
     * buffer based on the given format parameter.
     * 
     * @param format      The `format` parameter specifies the type of formatting to
     *                    be applied to the text.
     *                    It can be one of the following values: "bold", "faint",
     *                    "italic", or "underline". If the format
     *                    is not one of these values, the default formatting will be
     *                    applied.
     * @param startPosRow The `startPosRow` parameter in the `formatText` method
     *                    represents the
     *                    starting row index where the text formatting will begin.
     *                    If the `startPosRow` value is less than
     *                    0, it is set to 1 to ensure it starts from the first row.
     * @param startPosCol The `startPosCol` parameter in the `formatText` method
     *                    represents the
     *                    starting column position where the text formatting should
     *                    begin within a specific row of text.
     * @param endPosRow   The `endPosRow` parameter in the `formatText` method
     *                    represents the ending row
     *                    position where the text formatting will be applied. This
     *                    method formats text within a specified
     *                    range of rows and columns in a display buffer. The
     *                    formatting specified by the `format`
     *                    parameter (e.g., "bold",
     * @param endPosCol   The `endPosCol` parameter in the `formatText` method
     *                    represents the ending
     *                    column position within the text where the formatting
     *                    should be applied. This parameter specifies
     *                    the column index where the formatting should end for each
     *                    row of text within the specified
     *                    range.
     */
    public void formatText(String format,
            int startPosRow,
            int startPosCol,
            int endPosRow,
            int endPosCol) {
        if (startPosRow < 0)
            startPosRow = 1;

        String formatting;
        switch (format) {
            case "bold": {
                formatting = "\u001B[1m";
                break;
            }
            case "faint": {
                formatting = "\u001B[2m";
                break;
            }
            case "italic": {
                formatting = "\u001B[3m";
                break;
            }
            case "underline": {
                formatting = "\u001B[4m";
                break;
            }
            default: {
                formatting = COLOR_WHITE;
                break;
            }
        }
        for (int i = startPosRow; i < endPosRow; i++) {
            String displayLine = displayBuffer.get(i).toString();
            displayLine = displayLine.substring(0, startPosCol) + formatting
                    + displayLine.substring(startPosCol, endPosCol)
                    + COLOR_WHITE + displayLine.substring(endPosCol);
            displayBuffer.set(i, new StringBuffer(displayLine));
        }
    }

    /**
     * The `displayText` function displays text on a display buffer with specified
     * starting position
     * and maximum length per line.
     * 
     * @param text        The `text` parameter is the string that you want to
     *                    display on the screen. It
     *                    contains the content that you want to show in the display
     *                    area.
     * @param startPosRow The `startPosRow` parameter in the `displayText` method
     *                    represents the
     *                    starting row position where the text will be displayed on
     *                    the screen or display buffer. It
     *                    indicates the row number where the text will begin to be
     *                    displayed.
     * @param startPosCol The `startPosCol` parameter in the `displayText` method
     *                    represents the
     *                    starting column position where the text should be
     *                    displayed on the screen or display buffer. It
     *                    indicates the column index where the text should begin to
     *                    be shown.
     * @param maxLength   The `maxLength` parameter in the `displayText` method
     *                    specifies the maximum
     *                    number of characters that can be displayed on a single
     *                    line. If the input text exceeds this
     *                    length, it will be split into multiple lines with each
     *                    line containing up to `maxLength`
     *                    characters.
     */
    public void displayText(String text,
            int startPosRow,
            int startPosCol,
            int maxLength) {
        while (text != null) {
            String line;
            if (text.length() > maxLength) {
                line = text.substring(0, maxLength);
                text = text.substring(maxLength);
            } else {
                line = text;
                text = null;
            }
            String displayLine = displayBuffer.get(startPosRow).toString();
            displayLine = replaceAt(startPosCol, startPosCol + line.length(), displayLine, line);
            displayBuffer.set(startPosRow, new StringBuffer(displayLine));
            startPosRow++;
        }
    }

    /**
     * The `displayCursor` function updates the cursor position on the screen and
     * displays the cursor
     * character at that position.
     * 
     * @param screen The `screen` parameter in the `displayCursor` method represents
     *               the screen number
     *               where the cursor should be displayed. It is used to determine
     *               the specific cursor position based
     *               on the screen.
     * @param index  The `index` parameter in the `displayCursor` method represents
     *               the index of the
     *               cursor position within the `CursorPosition` array for a
     *               specific screen. It is used to determine
     *               the row and column coordinates of the cursor position on the
     *               screen.
     */
    public void displayCursor(int screen, int index) {
        if (!(lastCursorRow == -1) && !(lastCursorRow == -1)) {
            displayBuffer.get(lastCursorRow).setCharAt(lastCursorCol, lastCursorChar);
        }
        CursorPosition[] pos = cursorPairs[screen];
        int row = pos[index].row();
        int col = pos[index].col();
        lastCursorRow = row;
        lastCursorCol = col;
        lastCursorChar = displayBuffer.get(row).charAt(col);
        displayBuffer.get(row).setCharAt(col, cursorChars[cursor]);
    }

    /**
     * The `loadScreen` function initializes a new display buffer with the content
     * of a specified
     * screen index and resets cursor positions.
     * 
     * @param screenIndex The `screenIndex` parameter in the `loadScreen` method is
     *                    an integer value
     *                    that represents the index of the screen to be loaded from
     *                    the `screens` array.
     */
    public void loadScreen(int screenIndex) {
        displayBuffer = new ArrayList<StringBuffer>();
        String[] loadScreen = screens[screenIndex];
        for (int i = 0; i < loadScreen.length; i++) {
            displayBuffer.add(new StringBuffer(loadScreen[i]));
        }
        currentScreen = screenIndex;
        lastCursorCol = -1;
        lastCursorRow = -1;
    }

    /**
     * This Java function fills a rectangular area in a display buffer with a
     * specified character.
     * 
     * @param startPosRow The `startPosRow` parameter represents the starting row
     *                    index where the
     *                    character `c` will be filled in the display buffer.
     * @param startPosCol The `startPosCol` parameter represents the starting column
     *                    index where you
     *                    want to begin filling the character `c` in the display
     *                    buffer.
     * @param endPosRow   End position row is the row index where you want to stop
     *                    filling the character
     *                    'c' in the display buffer.
     * @param endPosCol   End position column index
     * @param c           The parameter `c` in the `fillChar` method represents the
     *                    character that will be used
     *                    to fill the specified range of positions in the display
     *                    buffer. This character will be set at
     *                    each position within the specified row and column range.
     */
    public void fillChar(int startPosRow, int startPosCol, int endPosRow, int endPosCol, char c) {
        for (int i = startPosRow; i <= endPosRow; i++) {
            for (int j = startPosCol; j <= endPosCol; j++) {
                displayBuffer.get(i).setCharAt(j, c);
            }
        }
    }

    /**
     * The `displayCard` function is used to display a card with specified
     * attributes at a given
     * position on the display buffer.
     * 
     * @param posRow    The `posRow` parameter in the `displayCard` method
     *                  represents the position of the
     *                  row where the card will be displayed on the screen. It is an
     *                  integer value that determines the
     *                  vertical placement of the card within the display area.
     * @param posCol    The `posCol` parameter in the `displayCard` method
     *                  represents the column position
     *                  where the card will be displayed within the display buffer.
     *                  It is used to determine where the
     *                  card's visual representation will start horizontally on the
     *                  screen.
     * @param name      The `name` parameter in the `displayCard` method represents
     *                  the name of the card
     *                  that will be displayed. It is a String value that contains
     *                  the name of the card.
     * @param health    The `health` parameter in the `displayCard` method
     *                  represents the health points of
     *                  a card in a card game. It indicates how much damage the card
     *                  can take before being defeated or
     *                  removed from play. The `health` value is typically reduced
     *                  when the card is attacked or takes
     *                  damage from other cards
     * @param power     The `power` parameter in the `displayCard` method represents
     *                  the attack power of
     *                  the card. It indicates how much damage the card can deal to
     *                  other cards or players in the game.
     * @param cost      The `cost` parameter in the `displayCard` method represents
     *                  the cost of playing the
     *                  card in a card game. It indicates the amount of resources or
     *                  currency required to play the card.
     *                  The cost is typically deducted from a player's available
     *                  resources when the card is played. In
     *                  the context of the
     * @param abilities The `displayCard` method you provided is used to display a
     *                  card with specific
     *                  attributes at a given position on the screen. The
     *                  `abilities` parameter in this method is an
     *                  `ArrayList<String>` that contains the abilities of the card.
     */
    public void displayCard(int posRow,
            int posCol,
            String name,
            int health,
            int power,
            int cost,
            ArrayList<String> abilities) {
        String line = displayBuffer.get(posRow).toString();
        line = replaceAt(posCol, posCol + 5, line, "┌───┐");
        displayBuffer.set(posRow, new StringBuffer(line));

        line = displayBuffer.get(posRow + 1).toString();
        line = replaceAt(posCol, posCol + 5, line, "│ " +
                (name == null || name.equals("") ? " " : Character.toUpperCase(name.charAt(0))) +
                (cost == 0 ? " " : cost) +
                "│");
        displayBuffer.set(posRow + 1, new StringBuffer(line));

        line = displayBuffer.get(posRow + 2).toString();
        line = replaceAt(posCol, posCol + 5, line, "├───┤");
        displayBuffer.set(posRow + 2, new StringBuffer(line));

        line = displayBuffer.get(posRow + 3).toString();
        line = replaceAt(posCol, posCol + 5, line, "│" +
                (health == 0 ? " " : health) +
                (abilities == null || abilities.isEmpty() ? " " : abilities.get(0).substring(0, 1)) +
                (power == 0 ? " " : power) +
                "│");
        displayBuffer.set(posRow + 3, new StringBuffer(line));

        line = displayBuffer.get(posRow + 4).toString();
        line = replaceAt(posCol, posCol + 5, line, "└───┘");
        displayBuffer.set(posRow + 4, new StringBuffer(line));
    }

    /**
     * The `displayCard` method in Java takes a position, card object, and displays
     * its attributes.
     * 
     * @param posRow The `posRow` parameter in the `displayCard` method represents
     *               the position of the
     *               card in the row where it will be displayed. It is used to
     *               determine the row in which the card
     *               will be displayed on the screen or user interface.
     * @param posCol The `posCol` parameter in the `displayCard` method represents
     *               the position column
     *               where the card will be displayed. It is used to determine the
     *               horizontal placement of the card on
     *               the display or game board.
     * @param card   The `card` parameter in the `displayCard` method is an object
     *               of the `Card` class. It
     *               contains information about a specific card, such as its name,
     *               health, power, cost, and abilities.
     */
    public void displayCard(int posRow, int posCol, Card card) {
        displayCard(posRow,
                posCol,
                card.getName(),
                card.getHealth(),
                card.getPower(),
                card.getCost(),
                card.getAbilities());
    }

    /**
     * The `displayCardBig` function in Java displays information about a card,
     * including its name,
     * cost, health, and power, in a visually appealing format.
     * 
     * @param card The `displayCardBig` method takes a `Card` object as a parameter.
     *             This method is
     *             responsible for displaying a larger version of the card on the
     *             screen, including the card's
     *             name, cost, health, and power.
     */
    public void displayCardBig(Card card) {
        fillChar(1, 1, 15, 22, emptyChar);
        if (card == null) {
            return;
        }
        String name = Character.toUpperCase(card.getName().charAt(0)) + card.getName().substring(1);
        displayText(name, 1, 1 + (21 - name.length()) / 2, 22);
        switch (card.getCostType()) {
            case 0: {
                displayText(Character.toString(emptyChar), 2, 22, 1);
                break;
            }
            case 1: {
                displayText(card.getCost() + "", 1, 22, 2);
                displayText("❢", 2, 22, 1);
                break;
            }
            case 2: {
                displayText(card.getCost() + "", 1, 22, 2);
                displayText("✖", 2, 22, 1);
                break;
            }

        }
        displayText(card.getHealth() + "", 14, 1, 2);
        displayText("♥", 15, 1, 1);

        displayText(card.getPower() + "", 14, 22, 2);
        displayText("⚔", 15, 22, 1);
    }

    private void drawNode(int posRow, int posCol, String node) {
        String line;

        line = displayBuffer.get(posRow).toString();
        line = replaceAt(posCol, posCol + 3, line, "┌─┐");
        displayBuffer.set(posRow, new StringBuffer(line));

        char nodeChar = ' ';
        switch (node) {
            case "campfire": {
                nodeChar = '♨';
                break;
            }
            case "choice": {
                nodeChar = '+';
                break;
            }
            case "trial": {
                nodeChar = '☗';
                break;
            }
            case "altar": {
                nodeChar = '✘';
                break;
            }
            case "sacrifice": {
                nodeChar = '⊼';
                break;
            }
        }


        line = displayBuffer.get(posRow + 1).toString();
        line = replaceAt(posCol, posCol + 3, line, "│" + nodeChar + "│");
        displayBuffer.set(posRow + 1, new StringBuffer(line));

        line = displayBuffer.get(posRow + 2).toString();
        line = replaceAt(posCol, posCol + 3, line, "│" + nodeChar + "│");
        displayBuffer.set(posRow + 2, new StringBuffer(line));

    }

    public void displayMap(Map map) {
        MapNode[][] nodes = map.getNodes();
        int progress = map.getProgress();
        int pos = map.getPos();
        int chapter = map.getChapter();
        String[] layouts = Map.LAYOUTS;
        int x = 0;
        for (int i = progress; i < nodes.length && i < progress + 3; i++) {
            int width = Integer.parseInt(layouts[i]);
            switch (width) {

            }
            x++;
        }

        String decor;
        switch (chapter) {
            case 1: {
                decor = "↟𐀛𐀗𐀂𐀭𐘃𖣂ᛉ";
                break;
            }
            case 2: {
                decor = "~•ᚠᛁ";
                break;
            }
            case 3: {
                decor = "^|";
                break;
            }
            default: {
                decor = "/\\";
                break;
            }
        }

    }

    /**
     * The `flush` method creates a new array from the elements in `displayBuffer`,
     * converts them to
     * strings, and then prints each non-null string.
     */
    public void flush() {
        display = new String[displayBuffer.size()];
        for (int i = 0; i < displayBuffer.size(); i++) {
            display[i] = displayBuffer.get(i).toString();
        }
        for (String s : display) {
            if (s != null)
                System.out.println(s);
        }
    }

    public int getScreen() {
        return currentScreen;
    }

    public int getSelected() {
        return currentSelected;
    }

    public static void main(String[] args) {
        Render r = new Render();
        r.loadScreen(0);
        r.formatText("underline", 3, 22, 5, 39); // preset for inscrption
        // r.colorText("\u001B[32m", 9, 10, 11, 17); // preset for play
        // r.colorText("\u001B[34m", 9, 50, 11, 63); // preset for play
        // r.displayCard(1,1,"", 0, 0, 0, new ArrayList<String>());
        r.flush();
        // System.out.println("\u001b[4mTest\u001b[0mTest");
    }

    /**
     * The function replaces a substring in a given string with a new substring
     * based on the specified
     * start and end indices.
     * 
     * @param startIndex The `startIndex` parameter specifies the index in the
     *                   `oldString` where the
     *                   replacement should begin.
     * @param endIndex   The `endIndex` parameter in the `replaceAt` method
     *                   represents the index position
     *                   in the `oldString` where the replacement should end. This
     *                   means that the characters in the
     *                   `oldString` from the `startIndex` up to (but not including)
     *                   the `endIndex` will be replaced
     * @param oldString  The `oldString` parameter is the original string that you
     *                   want to modify by
     *                   replacing a portion of it with a new string.
     * @param newString  The `newString` parameter is the string that will replace
     *                   the substring in the
     *                   `oldString` from the `startIndex` to the `endIndex`.
     * @return The `replaceAt` method returns a new string that is created by
     *         replacing the substring
     *         of `oldString` starting at `startIndex` and ending at `endIndex` with
     *         the `newString`.
     */
    private static String replaceAt(int startIndex,
            int endIndex,
            String oldString,
            String newString) {
        return oldString.substring(0, startIndex) +
                newString + oldString.substring(endIndex);
    }
}
