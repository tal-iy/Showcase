import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Properties;

public class UNO {

    private final int PORT = 2760;
    private SSLServerSocket serverSocket;

    public GameWindow window;

    public int numPlayers = 1;
    public boolean waitingToStart = true;
    public boolean playingGame = false;

    public ArrayList<PlayerThread> players;
    public PlayerThread mainPlayer;
    public GameState state;

    public UNO() {
        window = new GameWindow(this);
        players = new ArrayList<>();
        state = new GameState(this);
    }

    public void hostGame() {
        UNO game = this;
        System.out.println("Waiting for players...");
        setNumPlayers(1);
        waitingToStart = true;

        new Thread(() -> {
            try {
                // Create the host player
                mainPlayer = new PlayerThread(game, window.txtName.getText());
                players.add(mainPlayer);

                // Start listening for client connections
                serverSocket = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(PORT);
                serverSocket.setEnabledProtocols(new String[] {"TLSv1.2"});
                serverSocket.setEnabledCipherSuites(new String[] {"TLS_RSA_WITH_AES_128_GCM_SHA256"});

                // Loop until no longer waiting for more players
                while (waitingToStart) {
                    // Wait for a client connection
                    SSLSocket playerSocket = (SSLSocket) serverSocket.accept();

                    // Double check that we're still waiting for a player since the last accept
                    if (waitingToStart) {
                        // Start a new thread for the player
                        PlayerThread player = new PlayerThread(game, playerSocket);
                        new Thread(player).start();

                        // Update the GUI
                        setNumPlayers(numPlayers+1);

                        // Notify all players that another player joined
                        for (PlayerThread pl : players)
                            if (pl != mainPlayer)
                                pl.notifyPlayerJoined();

                        // Add the player thread to the list of players
                        players.add(player);
                    } else playerSocket.close();
                }
            } catch (SocketException ignored) { } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void startGame(String name) {
        waitingToStart = false;

        // Update the host player's name
        mainPlayer.name = name;

        // Create a deck and start game logic
        state.startNewGame();

        // Update the host player's screen
        for (Card card : mainPlayer.getHand()) {
            window.gameHandPanel.add(card);
            addCardClick(card);
            System.out.println("Card:"+card.getColor()+", "+card.getType()+", "+card.getValue());
        }
        window.gameCurrentCard.setColor(state.getDiscardTop().getColor());
        window.gameCurrentCard.setType(state.getDiscardTop().getType());
        window.gameCurrentCard.setValue(state.getDiscardTop().getValue());
        window.gameCurrentCard.setIcon(state.getDiscardTop().getIcon());

        // Choose a random player to start
        int index = (int)(Math.random()*(players.size()));
        state.currentPlayer = index;
        PlayerThread starter = players.get(index);

        // Notify all players that the game is started
        for (PlayerThread pl : players)
            if (pl != mainPlayer)
                pl.notifyGameStart(state.getDiscardTop(), starter.name);

        // Update the screen of the host player
        if (starter == mainPlayer)
            window.lblGameStatus.setText("Your turn: pick a card or draw from the pile...");
        else
            window.lblGameStatus.setText(starter.name+"s turn: please wait...");
    }

    public void startGame(ArrayList<Card> hand, Card top, String name) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                window.setScreen(GameWindow.SCREEN_GAME);

                // Update the players screen
                for (Card card : hand) {
                    window.gameHandPanel.add(card);
                    addCardClick(card);
                    System.out.println("Card:"+card.getColor()+", "+card.getType()+", "+card.getValue());
                }
                window.gameCurrentCard.setColor(top.getColor());
                window.gameCurrentCard.setType(top.getType());
                window.gameCurrentCard.setValue(top.getValue());
                window.gameCurrentCard.setIcon(top.getIcon());

                if (name.equals(mainPlayer.name))
                    window.lblGameStatus.setText("Your turn: pick a card or draw from the pile...");
                else
                    window.lblGameStatus.setText(name+"s turn: please wait...");
            });
        } catch (InterruptedException | InvocationTargetException e) { e.printStackTrace(); }
    }

    public void joinGame(String host, String name) {
        System.out.println("Waiting for players...");
        try {
            // Connect to the server using SSL
            SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(host, PORT);
            socket.setEnabledProtocols(new String[] {"TLSv1.2"});
            socket.setEnabledCipherSuites(new String[] {"TLS_RSA_WITH_AES_128_GCM_SHA256"});
            mainPlayer = new PlayerThread(this, socket, name);
            new Thread(mainPlayer).start();
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public synchronized void stopHosting() {
        playingGame = false;
        try {
            // Notify all players that the host quit
            for (PlayerThread pl : players)
                if (pl != mainPlayer)
                    pl.notifyHostQuit();

            // Close the socket
            serverSocket.close();

        } catch (Exception ignored) { }
        players.clear();
    }

    public synchronized boolean isPlaying() {
        return playingGame;
    }

    public synchronized void setPlaying(boolean playing) {
        playingGame = playing;
    }

    public synchronized void setNumPlayers(int change) {
        numPlayers = change;
        window.updateNumPlayers(numPlayers);
    }

    public synchronized int getNumPlayers() {
        return numPlayers;
    }

    public void mainMenu() {
        players.clear();
        try {
            SwingUtilities.invokeAndWait(() -> {
                window.setScreen(GameWindow.SCREEN_MAIN_MENU);
                window.gameHandPanel.removeAll();
            });
        } catch (InterruptedException | InvocationTargetException e) { e.printStackTrace(); }
    }

    public void addCardClick(Card card) {
        UNO game = this;
        card.addActionListener(e -> {
            if (game.mainPlayer.hosting) {
                // Check if it's wild
                if (card.getType() == GameState.TYPE_WILD || card.getType() == GameState.TYPE_WILD_DRAW_FOUR) {
                    Object[] options = { "Red", "Yellow", "Blue", "Green"};
                    int result = JOptionPane.showOptionDialog(null, "Choose a wildcard color:", "Choose a color",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,null, options, null);
                    card.setColor(result);
                }

                System.out.println("Host: "+mainPlayer.name+" played card - "+card.getColor()+", "+card.getType()+", "+card.getValue());

                // Make sure it's the players turn
                if (game.state.currentPlayer == 0) {
                    // Validate the card
                    if (game.state.validate(card)) {
                        // Put the card on the discard pile
                        game.state.discardCard(card);
                        game.mainPlayer.getHand().remove(card);
                        window.gameHandPanel.remove(card);
                        window.gameHandPanel.revalidate();
                        window.gameCurrentCard.setColor(card.getColor());
                        window.gameCurrentCard.setValue(card.getValue());
                        window.gameCurrentCard.setType(card.getType());
                        window.gameCurrentCard.setIcon(card.getIcon());
                        window.getContentPane().repaint();

                        // Tell all players about the new discard card
                        for (PlayerThread pl : game.players)
                            if (pl != game.mainPlayer)
                                pl.notifyDiscard(card);

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
                                // Tell the player that they picked up four cards
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
                        if (game.mainPlayer.getHand().size() == 0) {

                            // Tell all players that there is a winner
                            for (PlayerThread pl : game.players)
                                if (pl != game.mainPlayer)
                                    pl.notifyWinner(game.mainPlayer.name);

                            // Tell the host player that they won
                            Thread t = new Thread(() -> JOptionPane.showMessageDialog(game.window,"You won the game!","Congrats!",JOptionPane.WARNING_MESSAGE));
                            t.start();
                            playingGame = false;
                            players.clear();
                            numPlayers = 1;
                            window.setScreen(GameWindow.SCREEN_MAIN_MENU);
                        } else {
                            // Tell all players about the new player's turn
                            PlayerThread plCurrent = game.players.get(game.state.currentPlayer);
                            if (game.state.currentPlayer == 0)
                                game.updateStatus("Your turn: pick a card or draw from the pile...");
                            else
                                game.updateStatus(plCurrent.name+"s turn: please wait...");
                            for (PlayerThread pl : game.players)
                                if (pl != game.mainPlayer)
                                    pl.notifyTurnChange(plCurrent.name);
                        }

                        window.revalidate();
                    }
                }
            } else {
                // Notify the server that a card was played
                game.mainPlayer.notifyPlayCard(card);
                System.out.println("Guest: "+mainPlayer.name+" played card - "+card.getColor()+", "+card.getType()+", "+card.getValue());
            }
        });
    }

    public void drawCard() {
        if (mainPlayer.hosting) {
            // Make sure it's the players turn
            if (state.currentPlayer == 0) {
                mainPlayer.pickUpCard(state);
                Card card = mainPlayer.getHand().get(mainPlayer.getHand().size()-1);
                window.gameHandPanel.add(card);
                addCardClick(card);
                System.out.println("Card:"+card.getColor()+", "+card.getType()+", "+card.getValue());
                window.gameHandPanel.revalidate();
            }
        } else {
            // Notify the server that a card was drawn
            mainPlayer.notifyDrawCard();
        }
    }

    public void addToHand(Card card) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                window.gameHandPanel.add(card);
                addCardClick(card);
                window.gameHandPanel.revalidate();
            });
        } catch (InterruptedException | InvocationTargetException e) { e.printStackTrace(); }
    }

    public void updateDiscard(Card card) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                window.gameCurrentCard.setColor(card.getColor());
                window.gameCurrentCard.setType(card.getType());
                window.gameCurrentCard.setValue(card.getValue());
                window.gameCurrentCard.setIcon(card.getIcon());
            });
        } catch (InterruptedException | InvocationTargetException e) { e.printStackTrace(); }
    }

    public void removeHand(Card card) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                window.gameHandPanel.remove(card);
                window.getContentPane().repaint();
            });
        } catch (InterruptedException | InvocationTargetException e) { e.printStackTrace(); }
    }

    public void updateStatus(String status) {
        try {
            window.lblGameStatus.setText(status);
        } catch (Exception ignored) { }
    }

    public static void main(String[] args) throws InterruptedException {
        Properties p = System.getProperties();
        p.setProperty("javax.net.ssl.keyStore","keystore");
        p.setProperty("javax.net.ssl.keyStorePassword","passphrase");
        p.setProperty("javax.net.ssl.trustStore","keystore");
        p.setProperty("javax.net.ssl.trustStorePassword","passphrase");

        new UNO();

        Object o = new Object();
        synchronized (o) {
            o.wait();
        }
    }
}
