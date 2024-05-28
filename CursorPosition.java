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

/**
 * The `screen()` function in Java returns the value of the `screen` variable.
 * 
 * @return The method `screen()` is returning the value of the variable `screen`.
 */
    public int screen() {
        return screen;
    }

/**
 * The `row()` function in Java returns the value of the `row` variable.
 * 
 * @return The `row` variable is being returned.
 */
    public int row() {
        return row;
    }

/**
 * The `col()` function in Java returns the value of the variable `col`.
 * 
 * @return The method `col()` is returning the value of the variable `col`.
 */
    public int col() {
        return col;
    }
/**
 * The `action` function in Java returns the value of the `action` variable.
 * 
 * @return The `action` variable is being returned.
 */

    public String action() {
        return action;
    }

/**
 * The `hover` function in Java returns the value of the `hover` variable.
 * 
 * @return The `hover` variable is being returned.
 */
    // The `public String hover() {` method in the `CursorPosition` class is a getter method that
    // returns the value of the `hover` variable. It allows other parts of the program to access the
    // value of the `hover` variable stored in an instance of the `CursorPosition` class.
    public String hover() {
        return hover;
    }
}
