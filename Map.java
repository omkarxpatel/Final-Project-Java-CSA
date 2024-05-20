import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

public class Map {
    private int chapter;
    private ArrayList<MapNode> nodeBuffer = new ArrayList<MapNode>();
    private static TreeMap<String, Card> cards = Card.cards;
    public static final Card[] CARD_POOL_COMMON = new Card[] {
        cards.get("adder"),
        cards.get("stoat"),

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

    public void randNodes() {

    }
}
