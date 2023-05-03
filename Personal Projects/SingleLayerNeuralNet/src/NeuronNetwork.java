public class NeuronNetwork {

    NeuronLayer layer;
    double[] outputs;

    public NeuronNetwork(NeuronLayer layer) {
        this.layer = layer;
    }

    public void train(int iterations, double[][] inputData, double[][] outputData) {
        //Train the network a given number of times
        for(int iteration = 0;iteration < iterations;iteration++) {
            //Go through every training set
            for(int set = 0;set < inputData.length;set++) {
                //See what the network thinks of the input set
                outputs = layer.calculate(inputData[set]);

                //Create an empty set of adjustments
                double[][] change = new double[layer.numInputs][layer.numOutputs];

                //Go through every output neuron
                for(int outputIndex = 0;outputIndex<layer.numOutputs;outputIndex++) {
                    //Calculate the error and sigmoid derivative of every output
                    double error = outputData[set][outputIndex] - outputs[outputIndex];
                    double sigmoidDerivative = outputs[outputIndex] * (1 - outputs[outputIndex]);

                    //Go through every input to calculate how much to change it
                    for(int inputIndex = 0;inputIndex < layer.numInputs;inputIndex++) {
                        //Calculate the amount to change using change = input * error * sigmoid_derivative(output)
                        change[inputIndex][outputIndex] = inputData[set][inputIndex] * error* sigmoidDerivative;
                    }
                }

                //Teach the network
                layer.teach(change);
            }
        }
    }

    public double[] calculate(double[] inputData) {
        outputs = layer.calculate(inputData);
        return outputs;
    }
}
