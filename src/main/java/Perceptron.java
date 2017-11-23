public class Perceptron {
    private double inputValues[];
    private double inputWeights[];
    private double hiddenSums[];
    private double hiddenValues[];
    private double hiddenErrorMargin[];
    private double outputWeights[];
    private double outputSum;
    private double outputValue;
    private double outputErrorMargin;

    public Perceptron(int inputSize, int hiddenSize) {
        inputValues = new double[inputSize];
        inputWeights = new double[inputSize * hiddenSize];
        hiddenSums = new double[hiddenSize];
        hiddenValues = new double[hiddenSize];
        outputWeights = new double[hiddenSize];
        hiddenErrorMargin = new double[hiddenSize];
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
    private void calculate(double input[]) {
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

    private void calculateErrorMargins(double expectedOutput) {
        outputErrorMargin = expectedOutput - outputValue;
        for (int i=0; i<hiddenValues.length; i++) {
            hiddenErrorMargin[i] = outputWeights[i] * outputErrorMargin;
        }
        /*for (int i=0; i<inputValues.length; i++) {
            inputErrorMargin[i] = 0;
            for (int j=0; j<hiddenValues.length; j++) {
                inputErrorMargin[i] += inputWeights[j*inputValues.length+i] * hiddenErrorMargin[j];
            }
        }*/
    }

    private void recalculateWeights() {
        for (int i=0; i<hiddenValues.length; i++) {
            for (int j=0; j<inputValues.length; j++) {
                inputWeights[i*inputValues.length+j] -= 0.5 * hiddenErrorMargin[i] * transfertDerivative(hiddenSums[i]) * inputValues[j];
            }
        }
        for (int i=0; i<hiddenValues.length; i++) {
            outputWeights[i] -= 0.5 * outputErrorMargin * transfertDerivative(outputSum) * hiddenValues[i];
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
        calculate(input);
        calculateErrorMargins(expectedOutput);
        recalculateWeights();
        return outputValue;
    }
}
