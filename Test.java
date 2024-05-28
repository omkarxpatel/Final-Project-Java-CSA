import java.util.ArrayList;

public class Test {
    private int chapter;
    private int progress;
    private int posNode;
    private String[] layouts = new String[] { "11212321211321", "113211213211", "122113112211", "131" };
    private MapNode[][] nodes;
    private static String[] nonBattleNodes = new String[] {
            "campfire",
            "choice",
            "costChoice",
            "trial",
            "altar",
            "sacrifice",
            "items"
    };

    public Test(int chapter) {
        progress = 0;
        posNode = 0;

        char[] layout = layouts[chapter - 1].toCharArray();
        nodes = new MapNode[layout.length][3];

        nodes[0][0] = new MapNode(chapter, "empty", null);

        for (int i = 1; i < nodes.length - 1; i++) {
            if (layout[i] == 1) {
                nodes[i][0] = new MapNode(chapter, "battle");
            } else {
                for (int j = 0; j < layout[i]; j++) {
                    nodes[i][j] = new MapNode(chapter, nonBattleNodes[(int) (nonBattleNodes.length * Math.random())]);
                }
            }
        }
        nodes[nodes.length - 1][0] = new MapNode(chapter, "boss" + chapter, null);
    }

    public MapNode[][] getNodes() {
        return nodes;
    }
}
