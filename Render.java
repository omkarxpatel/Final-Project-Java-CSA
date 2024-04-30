import java.util.*;
import java.lang.StringBuilder;

public class Render {
    private char[][] display = new char[50][100];
    private ArrayList<StringBuffer> displayBuffer = new ArrayList<StringBuffer>();
    private static char emptyChar = '.';
    private static String emptyLine = null;

    public Render() {
        StringBuilder newLine = new StringBuilder();
        for (int i = 0; i < display[0].length; i++)
            newLine.append(emptyChar);
        emptyLine = newLine.toString();
        System.out.println(emptyLine);

    }

    public void update() {

    }

    public static void main(String[] args) {
        Render r = new Render();
    }
}
