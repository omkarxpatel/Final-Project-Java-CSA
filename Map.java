import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.*;

public class Map {
    private int chapter;
    private ArrayList<MapNode> nodeBuffer = new ArrayList<MapNode>();
    private static TreeMap<String, Card> cards = Card.cards;
    public static final Card[] CARD_POOL_COMMON = new Card[] {
        cards.get("adder"),
        cards.get("stoat"),

    };

    private MapNode[][] map = new MapNode[5][5];
    private int[][] mapLayout = {
        
        // {0,0,2,0,0}, // boss fight
        // {0,1,0,1,0},
        // {1,0,0,0,1},
        // {0,1,0,1,0},
        // {0,0,1,0,0},
        // {0,1,0,1,0},
        // {1,0,0,0,1},
        // {0,1,0,1,0},
        {0,0,1,0,0},
        {0,1,0,1,0},
        {1,0,0,0,1},
        {0,1,0,1,0},
        {0,0,1,0,0}
        };
    private int mapX = 2;
    private int mapY = map.length - 1;

    
    
    public Map(int chapter) {
        this.chapter = chapter;
        initNodes();
        randNodes();
        printMapTest();
    }

    public void initNodes() {
        for (int i = 0; i < 5 + (int)(3 * Math.random()); i++) {
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
    //     [0,      object, 0,      object, 0]
    //     [object, 0,      0,      0,      object]
    //     [0,      object, 0,      object, 0]
    //     [0,      0,      object, 0,      0]
    //     ]
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

// ........................│0│.......│0│........................
// ........................└─┘.......└─┘........................
// .......................╱.............╲.......................
// ......................╱...............╲......................
// ...................┌─┐.................┌─┐...................
// ...................│0│.................│0│...................
// ...................└─┘.................└─┘...................
// ......................╲...............╱......................
// .......................╲.............╱.......................
// ........................┌─┐.......┌─┐........................
// ........................│0│.......│0│........................
// ........................└─┘.......└─┘........................
// ...........................╲.....╱...........................
// ............................╲...╱............................
// .............................┌─┐.............................
// .............................│0│.............................
// .............................└─┘.............................
// .............................................................

    public void printMapTest() {

        for (MapNode[] map1 : map) {
            int countNull = 0;
            for (MapNode item : map1) {
                if (item == null) {
                    countNull++;
                }
            }
            countNull = map[0].length-countNull;
            System.out.println(countNull);
            switch (countNull) {
                case 5 -> {
                    for (int z = 0; z < 2; z++) {
                        System.out.println(".............................................................");
                    }
                } // default
                case 1 -> {
                    for (MapNode item : map1) {
                        if (item != null) {
                            System.out.println(".............................┌─┐.............................");
                            System.out.println(".............................│" + item.event().substring(0, 1) + "│.............................");
                            System.out.println(".............................└─┘.............................");
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
                                    System.out.println("........................┌─┐.......┌─┐........................");
                                    System.out.println("........................│"+first+"│.......│" + item.event().substring(0, 1) + "│........................");
                                    System.out.println("........................└─┘.......└─┘........................");
                                } else {
                                    System.out.println("...................┌─┐.................┌─┐...................");
                                    System.out.println("...................│"+first+"│.................│" + item.event().substring(0, 1) + "│...................");
                                    System.out.println("...................└─┘.................└─┘...................");
                                }
                            }
                        }
                    }
                }
                default -> System.out.println(countNull);
            }
            // for (int j = 0; j < map[i].length; j++) {
            //     if (map[i][j] != null) {
            //         System.out.print(map[i][j].event() + "          ");
            //     }
            //     else {
            //         System.out.print(map[i][j] + "          ");
            //     }
            // }
            // System.out.println();
        }
        System.out.println();
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
