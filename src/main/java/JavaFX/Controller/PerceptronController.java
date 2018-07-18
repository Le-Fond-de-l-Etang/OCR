package JavaFX.Controller;

import NeuronNetwork.Neuron;
import NeuronNetwork.Perceptron;
import Recognition.CharacterMapping;
import Recognition.FontCharacterExtractor;
import Recognition.ImageReader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class PerceptronController {
    @FXML
    AnchorPane fontListPane;
    @FXML
    ChoiceBox fontSelector;
    @FXML
    TextField learningCountField;
    @FXML
    Button learnButton;
    @FXML
    ProgressBar learnProgress;
    @FXML
    Canvas drawCanvas;
    @FXML
    Button guessButton;
    @FXML
    Text guessText;

    private static PerceptronController perceptronController;
    private static Perceptron perceptron;
    private static int imageWidth, imageHeight;

    public static void createPerceptron(int imageWidth, int imageHeight, int hiddenNeuronsCount) {
        PerceptronController.perceptron = new Perceptron(imageWidth * imageHeight, new int[]{hiddenNeuronsCount,hiddenNeuronsCount}, CharacterMapping.recognizedCharacters.length);
        PerceptronController.imageWidth = imageWidth;
        PerceptronController.imageHeight = imageHeight;
        perceptronController.learnButton.setDisable(false);
        perceptronController.fontListPane.getChildren().clear();
        perceptronController.guessButton.setDisable(false);
        perceptronController.guessText.setText("Draw a character and hit the Guess button.");
        perceptronController.drawCanvas.setWidth(imageWidth);
        perceptronController.drawCanvas.setHeight(imageHeight);
        perceptronController.prepareCanvas();
        System.out.println(String.format("Perceptron creation ! (%d, %d, %d)", imageWidth, imageHeight, hiddenNeuronsCount));
    }

    @FXML
    private void initialize()
    {
        perceptronController = this;
        populateFonts();
        prepareCanvas();
    }

    private void populateFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] allFonts = ge.getAllFonts();
        List<String> fontStrings = new ArrayList<>();
        for (Font font : allFonts) {
            String fontName = font.getFontName(Locale.US);
            if (!fontName.contains("Italic") && !fontName.contains("Bold") && !fontName.contains("Noto")) {
                fontStrings.add(fontName);
            }
        }
        ObservableList fontList = FXCollections.observableList(fontStrings);
        fontSelector.setItems(fontList);
    }

    @FXML
    private void handleLearn() {
        String learningCountString = learningCountField.getText();
        int learningCount = Integer.parseInt(learningCountString);
        String selectedFont = (String)fontSelector.getValue();
        System.out.println(String.format("Learning phase ! (%s, %d)", selectedFont, learningCount));
        // Learning phase
        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() {
                Map<Character, double[]> learningArrays = FontCharacterExtractor.readFont(selectedFont, PerceptronController.imageWidth, PerceptronController.imageHeight);
                for (int i=0; i<learningCount; i++) {
                    learnProgress.setProgress((double)++i / (double)learningCount);
                    for (Map.Entry<Character, double[]> entry : learningArrays.entrySet()) {
                        if (entry.getValue().length == PerceptronController.imageWidth * PerceptronController.imageHeight) {
                            double[] expectedOutput = CharacterMapping.getArrayForCharacter(entry.getKey());
                            perceptron.learn(entry.getValue(), expectedOutput);
                        }
                    }
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Text fontText = new Text(selectedFont + " - " + learningCount);
                        fontText.setY(fontListPane.getChildren().size() * 20 + 20);
                        fontText.setX(20);
                        fontListPane.getChildren().addAll(fontText);
                    }
                });
                return 0;
            }
        };
        new Thread(task).start();
    }

    private void prepareCanvas() {
        final GraphicsContext graphicsContext = drawCanvas.getGraphicsContext2D();
        graphicsContext.setLineWidth(drawCanvas.getHeight()/15);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, drawCanvas.getWidth(), drawCanvas.getHeight());
        drawCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.stroke();

                    }
                });
        drawCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            graphicsContext.setStroke(Color.BLACK);
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            graphicsContext.setStroke(Color.WHITE);
                        } else {
                            return;
                        }
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                        graphicsContext.closePath();
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                    }
                });
        drawCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.lineTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                        graphicsContext.closePath();
                    }
                });
    }

    @FXML
    private void handleClear() {
        prepareCanvas();
    }

    @FXML
    private void handleGuess() {
        System.out.println("Guessing time !");
        WritableImage writableImage = new WritableImage((int)drawCanvas.getWidth(), (int)drawCanvas.getHeight());
        drawCanvas.snapshot(null, writableImage);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        double[] imageArray = ImageReader.transformImageToArray(bufferedImage);
        Neuron[] outputNeurons = perceptron.propagate(imageArray);
        NavigableMap<Double, Character> matchingCharacters = CharacterMapping.getCharactersForArray(Neuron.getNeuronValues(outputNeurons));
        Map<Double,Character> sortedMap = new TreeMap<>();
        /** Transform char list to string*/
        matchingCharacters.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.naturalOrder())).skip(matchingCharacters.size()-3)
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

        DecimalFormat df = new DecimalFormat("##.##");


        List<String> letters = new ArrayList<>();
        Map.Entry<Double,Character> FirstEntry = sortedMap.entrySet().stream().findFirst().get();

        sortedMap.forEach((aDouble, character) ->{  letters.add(character + "("+ df.format(aDouble) +") "); System.out.println(character + "("+ df.format(aDouble) +")");});


        if (matchingCharacters.lastEntry().getKey()>= 0.75) {
            guessText.setText("Your character is a " +  matchingCharacters.lastEntry().getValue()+ "(" + df.format(matchingCharacters.lastEntry().getKey()) + ")");
        } else if (matchingCharacters.lastEntry().getKey()>= 0.5) {
            guessText.setText("Your character is probably  " + String.join(" or ", letters));
        } else if (matchingCharacters.lastEntry().getKey() >= 0.25) {
            guessText.setText("Your character might be  "  + String.join(" or ", letters));
        } else {
            guessText.setText("Your character could be "  + String.join(" or ", letters));
        }
    }
}
