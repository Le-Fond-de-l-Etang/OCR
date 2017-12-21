package Application;

import NeuronNetwork.Neuron;
import NeuronNetwork.Perceptron;
import Recognition.CharacterMapping;
import Recognition.FontCharacterExtractor;
import Recognition.ImageReader;

import java.util.Map;

public class OCRApplication {
    public static int INPUT_WIDTH = 40;
    public static int INPUT_HEIGHT = 48;
    public static int INPUT_SIZE = INPUT_WIDTH * INPUT_HEIGHT;
    public static int OUTPUT_SIZE = 10;

    public static void main(String[] args) {
        Perceptron perceptron = new Perceptron(INPUT_SIZE, new int[]{100}, OUTPUT_SIZE);

        Map<Character, double[]> learningArrays = FontCharacterExtractor.readAllFonts();

        System.out.println("Learning stuff...");
        for (int i=0; i<50000; i++) {
            for (Map.Entry<Character, double[]> entry : learningArrays.entrySet()) {
                if (entry.getValue().length == INPUT_SIZE) {
                    double[] expectedOutput = CharacterMapping.getArrayForCharacter(entry.getKey());
                    Neuron[] outputNeurons = perceptron.learn(entry.getValue(), expectedOutput);
                }
            }
        }
        System.out.println("Learning finished !");

        Map<Character, double[]> testingArrays = ImageReader.readImagesFromFolder("./images/", INPUT_WIDTH, INPUT_HEIGHT);
        for (Map.Entry<Character, double[]> entry : testingArrays.entrySet()) {
            if (entry.getValue().length == INPUT_SIZE) {
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
