import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OptimizerUI extends JFrame {

    private JLabel[] threadLabels;
    private JLabel[][] bestLabels;
    private JLabel threadInfoLabel;
    private JLabel bestInfoLabel;

    private JButton startBtn;
    private JButton resetBtn;

    public int bestScore = 0;
    public ArrayList<Integer> doneThreads = new ArrayList<>();

    private FactoryOptimizer optimizer;

    public OptimizerUI(FactoryOptimizer optimizer) {
        super("Factory Optimizer");

        this.optimizer = optimizer;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        constructUI();
        pack();
        setVisible(true);
        setSize(768, 512);
    }

    /**
     * Adds UI elements to the window
     */
    public void constructUI() {
        Container windowPanel = getContentPane();

        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        JPanel controlPanel = new JPanel(new GridLayout(1, 2));

        JPanel threadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel bestPanel = new JPanel(new GridLayout(FactoryOptimizer.FLOOR_WIDTH, FactoryOptimizer.FLOOR_HEIGHT));

        threadInfoLabel = new JLabel("Threads: 0/"+FactoryOptimizer.NUM_THREADS);
        bestInfoLabel = new JLabel("Best Floor: 0");
        infoPanel.add(threadInfoLabel);
        infoPanel.add(bestInfoLabel);

        startBtn = new JButton("start");
        resetBtn = new JButton("stop");
        resetBtn.setEnabled(false);
        controlPanel.add(startBtn);
        controlPanel.add(resetBtn);

        windowPanel.add(infoPanel, BorderLayout.PAGE_START);
        windowPanel.add(contentPanel, BorderLayout.CENTER);
        windowPanel.add(controlPanel, BorderLayout.PAGE_END);

        contentPanel.add(threadPanel);
        contentPanel.add(bestPanel);

        // Set up the list of labels for each thread
        threadLabels = new JLabel[FactoryOptimizer.NUM_THREADS];
        for(int i=0; i<FactoryOptimizer.NUM_THREADS; i++) {
            threadLabels[i] = new JLabel("1");
            threadLabels[i].setOpaque(true);
            threadLabels[i].setBackground(Color.LIGHT_GRAY);
            threadLabels[i].setHorizontalAlignment(JLabel.CENTER);
            threadPanel.add(threadLabels[i]);
        }

        // Set up the grid of labels for the best floor
        bestLabels = new JLabel[FactoryOptimizer.FLOOR_WIDTH][FactoryOptimizer.FLOOR_HEIGHT];
        for(int x=0; x<FactoryOptimizer.FLOOR_WIDTH; x++) {
            for(int y=0; y<FactoryOptimizer.FLOOR_HEIGHT; y++) {
                bestLabels[x][y] = new JLabel("0");
                bestLabels[x][y].setOpaque(true);
                bestLabels[x][y].setBackground(Color.WHITE);
                bestLabels[x][y].setHorizontalAlignment(JLabel.CENTER);
                bestPanel.add(bestLabels[x][y]);
            }
        }

        // Make the start button reset and then start the optimization
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optimizer.resetThreads();
                resetInfo();
                optimizer.startThreads();
                startBtn.setEnabled(false);
                resetBtn.setEnabled(true);
                resetBtn.setText("stop");
            }
        });

        // Make the reset button able to stop/reset the optimization
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                optimizer.resetThreads();
                if (resetBtn.getText().compareTo("reset") == 0) {
                    resetInfo();
                    resetBtn.setEnabled(false);
                }
                resetBtn.setText("reset");
                startBtn.setEnabled(true);
            }
        });
    }


    /**
     * Resets all info displayed on the GUI
     */
    public void resetInfo() {
        bestScore = 0;
        doneThreads.clear();

        // Reset the info panel labels
        threadInfoLabel.setText("Threads: 0/"+FactoryOptimizer.NUM_THREADS);
        bestInfoLabel.setText("Best Floor: 0");

        // Reset the thread list labels
        for(int i=0; i<FactoryOptimizer.NUM_THREADS; i++) {
            threadLabels[i].setText("1");
            threadLabels[i].setBackground(Color.LIGHT_GRAY);
        }

        // Reset the grid of best floor labels
        for(int x=0; x<FactoryOptimizer.FLOOR_WIDTH; x++) {
            for (int y = 0; y < FactoryOptimizer.FLOOR_HEIGHT; y++) {
                bestLabels[x][y].setText("0");
                bestLabels[x][y].setBackground(Color.WHITE);
            }
        }
    }

    /**
     * Updates the best factory floor displayed on the GUI
     * @param best
     */
    public void updateBest(FactoryFloor best) {
        // Make sure to only update when the optimizer is still running
        if (optimizer.isRunning()) {
            // Double check that the new best is actually better
            if (best.getTotalFitness() > bestScore) {

                bestScore = best.getTotalFitness();
                System.out.println("Best Floor: "+bestScore);

                // Update the info panel and the grid of best floor labels
                bestInfoLabel.setText("Best Floor: " + bestScore);
                for (int x = 0; x < FactoryOptimizer.FLOOR_WIDTH; x++) {
                    for (int y = 0; y < FactoryOptimizer.FLOOR_HEIGHT; y++) {
                        bestLabels[x][y].setText("" + best.getAt(x, y));
                        switch (best.getAt(x, y)) {
                            case (0):
                                bestLabels[x][y].setBackground(Color.WHITE);
                                break;
                            case (1):
                                bestLabels[x][y].setBackground(Color.BLUE);
                                break;
                            case (2):
                                bestLabels[x][y].setBackground(Color.RED);
                                break;
                            case (3):
                                bestLabels[x][y].setBackground(Color.GREEN);
                                break;
                            default:
                                bestLabels[x][y].setBackground(Color.BLACK);
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates the generation information on a thread label
     * @param id the id of the thread
     * @param gen the generation of the genetic algorithm
     */
    public void updateThread(int id, int gen) {
        int generation = Math.min(gen, OptimizerThread.GEN_MAX);
        threadLabels[id].setText("" + generation);
    }

    /**
     * Updates the generation information on a thread label and
     * keeps track of finished/failed threads
     * @param id the id of the thread
     * @param gen the generation of the genetic algorithm
     */
    public void updateFinished(int id, int gen) {
        // Only update if it hasn't finished before
        if (!doneThreads.contains(id)) {
            int generation = Math.min(gen, OptimizerThread.GEN_MAX);
            threadLabels[id].setText("" + generation);

            doneThreads.add(id);

            // Make the label green if the thread completed, red if it failed
            if (generation >= OptimizerThread.GEN_MAX) {
                threadLabels[id].setBackground(Color.GREEN);
            } else {
                threadLabels[id].setBackground(Color.RED);
            }

            // Update the number of finished threads and print to console
            threadInfoLabel.setText("Threads: " + doneThreads.size() + "/" + FactoryOptimizer.NUM_THREADS);
            System.out.println("Thread " + id + " done! (" + doneThreads.size() + "/" + FactoryOptimizer.NUM_THREADS + " threads)");

            // Stop the optimizer when all threads are done
            if (doneThreads.size() >= FactoryOptimizer.NUM_THREADS) {
                optimizer.resetThreads();
                startBtn.setEnabled(true);
                resetBtn.setEnabled(true);
                resetBtn.setText("reset");
            }
        }
    }
}
