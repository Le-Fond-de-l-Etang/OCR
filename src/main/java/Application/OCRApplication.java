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
                }
            }
        }
        for (Map.Entry<Character, double[]> entry : characters.entrySet()) {
            if (entry.getValue().length == 30) {
                double[] expectedOutput = CharacterMapping.getArrayForCharacter(entry.getKey());
                Neuron[] outputNeurons = perceptron.learn(entry.getValue(), expectedOutput);
                System.out.println("Entry character is " + entry.getKey() + ".");
                Map<Double, Character> matchingCharacters = CharacterMapping.getCharactersForArray(Neuron.getNeuronValues(outputNeurons));
                for(Map.Entry<Double, Character> charEntry : matchingCharacters.entrySet()) {
                    if (charEntry.getKey() > 0.5) {
                        System.out.println("There is a " + charEntry.getKey() + " probability of it being a " + charEntry.getValue());
                    }
                }
            }
        }
   }
}
