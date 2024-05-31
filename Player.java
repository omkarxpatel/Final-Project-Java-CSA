import java.util.*;

public class Player {
    private ArrayList<Card> hand; 
    private ArrayList<Card> cards;
                        
    public Player(ArrayList<Card> cards) {
        this.cards = cards;
        hand = new ArrayList<Card>(); 
    }

    public Player() {
        ArrayList<Card> initCards = new ArrayList<Card>();
        initCards.add(Card.cards.get("stoat"));
        initCards.add(Card.cards.get("wolf"));
        initCards.add(Card.cards.get("bullfrog"));
        this.cards = initCards;
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
