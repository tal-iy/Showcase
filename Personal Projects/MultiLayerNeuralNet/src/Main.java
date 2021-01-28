/*
 *   Multi-Layer Neural Network
 *   By Vitaliy Shydlonok
 *   Written 2/2/2019
 *
 *   Based on "Building a simple neural net in Java" by Victor Parmar & Fabian Dreier
 *   https://smalldata.tech/blog/2016/05/03/building-a-simple-neural-net-in-java
 *
 *   And on "A Step by Step Backpropagation Example" by Matt Mazur
 *   https://mattmazur.com/2015/03/17/a-step-by-step-backpropagation-example/
 */

public class Main {

    //Full training data
    //static double[][] inputs = new double[][]{{0, 0, 0}, {0, 0, 1}, {0, 1, 0}, {0, 1, 1}, {1, 0, 0}, {1, 0, 1}, {1, 1, 0}, {1, 1, 1}};
    //static double[][] outputs = new double[][]{{0}, {0}, {0}, {0}, {1}, {1}, {1}, {1}};

    //Even training data
    static double[][] inputs = new double[][]{{0, 0, 0}, {0, 0, 1}, {0, 1, 0}, {1, 0, 0}, {1, 0, 1}, {1, 1, 0}, {1, 1, 1}};
    static double[][] outputs = new double[][]{{0}, {0}, {0}, {1}, {1}, {1}, {1}};

    //Partial training data
    //static double[][] inputs = new double[][]{{0, 0, 1}, {0, 1, 1}, {1, 0, 1}, {1, 1, 1}, {0, 1, 0}};
    //static double[][] outputs = new double[][]{{0}, {0}, {1}, {1}, {0}};

    public static void main(String[] args) {

        //Set up a multi-layer neural network with 3 inputs, 3 hidden neurons, 1 output, and two layers
        NeuronNetwork network = new NeuronNetwork(3,3,1,3);

        System.out.println("Starting weights:");
        for(int i=0;i<network.layers.length;i++) {
            System.out.println("Layer "+i+":");
            network.layers[i].print();
        }

        //Train the network using the training data 1000000 times
        System.out.println("\nTraining...\n");
        network.learn(1000, inputs, outputs);

        System.out.println("Trained weights:");
        for(int i=0;i<network.layers.length;i++) {
            System.out.println("Layer "+i+":");
            network.layers[i].print();
        }

        //Test the network with every possible input
        System.out.println("\nResults:  input    output        expected");
        System.out.println("Predict (0, 0, 0): "+String.format("%.3f",network.calculate(new double[] {0,0,0})[0])+", expected: 0.0");
        System.out.println("Predict (0, 0, 1): "+String.format("%.3f",network.calculate(new double[] {0,0,1})[0])+", expected: 0.0");
        System.out.println("Predict (0, 1, 0): "+String.format("%.3f",network.calculate(new double[] {0,1,0})[0])+", expected: 0.0");
        System.out.println("Predict (0, 1, 1): "+String.format("%.3f",network.calculate(new double[] {0,1,1})[0])+", expected: 0.0");
        System.out.println("Predict (1, 0, 0): "+String.format("%.3f",network.calculate(new double[] {1,0,0})[0])+", expected: 1.0");
        System.out.println("Predict (1, 0, 1): "+String.format("%.3f",network.calculate(new double[] {1,0,1})[0])+", expected: 1.0");
        System.out.println("Predict (1, 1, 0): "+String.format("%.3f",network.calculate(new double[] {1,1,0})[0])+", expected: 1.0");
        System.out.println("Predict (1, 1, 1): "+String.format("%.3f",network.calculate(new double[] {1,1,1})[0])+", expected: 1.0");
    }
}
