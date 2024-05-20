public class CursorPosition {
    private int screen;
    private int row;
    private int col;
    private String action;
    private String hover;

    public CursorPosition(int screen, int row, int col, String action, String hover) {
        this.screen = screen;
        this.row = row;
        this.col = col;
        this.action = action;
        this.hover = hover;
    }

    public int screen() {
        return screen;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public String action() {
        return action;
    }

    public String hover() {
        return hover;
    }
}
