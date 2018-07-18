package NeuronNetwork;

public class Perceptron {
    private double LEARNING_STEP = 0.15;

    private double[][] weights;

    private NeuronLayer inputLayer;
    private NeuronLayer hiddenLayer[];
    private NeuronLayer outputLayer;

    /**
     * Initialise un réseau de neurones
     * @param inputSize Taille de la couche de neurones d'entrées
     * @param hiddenSizes Tableau de tailles de la ou des couches de neurones cachées
     * @param outputSize Taille de la couche de neurones de sortie
     */
    public Perceptron(int inputSize, int[] hiddenSizes, int outputSize) {
        inputLayer = new NeuronLayer(inputSize);
        hiddenLayer = new NeuronLayer[hiddenSizes.length];
        for (int i=0; i<hiddenSizes.length; i++) {
            hiddenLayer[i] = new NeuronLayer(hiddenSizes[i]);
        }
        outputLayer = new NeuronLayer(outputSize);
        weights = new double[hiddenSizes.length+1][];
        weights[0] = new double[inputSize * hiddenSizes[0]];
        for (int i=1; i<hiddenSizes.length; i++) {
            weights[i] = new double[hiddenSizes[i-1] * hiddenSizes[i]];
        }
        weights[hiddenSizes.length] = new double[hiddenSizes[hiddenSizes.length-1] * outputSize];
        initializeWeights();
    }



    /**
     * Initialise aléatoirement les poids des neurones
     */
    private void initializeWeights() {
        for (int i=0; i<weights.length; i++) {
            for (int j=0; j<weights[i].length; j++) {
                weights[i][j] = Math.random()/100;
            }
        }
    }



    /**
     * Fonction de propagation qui recalcule les sommes et les valeurs des neurones à partir des poids
     * @param inputNeuronValues Nouvelle couche de neurones d'entrée
     */
    public Neuron[] propagate(double inputNeuronValues[]) {
        int x = 0;
        for (double inputNeuronValue : inputNeuronValues) {
            inputLayer.neurons[x].value = inputNeuronValue;
            x++;
        }
        calculateLayer(inputLayer, hiddenLayer[0], weights[0]);
        for (int i=1; i<hiddenLayer.length; i++) {
            calculateLayer(hiddenLayer[i-1], hiddenLayer[i], weights[i]);
        }
        calculateLayer(hiddenLayer[hiddenLayer.length-1], outputLayer, weights[weights.length-1]);
        return outputLayer.neurons;
    }

    /**
     * Fonction de propagation entre deux couches précises
     * @param inLayer Couche d'entrée
     * @param outLayer Couche de sortie
     * @param weights Poids applicables entre la couche d'entrée et de sortie
     */
    private void calculateLayer(NeuronLayer inLayer, NeuronLayer outLayer, double[] weights) {
        for (int j = 0; j< outLayer.neurons.length; j++) {
            outLayer.neurons[j].sum = 0;
            for (int i = 0; i< inLayer.neurons.length; i++) {
                outLayer.neurons[j].sum += inLayer.neurons[i].value * weights[j* inLayer.neurons.length + i];
            }
            outLayer.neurons[j].value = transfert(outLayer.neurons[j].sum);
        }
    }


    /**
     * Calcule les marges d'erreurs du réseau
     * @param expectedOutput Valeurs de sortie cibles
     */
    private void retropropagateErrorMargins(double[] expectedOutput) {
        for (int i = 0; i< outputLayer.neurons.length; i++) {
            outputLayer.neurons[i].error = (expectedOutput[i] - outputLayer.neurons[i].value);
        }
        for (int i=hiddenLayer.length-1; i>=1; i--) {
            calculateErrorMargins(hiddenLayer[i-1], hiddenLayer[i], weights[i]);
        }
        calculateErrorMargins(hiddenLayer[hiddenLayer.length-1], outputLayer, weights[weights.length-1]);
    }

    /**
     * Calcule les marges d'erreurs entre deux couches
     * @param inLayer Couche d'entrée
     * @param outLayer Couche de sortie
     * @param weights Poids applicables entre les deux couches
     */
    private void calculateErrorMargins(NeuronLayer inLayer, NeuronLayer outLayer, double[] weights) {
        for (int i = 0; i< inLayer.neurons.length; i++) {
            inLayer.neurons[i].error = 0;
            for (int j = 0; j< outLayer.neurons.length; j++) {
                inLayer.neurons[i].error += weights[j* inLayer.neurons.length+i] * outLayer.neurons[j].error;
            }
        }
    }


    /**
     * Recalcule les poids en fonction des marges d'erreur
     */
    private void retroPropagateWeights() {
        recalculateWeights(weights[0], inputLayer, hiddenLayer[0]);
        for (int i=1; i<hiddenLayer.length; i++) {
            recalculateWeights(weights[i], hiddenLayer[i-1], hiddenLayer[i]);
        }
        recalculateWeights(weights[weights.length-1], hiddenLayer[hiddenLayer.length-1], outputLayer);
    }

    /**
     * Recalcule les poids entre deux couches à partir de la couche de sortie
     * @param weights Poids à recalculer
     * @param inLayer Couche de neurones d'entrée
     * @param outLayer Couche de neurones de sortie
     */
    private void recalculateWeights(double[] weights, NeuronLayer inLayer, NeuronLayer outLayer) {
        for (int i = 0; i< outLayer.neurons.length; i++) {
            for (int j = 0; j< inLayer.neurons.length; j++) {
                weights[i* inLayer.neurons.length+j] += LEARNING_STEP * outLayer.neurons[i].error * transfertDerivative(outLayer.neurons[i].sum) * inLayer.neurons[j].value;
            }
        }
    }



    /**
     * Effectue un cycle d'apprentissage
     * @param input Tableau d'entrées
     * @param expectedOutput Sortie attendue
     * @return Nouvelle sortie
     */
    public Neuron[] learn(double[] input, double[] expectedOutput) {
        propagate(input);
        retropropagateErrorMargins(expectedOutput);
        retroPropagateWeights();
        return outputLayer.neurons;
    }



    /**
     * Fonction de transfert
     * @param input Somme des produits des poids par les valeurs de neurones
     * @return Valeur du neurone de sortie
     */
    private double transfert(double input) {
        double denominateur = 1.00 + Math.exp(-input);
        return 1.00/denominateur;
    }

    /**
     * Dérivée de la fonction de transfert
     * @param input Somme des produits des poids par les valeurs de neurones
     * @return Dérivée
     */
    private double transfertDerivative(double input) {
        double nominateur = Math.exp(-input);
        double denominateur = Math.pow(1 + Math.exp(-input), 2);
        return nominateur / denominateur;
    }
}
