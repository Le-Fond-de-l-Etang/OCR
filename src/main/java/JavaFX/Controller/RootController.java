package JavaFX.Controller;

import Application.GUIApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RootController {
    private Scene popupScene;

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
        TextField imageWidthField = (TextField) popupScene.lookup("#imageWidth");
        PerceptronController.createPerceptron(imageWidthField.getText());
    }

    @FXML
    private void handleShowAbout() {

    }
}
