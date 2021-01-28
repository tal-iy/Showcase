
package deckofcards;

/**
 *
 * @author v.shydlonok
 */

public class DeckOfCards 
{
    private Card cards[] = new Card[52];
    private int count = 0;
    
    /*
        Creates an array of 52 unique cards
    */
    public DeckOfCards()
    {
        for(int suit=1;suit<=4;suit++)
            for(int num=1;num<=13;num++)
            {
                cards[count] = new Card(num,suit);
                count++;
            }
    }
    
    /*
        Returns a card from the deck and then decreases card count
    */
    public Card deal()
    {
        count--;
        return cards[count];
    }
    
    /*
        Shuffles the deck within the bounds of card count
    */
    public void shuffle()
    {
        for(int i=0;i<count;i++)
        {
            switchCards(i,(int)(Math.random()*(count-1)));
        }
    }
    
    /*
        Returns the number of cards left in the deck
    */
    public int getCardCount()
    {
        return count;
    }
    
    /*
        Utility method for switching two cards within the array
    */
    private void switchCards(int c1,int c2)
    {
        Card temp = cards[c1];
        cards[c1] = cards[c2];
        cards[c2] = temp;
    }
}
