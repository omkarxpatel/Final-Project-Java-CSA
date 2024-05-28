import java.util.*;

public class Map {
    private int chapter;
    private int nodeStep;
    private ArrayList<MapNode> nodeBuffer = new ArrayList<MapNode>();
    private static TreeMap<String, Card> cards = Card.cards;
    public static final Card[] CARD_POOL_COMMON = new Card[] {
            cards.get("adder"),
            cards.get("stoat"),

    };

    private ArrayList<String> mapResult = new ArrayList<String>();
    private MapNode[][] map = new MapNode[9][5];
    private int[][] mapLayout = {

            // {0,0,1,0,0}, // boss fight
            // {0,1,0,1,0},
            // {1,0,0,0,1},
            // { 0, 1, 0, 1, 0 },
            { 0, 0, 1, 0, 0 },
            { 0, 1, 0, 1, 0 },
            { 1, 0, 0, 0, 1 },
            { 0, 1, 0, 1, 0 },
            { 0, 0, 1, 0, 0 },
            { 0, 1, 0, 1, 0 },
            { 1, 0, 0, 0, 1 },
            { 0, 1, 0, 1, 0 },
            { 0, 0, 1, 0, 0 }
    };
    private int mapX = 2;
    private int mapY = 29;

    public Map(int chapter) {
        this.chapter = chapter;
        initNodes();
        randNodes();
        // ArrayList<String> map = getMap();
        // for (String s : map) {
        // System.out.println(s);
        // }
    }

    /**
     * The `initNodes` function randomly adds nodes related to different events in a
     * game, such as
     * battles, sacrifices, campfires, and trials.
     */
    public void initNodes() {
        for (int i = 0; i < 5 + (int) (3 * Math.random()); i++) {
            addNode("battle");
        }
        for (int i = 0; i < 2; i++) {
            addNode("sacrifice");
            addNode("add");
            addNode("remove");
            addNode("campfire");
            addNode("addTribe");
        }
        if (chapter >= 2) {
            addNode("trial");
        }
        if (Math.random() < 0.5) {
            addNode("mycologist");
        }
        if (Math.random() < 0.0) {
            addNode("copy");
        }
        addNode("items");
    }

    /**
     * The `addNode` function adds a new `MapNode` object to the `nodeBuffer` list
     * with the given `event`
     * and `chapter` values.
     * 
     * @param event The `event` parameter in the `addNode` method represents the
     *              event that you want to
     *              associate with the new `MapNode` being added to the
     *              `nodeBuffer`. This event could be any relevant
     *              information or data that you want to store within the `MapNode`.
     */
    private void addNode(String event) {
        nodeBuffer.add(new MapNode(chapter, event));
    }

    // map = [
    // [0, object, 0, object, 0]
    // [object, 0, 0, 0, object]
    // [0, object, 0, object, 0]
    // [0, 0, object, 0, 0]
    // ]
    public void randNodes() {
        Stack<MapNode> shuffled = shuffle(nodeBuffer);
        for (int i = 0; i < mapLayout.length; i++) {
            for (int j = 0; j < mapLayout[0].length; j++) {
                if (mapLayout[i][j] == 1) {
                    map[i][j] = shuffled.pop();
                }
            }
        }
    }

