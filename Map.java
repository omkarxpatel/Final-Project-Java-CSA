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
        //     System.out.println(s);
        // }
    }

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
        //     System.out.println(i);
        // }

    }

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
