public class Test {
    private int chapter;
    private int progress;
    private int posNode;
    private String[] layouts = new String[] {"11212321212321", ""};
    private MapNode[][] nodes;

    public Test() {
        

        
        nodes[0][0] = new MapNode(1, "empty", null);

    }

    public Test(int chapter) {
        progress = 0;
        posNode = 0;
        String layout = layouts[chapter - 1];
        switch (chapter) {
            case 1:
                break;
        }
    }
}
