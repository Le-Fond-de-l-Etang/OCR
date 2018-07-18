package Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApplication extends Application {
    public static Stage primaryStage;
    private BorderPane rootLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GUIApplication.primaryStage = primaryStage;
        GUIApplication.primaryStage.setTitle("OCR");

        this.initRootLayout();
        this.showPerceptronView();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUIApplication.class.getClassLoader().getResource("RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the person overview inside the root layout.
     */
    public void showPerceptronView() {
        try {
            // Load perceptron view.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GUIApplication.class.getClassLoader().getResource("PerceptronLayout.fxml"));
            AnchorPane perceptronOverview = (AnchorPane) loader.load();

            // Set perceptron view into the center of root layout.
            rootLayout.setCenter(perceptronOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
