public class NeuronLayer {

    double[][] weights;
    int numInputs, numOutputs;

    public NeuronLayer(int numInputs, int numOutputs) {

        this.numInputs = numInputs;
        this.numOutputs = numOutputs;

        weights = new double[numInputs][numOutputs];

        // Go through every input neuron
        for (int inputIndex = 0;inputIndex < numInputs;inputIndex++) {
            // Go through every output neuron connected to the current input neuron
            for (int outputIndex = 0;outputIndex < numOutputs;outputIndex++) {
                // Start the weight at a random spot between -1 to 1
                weights[inputIndex][outputIndex] = (2*Math.random())-1;
            }
        }
    }

    public double[] calculate(double[] inputs) {
        // A place to put the output values
        double[] outputs = new double[numOutputs];

        // Go through every output neuron
        for (int outputIndex = 0;outputIndex < numOutputs;outputIndex++) {
            // Go through every input neuron connected to the current output neuron
            for (int inputIndex = 0;inputIndex < numInputs;inputIndex++) {
                // Calculate the effectiveness of each input neuron on the current output neuron
                outputs[outputIndex] += weights[inputIndex][outputIndex]*inputs[inputIndex];
            }

            // Apply the sigmoid curve to every output to clamp it between 0 and 1
            outputs[outputIndex] = 1 / (1 + Math.exp(-outputs[outputIndex]));
        }

        return outputs;
    }

    public void teach(double[][] change) {
        // Go through every input neuron
        for (int inputIndex = 0;inputIndex < numInputs;inputIndex++) {
            // Go through every output neuron connected to the current input neuron
            for (int outputIndex = 0;outputIndex < numOutputs;outputIndex++) {
                // Change the current weight by the given amount
                weights[inputIndex][outputIndex] += change[inputIndex][outputIndex];
            }
        }
    }

    public void print() {
        // Go through every input neuron
        for (int inputIndex = 0;inputIndex < numInputs;inputIndex++) {
            // Go through every output neuron connected to the current input neuron
            for (int outputIndex = 0;outputIndex < numOutputs;outputIndex++) {
                // Print the current weight between the two neurons
                System.out.println("Weight "+inputIndex+" to "+outputIndex+": "+weights[inputIndex][outputIndex]);
            }
        }
    }
}
