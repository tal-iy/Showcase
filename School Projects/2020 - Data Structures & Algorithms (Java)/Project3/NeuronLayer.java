import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NeuronLayer {

    public double[][] weights;
    public int numInputs;
    public int numOutputs;

    public NeuronLayer(int numInputs, int numOutputs) {

        this.numInputs = numInputs;
        this.numOutputs = numOutputs;

        weights = new double[numInputs][numOutputs];

        // Go through every input neuron
        for (int inputIndex = 0;inputIndex < numInputs;inputIndex++) {
            // Go through every output neuron connected to the current input neuron
            for (int outputIndex = 0;outputIndex < numOutputs;outputIndex++) {
                // Start the weight at a number close to 0
                weights[inputIndex][outputIndex] = 0.0001;
            }
        }
    }

    public double[] calculate(double[] inputs) {
        // A place to put the output values
        double[] outputs = new double[numOutputs];

        // Go through every output neuron
        for (int outputIndex = 0; outputIndex < numOutputs; outputIndex++) {
            // Go through every input neuron connected to the current output neuron
            for (int inputIndex = 0; inputIndex < numInputs; inputIndex++) {
                // Calculate the effectiveness of each input neuron on the current output neuron
                outputs[outputIndex] += weights[inputIndex][outputIndex] * inputs[inputIndex];
            }

            // Apply the sigmoid curve to every output to clamp it between 0 and 1
            outputs[outputIndex] = 1 / (1 + Math.exp(-outputs[outputIndex]));
        }

        return outputs;
    }

    public void train(double learnRate, double[][] inputData, double[][] outputData) {

        // Shuffle the input data
        Integer[] setRange = new Integer[inputData.length];
        List<Integer> setList = Arrays.stream(IntStream.rangeClosed(0, inputData.length-1).toArray()).boxed().collect(Collectors.toList());
        Collections.shuffle(setList);
        setList.toArray(setRange);

        // Go through every training set
        for(int set = 0; set < inputData.length; set++) {

            // See what the network thinks of the input set
            double[] outputs = calculate(inputData[setRange[set]]);

            //Create an empty set of adjustments
            double[][] change = new double[numInputs][numOutputs];

            //Go through every output neuron
            for(int outputIndex = 0; outputIndex<numOutputs; outputIndex++) {

                //Calculate the error and sigmoid derivative of every output
                double error = outputData[setRange[set]][outputIndex] - outputs[outputIndex];
                double sigmoidDerivative = outputs[outputIndex] * (1 - outputs[outputIndex]);

                //Go through every input to calculate how much to change it
                for(int inputIndex = 0; inputIndex < numInputs; inputIndex++) {

                    //Calculate the amount to change using change = input * error * sigmoid_derivative(output)
                    weights[inputIndex][outputIndex] += inputData[setRange[set]][inputIndex] * error * sigmoidDerivative * learnRate;
                }
            }
        }

    }

    public boolean test(double[] inputData, double[] outputData) {
        // Get a prediction for a set of data
        double[] outputs = calculate(inputData);

        // Figure out which prediction label has the highest % certainty
        double biggest = 0;
        int answer = -1;
        for (int i=0; i<outputs.length; i++) {
            if (outputs[i] > biggest) {
                biggest = outputs[i];
                answer = i;
            }
        }

        // Figure out which expected label has the highest % certainty
        biggest = 0;
        int expected = -1;
        for (int i=0; i<outputs.length; i++) {
            if (outputData[i] > biggest) {
                biggest = outputData[i];
                expected = i;
            }
        }

        // Return whether the predicted and expected labels are the same
        return (answer == expected && answer != -1 && expected != -1);
    }

    public double getAccuracy(double[][] testingData, double[][] testingLabels) {
        // Count the number of correct predictions
        double numRight = 0;
        for (int i=0; i<testingData.length; i++)
            numRight += test(testingData[i], testingLabels[i]) ? 1:0;

        // Return % accuracy of the network
        return (numRight/testingData.length)*100;
    }

    public void saveNetwork(String fileName) throws Exception {
        // Open the weights file for writing
        File fileWeights = new File(fileName);
        fileWeights.createNewFile();
        FileWriter outWeights = new FileWriter(fileWeights);

        // Save weight value for every input-output pair
        for (int i=0; i<numInputs; i++) {
            for (int j=0; j<numOutputs; j++) {
                outWeights.write(weights[i][j]+"\n");
            }
        }

        outWeights.close();
    }

    public void loadNetwork(String fileName) throws Exception{
        // Make sure the weights file exists
        File reviewFile = new File(fileName);
        if (!reviewFile.exists())
            return;

        // Read the weights file
        BufferedReader reviewReader = new BufferedReader(new FileReader(reviewFile));
        String reviewLine = reviewReader.readLine();
        int word = 0;

        // Read until the end of the file
        while (reviewLine != null) {
            // Go through each label
            for (int j=0; j<numOutputs; j++) {
                // Load the weight for the word and label
                weights[word][j] = new Double(reviewLine);
                reviewLine = reviewReader.readLine();
            }
            word++;
        }

        reviewReader.close();
    }

    public void print() {
        // Go through every input neuron
        for (int inputIndex = 0; inputIndex < numInputs; inputIndex++) {
            // Go through every output neuron connected to the current input neuron
            for (int outputIndex = 0; outputIndex < numOutputs; outputIndex++) {
                // Print the current weight between the two neurons
                System.out.println("Weight "+inputIndex+" to "+outputIndex+": "+weights[inputIndex][outputIndex]);
            }
        }
    }
}
