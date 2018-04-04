package JavaFX.Controller;

import NeuronNetwork.Perceptron;
import Recognition.CharacterMapping;
import Recognition.FontCharacterExtractor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    Canvas drawCanvas;

    private static PerceptronController perceptronController;
    private static Perceptron perceptron;
    private static int imageWidth, imageHeight;

    public static void createPerceptron(int imageWidth, int imageHeight, int hiddenNeuronsCount) {
        PerceptronController.perceptron = new Perceptron(imageWidth * imageHeight, new int[]{hiddenNeuronsCount}, CharacterMapping.recognizedCharacters.length);
        PerceptronController.imageWidth = imageWidth;
        PerceptronController.imageHeight = imageHeight;
        perceptronController.learnButton.setDisable(false);
        perceptronController.fontListPane.getChildren().clear();
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
        Map<Character, double[]> learningArrays = FontCharacterExtractor.readFont(selectedFont, PerceptronController.imageWidth, PerceptronController.imageHeight);
        for (int i=0; i<learningCount; i++) {
            for (Map.Entry<Character, double[]> entry : learningArrays.entrySet()) {
                if (entry.getValue().length == PerceptronController.imageWidth * PerceptronController.imageHeight) {
                    double[] expectedOutput = CharacterMapping.getArrayForCharacter(entry.getKey());
                    perceptron.learn(entry.getValue(), expectedOutput);
                }
            }
        }
        Text fontText = new Text(selectedFont + " - " + learningCount);
        fontText.setY(fontListPane.getChildren().size() * 20 + 20);
        fontListPane.getChildren().addAll(fontText);
    }

    private void prepareCanvas() {
        final GraphicsContext graphicsContext = drawCanvas.getGraphicsContext2D();
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
                            graphicsContext.setLineWidth(drawCanvas.getHeight()/12);
                        } else if (event.getButton() == MouseButton.SECONDARY) {
                            graphicsContext.setStroke(Color.WHITE);
                            graphicsContext.setLineWidth(drawCanvas.getHeight()/6);
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
}
