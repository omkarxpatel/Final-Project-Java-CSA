import java.util.ArrayList;

public class MapNode {
    private int chapter;
    private String event;
    private ArrayList<MapNode> connections;

    public MapNode(int chapter, String event) {
        this.chapter = chapter;
        this.event = event;
        connections = null;
    }

    public MapNode(int chapter, String event, ArrayList<MapNode> connections) {
        this(chapter, event);
        this.connections = connections;
    }

    public int chapter() {
        return chapter;
    }

    public String event() {
        return event;
    }

    public ArrayList<MapNode> connections() {
        return connections;
    }
}