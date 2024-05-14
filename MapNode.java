import java.util.ArrayList;

public class MapNode {
    private int chapter;
    private String event;
    private ArrayList<MapNode> connections;

    public MapNode(int chapter) {
        this.chapter = chapter;
        event = "boss";
        connections = new ArrayList<MapNode>();
    }

    public MapNode(int chapter, String event, ArrayList<MapNode> connections) {
        this.chapter = chapter;
        this.event = event;
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