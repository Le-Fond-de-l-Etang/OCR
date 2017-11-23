public class Application {
    public static void main(String[] args) {
        Perceptron perceptron = new Perceptron(2, 3);
        for (int i=0; i<3000; i++) {
            System.out.println("Phase d'apprentissage " + (i+1) + " :");

            double[] input1 = {1, 1};
            double output1 = perceptron.learn(input1, 0);
            System.out.println("Input : " + input1[0] + " - " + input1[1] + " ; Output : " + output1);

            double[] input2 = {0, 1};
            double output2 = perceptron.learn(input2, 1);
            System.out.println("Input : " + input2[0] + " - " + input2[1] + " ; Output : " + output2);

            double[] input3 = {1, 0};
            double output3 = perceptron.learn(input3, 1);
            System.out.println("Input : " + input3[0] + " - " + input3[1] + " ; Output : " + output3);

            double[] input4 = {0, 0};
            double output4 = perceptron.learn(input4, 0);
            System.out.println("Input : " + input4[0] + " - " + input4[1] + " ; Output : " + output4);
        }
    }
}
