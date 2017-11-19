public class Perceptron {
    private double inputValues[];
    private double inputWeights[];
    private double hiddenSums[];
    private double hiddenValues[];
    private double outputWeights[];
    private double outputSum;
    private double outputValue;

    public Perceptron(int inputSize, int hiddenSize) {
        inputValues = new double[inputSize];
        inputWeights = new double[inputSize * hiddenSize];
        hiddenSums = new double[hiddenSize];
        hiddenValues = new double[hiddenSize];
        outputWeights = new double[hiddenSize];
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
    private void propagate(double input[]) {
        inputValues = input;
        for (int j=0; j<hiddenValues.length; j++) {
            hiddenSums[j] = 0;
            for (int i=0; i<inputValues.length; i++) {
                hiddenSums[j] += inputValues[i] * inputWeights[j*inputValues.length + i];
            }
            hiddenValues[j] = transfert(hiddenSums[j]);
        }
        outputSum = 0;
        for (int j=0; j<outputWeights.length; j++) {
            outputSum += outputWeights[j] * hiddenValues[j];
        }
        outputValue = transfert(outputSum);
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
     * Calcul des marges d'erreurs et recalcul des poids
     * @param expectedOutput Tableau des sorties attendues
     */
    private void correctWeights(double expectedOutput) {
        double outputErrorMargin = transfertDerivative(outputSum) * (expectedOutput - outputValue);
        for(int i=0; i<hiddenValues.length; i++) {
            double deltaWeight = Math.abs(outputErrorMargin/hiddenValues[i]);
            outputWeights[i] = outputWeights[i] - deltaWeight;
        }
        for(int i=0; i<hiddenValues.length; i++) {
            double errorMargin = transfertDerivative(hiddenSums[i]) * (expectedOutput - hiddenValues[i]);
            for(int j=0; j<inputValues.length; j++) {
                if (inputValues[j] != 0) {
                    double deltaWeight = Math.abs(errorMargin / inputValues[j]);
                    inputWeights[i * inputValues.length + j] = inputWeights[i * inputValues.length + j] - deltaWeight;
                }
            }
        }
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

    /**
     * Effectue un cycle d'apprentissage
     * @param input Tableau d'entrées
     * @param expectedOutput Sortie attendue
     * @return Nouvelle sortie
     */
    public double learn(double[] input, double expectedOutput) {
        propagate(input);
        correctWeights(expectedOutput);
        propagate(input);
        return outputValue;
    }
}