    /**
     * The `getMap` function generates a map layout with different elements and
     * decorations, including
     * random greenery objects, and returns it as an ArrayList of strings.
     * 
     * @return The `getMap()` method returns an `ArrayList<String>` named
     *         `mapResult` containing a map
     *         representation with various symbols and colors. The method generates
     *         the map based on the data
     *         in the `map` array, adds decorations like greenery represented by
     *         symbols like "↟𐀛𐀗𐀂𐀭𐘃𖣂",
     */
    public ArrayList<String> getMap() {
        // for (MapNode[] map1 : map) {

        mapResult = new ArrayList<String>();

        for (int i = 0; i < map.length; i++) {
            MapNode[] map1 = map[i];
            int countNull = 0;
            for (MapNode item : map1) {
                if (item == null) {
                    countNull++;
                }
            }
            countNull = map[0].length - countNull;
            for (int z = 0; z < 2; z++) {
                if (map1 != map[0] || z == 1)
                    mapResult.add(".............................................................");
            }
            switch (countNull) {

                case 1 -> {
                    for (MapNode item : map1) {
                        if (item != null) {
                            String color = "";
                            String white = "\u001B[0m";
                            if (map1.equals(map[0]))
                                color = "\u001B[31m"; // for final boss
                            mapResult.add("............................." + color + "┌─┐" + white
                                    + ".............................");
                            mapResult.add("............................." + color + "│"
                                    + item.event().substring(0, 1) + "│" + white + ".............................");
                            mapResult.add("............................." + color + "└─┘" + white
                                    + ".............................");
                        }
                    }
                }
                case 2 -> {
                    String first = "";
                    for (MapNode item : map1) {
                        if (item != null) {
                            if ("".equals(first)) {
                                first = item.event().substring(0, 1);
                            } else {
                                if (map1[1] != null) {
                                    mapResult.add("........................┌─┐.......┌─┐........................");
                                    mapResult.add("........................│" + first + "│.......│"
                                            + item.event().substring(0, 1) + "│........................");
                                    mapResult.add("........................└─┘.......└─┘........................");
                                } else {
                                    mapResult.add("...................┌─┐.................┌─┐...................");
                                    mapResult.add("...................│" + first + "│.................│"
                                            + item.event().substring(0, 1) + "│...................");
                                    mapResult.add("...................└─┘.................└─┘...................");
                                }
                            }
                        }
                    }
                }
                default -> System.out.println(countNull);
            }
        }
        mapResult.add(".............................................................");
        mapResult.add("\n");
        colorText("\u001B[34m", mapResult.size() - mapX - 3, mapY, mapResult.size() - mapX, mapY + 3);

        // add greenery
        Random rand = new Random();

        int max = 5;
        int min = 0;
        String objects = "↟𐀛𐀗𐀂𐀭𐘃𖣂";
        // for (String i : mapResult) {
        for (int x = 0; x < mapResult.size(); x++) {
            String i = mapResult.get(x);
            int randomNum = rand.nextInt((max - min) + 1) + min;
            for (int j = 0; j < randomNum; j++) {
                int randomIdx = rand.nextInt((i.length() - 1 - min) + 1) + min;
                if (i.charAt(randomIdx) == '.') {
                    int randVal = rand.nextInt((objects.length() - 1));
                    String randomObject = objects.substring(randVal, randVal + 1);
                    mapResult.set(x, i.substring(0, randomIdx) + randomObject + i.substring(randomIdx + 1));
                }
            }
        }
        return mapResult;
        // for (String i : mapResult) {
        // System.out.println(i);
        // }

    }

    /**
     * This function colors a specified range of text in a multi-line display.
     * 
     * @param color The `color` parameter in the `colorText` method is a String that represents the
     * color you want to apply to the text. This could be a color code, such as "\u001B[31m" for red,
     * "\u001B[32m" for green, etc.,
     * @param startPosRow The `startPosRow` parameter specifies the starting row index for coloring the
     * text in the `mapResult` list. If the `startPosRow` is less than 0, it is set to 1 to ensure it
     * is within a valid range. The method then iterates over the rows
     * @param startPosCol The `startPosCol` parameter in the `colorText` method represents the starting
     * column position where the color should be applied within the text of each line specified by the
     * `startPosRow` and `endPosRow` parameters.
     * @param endPosRow The `endPosRow` parameter represents the ending row position for the text
     * colorization in the `colorText` method. It indicates the row number where the colorization
     * should stop.
     * @param endPosCol The `endPosCol` parameter in the `colorText` method represents the ending
     * column position where the color should be applied in the text. The method takes a color as input
     * and applies that color to the specified range of text within the specified rows and columns in
     * the `mapResult` data structure
     */
    public void colorText(String color,
            int startPosRow,
            int startPosCol,
            int endPosRow,
            int endPosCol) {
        if (startPosRow < 0)
            startPosRow = 1;
        for (int i = startPosRow; i < endPosRow; i++) {
            String displayLine = mapResult.get(i).toString();
            displayLine = displayLine.substring(0, startPosCol) + color + displayLine.substring(startPosCol, endPosCol)
                    + "\u001B[0m" + displayLine.substring(endPosCol);
            mapResult.set(i, new String(displayLine));
        }
    }

    /**
     * The function shuffles an ArrayList of MapNode objects and returns a Stack containing the
     * shuffled elements.
     * 
     * @param deck1 The `deck1` parameter is an `ArrayList` of `MapNode` objects.
     * @return A shuffled stack of MapNode objects is being returned.
     */
    public Stack<MapNode> shuffle(ArrayList<MapNode> deck1) {
        int index = 0;
        Stack<MapNode> returnDeck = new Stack<MapNode>();
        while (!deck1.isEmpty()) {
            index = (int) (Math.random() * deck1.size());
            returnDeck.add(deck1.remove(index));
        }
        return returnDeck;
    }
}
