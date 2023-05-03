public class NeuronNetwork {

    NeuronLayer[] layers;
    double[][] inputs;
    double[][] outputs;

    public NeuronNetwork(int numInputs, int numHidden, int numOutputs, int numLayers) {
        //A place to put the neuron layers
        layers = new NeuronLayer[numLayers];

        //A place to put the input and output values
        inputs = new double[numLayers+1][];
        outputs = new double[numLayers][];

        //Create the input layer
        layers[0] = new NeuronLayer(numInputs, numHidden);
        outputs[0] = new double[numHidden];

        //Create the output layer
        layers[numLayers-1] = new NeuronLayer(numHidden, numOutputs);
        outputs[numLayers-1] = new double[numOutputs];

        //Create the hidden layers
        for(int i=1;i<numLayers-1;i++) {
            layers[i] = new NeuronLayer(numHidden, numHidden);
            outputs[i] = new double[numHidden];
        }
    }

    public void learn(int iterations, double[][] inputData, double[][] targetData) {
        //Train the network a given number of times
        for (int iteration = 0; iteration < iterations; iteration++) {
            //Go through every training set
            for (int set = 0; set < inputData.length; set++) {
                //See what the network thinks of the input set
                calculate(inputData[set]);

                //Go through every layer from start to end
                for (int layerIndex = 0;layerIndex < layers.length;layerIndex++) {
                    //Empty set of adjustments
                    double[][] change = new double[layers[layerIndex].numInputs][layers[layerIndex].numOutputs];

                    //Go through every input on the current layer
                    for(int inputIndex = 0;inputIndex < layers[layerIndex].weights.length;inputIndex++) {
                        //Go through every connection to the current input
                        for(int outputIndex = 0;outputIndex < layers[layerIndex].weights[inputIndex].length;outputIndex++) {
                            //Change = W->NET * NET->OUT
                            change[inputIndex][outputIndex] = inputs[layerIndex][inputIndex] * outputs[layerIndex][outputIndex]*(1-outputs[layerIndex][outputIndex]);
                            //Change = W->NET * NET->OUT * OUT->ERROR[output]
                            change[inputIndex][outputIndex] *= buildError(layerIndex+1, outputIndex, targetData[set]);
                        }
                    }

                    //Teach the current layer
                    layers[layerIndex].teach(change);
                }
            }
        }
    }

    private double buildError(int layerIndex, int inputIndex, double[] targetData) {
        double change = 0;
        //Check if this is the last layer
        if (layerIndex >= layers.length) {
            //Change = ERROR[output]
            change = inputs[layerIndex][inputIndex]-targetData[inputIndex];
        } else {
            //Go through every connection to the current input
            for(int outputIndex = 0;outputIndex < layers[layerIndex].weights[inputIndex].length;outputIndex++) {
                //Change = OUT[input]->NET[output] * NET[output]->OUT[output] * ERROR[output]
                change += buildError(layerIndex+1,outputIndex, targetData)*(outputs[layerIndex][outputIndex]*(1-outputs[layerIndex][outputIndex]))*layers[layerIndex].weights[inputIndex][outputIndex];
            }
        }
        return change;
    }

    public double[] calculate(double[] inputData) {
        //Starting inputs
        inputs[0] = inputData;

        //See what the first layer thinks of the input set
        outputs[0] = inputs[1] = layers[0].calculate(inputs[0]);

        //Go through every neural layer, starting with the second layer
        for (int layerIndex = 1; layerIndex < layers.length; layerIndex++) {
            //See what the current layer thinks of the last layer's output set
            outputs[layerIndex] = inputs[layerIndex+1] = layers[layerIndex].calculate(inputs[layerIndex]);
        }

        return outputs[layers.length-1];
    }
}
