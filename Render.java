import java.util.*;
import java.lang.StringBuilder;
import java.lang.StringBuffer;

public class Render {
    private String[] display = new String[15];
    private ArrayList<StringBuffer> displayBuffer = new ArrayList<StringBuffer>();
    private int currentScreen = 0;
    private int currentSelected = 0;
    private int lastCursorRow = -1;
    private int lastCursorCol = -1;
    private char lastCursorChar = (char)0;
    private static char emptyChar = '.';
    private static String emptyLine = null;
    private static char cursorChar = '✦';
    private static int lineLength = 100;


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
        "-------------------------------------------------------------"
    };
    private static String[][] screens = new String[][] {
        screenTitle, 
        screenDeck};
    public static final CursorPosition[][] CURSOR_PAIRS = new CursorPosition[][] {
        {
            new CursorPosition(0, 7, 13, "play"),
            new CursorPosition(0, 1, 30, "credits"),
            new CursorPosition(0, 7, 47, "continue")
        },
        {
            new CursorPosition(1,0,5,"card")
        }
    };

    public Render() {
        StringBuilder newLine = new StringBuilder();
        for (int i = 0; i < lineLength; i++)
            newLine.append(emptyChar);
        emptyLine = newLine.toString();

        display = screenTitle;
        loadScreen(0);
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
                displayLine = displayLine.substring(0, startPosCol) + color + displayLine.substring(startPosCol, endPosCol) + COLOR_WHITE + displayLine.substring(endPosCol);
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
                }
                else {
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
        displayBuffer.get(row).setCharAt(col, cursorChar);
    }

    public void loadScreen(int screenIndex) {
        displayBuffer = new ArrayList<StringBuffer>();
        String[] loadScreen = screens[screenIndex];
        for (int i = 0; i < loadScreen.length; i++) {
            displayBuffer.add(new StringBuffer(loadScreen[i]));
        }
        currentScreen = screenIndex;
    }

    public void flush() {
        for (int i = 0; i < display.length; i++) {
            display[i] = displayBuffer.get(i).toString();
        }
        for (String s : display) {
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
        
        r.colorText("\u001B[31m", 3, 22, 5, 39); // preset for inscrption
        r.colorText("\u001B[32m", 9, 10, 11, 17); // preset for play
        r.colorText("\u001B[34m", 9, 50, 11, 63); // preset for play

        r.flush();
    }

    private static String replaceAt(int startIndex, 
        int endIndex, 
        String oldString, 
        String newString) {
            return oldString.substring(0, startIndex) + 
                newString + oldString.substring(endIndex);
    }
}
