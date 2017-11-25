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
}
