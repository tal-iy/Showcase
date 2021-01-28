import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class PlayerThread implements Runnable {

    // Message codes
    public static final int MSG_ACCEPTED = 13;
    public static final int MSG_DECLINED = 14;
    public static final int MSG_PLAYER_JOINED = 15;
    public static final int MSG_PLAYER_QUIT = 16;
    public static final int MSG_HOST_QUIT = 17;
    public static final int MSG_GAME_START = 18;
    public static final int MSG_PLAY_CARD = 19;
    public static final int MSG_DRAW_CARD = 20;
    public static final int MSG_SKIP_TURN = 22;
    public static final int MSG_DRAW_TWO = 23;
    public static final int MSG_DRAW_FOUR = 24;
    public static final int MSG_TURN_CHANGE = 25;
    public static final int MSG_CARD_ACCEPTED = 26;
    public static final int MSG_WINNER = 27;

    private UNO game;
    private Socket connection;
    private ArrayList<Card> hand;
    public String name;

    public boolean hosting;

    public DataOutputStream out;
    public DataInputStream in;

    public PlayerThread(UNO game, String name) {
        this.game = game;
        hand = new ArrayList<>();
        this.hosting = true;
        this.name = name;
    }

    public PlayerThread(UNO game, SSLSocket connection) {
        this.game = game;
        this.connection = connection;
        this.hosting = true;
        hand = new ArrayList<>();
    }

    public PlayerThread(UNO game, SSLSocket connection, String name) {
        this.game = game;
        this.connection = connection;
        this.name = name;
        this.hosting = false;
        hand = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            out = new DataOutputStream(connection.getOutputStream());
            in = new DataInputStream(connection.getInputStream());

            if (hosting) {

                // Read the request
                String req = in.readUTF();
                System.out.println("player " + req + " joined!");
                name = req;

                // Make sure the name is unique
                boolean unique = true;
                for (PlayerThread pl : game.players)
                    if (pl != this && pl.name.equals(name)) {
                        unique = false;
                        break;
                    }

                if (unique) {
                    // Send an ack
                    out.writeByte(MSG_ACCEPTED);
                    out.writeInt(game.getNumPlayers());

                } else out.writeByte(MSG_DECLINED); // Send an error
                out.flush();

                boolean quitting = false;
                while (!quitting && game.isPlaying()) {
                    byte msg = in.readByte();
                    switch(msg) {
                        case(MSG_PLAY_CARD):
                            // Get the card id
                            int index = in.readInt();
                            Card card = hand.get(index);

                            // Check if it's wild
                            if (card.getType() == GameState.TYPE_WILD || card.getType() == GameState.TYPE_WILD_DRAW_FOUR)
                                card.setColor(in.readByte());

                            System.out.println("Host: "+name+" played card - "+card.getColor()+", "+card.getType()+", "+card.getValue());

                            // Make sure it's the players turn
                            if (game.state.currentPlayer == game.players.indexOf(this)) {
                                // Validate the card
                                if (game.state.validate(card)) {
                                    // Accept the card
                                    out.writeByte(MSG_CARD_ACCEPTED);
                                    out.writeInt(index);
                                    out.flush();

                                    // Put the card on the discard pile
                                    game.state.discardCard(card);
                                    hand.remove(card);
                                    game.updateDiscard(card);

                                    // Tell all players about the new discard card
                                    for (PlayerThread pl : game.players) {
                                        if (pl != game.mainPlayer) {
                                            pl.notifyDiscard(card);
                                        }
                                    }

                                    // Check what type of card was played
                                    switch(card.getType()){
                                        case(GameState.TYPE_NUMBER):
                                        case(GameState.TYPE_WILD):
                                            // Just play it and move to the next player
                                            game.state.nextPlayer();
                                            break;
                                        case(GameState.TYPE_SKIP):
                                            // Move to the next player
                                            game.state.nextPlayer();
                                            // Tell that player that they were skipped
                                            if (game.state.currentPlayer == 0) {
                                                Thread t = new Thread(() -> JOptionPane.showMessageDialog(game.window,"Your turn was skipped!","Skip!",JOptionPane.WARNING_MESSAGE));
                                                t.start();
                                            } else game.players.get(game.state.currentPlayer).notifySkipped();
                                            // Move to the next player again
                                            game.state.nextPlayer();
                                            break;
                                        case(GameState.TYPE_REVERSE):
                                            // Change the direction of play
                                            game.state.currentDirection *= -1;
                                            // Move to the next player
                                            game.state.nextPlayer();
                                            break;
                                        case(GameState.TYPE_DRAW_TWO):
                                            // Move to the next player
                                            game.state.nextPlayer();
                                            PlayerThread pl = game.players.get(game.state.currentPlayer);
                                            // Draw two cards for that player
                                            pl.pickUpCard(game.state);
                                            Card card1 = pl.getHand().get(pl.getHand().size()-1);
                                            pl.pickUpCard(game.state);
                                            Card card2 = pl.getHand().get(pl.getHand().size()-1);
                                            // Tell the player that they picked up two cards
                                            if (pl == game.mainPlayer) {
                                                game.addToHand(card1);
                                                game.addToHand(card2);
                                            } else pl.notifyDrawTwo(card1, card2);
                                            // Move to the next player again
                                            game.state.nextPlayer();
                                            break;
                                        case(GameState.TYPE_WILD_DRAW_FOUR):
                                            // Move to the next player
                                            game.state.nextPlayer();
                                            pl = game.players.get(game.state.currentPlayer);
                                            // Draw four cards for that player
                                            pl.pickUpCard(game.state);
                                            card1 = pl.getHand().get(pl.getHand().size()-1);
                                            pl.pickUpCard(game.state);
                                            card2 = pl.getHand().get(pl.getHand().size()-1);
                                            pl.pickUpCard(game.state);
                                            Card card3 = pl.getHand().get(pl.getHand().size()-1);
                                            pl.pickUpCard(game.state);
                                            Card card4 = pl.getHand().get(pl.getHand().size()-1);
                                            // Tell the player that they picked up two cards
                                            if (pl == game.mainPlayer) {
                                                game.addToHand(card1);
                                                game.addToHand(card2);
                                                game.addToHand(card3);
                                                game.addToHand(card4);
                                            } else pl.notifyDrawFour(card1, card2, card3, card4);
                                            // Move to the next player again
                                            game.state.nextPlayer();
                                            break;
                                    }

                                    // Check if the player won
                                    if (getHand().size() == 0) {

                                        // Tell all players that there is a winner
                                        for (PlayerThread pl : game.players)
                                            if (pl != game.mainPlayer)
                                                pl.notifyWinner(name);

                                        // Tell the host player that someone won
                                        Thread t = new Thread(() -> JOptionPane.showMessageDialog(game.window,name+" won the game! Your score: "+game.state.score(game.mainPlayer),"You lost!",JOptionPane.WARNING_MESSAGE));
                                        t.start();
                                        game.playingGame = false;
                                        game.players.clear();
                                        game.setNumPlayers(1);
                                        game.mainMenu();
                                        quitting = true;
                                    } else {
                                        PlayerThread plCurrent = game.players.get(game.state.currentPlayer);

                                        // Check if the next player is the host player
                                        if (game.state.currentPlayer == 0)
                                            game.updateStatus("Your turn: pick a card or draw from the pile...");
                                        else
                                            game.updateStatus(plCurrent.name+"s turn: please wait...");

                                        // Tell all players about the new player's turn
                                        for (PlayerThread pl : game.players) {
                                            if (pl != game.mainPlayer) {
                                                pl.notifyTurnChange(plCurrent.name);
                                            }
                                        }
                                    }
                                }
                            }

                            break;
                        case(MSG_DRAW_CARD):
                            // Make sure it's the players turn
                            if (game.state.currentPlayer == game.players.indexOf(this)) {
                                // Draw a new card
                                pickUpCard(game.state);
                                card = hand.get(hand.size()-1);

                                // Notify the player of the new card
                                out.writeByte(MSG_DRAW_CARD);
                                out.writeByte(card.getColor());
                                out.writeByte(card.getType());
                                out.writeByte(card.getValue());
                                out.flush();
                            }
                            break;
                        case(MSG_PLAYER_QUIT):
                            // Remove self from the list of players
                            game.setNumPlayers(game.getNumPlayers()-1);
                            System.out.println("Player quit! "+game.getNumPlayers());
                            quitting = true;
                            game.players.remove(this);

                            // Tell all players that a player quit
                            for (PlayerThread pl : game.players)
                                pl.notifyPlayerQuit();

                            break;
                        default:
                            break;
                    }
                }

                System.out.println("Closing player thread...");

            } else {

                // Send the server the player's name
                out.writeUTF(name);
                out.flush();

                // Wait for a response
                byte res = in.readByte();

                // Make sure the player was accepted
                if (res == MSG_ACCEPTED) {
                    game.setNumPlayers(in.readInt());
                } else {
                    game.setPlaying(false);
                    game.mainMenu();
                }

                int color;
                int type;
                int value;

                // Continuously read messages from the server until the game ends
                boolean quitting = false;
                while (!quitting && game.isPlaying()) {
                    byte msg = in.readByte();
                    switch(msg) {
                        case(MSG_WINNER):
                            String winner = in.readUTF();
                            Thread t = new Thread(() -> {
                                if (winner.equals(name))
                                    JOptionPane.showMessageDialog(game.window,"You won the game!","Congrats!",JOptionPane.WARNING_MESSAGE);
                                else
                                    JOptionPane.showMessageDialog(game.window,winner+" won the game! Your score: "+game.state.score(game.mainPlayer),"You lost!",JOptionPane.WARNING_MESSAGE);
                            });
                            t.start();
                            game.playingGame = false;
                            game.setNumPlayers(1);
                            game.mainMenu();
                            quitting = true;
                            break;
                        case(MSG_DRAW_TWO):
                            color = in.readByte();
                            type = in.readByte();
                            value = in.readByte();
                            Card card = new Card(color, type, value);
                            hand.add(card);
                            game.addToHand(card);
                            System.out.println("Card:"+card.getColor()+", "+card.getType()+", "+card.getValue());
                            color = in.readByte();
                            type = in.readByte();
                            value = in.readByte();
                            card = new Card(color, type, value);
                            hand.add(card);
                            game.addToHand(card);
                            System.out.println("Card:"+card.getColor()+", "+card.getType()+", "+card.getValue());
                            break;
                        case(MSG_DRAW_FOUR):
                            color = in.readByte();
                            type = in.readByte();
                            value = in.readByte();
                            card = new Card(color, type, value);
                            hand.add(card);
                            game.addToHand(card);
                            System.out.println("Card:"+card.getColor()+", "+card.getType()+", "+card.getValue());
                            color = in.readByte();
                            type = in.readByte();
                            value = in.readByte();
                            card = new Card(color, type, value);
                            hand.add(card);
                            game.addToHand(card);
                            System.out.println("Card:"+card.getColor()+", "+card.getType()+", "+card.getValue());
                            color = in.readByte();
                            type = in.readByte();
                            value = in.readByte();
                            card = new Card(color, type, value);
                            hand.add(card);
                            game.addToHand(card);
                            System.out.println("Card:"+card.getColor()+", "+card.getType()+", "+card.getValue());
                            color = in.readByte();
                            type = in.readByte();
                            value = in.readByte();
                            card = new Card(color, type, value);
                            hand.add(card);
                            game.addToHand(card);
                            System.out.println("Card:"+card.getColor()+", "+card.getType()+", "+card.getValue());
                            break;
                        case(MSG_SKIP_TURN):
                            JOptionPane.showMessageDialog(game.window,"Your turn was skipped!","Skip!",JOptionPane.WARNING_MESSAGE);
                            break;
                        case(MSG_DRAW_CARD):
                            color = in.readByte();
                            type = in.readByte();
                            value = in.readByte();
                            card = new Card(color, type, value);
                            hand.add(card);
                            game.addToHand(card);
                            System.out.println("Card:"+card.getColor()+", "+card.getType()+", "+card.getValue());
                            break;
                        case(MSG_PLAY_CARD):
                            System.out.println("Discard updated!");
                            color = in.readByte();
                            type = in.readByte();
                            value = in.readByte();
                            game.updateDiscard(new Card(color, type, value));
                            break;
                        case(MSG_CARD_ACCEPTED):
                            System.out.println("Card accepted!");
                            int index = in.readInt();
                            game.removeHand(hand.get(index));
                            hand.remove(index);
                            break;
                        case(MSG_TURN_CHANGE):
                            String turn = in.readUTF();
                            if (turn.equals(name))
                                game.updateStatus("Your turn: pick a card or draw from the pile...");
                            else
                                game.updateStatus(turn+"s turn: please wait...");
                            break;
                        case(MSG_PLAYER_JOINED):
                            int num = in.readInt();
                            System.out.println("Player joined! "+num);
                            game.setNumPlayers(num);
                            break;
                        case(MSG_PLAYER_QUIT):
                            game.setNumPlayers(game.getNumPlayers()-1);
                            System.out.println("Player quit! "+game.getNumPlayers());
                            break;
                        case(MSG_HOST_QUIT):
                            System.out.println("Host quit! ");
                            // Quit the game and show the main menu
                            game.setPlaying(false);
                            quitting = true;
                            game.mainMenu();
                            break;
                        case(MSG_GAME_START):
                            System.out.println("Game started!");

                            // Read the hand that the player has
                            int handSize = in.readInt();
                            for (int i=0; i<handSize; i++) {
                                color = in.readByte();
                                type = in.readByte();
                                value = in.readByte();
                                hand.add(new Card(color, type, value));
                            }

                            // Read the discard card
                            color = in.readByte();
                            type = in.readByte();
                            value = in.readByte();
                            Card discard = new Card(color, type, value);

                            // Read whether this player is starting
                            String starter = in.readUTF();

                            // Start the game with the hand and discard card
                            game.startGame(hand, discard, starter);
                            break;
                        default:
                            break;
                    }
                }
            }

            in.close();
            out.close();
            connection.close();
        } catch (SocketException ex) {
            if (hosting) {
                // Make sure there are still at least two players left
                game.setNumPlayers(game.getNumPlayers()-1);
                if (game.getNumPlayers() > 1) {
                    // Put the disconnected players hand onto the bottom of the discard pile
                    for (Card card : hand)
                        game.state.discardCardHidden(card);

                    // Check if it was the player's turn
                    if (game.state.currentPlayer == game.players.indexOf(this)) {

                        // Make it the next player's turn
                        game.state.nextPlayer();

                        // Notify everyone that it's a new player's turn
                        for (PlayerThread pl : game.players)
                            if (pl != this)
                                pl.notifyTurnChange(game.players.get(game.state.currentPlayer).name);
                    }

                    // Remove the player from the players list
                    game.players.remove(this);

                } else {
                    // Quit the game because there aren't enough players
                    game.setPlaying(false);
                    game.mainMenu();
                }
            } else {
                // Quit the game when connection to the server is lost
                game.setPlaying(false);
                game.mainMenu();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyPlayerJoined() {
        try {
            out.writeByte(MSG_PLAYER_JOINED);
            out.writeInt(game.getNumPlayers());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyPlayerQuit() {
        try {
            out.writeByte(MSG_PLAYER_QUIT);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyHostQuit() {
        try {
            out.writeByte(MSG_HOST_QUIT);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyGameStart(Card top, String starter) {
        try {
            out.writeByte(MSG_GAME_START);
            out.writeInt(hand.size());
            for (Card card : hand) {
                out.writeByte(card.getColor());
                out.writeByte(card.getType());
                out.writeByte(card.getValue());
            }
            out.writeByte(top.getColor());
            out.writeByte(top.getType());
            out.writeByte(top.getValue());
            out.writeUTF(starter);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyTurnChange(String turn) {
        try {
            out.writeByte(MSG_TURN_CHANGE);
            out.writeUTF(turn);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyPlayCard(Card card) {
        try {
            out.writeByte(MSG_PLAY_CARD);
            out.writeInt(hand.indexOf(card));

            // Take care of wild cards
            if (card.getType() == GameState.TYPE_WILD || card.getType() == GameState.TYPE_WILD_DRAW_FOUR) {
                Object[] options = { "Red", "Yellow", "Blue", "Green"};
                int result = JOptionPane.showOptionDialog(null, "Choose a wildcard color:", "Choose a color",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, null);
                out.writeByte(result);
            }

            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyDiscard(Card card) {
        try {
            out.writeByte(MSG_PLAY_CARD);
            out.writeByte(card.getColor());
            out.writeByte(card.getType());
            out.writeByte(card.getValue());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyDrawCard() {
        try {
            out.writeByte(MSG_DRAW_CARD);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifySkipped() {
        try {
            out.writeByte(MSG_SKIP_TURN);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyDrawTwo(Card card1, Card card2) {
        try {
            out.writeByte(MSG_DRAW_TWO);
            out.writeByte(card1.getColor());
            out.writeByte(card1.getType());
            out.writeByte(card1.getValue());
            out.writeByte(card2.getColor());
            out.writeByte(card2.getType());
            out.writeByte(card2.getValue());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyDrawFour(Card card1, Card card2, Card card3, Card card4) {
        try {
            out.writeByte(MSG_DRAW_FOUR);
            out.writeByte(card1.getColor());
            out.writeByte(card1.getType());
            out.writeByte(card1.getValue());
            out.writeByte(card2.getColor());
            out.writeByte(card2.getType());
            out.writeByte(card2.getValue());
            out.writeByte(card3.getColor());
            out.writeByte(card3.getType());
            out.writeByte(card3.getValue());
            out.writeByte(card4.getColor());
            out.writeByte(card4.getType());
            out.writeByte(card4.getValue());
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void notifyWinner(String winner) {
        try {
            out.writeByte(MSG_WINNER);
            out.writeUTF(winner);
            out.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void pickUpCard(GameState deck) {
        hand.add(deck.takeCard());
    }

    public ArrayList<Card> getHand() {
        return hand;
    }
}
