import java.util.*;
import java.lang.StringBuilder;
import java.lang.StringBuffer;

public class Render {
    private String[] display = new String[15];
    private ArrayList<StringBuffer> displayBuffer = new ArrayList<StringBuffer>();
    private static char emptyChar = '.';
    private static String emptyLine = null;
    private static char cursorChar = '⟡';
    private static int lineLength = 100;
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
        //TODO
    };
    private static String[][] screens = new String[][] {
        screenTitle, 
        screenDeck};
    private static CursorPosition[][] cursorPairs = new CursorPosition[][] {
        {
            new CursorPosition(0, 7, 13, "play"),
            new CursorPosition(0, 1, 30, "credits"),
            new CursorPosition(0, 7, 47, "continue")
        },
        {
            //TODO
        }
    };

    public Render() {
        StringBuilder newLine = new StringBuilder();
        for (int i = 0; i < lineLength; i++)
            newLine.append(emptyChar);
        emptyLine = newLine.toString();

        display = screenTitle;
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
        CursorPosition[] pos = cursorPairs[screen];
        displayBuffer.get(pos[index].row()).setCharAt(pos[index].col(), cursorChar);
    }

    public void loadScreen(int screenIndex) {
        displayBuffer = new ArrayList<StringBuffer>();
        String[] loadScreen = screens[screenIndex];
        for (int i = 0; i < loadScreen.length; i++) {
            displayBuffer.add(new StringBuffer(loadScreen[i]));
        }
    }

    public void flush() {
        for (int i = 0; i < display.length; i++) {
            display[i] = displayBuffer.get(i).toString();
        }
        for (String s : display) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        Render r = new Render();
        r.loadScreen(0);
        r.displayText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt",
            2, 2, 20);
        r.flush();
        int i = 5;
        i+=+i;
        System.out.println(i);
    }

    private static String replaceAt(int startIndex, 
        int endIndex, 
        String oldString, 
        String newString) {
            return oldString.substring(0, startIndex) + 
                newString + oldString.substring(endIndex);
    }
}
