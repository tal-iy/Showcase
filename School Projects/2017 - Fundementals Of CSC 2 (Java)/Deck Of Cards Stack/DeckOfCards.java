package deckofcardsstack;

import deckofcardsstack.Card;

/**
 *
 * @author v.shydlonok
 */

public class DeckOfCards
{
    private int count = 0;
    ArrayStack<Card> cardsStack;
    
    /*
        Creates an array of 52 unique cards
    */
    public DeckOfCards()
    {
        cardsStack = new ArrayStack();
        for(int suit=1;suit<=4;suit++)
            for(int num=1;num<=13;num++)
            {
                cardsStack.push(new Card(num,suit));
                count++;
            }
    }
    
    /*
        Returns a card from the deck and then decreases card count
    */
    public Card deal()
    {
        count--;
        return cardsStack.pop();
    }
    
    /*
        Shuffles the deck within the bounds of card count
    */
    public void shuffle()
    {
        //populate an array with stack contents
        Card cards[] = new Card[52];
        for(int i=0;i<count;i++)
            cards[i] = cardsStack.pop();
        //shuffle array
        for(int i=0;i<count;i++)
        {
            int c2 = (int)(Math.random()*(count-1));
            Card temp = cards[i];
            cards[i] = cards[c2];
            cards[c2] = temp;
        }
        //push shuffled array back onto stack
        for(int i=0;i<count;i++)
            cardsStack.push(cards[i]);
    }
    
    /*
        Returns the number of cards left in the deck
    */
    public int getCardCount()
    {
        return count;
    }
}
