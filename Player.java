import java.util.*;

public class Player {
    private ArrayList<Card> cards;

    public Player(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Player() {
        this.cards = new ArrayList<Card>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

}
