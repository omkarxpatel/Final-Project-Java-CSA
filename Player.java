import java.util.*;

public class Player {
    private ArrayList<Card> cards;
    private ArrayList<Card> hand; 

    public Player(ArrayList<Card> cards) {
        this.cards = cards;
        hand = new ArrayList<Card>(); 
    }

    public Player() {
        this.cards = new ArrayList<Card>();
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addCards(Card add) {
        cards.add(add);
    }

    public void draw(Card add) {
        hand.add(add);
    }

    
}
