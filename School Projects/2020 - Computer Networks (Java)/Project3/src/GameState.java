import java.util.ArrayList;
import java.util.Collections;

public class GameState {
    private UNO game;
    private ArrayList<Card> deck;
    private ArrayList<Card> discarded;

    public static final int COLOR_RED = 0;
    public static final int COLOR_YELLOW = 1;
    public static final int COLOR_BLUE = 2;
    public static final int COLOR_GREEN = 3;
    public static final int COLOR_BLACK = 4;

    public static final int TYPE_NUMBER = 0;
    public static final int TYPE_SKIP = 1;
    public static final int TYPE_REVERSE = 2;
    public static final int TYPE_DRAW_TWO = 3;
    public static final int TYPE_WILD = 4;
    public static final int TYPE_WILD_DRAW_FOUR = 5;

    public int currentPlayer = 0;
    public int currentDirection = 1;

    public GameState(UNO game) {
        this.game = game;
        deck = new ArrayList<>();
        discarded = new ArrayList<>();
    }

    public void startNewGame() {
        // Create a deck and pick up one discard card
        createDeck();
        discardCard(takeNumberCard());

        // Give all players seven cards
        for(int j=0;j<7;j++){
            for(int i=0;i<game.players.size();i++){
                game.players.get(i).pickUpCard(this);
            }
        }
    }

    public void extendDeck() {
        // Add the number cards
        for (int i=0;i<9;i++){
            deck.add(new Card(COLOR_RED, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_RED, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_YELLOW, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_YELLOW, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_BLUE, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_BLUE, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_GREEN, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_GREEN, TYPE_NUMBER, i));
        }

        // Add the 20 point cards
        for (int i=0; i<2; i++) {
            deck.add(new Card(COLOR_RED, TYPE_SKIP, 20));
            deck.add(new Card(COLOR_RED, TYPE_REVERSE, 20));
            deck.add(new Card(COLOR_RED, TYPE_DRAW_TWO, 20));
            deck.add(new Card(COLOR_YELLOW, TYPE_SKIP, 20));
            deck.add(new Card(COLOR_YELLOW, TYPE_REVERSE, 20));
            deck.add(new Card(COLOR_YELLOW, TYPE_DRAW_TWO, 20));
            deck.add(new Card(COLOR_BLUE, TYPE_SKIP, 20));
            deck.add(new Card(COLOR_BLUE, TYPE_REVERSE, 20));
            deck.add(new Card(COLOR_BLUE, TYPE_DRAW_TWO, 20));
            deck.add(new Card(COLOR_GREEN, TYPE_SKIP, 20));
            deck.add(new Card(COLOR_GREEN, TYPE_REVERSE, 20));
            deck.add(new Card(COLOR_GREEN, TYPE_DRAW_TWO, 20));
        }

        // Add the wild cards
        for (int i=0; i<4; i++) {
            deck.add(new Card(COLOR_BLACK, TYPE_WILD, 50));
            deck.add(new Card(COLOR_BLACK, TYPE_WILD_DRAW_FOUR, 50));
        }

        // Shuffle the new deck
        Collections.shuffle(deck);
    }

    public void createDeck() {

        // Clear the deck and discard pile
        deck.clear();
        discarded.clear();

        // Add the number cards
        for (int i=0;i<9;i++){
            deck.add(new Card(COLOR_RED, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_RED, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_YELLOW, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_YELLOW, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_BLUE, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_BLUE, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_GREEN, TYPE_NUMBER, i));
            deck.add(new Card(COLOR_GREEN, TYPE_NUMBER, i));
        }

        // Add the 20 point cards
        for (int i=0; i<2; i++) {
            deck.add(new Card(COLOR_RED, TYPE_SKIP, 20));
            deck.add(new Card(COLOR_RED, TYPE_REVERSE, 20));
            deck.add(new Card(COLOR_RED, TYPE_DRAW_TWO, 20));
            deck.add(new Card(COLOR_YELLOW, TYPE_SKIP, 20));
            deck.add(new Card(COLOR_YELLOW, TYPE_REVERSE, 20));
            deck.add(new Card(COLOR_YELLOW, TYPE_DRAW_TWO, 20));
            deck.add(new Card(COLOR_BLUE, TYPE_SKIP, 20));
            deck.add(new Card(COLOR_BLUE, TYPE_REVERSE, 20));
            deck.add(new Card(COLOR_BLUE, TYPE_DRAW_TWO, 20));
            deck.add(new Card(COLOR_GREEN, TYPE_SKIP, 20));
            deck.add(new Card(COLOR_GREEN, TYPE_REVERSE, 20));
            deck.add(new Card(COLOR_GREEN, TYPE_DRAW_TWO, 20));
        }

        // Add the wild cards
        for (int i=0; i<4; i++) {
            deck.add(new Card(COLOR_BLACK, TYPE_WILD, 50));
            deck.add(new Card(COLOR_BLACK, TYPE_WILD_DRAW_FOUR, 50));
        }

        // Shuffle the new deck
        Collections.shuffle(deck);
    }

    public Card takeCard() {
        int num = (int)(Math.random()*(deck.size()-1));
        Card card = deck.get(num);
        deck.remove(num);

        // Make sure there is enough cards in the deck left
        if (deck.size() <= 4)
            extendDeck();

        return card;
    }

    public Card takeNumberCard() {
        int num = (int)(Math.random()*(deck.size()-1));
        Card card = deck.get(num);

        // Keep picking up random cards until a number card is found
        while (card.getType() != TYPE_NUMBER) {
            num = (int)(Math.random()*(deck.size()-1));
            card = deck.get(num);
        }

        deck.remove(num);

        // Make sure there is enough cards in the deck left
        if (deck.size() <= 4)
            extendDeck();

        return card;
    }

    public Card getDiscardTop() {
        // Return the last card on the discard pile
        return discarded.get(discarded.size()-1);
    }

    public void discardCard(Card card) {
        discarded.add(card);
    }

    public void discardCardHidden(Card card) {
        discarded.add(0, card);
    }

    public boolean validate(Card card) {
        // Make sure that the card is valid to play
        return (card.getType() == TYPE_WILD || card.getType() == TYPE_WILD_DRAW_FOUR) || // Wild card
                (card.getColor() == getDiscardTop().getColor()) || // Colors match
                (card.getType() == TYPE_SKIP && getDiscardTop().getType() == TYPE_SKIP) || // Both a skip card
                (card.getType() == TYPE_DRAW_TWO && getDiscardTop().getType() == TYPE_DRAW_TWO) || // Both a draw two card
                (card.getType() == TYPE_NUMBER && card.getValue() == getDiscardTop().getValue()) || // Matching numbers
                (card.getType() == TYPE_REVERSE && getDiscardTop().getType() == TYPE_REVERSE); // Both a reverse card
    }

    public synchronized void nextPlayer() {
        currentPlayer += currentDirection;
        // Make the next player loop around in a circle
        if (currentPlayer >= game.players.size())
            currentPlayer = 0;
        if (currentPlayer <= -1)
            currentPlayer = game.players.size()-1;
    }

    public int score(PlayerThread player) {
        // Count up the values of the cards in the players hand
        int num = 0;
        for (Card card : player.getHand())
            num += card.getValue();
        return num;
    }
}


