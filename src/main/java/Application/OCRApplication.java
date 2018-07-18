package Application;

import NeuronNetwork.Neuron;
import NeuronNetwork.Perceptron;
import Recognition.CharacterMapping;
import Recognition.FontCharacterExtractor;
import Recognition.ImageReader;

import java.util.Map;

public class OCRApplication {
    public static int INPUT_WIDTH = 30;
    public static int INPUT_HEIGHT = 36;
    public static int INPUT_SIZE = INPUT_WIDTH * INPUT_HEIGHT;
    public static int HIDDEN_SIZE = 200;
    public static int OUTPUT_SIZE = CharacterMapping.recognizedCharacters.length;
    public static int LEARNING_COUNT = 5000;

    public static void main(String[] args) {
        Perceptron perceptron = new Perceptron(INPUT_SIZE, new int[]{HIDDEN_SIZE}, OUTPUT_SIZE);


        Map<Character, double[]> learningArrays = FontCharacterExtractor.readAllFonts(INPUT_WIDTH, INPUT_HEIGHT);
        Map<Character, double[]> characters = ImageReader.readImagesFromFolder("./images/learn/", INPUT_WIDTH, INPUT_HEIGHT);
        learningArrays.putAll(characters);
        System.out.println("\nLearning phase...");
        for (int i=0; i<LEARNING_COUNT; i++) {
            System.out.print("\rLearning phase "+(i+1)+"/"+LEARNING_COUNT+"...");
            for (Map.Entry<Character, double[]> entry : learningArrays.entrySet()) {
                if (entry.getValue().length == INPUT_SIZE) {
                    double[] expectedOutput = CharacterMapping.getArrayForCharacter(entry.getKey());
                    Neuron[] outputNeurons = perceptron.learn(entry.getValue(), expectedOutput);
                }
            }
        }
        System.out.println("\nLearning finished !\n");

        Map<Character, double[]> testingArrays = ImageReader.readImagesFromFolder("./images/test/", INPUT_WIDTH, INPUT_HEIGHT);
        for (Map.Entry<Character, double[]> entry : testingArrays.entrySet()) {
            if (entry.getValue().length == INPUT_SIZE) {
                double[] expectedOutput = CharacterMapping.getArrayForCharacter(entry.getKey());
                Neuron[] outputNeurons = perceptron.learn(entry.getValue(), expectedOutput);
                Map<Double, Character> matchingCharacters = CharacterMapping.getCharactersForArray(Neuron.getNeuronValues(outputNeurons));
                for(Map.Entry<Double, Character> charEntry : matchingCharacters.entrySet()) {
                    if (charEntry.getKey() > 0.5) {
                        System.out.println("There is a " + charEntry.getKey() + " probability of it being a '" + charEntry.getValue() + "'");
                    }
                }
            }
        }
   }
}
