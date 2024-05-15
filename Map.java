import java.util.ArrayList;
import java.util.TreeSet;

public class Map {
    private int chapter;
    private ArrayList<MapNode> nodeBuffer = new ArrayList<MapNode>();
    public static final Card[] CARD_POOL_COMMON = new Card[] {
        new Card("adder", 1, 1, 2, 1, null, null),
        new Card(null, 0, 0, 0, 0, null, null),
    };
    
    public Map(int chapter) {
        this.chapter = chapter;
        initNodes();
        randNodes();
    }

    public void initNodes() {
        for (int i = 0; i < 5 + (int)(3 * Math.random()); i++) {

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
        if (Math.random() < 0.3) {
            addNode("copy");
        }
        nodeBuffer.add(new MapNode(chapter, "sacrifice"));
    }

    private void addNode(String event) {
        nodeBuffer.add(new MapNode(chapter, event));
    }

    public void randNodes() {

    }
}
