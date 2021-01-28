public class NeuronNetwork {

    public final int MEAN_SQUARED = 0;
    public final int CROSS_ENTROPY = 1;

    float[][][] weights;
    float[][] inputs;
    float[][] outputs;
    float[][] errors;

    boolean fastSigmoid;
    int lossType;

    public NeuronNetwork(int numInputs, int numHidden, int numOutputs, int numLayers) {

        //Whether to use the fast activation function
        fastSigmoid = false;

        //Type of loss function to use
        lossType = MEAN_SQUARED;

        //A place to put the input and output values
        weights = new float[numLayers][][];
        inputs = new float[numLayers+1][];
        outputs = new float[numLayers][];
        errors = new float[numLayers][];

        //Create the first layer
        weights[0] = new float[numInputs][numHidden];
        inputs[0] = new float[numInputs];
        outputs[0] = new float[numHidden];
        errors[0] = new float[numHidden];

        //Create the hidden layers
        for(int i=1;i<numLayers-1;i++) {
            weights[i] = new float[numHidden][numHidden];
            inputs[i] = new float[numHidden];
            outputs[i] = new float[numHidden];
            errors[i] = new float[numHidden];
        }

        //Create the last layer
        weights[numLayers-1] = new float[numHidden][numOutputs];
        inputs[numLayers-1] = new float[numHidden];
        outputs[numLayers-1] = new float[numOutputs];
        errors[numLayers-1] = new float[numOutputs];

        //Create the last inputs layer
        inputs[numLayers] = new float[numOutputs];

        //Go through every layer
        for (int layerIndex = 0;layerIndex < numLayers;layerIndex++) {
            //Go through every input neuron
            for (int inputIndex = 0; inputIndex < weights[layerIndex].length; inputIndex++) {
                //Go through every output neuron connected to the current input neuron
                for (int outputIndex = 0; outputIndex < weights[layerIndex][inputIndex].length; outputIndex++) {
                    //Start the weight at a random spot between -1 to 1
                    weights[layerIndex][inputIndex][outputIndex] = (float)((2 * Math.random()) - 1);
                }
            }
        }
    }

    public void learn(int iterations, float learnRate, float[][][] inputData, float[][] targetData) {
        //Train the network a given number of times
        for (int iteration = 0; iteration < iterations; iteration++) {
            //Go through every variation of the training data
            for(int variation = 0;variation < inputData.length;variation++) {
                //Go through every training set
                for (int set = 0; set < inputData[variation].length; set++) {
                    //See what the network thinks of the input set
                    calculate(inputData[variation][set]);

                    //Go through every layer, last to first
                    for(int layerIndex = weights.length-1;layerIndex >= 0;layerIndex--) {
                        //Go through every output on each layer
                        for(int outputIndex = 0;outputIndex < outputs[layerIndex].length;outputIndex++) {
                            //Calculate the sum of the errors that originate from this output
                            float errorTotal = 0;
                            if (layerIndex == weights.length-1) {
                                errorTotal = lossFunction(targetData[set][outputIndex],outputs[weights.length-1][outputIndex]);
                            }
                            else {
                                for(int index=0;index < outputs[layerIndex+1].length;index++) {
                                    //ERROR = SUMn(ERRORn * Wn)
                                    errorTotal += (errors[layerIndex + 1][index] * weights[layerIndex][outputIndex][index]);
                                }
                            }

                            //ERROR = SUMn(Wn * ERRORn) * (OUTPUT * (1-OUTPUT))
                            errors[layerIndex][outputIndex] = errorTotal * sigmoidDerivative(outputs[layerIndex][outputIndex]);

                            //Go through every input connected to the current output
                            for(int inputIndex = 0;inputIndex < inputs[layerIndex].length;inputIndex++) {
                                //CHANGE = LEARN_RATE * INPUT * SUMn(Wn * ERRORn) * (OUTPUT * (1-OUTPUT))
                                weights[layerIndex][inputIndex][outputIndex] -= learnRate * inputs[layerIndex][inputIndex] * errors[layerIndex][outputIndex];
                            }
                        }
                    }
                }
            }
        }
    }

    public float[] calculate(float[] inputData) {
        //Starting inputs
        for(int i=0;i<inputs[0].length;i++)
            inputs[0][i] = inputData[i];

        //Go through every neural layer, starting with the first layer
        for (int layerIndex = 0; layerIndex < weights.length; layerIndex++) {
            //Go through every output neuron
            for (int outputIndex = 0; outputIndex < outputs[layerIndex].length; outputIndex++) {
                //Reset the outputs
                outputs[layerIndex][outputIndex] = 0;

                //Go through every input neuron connected to the current output neuron
                for (int inputIndex = 0; inputIndex < inputs[layerIndex].length; inputIndex++) {
                    //Calculate the effectiveness of each input neuron on the current output neuron
                    outputs[layerIndex][outputIndex] += weights[layerIndex][inputIndex][outputIndex] * inputs[layerIndex][inputIndex];
                }

                //Apply the sigmoid curve to every output to clamp it between 0 and 1
                inputs[layerIndex+1][outputIndex] = outputs[layerIndex][outputIndex] = sigmoid(outputs[layerIndex][outputIndex]);
            }
        }

        return outputs[weights.length-1];
    }

    public void setFast(boolean fast) {
        this.fastSigmoid = fast;
    }

    private float lossFunction(float target, float answer) {
        switch(lossType) {
            case(0):
                return 2*(answer-target);
            case(1):
                if (target > 0.999)
                    return -(float)Math.log(answer);
                else
                    return -(float)Math.log(1-answer);
        }
        return 2*(answer-target);
    }

    private float sigmoid(float x) {
        if (fastSigmoid) return (float)(0.5f * (x / (1 + Math.abs(x))) + 0.5f);
        return (float)(1 / (1 + Math.exp(-x)));
    }

    private float sigmoidDerivative(float x) {
        //if (fast) return (float)((-2*(x-0.5)*(x-0.5))+0.25);
        return (float)(x*(1-x));
    }
}
