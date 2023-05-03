import javafx.scene.input.MouseButton;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class TestNetwork {

    float[][][] inputs = new float[3][11][];
    float[][] outputs = new float[11][];
    NeuronNetwork network;

    float[] inputGrid = new float[64];
    GUI gui;

    public static void main(String[] args) {
        new TestNetwork();
    }

    public TestNetwork() {

        //Load the training data
        for(int i=0;i<11;i++) for(int j=0;j<3;j++)
            inputs[j][i] = loadImage(i+"_"+j+".png");

        //Define the answers to the training data
        outputs[0] = new float[]{1,0,0,0,0,0,0,0,0,0};
        outputs[1] = new float[]{0,1,0,0,0,0,0,0,0,0};
        outputs[2] = new float[]{0,0,1,0,0,0,0,0,0,0};
        outputs[3] = new float[]{0,0,0,1,0,0,0,0,0,0};
        outputs[4] = new float[]{0,0,0,0,1,0,0,0,0,0};
        outputs[5] = new float[]{0,0,0,0,0,1,0,0,0,0};
        outputs[6] = new float[]{0,0,0,0,0,0,1,0,0,0};
        outputs[7] = new float[]{0,0,0,0,0,0,0,1,0,0};
        outputs[8] = new float[]{0,0,0,0,0,0,0,0,1,0};
        outputs[9] = new float[]{0,0,0,0,0,0,0,0,0,1};
        outputs[10] = new float[]{0,0,0,0,0,0,0,0,0,0};

        //Set up a multi-layer neural network with 64 inputs, 10 hidden neurons, 10 output, and two layers
        network = new NeuronNetwork(64,64,10,3);

        //Run the neural network program with a GUI
        withGUI();

        //Train the network 1000 times using the training data
        System.out.println("Training....");
        network.learn(1000, 1.0f, inputs, outputs);
        System.out.println("Done Training!");

        //Give the gui the final weights to display
        gui.giveWeights(network.weights);

        //Run the neural network program with no GUI
        noGUI();
    }

    public void withGUI() {
        gui = new GUI(this);

        float[] outputs = new float[10];
        inputGrid = new float[64];
        for(int i=0;i<64;i++)
            inputGrid[i] = 1.0f;
        for(int i=0;i<10;i++)
            outputs[i] = 0.0f;
        gui.setInputs(inputGrid);
        gui.setOutputs(outputs);
    }

    public void noGUI() {
        float[] results;
        float certainty = 0.0f;

        //Tally the % certainty of each digit recognition
        for(int i=0;i<10;i++) {
            results = network.calculate(inputs[0][i]);
            certainty += results[i];
        }

        //Print the % error of the network
        System.out.println("Error: "+String.format("%.2f", 100-(certainty*10))+"%");

        //Test the network with every possible input
        System.out.println("\nResults:   0     1     2     3     4     5     6     7     8     9");
        for(int i=0;i<10;i++) {
            results = network.calculate(inputs[0][i]);
            //Print the guess % for each digit
            System.out.println("Predict "+i+": " + String.format("%.2f", results[0]) + ", " + String.format("%.2f", results[1]) + ", " + String.format("%.2f", results[2]) + ", " + String.format("%.2f", results[3]) + ", " + String.format("%.2f", results[4]) + ", " + String.format("%.2f", results[5]) + ", " + String.format("%.2f", results[6]) + ", " + String.format("%.2f", results[7]) + ", " + String.format("%.2f", results[8]) + ", " + String.format("%.2f", results[9]));
        }
    }

    private float[] loadImage(String file) {
        BufferedImage image;
        float[] data = {0};

        try {
            //Load an image file
            image = ImageIO.read(new File(file));
            data = new float[image.getWidth() * image.getHeight()];

            //Go through every pixel in the image
            for(int x=0;x<image.getWidth();x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    //Get the blue data of each pixel and clamp it between 0 and 1
                    data[x+(y*image.getWidth())] = (float)((image.getRGB(x,y) & 0xFF)/255.0);
                }
            }
        } catch (Exception e) { }

        return data;
    }

    public void clickOutput(int index) {
        //Load the training data into the input grid
        for(int i=0;i<inputGrid.length;i++)
            inputGrid[i] = inputs[0][index][i];

        //Update the GUI on the new inputs
        gui.setInputs(inputGrid);

        //See what the network thinks of the new inputs
        float[] results = network.calculate(inputGrid);

        //Update the GUI on the new outputs
        gui.setOutputs(results);
    }

    public void clickHidden(int index, int mouseButton) {
        if (mouseButton == MouseEvent.BUTTON1) {
            //Cycle the hidden layer that is visible on the GUI forward
            gui.hiddenLayer++;
            if (gui.hiddenLayer >= network.weights.length)
                gui.hiddenLayer = 0;
        } else {
            //Cycle the hidden layer that is visible on the GUI backward
            gui.hiddenLayer--;
            if (gui.hiddenLayer < 0)
                gui.hiddenLayer = network.weights.length-1;
        }

        //Update the GUI to show the new hidden layer
        gui.giveWeights(network.weights);
    }

    public void clickClear() {
        float[] outputs = new float[10];
        inputGrid = new float[64];

        //Generate starting values for the grid of inputs and outputs
        for(int i=0;i<64;i++)
            inputGrid[i] = 1.0f;
        for(int i=0;i<10;i++)
            outputs[i] = 0.0f;

        //See what the network thinks of an empty input grid
        float[] results = network.calculate(inputGrid);

        //Update the GUI with the new inputs and outputs
        gui.setInputs(inputGrid);
        gui.setOutputs(results);
    }

    public void clickInput(int index, int mouseButton) {
        //Left mouse button to draw, right mouse button to erase
        if (mouseButton == MouseEvent.BUTTON1)
            inputGrid[index] = 0.0f;
        else
            inputGrid[index] = 1.0f;

        //See what the network thinks of the new input
        float[] results = network.calculate(inputGrid);

        //Update the GUI with the new outputs
        gui.setOutputs(results);
    }
}
