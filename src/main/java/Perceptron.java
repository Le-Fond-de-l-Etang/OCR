public class Perceptron {
    private double inputWeights[];
    private double outputWeights[];

    private NeuronLayer inputLayer;
    private NeuronLayer hiddenLayer;
    private NeuronLayer outputLayer;

    public Perceptron(int inputSize, int hiddenSize, int outputSize) {
        inputLayer = new NeuronLayer(inputSize);
        inputWeights = new double[inputSize * hiddenSize];
        hiddenLayer = new NeuronLayer(hiddenSize);
        outputWeights = new double[hiddenSize * outputSize];
        outputLayer = new NeuronLayer(outputSize);
        initializeWeights();
    }



    /**
     * Initialise aléatoirement les poids des neurones
     */
    private void initializeWeights() {
        for(int i=0; i<inputWeights.length; i++) {
            inputWeights[i] = Math.random();
        }
        for(int i=0; i<outputWeights.length; i++) {
            outputWeights[i] = Math.random();
        }
    }

    /**
     * Fixe artificiellement les poids pour tester
     * @param inputWeights Poids des neurones d'entrée
     * @param outputWeights Poids relatifs aux neurones de sorties
     */
    public void setWeights(double[] inputWeights, double[] outputWeights) {
        this.inputWeights = inputWeights;
        this.outputWeights = outputWeights;
    }



    /**
     * Fonction de propagation qui recalcule les sommes et les valeurs des neurones à partir des poids
     */
    private void calculate(double inputNeuronValues[]) {
        int x = 0;
        for (double inputNeuronValue : inputNeuronValues) {
            inputLayer.neurons[x].value = inputNeuronValue;
            x++;
        }
        for (int j = 0; j< hiddenLayer.neurons.length; j++) {
            hiddenLayer.neurons[j].sum = 0;
            for (int i = 0; i< inputLayer.neurons.length; i++) {
                hiddenLayer.neurons[j].sum += inputLayer.neurons[i].value * inputWeights[j* inputLayer.neurons.length + i];
            }
            hiddenLayer.neurons[j].value = transfert(hiddenLayer.neurons[j].sum);
        }
        for (int j = 0; j< outputLayer.neurons.length; j++) {
            outputLayer.neurons[j].sum = 0;
            for (int i = 0; i< hiddenLayer.neurons.length; i++) {
                outputLayer.neurons[j].sum += hiddenLayer.neurons[i].value * outputWeights[j* hiddenLayer.neurons.length + i];
            }
            outputLayer.neurons[j].value = transfert(outputLayer.neurons[j].sum);
        }
    }

    /**
     * Calcule les marges d'erreurs
     * @param expectedOutput Valeurs de sortie cibles
     */
    private void calculateErrorMargins(double[] expectedOutput) {
        for (int i = 0; i< outputLayer.neurons.length; i++) {
            outputLayer.neurons[i].error = (expectedOutput[i] - outputLayer.neurons[i].value);
        }
        for (int i = 0; i< hiddenLayer.neurons.length; i++) {
            hiddenLayer.neurons[i].error = 0;
            for (int j = 0; j< outputLayer.neurons.length; j++) {
                hiddenLayer.neurons[i].error += outputWeights[j* hiddenLayer.neurons.length+i] * outputLayer.neurons[j].error;
            }
        }
    }

    /**
     * Recalcule les poids en fonction des marges d'erreur
     */
    private void recalculateWeights() {
        for (int i = 0; i< hiddenLayer.neurons.length; i++) {
            for (int j = 0; j< inputLayer.neurons.length; j++) {
                inputWeights[i* inputLayer.neurons.length+j] -= 0.5 * hiddenLayer.neurons[i].error * transfertDerivative(hiddenLayer.neurons[i].sum) * inputLayer.neurons[j].value;
            }
        }
        for (int i = 0; i< outputLayer.neurons.length; i++) {
            for (int j = 0; j< hiddenLayer.neurons.length; j++) {
                outputWeights[i* hiddenLayer.neurons.length+j] -= 0.5 * outputLayer.neurons[i].error * transfertDerivative(outputLayer.neurons[i].sum) * hiddenLayer.neurons[j].value;
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
        calculate(input);
        calculateErrorMargins(expectedOutput);
        recalculateWeights();
        return outputLayer.neurons;
    }



    /**
     * Fonction de transfert
     * @param input Somme des produits des poids par les valeurs de neurones
     * @return Valeur du neurone de sortie
     */
    private double transfert(double input) {
        double denominateur = 1 + Math.exp(-input);
        return 1/denominateur;
    }

    /**
     * Dérivée de la fonction de transfert
     * @param input Somme des produits des poids par les valeurs de neurones
     * @return Dérivée
     */
    private double transfertDerivative(double input) {
        double nominateur = -Math.exp(-input);
        double denominateur = Math.pow(1 + Math.exp(-input), 2);
        return nominateur / denominateur;
    }
}
