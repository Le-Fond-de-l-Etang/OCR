package JavaFX.Controller;

import NeuronNetwork.Perceptron;

public class PerceptronController {
    private static Perceptron perceptron;

    public static void createPerceptron(int imageWidth, int imageHeight, int hiddenNeuronsCount) {
        PerceptronController.perceptron = new Perceptron(imageWidth, new int[]{imageHeight}, hiddenNeuronsCount);
        System.out.println(String.format("Perceptron creation ! (%d, %d, %d)", imageWidth, imageHeight, hiddenNeuronsCount));
    }
}
