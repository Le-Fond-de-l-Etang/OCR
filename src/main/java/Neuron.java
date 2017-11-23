public class Neuron {
    public double sum;
    public double value;
    public double error;

    @Override
    public String toString() {
        return "Neuron(value:" + value + ", sum:" + sum + ", error:" + error + ")";
    }
}
