import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ThreadLocalRandom;

public class GameWindow extends JFrame implements ActionListener {

    private Flip flip;
    private GridIcon[][] gameGrid = new GridIcon[Flip.GAME_WIDTH][Flip.GAME_HEIGHT];

    public GameWindow(Flip flip) {
        super("Flip Game");

        this.flip = flip;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        constructUI();
        pack();
        setVisible(true);
        setSize(768, 768);
    }

    public void constructUI() {
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());

        final JPanel controlPanel = new JPanel(new FlowLayout());

        // Create the control buttons
        JButton btnSolve = new JButton("Solve");
        btnSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!flip.isComputing()) flip.solve(getGrid());
            }
        });

        JButton btnRandomize = new JButton("Randomize");
        btnRandomize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!flip.isComputing()) randomizeGrid();
            }
        });

        controlPanel.add(btnSolve);
        controlPanel.add(btnRandomize);



        final JPanel gamePanel = new JPanel(new GridLayout(Flip.GAME_HEIGHT,Flip.GAME_WIDTH));

        // Create a grid of icons
        for(int y=0; y<Flip.GAME_HEIGHT; y++) {
            for(int x=0; x<Flip.GAME_WIDTH; x++) {
                GridIcon icon = new GridIcon(x, y);
                icon.addActionListener(this);
                gameGrid[x][y] = icon;
                gamePanel.add(icon);
            }
        }

        pane.add(controlPanel, BorderLayout.NORTH);
        pane.add(gamePanel, BorderLayout.CENTER);
    }

    public void setGrid(long data) {
        String dataString = Long.toBinaryString(data);

        // Loop through the whole grid
        for(int x=0; x<Flip.GAME_WIDTH; x++) {
            for(int y=0; y<Flip.GAME_HEIGHT; y++) {
                // Convert coordinates to an index in the binary string
                int gridIndex = dataString.length()-1-(x+(y*Flip.GAME_WIDTH));
                if (gridIndex < 0) gameGrid[x][y].setValue(false);
                else gameGrid[x][y].setValue(dataString.charAt(gridIndex) == '1');
            }
        }
    }

    public void setSolution(long moves) {
        String movesString = Long.toBinaryString(moves);

        // Loop through the whole grid
        for(int x=0; x<Flip.GAME_WIDTH; x++) {
            for(int y=0; y<Flip.GAME_HEIGHT; y++) {
                // Convert coordinates to an index in the binary string
                int gridIndex = movesString.length()-1-(x+(y*Flip.GAME_WIDTH));
                if (gridIndex < 0) gameGrid[x][y].setSolution(false);
                else gameGrid[x][y].setSolution(movesString.charAt(gridIndex) == '1');
            }
        }
    }

    public void randomizeGrid() {

        // Clear the grid
        for(int x=0; x<Flip.GAME_WIDTH; x++) {
            for(int y=0; y<Flip.GAME_HEIGHT; y++) {
                gameGrid[x][y].setValue(false);
            }
        }

        // Pick a random number of moves
        int numMoves = ThreadLocalRandom.current().nextInt(4,Flip.GAME_WIDTH*Flip.GAME_HEIGHT);

        // Flip a random square a number of times
        for(int i=0; i<numMoves; i++) {
            int x = ThreadLocalRandom.current().nextInt(0, Flip.GAME_WIDTH);
            int y = ThreadLocalRandom.current().nextInt(0, Flip.GAME_HEIGHT);

            // Flip the current icon
            gameGrid[x][y].flip();

            // Flip the surrounding icons
            if (x > 0) gameGrid[x-1][y].flip();
            if (y > 0) gameGrid[x][y-1].flip();
            if (x < Flip.GAME_WIDTH-1) gameGrid[x+1][y].flip();
            if (y < Flip.GAME_HEIGHT-1) gameGrid[x][y+1].flip();
        }
    }

    public long getGrid() {
        // Make a new binary string
        String dataString = "";

        // Loop through the whole grid
        for(int y=0; y<Flip.GAME_HEIGHT; y++) {
            for(int x=0; x<Flip.GAME_WIDTH; x++) {
                // Build onto the binary string
                dataString = (gameGrid[x][y].getValue()? "1" : "0")+dataString;
            }
        }

        // Convert the binary string to an int
        return Long.parseLong(dataString, 2);
    }

    public boolean testWin() {
        boolean win = true;
        // Loop through the whole grid to find any un-flipped squares
        for(int x=0; x<Flip.GAME_WIDTH && win; x++)
            for (int y = 0; y < Flip.GAME_HEIGHT && win; y++)
                if (gameGrid[x][y].getValue()) win = false;
        return win;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (flip.isComputing()) return;

        GridIcon icon = (GridIcon)e.getSource();
        int x = icon.x;
        int y = icon.y;

        // Flip the current icon
        icon.flip();

        // Flip the surrounding icons
        if (x > 0) gameGrid[x-1][y].flip();
        if (y > 0) gameGrid[x][y-1].flip();
        if (x < Flip.GAME_WIDTH-1) gameGrid[x+1][y].flip();
        if (y < Flip.GAME_HEIGHT-1) gameGrid[x][y+1].flip();

        // Remove solutions
        if (icon.isSolution())
            icon.setSolution(false);

        if (testWin()) {
            System.out.println("YOU WIN!");
            Object[] options = {"Play Again", "Quit"};
            int n = JOptionPane.showOptionDialog(this,"YOU WIN!","", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options, options[0]);
            if (n == 0) randomizeGrid();
            else System.exit(0);
        }
    }

    private class GridIcon extends JButton {

        public final int x;
        public final int y;
        private boolean value;
        private boolean solution = false;

        public GridIcon(int x, int y) {
            this.x = x;
            this.y = y;

            setText(x+","+y);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        }

        public void setValue(boolean val) {
            value = val;
            setBackground(value? Color.BLACK : Color.WHITE);
            setForeground(value? Color.WHITE : Color.BLACK);
        }

        public void setSolution(boolean sol) {
            solution = sol;
            if (sol) {
                setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                setForeground(Color.RED);
            }
            else {
                setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
                setForeground(value? Color.WHITE : Color.BLACK);
            }
        }

        public boolean getValue() {
            return value;
        }

        public boolean isSolution() { return solution; }

        public void flip() {
            value = !value;
            setBackground(value? Color.BLACK : Color.WHITE);
            if (!solution)
                setForeground(value? Color.WHITE : Color.BLACK);
        }
    }
}
