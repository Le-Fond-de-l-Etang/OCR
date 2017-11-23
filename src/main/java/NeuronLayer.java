public class NeuronLayer {
    public Neuron[] neurons;

    public NeuronLayer(int neuronCount) {
        neurons = new Neuron[neuronCount];
        for (int i=0; i<neuronCount; i++) {
            neurons[i] = new Neuron();
        }
    }
}
