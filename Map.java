import java.util.ArrayList;

public class Map {
    private int chapter;
    private int progress;
    private int pos;
    public static final String[] LAYOUTS = new String[] {"1212321212321", "1132112113211", "122113112211", "131" };
    private MapNode[][] nodes;
    private static String[] nonBattleNodes = new String[] {
            "campfire",
            "choice",
            "trial",
            "altar",
            "sacrifice"
    };

    public Map(int chapter) {
        this.chapter = chapter;
        progress = 0;
        pos = 0;

        char[] layout = LAYOUTS[chapter - 1].toCharArray();
        nodes = new MapNode[layout.length][3];

        nodes[0][0] = new MapNode(chapter, "empty", null);

        for (int i = 1; i < nodes.length - 1; i++) {
            if (layout[i] == '1') {
                nodes[i][0] = new MapNode(chapter, "battle");
            } else {
                for (int j = 0; j < Integer.parseInt(layout[i] + ""); j++) {
                    nodes[i][j] = new MapNode(chapter, nonBattleNodes[(int) (nonBattleNodes.length * Math.random())]);
                }
            }
        }
        if (chapter == 1) {
            
        }
        nodes[nodes.length - 1][0] = new MapNode(chapter, "boss" + chapter, null);
    }

    public MapNode[][] getNodes() {
        return nodes;
    }

    public int getProgress() {
        return progress;
    }

    public int getPos() {
        return pos;
    }

    public int getChapter() {
        return chapter;
    }

    public void setProgress(int p) {
        progress = p;
    }

    public void setPos(int p) {
        pos = p;
    }
}
