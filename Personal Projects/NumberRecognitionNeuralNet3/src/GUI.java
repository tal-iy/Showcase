import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class GUI extends JFrame {

    private final TestNetwork network;

    private JPanel mainPanel;
    private JPanel inputPanel;
    private JPanel hiddenPanel;
    private JPanel outputPanel;

    private JButton[] outputButtons = new JButton[10];
    private JButton clearButton;

    private JPanel[] inputSquares = new JPanel[64];
    private JPanel[] hiddenSquares = new JPanel[64];

    public BufferedImage[] hiddenImages = new BufferedImage[64];
    public int hiddenLayer = 0;

    public GUI(TestNetwork network) {
        this.network = network;

        //GridBag Panel to hold everything
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        getContentPane().add(mainPanel);

        //Constraints to define placement of items within the main Panel
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;

        //Grid Panel to hold the input squares
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8,8,1,1));
        mainPanel.add(inputPanel, c);

        //Panels to represent inputs to the network
        for(int i=0;i<64;i++) {
            final int index = i;
            inputSquares[i] = new JPanel();
            inputSquares[i].setBackground(Color.WHITE);
            inputPanel.add(inputSquares[i]);

            //Make each square clickable
            inputSquares[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    //Update the main program that an input was clicked
                    network.clickInput(index,e.getButton());

                    //Change the color of the input square depending on the mouse button
                    if (e.getButton() == MouseEvent.BUTTON1)
                        inputSquares[index].setBackground(Color.BLACK);
                    else if (e.getButton() == MouseEvent.BUTTON3)
                        inputSquares[index].setBackground(Color.WHITE);
                }
            });
        }

        c.gridx = 1;
        c.gridy = 1;

        //Grid panel to hold the output squares
        hiddenPanel = new JPanel();
        hiddenPanel.setLayout(new GridLayout(8,8,1,1));
        mainPanel.add(hiddenPanel, c);

        //Panels to represent the hidden layer
        for(int i=0;i<64;i++) {
            final int index = i;
            //Give each square an image to draw
            hiddenImages[i] = new BufferedImage(8, 8, BufferedImage.TYPE_BYTE_GRAY);;
            hiddenSquares[i] = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(hiddenImages[index], 0, 0, 61, 62, 0, 0, 8, 8, this);
            }};
            hiddenSquares[i].setBackground(Color.WHITE);
            //Make each square clickable
            hiddenSquares[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    //Update the main program that a hidden square was clicked
                    network.clickHidden(index,e.getButton());
                }
            });
            hiddenPanel.add(hiddenSquares[i]);
        }

        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        //Flow Panel to hold the output buttons
        outputPanel = new JPanel();
        outputPanel.setLayout(new FlowLayout());
        outputPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        mainPanel.add(outputPanel, c);

        //Buttons to show output percentages and for loading input data
        for(int i=0;i<10;i++) {
            final int index = i;
            outputButtons[i] = new JButton(i+": 100%");
            outputButtons[i].setBackground(Color.WHITE);
            outputButtons[i].setForeground(Color.BLACK);
            outputPanel.add(outputButtons[i]);
            outputButtons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    network.clickOutput(index);
                }
            });
        }

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;

        //Button to clear the input data
        clearButton = new JButton("Clear");
        mainPanel.add(clearButton, c);
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                network.clickClear();
            }
        });

        //Window options
        pack();
        setTitle("Number Recognition Neural Network");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1000,600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void giveWeights(float[][][] weights) {
        //Go through every hidden layer square
        for(int index = 0;index < 64;index++) {
            //Go through every x position on the image of the square
            for(int x = 0;x < 8;x++) {
                //Go through every y position on the image of the square
                for(int y = 0;y < 8;y++) {
                    //Change the color of the pixel based on the weight connected to the hidden layer neuron represented by index
                    if (index >= weights[hiddenLayer][0].length) {
                        hiddenImages[index].setRGB(x, y, Color.BLACK.getRGB());
                    } else {
                        hiddenImages[index].setRGB(x, y, (new Color((int) Math.max(Math.min((flatten(weights[hiddenLayer][x + (y * 8)][index]) * 255), 255), 0), (int) Math.max(Math.min((flatten(weights[hiddenLayer][x + (y * 8)][index]) * 255), 255), 0), (int) Math.max(Math.min((flatten(weights[hiddenLayer][x + (y * 8)][index]) * 255), 255), 0))).getRGB());
                    }
                }
            }
        }

        repaint();
    }

    private float flatten(float x) {
        //Flatten x from between negative infinity and positive infinity to between -1 and 1
        return 0.5f * (x / (1 + Math.abs(x))) + 0.5f;
    }

    public void setInputs(float[] inputs) {
        //Update every input square color based on the inputs
        for(int i=0;i<inputSquares.length;i++) {
            inputSquares[i].setBackground(new Color((int)(inputs[i]*255),(int)(inputs[i]*255),(int)(inputs[i]*255)));
        }
    }

    public void setOutputs(float[] outputs) {
        //Update every output button text and color based on the outputs
        for(int i=0;i<outputButtons.length;i++) {
            outputButtons[i].setText(i+": "+(int)(outputs[i]*100)+"%");
            outputButtons[i].setBackground(new Color((int)Math.max(Math.min((outputs[i]*255),255),0),(int)Math.max(Math.min((outputs[i]*255),255),0),(int)Math.max(Math.min((outputs[i]*255),255),0)));
            if (outputs[i] > 0.5)
                outputButtons[i].setForeground(Color.BLACK);
            else
                outputButtons[i].setForeground(Color.WHITE);
        }
    }
}
