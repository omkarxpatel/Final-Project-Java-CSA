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
    private static char[] cursorChars = new char[] {'✦', };
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
            ".............................................................",
            ".............................................................",
            ".............................................................",
            "......................┳┏┓┏┏┏┓┓┏┏┓╋┓┏┓┏┓......................",
            "......................┻┛┗┛┗┛ ┗┫┣┛┗┗┗┛┛┗......................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            "..........┏┓┓┏┓┓┏........................┏┏┓┏┓╋┓┏┓┓┏┏┓.......",
            "..........┣┛┗┗┻┗┫........................┗┗┛┛┗┗┗┛┗┗┻┗........",
            ".............................................................",
            ".............................................................",
            ".............................................................",
            "............................................................."
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
            "│......................│.....................................",
            "│......................│....................................>",
            "│......................│.....................................",
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
            "...................Lynbrook High School......................",
            ".......................2023 - 2024 ..........................",
            "...............AP CS A Period 3, Mark Kwong..................",
            "........Omkar Patel, Sriman Ratnapu, Jianyu Wang.............",
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
            cursorPositionsDeck[i] = new CursorPosition(1, 6 * (i / 7), 27 + 5 * (i % 7), "card", "showCardInDeck");
        }
        cursorPositionsDeck[21] = new CursorPosition(1, 7, 60, "exitDeck", null);
    }
    public static final CursorPosition[][] CURSOR_PAIRS = new CursorPosition[][] {
            {
                new CursorPosition(0, 7, 13, "play", null),
                new CursorPosition(0, 1, 30, "credits", null),
                new CursorPosition(0, 7, 47, "continue", null)
            },
            cursorPositionsDeck,
            {
                new CursorPosition(2, 9, 26, "title", null)
            },
            {
                new CursorPosition(3,0,0,"test",null),
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
            displayLine = displayLine.substring(0, startPosCol) + formatting + displayLine.substring(startPosCol, endPosCol)
                    + COLOR_WHITE + displayLine.substring(endPosCol);
            displayBuffer.set(i, new StringBuffer(displayLine));
        }
    }

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

    public void displayCursor(int screen, int index) {
        if (!(lastCursorRow == -1) && !(lastCursorRow == -1)) {
            displayBuffer.get(lastCursorRow).setCharAt(lastCursorCol, lastCursorChar);
        }
        CursorPosition[] pos = CURSOR_PAIRS[screen];
        int row = pos[index].row();
        int col = pos[index].col();
        lastCursorRow = row;
        lastCursorCol = col;
        lastCursorChar = displayBuffer.get(row).charAt(col);
        displayBuffer.get(row).setCharAt(col, cursorChars[cursor]);
    }

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

    public void fillChar(int startPosRow, int startPosCol, int endPosRow, int endPosCol, char c) {
        for (int i = startPosRow; i <= endPosRow; i++ ) {
            for (int j = startPosCol; j <= endPosCol; j++) {
                displayBuffer.get(i).setCharAt(j, c);
            }
        }
    }

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

    public void displayCard(int posRow, int posCol, Card card) {
        displayCard(posRow,
                posCol,
                card.getName(),
                card.getHealth(),
                card.getPower(),
                card.getCost(),
                card.getAbilities());
    }

    public void displayCardBig(Card card) {
        fillChar(1, 1, 17, 22, emptyChar);
        if (card == null) {
            return;
        }
        String name = Character.toUpperCase(card.getName().charAt(0)) + card.getName().substring(1);
        displayText(name, 1, 1 + (21-name.length()) / 2, 22);
        switch (card.getCostType()) {
            case 0: {
                displayText(Character.toString(emptyChar), 2, 22, 1);
                break;
            }
            case 1: {
                displayText("‡", 2, 22, 1);
                break;
            }
        }
    }

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

    private static String replaceAt(int startIndex,
            int endIndex,
            String oldString,
            String newString) {
        return oldString.substring(0, startIndex) +
                newString + oldString.substring(endIndex);
    }
}
