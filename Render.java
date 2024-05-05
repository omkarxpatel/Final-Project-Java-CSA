import java.util.*;
import java.lang.StringBuilder;
import java.lang.StringBuffer;

public class Render {
    private String[] display = new String[15];
    private ArrayList<StringBuffer> displayBuffer = new ArrayList<StringBuffer>();
    private static char emptyChar = '.';
    private static String emptyLine = null;
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
        screenTitle, screenDeck};

    public Render() {
        StringBuilder newLine = new StringBuilder();
        for (int i = 0; i < lineLength; i++)
            newLine.append(emptyChar);
        emptyLine = newLine.toString();

        display = screenTitle;

        String test = "apple";
        test = replaceAt(0, 1, test, "new");
        System.out.println(test);
    }

    public void displayText(String text, 
        int startPosRow, 
        int startPosCol, 
        int maxLength) {

    }

    public void loadPreset(int screenIndex) {
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
        
    }

    private static String replaceAt(int startIndex, 
        int endIndex, 
        String oldString, 
        String newString) {
            return oldString.substring(0, startIndex) + 
                newString + oldString.substring(endIndex);
    }
}
