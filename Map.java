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
        
        {0,1,0,1,0},
        {1,0,0,0,1},
        {0,1,0,1,0},
        {0,0,1,0,0}
        };
    
    public Map(int chapter) {
        this.chapter = chapter;
        initNodes();
        randNodes();
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
        addNode("consumable");
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
        int count = 0;
        Stack<MapNode> shuffled = shuffle(nodeBuffer);
        for (int i = 0; i < mapLayout.length; i++) {
            for (int j = 0; j < mapLayout[0].length; j++) {
                if (mapLayout[i][j] == 1) {
                    map[i][j] = shuffled.pop();
                }
            }
        }
    }

    public void printMap() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != null) {
                    // have logic for printing mapnode object
                }
                else {
                    
                }
            }
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
