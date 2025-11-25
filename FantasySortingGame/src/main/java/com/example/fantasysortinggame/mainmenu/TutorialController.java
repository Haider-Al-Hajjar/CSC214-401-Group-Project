package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;

/**
 * Handles displaying the tutorial images to the player.
 */
public class TutorialController {

    private final Database database;

    @FXML private ImageView tutorialImageView;
    @FXML private Button nextButton;

    private Stage stage;
    private List<Image> tutorialImages;
    private int currentIndex = 0;

    public TutorialController(Database database) {
        this.database = database;
    }

    /** Assigns stage so we can control it */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /** Launches the tutorial in a new Stage */
    public static void showTutorial(Database database, List<Image> images) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    TutorialController.class.getResource("/path/to/Tutorial.fxml")
            );
            loader.setControllerFactory(param -> new TutorialController(database));
            Parent root = loader.load();

            TutorialController controller = loader.getController();
            controller.tutorialImages = images;

            Stage stage = new Stage();
            stage.setTitle("Tutorial");
            stage.setScene(new Scene(root));
            stage.show();

            controller.setStage(stage);
            controller.displayTutorial();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Displays the first tutorial image */
    public void displayTutorial() {
        if (tutorialImages == null || tutorialImages.isEmpty()) return;
        currentIndex = 0;
        tutorialImageView.setImage(tutorialImages.get(currentIndex));

        nextButton.setOnAction(e -> onNextButtonHandler());
    }

    /** Advances to the next tutorial image */
    public void onNextButtonHandler() {
        currentIndex++;
        if (currentIndex >= tutorialImages.size()) {
            // Close tutorial at the end
            if (stage != null) stage.close();
            return;
        }
        tutorialImageView.setImage(tutorialImages.get(currentIndex));
    }
}
