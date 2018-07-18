package JavaFX.Controller;

import Application.GUIApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RootController {
    private Scene popupScene;

    @FXML
    private TextField imageWidthField;
    @FXML
    private TextField imageHeightField;
    @FXML
    private TextField hiddenSizeField;
    @FXML
    private Button closeAboutButton;

    @FXML
    private void handleCreatePopup() {
        try {
            // Load popup layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUIApplication.class.getClassLoader().getResource("CreatePerceptronPopupLayout.fxml"));
            AnchorPane popupLayout = (AnchorPane) loader.load();
            popupScene = new Scene(popupLayout);
            // Create dialog box
            final Stage dialog = new Stage();
            dialog.setTitle("New perceptron");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(GUIApplication.primaryStage);
            dialog.setScene(popupScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreatePerceptron() {
        String widthString = imageWidthField.getText();
        String heightString = imageHeightField.getText();
        String hiddenSizeString = hiddenSizeField.getText();
        int width = Integer.parseInt(widthString);
        int height = Integer.parseInt(heightString);
        int hiddenSize = Integer.parseInt(hiddenSizeString);
        PerceptronController.createPerceptron(width, height, hiddenSize);
        imageWidthField.getScene().getWindow().hide();
    }

    @FXML
    private void handleShowAbout() {
        try {
            // Load popup layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUIApplication.class.getClassLoader().getResource("AboutLayout.fxml"));
            AnchorPane popupLayout = (AnchorPane) loader.load();
            Scene aboutScene = new Scene(popupLayout);
            // Create dialog box
            final Stage dialog = new Stage();
            dialog.setTitle("About");
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(GUIApplication.primaryStage);
            dialog.setScene(aboutScene);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCloseAbout() {
        closeAboutButton.getScene().getWindow().hide();
    }
}
