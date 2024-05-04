import java.util.*;
import java.lang.StringBuilder;
import java.lang.StringBuffer;

public class Render {
    private String[] display = new String[15];
    private ArrayList<StringBuffer> displayBuffer = new ArrayList<StringBuffer>();
    private static char emptyChar = '.';
    private static String emptyLine = null;
    private static int lineLength = 100;
    private static final String[] TITLE_SCREEN = new String[] {
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

    public Render() {
        StringBuilder newLine = new StringBuilder();
        for (int i = 0; i < lineLength; i++)
            newLine.append(emptyChar);
        emptyLine = newLine.toString();
        // System.out.println(emptyLine);

        display = TITLE_SCREEN;
    }

    public void update() {
        for (String s : display) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        Render r = new Render();
        while (true) {
            r.update();
        }
        
    }
}
