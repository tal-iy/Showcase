import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class GameWindow extends JFrame {

    public UNO game;

    public JPanel windowPanel;
    public JPanel mainMenuPanel;
    public JPanel hostMenuPanel;
    public JPanel joinMenuPanel;
    public JPanel joinWaitPanel;
    public JPanel gamePanel;

    public JLabel lblPlayersHost;
    public JLabel lblPlayersJoin;
    public JLabel lblGameStatus;

    public JTextField txtName;

    public JPanel gameHandPanel;
    public Card gameCurrentCard;
    public JPanel middlePanel;

    public static final String SCREEN_MAIN_MENU = "mainMenu";
    public static final String SCREEN_HOST_MENU = "hostMenu";
    public static final String SCREEN_JOIN_MENU = "joinMenu";
    public static final String SCREEN_WAIT_MENU = "waitMenu";
    public static final String SCREEN_GAME = "game";

    public GameWindow(UNO game) {
        super("UNO");

        this.game = game;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();

        setLocationRelativeTo(null);
        setVisible(true);
        setSize(768, 600);
    }

    public void initComponents() {
        windowPanel = new JPanel(new CardLayout());
        getContentPane().add(windowPanel);

        // Initialize all menu and game panels
        initMainMenu();
        initHostMenu();
        initJoinMenu();
        initWaitMenu();
        initGameUI();

        // Add all game panels to the main window
        windowPanel.add(mainMenuPanel,SCREEN_MAIN_MENU);
        windowPanel.add(hostMenuPanel,SCREEN_HOST_MENU);
        windowPanel.add(joinMenuPanel,SCREEN_JOIN_MENU);
        windowPanel.add(joinWaitPanel,SCREEN_WAIT_MENU);
        windowPanel.add(gamePanel,SCREEN_GAME);

        // Begin with the main menu screen
        setScreen(SCREEN_MAIN_MENU);
        pack();
    }

    public void setScreen(String screen) {
        ((CardLayout)(windowPanel.getLayout())).show(windowPanel, screen);
        revalidate();
    }

    public void initMainMenu() {
        mainMenuPanel = new JPanel();
        mainMenuPanel.setBackground(Color.BLACK);
        mainMenuPanel.setLayout(new BoxLayout(mainMenuPanel, BoxLayout.Y_AXIS));

        // Game title
        JLabel lblTitle = new JLabel("UNO - Online");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitle.setForeground(Color.YELLOW);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(64, 32, 48, 32));

        // Host new game button
        JButton btnMainHost = new JButton("Host New Game");
        btnMainHost.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMainHost.setFont(new Font("Arial", Font.PLAIN, 24));
        btnMainHost.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));
        btnMainHost.setBackground(Color.RED);
        btnMainHost.setForeground(Color.WHITE);
        btnMainHost.setFocusPainted(false);
        btnMainHost.addActionListener(e -> {
            // Go to the host new game screen
            System.out.println("HOST GAME");
            setScreen(SCREEN_HOST_MENU);
            game.setPlaying(true);
            game.hostGame();
        });

        // Join game button
        JButton btnMainJoin = new JButton("Join Game");
        btnMainJoin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMainJoin.setFont(new Font("Arial", Font.PLAIN, 24));
        btnMainJoin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));
        btnMainJoin.setBackground(Color.RED);
        btnMainJoin.setForeground(Color.WHITE);
        btnMainJoin.setFocusPainted(false);
        btnMainJoin.addActionListener(e -> {
            // GO TO THE JOIN GAME SCREEN
            System.out.println("JOIN GAME");
            setScreen(SCREEN_JOIN_MENU);
        });

        // Quit button
        JButton btnMainQuit = new JButton("Quit");
        btnMainQuit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnMainQuit.setFont(new Font("Arial", Font.PLAIN, 24));
        btnMainQuit.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));
        btnMainQuit.setBackground(Color.RED);
        btnMainQuit.setForeground(Color.WHITE);
        btnMainQuit.setFocusPainted(false);
        btnMainQuit.addActionListener(e -> {
            // Exit game
            System.exit(-1);
        });

        // Add components to the menu panel
        mainMenuPanel.add(lblTitle);
        mainMenuPanel.add(btnMainHost);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        mainMenuPanel.add(btnMainJoin);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        mainMenuPanel.add(btnMainQuit);
        mainMenuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
    }

    public void initHostMenu() {
        hostMenuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hostMenuPanel.setBackground(Color.BLACK);
        hostMenuPanel.setLayout(new BoxLayout(hostMenuPanel, BoxLayout.Y_AXIS));

        // Game title
        JLabel lblTitle = new JLabel("UNO - Online");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitle.setForeground(Color.YELLOW);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(64, 32, 48, 32));

        // Host label
        String hostIP = "127.0.0.1";
        try {
            hostIP = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream())).readLine();
        } catch (IOException ignored) { }

        JLabel lblHost = new JLabel("IP: "+hostIP);
        lblHost.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHost.setFont(new Font("Arial", Font.PLAIN, 24));
        lblHost.setForeground(Color.WHITE);
        lblHost.setBorder(BorderFactory.createEmptyBorder(0, 32, 16, 32));

        // Name input label
        JLabel lblName = new JLabel("Enter a nickname:");
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblName.setFont(new Font("Arial", Font.PLAIN, 24));
        lblName.setForeground(Color.WHITE);
        lblName.setBorder(BorderFactory.createEmptyBorder(0, 32, 16, 32));

        // Name input textbox
        txtName = new JTextField("Guest"+(int)(Math.random()*100),25);
        txtName.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtName.setFont(new Font("Arial", Font.PLAIN, 24));
        txtName.setForeground(Color.BLACK);
        txtName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));
        txtName.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { upd(); }
            public void removeUpdate(DocumentEvent e) { upd(); }
            public void insertUpdate(DocumentEvent e) { upd(); }
            public void upd() {
                game.mainPlayer.name = txtName.getText();
            }
        });

        // Players label
        lblPlayersHost = new JLabel("Waiting for players to join... 1/20");
        lblPlayersHost.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPlayersHost.setFont(new Font("Arial", Font.PLAIN, 24));
        lblPlayersHost.setForeground(Color.WHITE);
        lblPlayersHost.setBorder(BorderFactory.createEmptyBorder(0, 32, 32, 32));

        // Start game button
        JButton btnHostStart = new JButton("Start Game");
        btnHostStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHostStart.setFont(new Font("Arial", Font.PLAIN, 24));
        btnHostStart.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));
        btnHostStart.setBackground(Color.RED);
        btnHostStart.setForeground(Color.WHITE);
        btnHostStart.setFocusPainted(false);
        btnHostStart.addActionListener(e -> {
            // START THE GAME
            if (game.getNumPlayers() > 1) {
                System.out.println("STARTING GAME");
                setScreen(SCREEN_GAME);
                game.startGame(txtName.getText());
            }
        });

        // Cancel host button
        JButton btnHostCancel = new JButton("Cancel");
        btnHostCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHostCancel.setFont(new Font("Arial", Font.PLAIN, 24));
        btnHostCancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));
        btnHostCancel.setBackground(Color.RED);
        btnHostCancel.setForeground(Color.WHITE);
        btnHostCancel.setFocusPainted(false);
        btnHostCancel.addActionListener(e -> {
            // CANCEL THE GAME HOSTING
            System.out.println("CANCEL GAME");
            setScreen(SCREEN_MAIN_MENU);
            game.stopHosting();
        });

        hostMenuPanel.add(lblTitle);
        hostMenuPanel.add(lblName);
        hostMenuPanel.add(txtName);
        hostMenuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        hostMenuPanel.add(lblHost);
        hostMenuPanel.add(lblPlayersHost);
        hostMenuPanel.add(btnHostStart);
        hostMenuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        hostMenuPanel.add(btnHostCancel);
        hostMenuPanel.add(Box.createRigidArea(new Dimension(0, 32)));
    }

    public void initJoinMenu() {
        joinMenuPanel = new JPanel();
        joinMenuPanel.setBackground(Color.BLACK);
        joinMenuPanel.setLayout(new BoxLayout(joinMenuPanel, BoxLayout.Y_AXIS));

        // Game title
        JLabel lblTitle = new JLabel("UNO - Online");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitle.setForeground(Color.YELLOW);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(64, 32, 48, 32));

        // Name input label
        JLabel lblName = new JLabel("Enter a nickname:");
        lblName.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblName.setFont(new Font("Arial", Font.PLAIN, 24));
        lblName.setForeground(Color.WHITE);
        lblName.setBorder(BorderFactory.createEmptyBorder(0, 32, 16, 32));

        // Name input textbox
        JTextField txtName = new JTextField("Guest"+(int)(Math.random()*100),25);
        txtName.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtName.setFont(new Font("Arial", Font.PLAIN, 24));
        txtName.setForeground(Color.BLACK);
        txtName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));

        // Host input label
        JLabel lblHost = new JLabel("Enter a game IP:");
        lblHost.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblHost.setFont(new Font("Arial", Font.PLAIN, 24));
        lblHost.setForeground(Color.WHITE);
        lblHost.setBorder(BorderFactory.createEmptyBorder(16, 32, 16, 32));

        // Host input textbox
        JTextField txtHost = new JTextField("127.0.0.1",15);
        txtHost.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtHost.setFont(new Font("Arial", Font.PLAIN, 24));
        txtHost.setForeground(Color.BLACK);
        txtHost.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));

        // Join game button
        JButton btnJoinStart = new JButton("Join Game");
        btnJoinStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJoinStart.setFont(new Font("Arial", Font.PLAIN, 24));
        btnJoinStart.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));
        btnJoinStart.setBackground(Color.RED);
        btnJoinStart.setForeground(Color.WHITE);
        btnJoinStart.setFocusPainted(false);
        btnJoinStart.addActionListener(e -> {
            // JOIN THE GAME
            System.out.println("JOINING GAME");
            setScreen(SCREEN_WAIT_MENU);
            game.setPlaying(true);
            game.joinGame(txtHost.getText(), txtName.getText());
        });

        // Cancel join button
        JButton btnJoinCancel = new JButton("Cancel");
        btnJoinCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJoinCancel.setFont(new Font("Arial", Font.PLAIN, 24));
        btnJoinCancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));
        btnJoinCancel.setBackground(Color.RED);
        btnJoinCancel.setForeground(Color.WHITE);
        btnJoinCancel.setFocusPainted(false);
        btnJoinCancel.addActionListener(e -> {
            // CANCEL THE GAME JOINING
            System.out.println("CANCEL JOIN");
            setScreen(SCREEN_MAIN_MENU);
        });

        joinMenuPanel.add(lblTitle);
        joinMenuPanel.add(lblName);
        joinMenuPanel.add(txtName);
        joinMenuPanel.add(lblHost);
        joinMenuPanel.add(txtHost);
        joinMenuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        joinMenuPanel.add(btnJoinStart);
        joinMenuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        joinMenuPanel.add(btnJoinCancel);
        joinMenuPanel.add(Box.createRigidArea(new Dimension(0, 24)));
    }

    public void initWaitMenu() {
        joinWaitPanel = new JPanel();
        joinWaitPanel.setBackground(Color.BLACK);
        joinWaitPanel.setLayout(new BoxLayout(joinWaitPanel, BoxLayout.Y_AXIS));

        // Game title
        JLabel lblTitle = new JLabel("UNO - Online");
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitle.setForeground(Color.YELLOW);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(64, 32, 48, 32));

        // Players label
        lblPlayersJoin = new JLabel("Waiting for players to join... 1/20");
        lblPlayersJoin.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPlayersJoin.setFont(new Font("Arial", Font.PLAIN, 24));
        lblPlayersJoin.setForeground(Color.WHITE);
        lblPlayersJoin.setBorder(BorderFactory.createEmptyBorder(0, 32, 32, 32));

        // Cancel join button
        JButton btnJoinCancel = new JButton("Cancel");
        btnJoinCancel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJoinCancel.setFont(new Font("Arial", Font.PLAIN, 24));
        btnJoinCancel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE),
                BorderFactory.createEmptyBorder(8, 32, 8, 32)));
        btnJoinCancel.setBackground(Color.RED);
        btnJoinCancel.setForeground(Color.WHITE);
        btnJoinCancel.setFocusPainted(false);
        btnJoinCancel.addActionListener(e -> {
            // CANCEL THE GAME JOINING
            System.out.println("CANCEL JOIN");
            setScreen(SCREEN_MAIN_MENU);
            game.mainPlayer.notifyPlayerQuit();
            game.setPlaying(false);
        });

        joinWaitPanel.add(lblTitle);
        joinWaitPanel.add(lblPlayersJoin);
        joinWaitPanel.add(btnJoinCancel);
        joinWaitPanel.add(Box.createRigidArea(new Dimension(0, 24)));
    }

    public void initGameUI() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.BLACK);

        // Player status label
        lblGameStatus = new JLabel("Please wait...");
        lblGameStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblGameStatus.setFont(new Font("Arial", Font.PLAIN, 24));
        lblGameStatus.setHorizontalAlignment(SwingConstants.CENTER);
        lblGameStatus.setForeground(Color.YELLOW);
        lblGameStatus.setBorder(BorderFactory.createEmptyBorder(64, 32, 48, 32));

        // Middle panel
        middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        middlePanel.setBackground(Color.BLACK);

        // Current card
        gameCurrentCard = new Card(GameState.COLOR_RED, GameState.TYPE_NUMBER, 0);

        // Deck card button
        Card gameDeckCard = new Card(GameState.COLOR_BLACK, GameState.TYPE_NUMBER, 1);
        gameDeckCard.setIcon(new ImageIcon("res/UNO.png"));
        gameDeckCard.setBackground(Color.ORANGE);
        gameDeckCard.addActionListener(e -> game.drawCard());

        // Players hand panel
        JScrollPane gameHandScroller = new JScrollPane();
        gameHandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gameHandScroller.setViewportView(gameHandPanel);
        gameHandScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        gameHandScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        gameHandScroller.getHorizontalScrollBar().setUnitIncrement(16);
        gameHandPanel.setBackground(Color.GRAY);

        gamePanel.add(lblGameStatus, BorderLayout.NORTH);
        middlePanel.add(gameCurrentCard);
        middlePanel.add(Box.createRigidArea(new Dimension(24, 150)));
        middlePanel.add(gameDeckCard);
        gamePanel.add(middlePanel, BorderLayout.CENTER);
        gamePanel.add(gameHandScroller, BorderLayout.SOUTH);
    }

    public synchronized void updateNumPlayers(int numPlayers) {
        lblPlayersHost.setText("Waiting for players to join... "+numPlayers+"/20");
        lblPlayersJoin.setText("Waiting for players to join... "+numPlayers+"/20");
    }

}
