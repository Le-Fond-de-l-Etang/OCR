package Application;

import NeuronNetwork.Neuron;
import NeuronNetwork.Perceptron;
import Recognition.CharacterMapping;
import Recognition.ImageReader;

import java.util.Map;

public class OCRApplication {
    public static void main(String[] args) {
        Perceptron perceptron = new Perceptron(30, 25, 10);

        ImageReader imageReader = new ImageReader();
        Map<Character, double[]> characters = imageReader.readImagesFromFolder("./images/");

        for (int i=0; i<5000; i++) {
            for (Map.Entry<Character, double[]> entry : characters.entrySet()) {
                if (entry.getValue().length == 30) {
                    double[] expectedOutput = CharacterMapping.getArrayForCharacter(entry.getKey());
                    Neuron[] outputNeurons = perceptron.learn(entry.getValue(), expectedOutput);
                    System.out.println(entry.getKey() + " : " + CharacterMapping.getCharactersForArray(Neuron.getNeuronValues(outputNeurons)));
                }
            }
        }
   }
}
