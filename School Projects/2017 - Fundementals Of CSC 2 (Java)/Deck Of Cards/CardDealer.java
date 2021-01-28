package deckofcards;

/**
 *
 * @author v.shydlonok
 */
public class CardDealer {
    
    public static void main(String[] args) {
        
        //testing a brand new deck
        DeckOfCards deck = new DeckOfCards();
        System.out.println("Unshuffled Deck:");
        for(int i=0;i<52;i++)
        {
            System.out.println(deck.deal());
        }
        
        //testing a shuffled deck
        deck = new DeckOfCards();
        System.out.println("\nShuffled Deck:");
        deck.shuffle();
        for(int i=0;i<52;i++)
        {
            System.out.println(deck.deal());
        }
        
        //testing dealing out part of a deck and getting a number of cards remaining
        deck = new DeckOfCards();
        System.out.println("\nDealing out 13 cards:");
        for(int i=0;i<13;i++)
        {
            System.out.println(deck.deal());
        }
        System.out.println("There are "+deck.getCardCount()+" cards left!");
        
        //testing shuffling of a smaller deck
        System.out.println("\nShuffling remaining cards:");
        deck.shuffle();
        for(int i=0;i<52-13;i++)
        {
            System.out.println(deck.deal());
        }
        System.out.println("There are "+deck.getCardCount()+" cards left!");
        
    }
    
}
