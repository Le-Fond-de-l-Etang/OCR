public class Application {
    public static void main(String[] args) {
        Perceptron perceptron = new Perceptron(2, 3, 1);
        for (int i=0; i<30000; i++) {
            System.out.println("Phase d'apprentissage " + (i+1) + " :");

            double[] input1 = {1, 1};
            double[] output1 = {0};
            Neuron[] result1 = perceptron.learn(input1, output1);
            System.out.println(input1[0] + "-" + input1[1] + " => " + result1[0]);

            double[] input2 = {0, 1};
            double[] output2 = {1};
            Neuron[] result2 = perceptron.learn(input2, output2);
            System.out.println(input2[0] + "-" + input2[1] + " => " + result2[0]);

            double[] input3 = {1, 0};
            double[] output3 = {1};
            Neuron[] result3 = perceptron.learn(input3, output3);
            System.out.println(input3[0] + "-" + input3[1] + " => " + result3[0]);

            double[] input4 = {0, 0};
            double[] output4 = {0};
            Neuron[] result4 = perceptron.learn(input4, output4);
            System.out.println(input4[0] + "-" + input4[1] + " => " + result4[0]);
        }
    }
}
