package NeuronNetwork;

import java.text.DecimalFormat;

public class Neuron {
    public double sum;
    public double value;
    public double error;

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("####.###");
        return "Neuron(value:" + df.format(value) + ", sum:" + df.format(sum) + ", error:" + df.format(error) + ")";
    }

    static public double[] getNeuronValues(Neuron[] neurons) {
        double[] values = new double[neurons.length];
        for(int i=0; i<neurons.length; i++) {
            values[i] = neurons[i].value;
        }
        return values;
    }
}
