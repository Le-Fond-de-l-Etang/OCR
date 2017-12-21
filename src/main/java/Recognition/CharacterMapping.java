package Recognition;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CharacterMapping {
    /** Characters the OCR can handle   **/
    public static char[] recognizedCharacters = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    /**
     * Mapping output neuron number with a character
     */
    private static final Map<Integer, Character> characters = loadCharacters();
    private static Map<Integer, Character> loadCharacters()
    {
        Map<Integer, Character> map = new HashMap<>();
        int neuronNumber = 0;
        for(char character : recognizedCharacters) {
            map.put(neuronNumber, character);
            neuronNumber++;
        }
        return map;
    }

    /**
     * Return output neuron number for a given character
     * @param character
     * @return Position
     */
    private static int getMatchingCharacterKey(char character) {
        int key = -1;
        for (Map.Entry<Integer, Character> entry : characters.entrySet()) {
            if (entry.getValue().equals(character)) {
                key = entry.getKey();
            }
        }
        return key;
    }

    /**
     * Return character corresponding to a given neuron position
     * @param key Position of the neuron
     * @return Character associated with this position
     */
    private static char getMatchingKeyCharacter(int key) {
        if (characters.containsKey(key)) {
            return characters.get(key);
        } else {
            return ' ';
        }
    }

    /**
     * Return an array of 0 fitting the number of existing characters, and set a value corresponding to a character to 1
     * @param character Character to set as 1
     * @return Array of double used as a Perceptron output
     */
    public static double[] getArrayForCharacter(char character) {
        double[] array = new double[characters.size()];
        for (int i=0; i<array.length; i++) {
            array[i] = 0;
        }
        int characterKey = getMatchingCharacterKey(character);
        if (characterKey >= 0) {
            array[characterKey] = 1;
        }
        return array;
    }

    /**
     * Read a Perceptron output array and returns possible characters with matching probability
     * @param array Array from perceptron
     * @return Map of possible characters with probabilities
     */
    public static Map<Double, Character> getCharactersForArray(double[] array) {
        Map<Double, Character> map = new HashMap<>();
        for (int i=0; i<array.length; i++) {
            map.put(array[i], getMatchingKeyCharacter(i));
        }
        return new TreeMap<>(map);
    }
}
