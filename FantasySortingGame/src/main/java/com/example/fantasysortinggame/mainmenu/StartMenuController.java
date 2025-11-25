package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class StartMenuController {

    private final Database database;
    private Stage stage;

    @FXML
    private TextField gameNameField;
    @FXML
    private ToggleButton startNewGameButton;
    @FXML
    private ToggleButton loadSavedGameButton;
    @FXML
    private ImageView menuImageView;

    private String gameMode = "default";

    public StartMenuController(Database database) {
        this.database = database;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Opens the start menu in a new Stage
     */
    public static void showStartMenu(Database database) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    StartMenuController.class.getResource("/path/to/StartMenu.fxml")
            );
            loader.setControllerFactory(param -> new StartMenuController(database));
            Parent root = loader.load();

            StartMenuController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Start Menu");
            stage.setScene(new Scene(root));
            stage.show();

            controller.setStage(stage);
            controller.attachButtonHandlers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Attaches actions to the buttons
     */
    private void attachButtonHandlers() {
        startNewGameButton.setOnAction(e -> onStartGameButtonClickHandler(true));
        loadSavedGameButton.setOnAction(e -> onStartGameButtonClickHandler(false));
    }

    /**
     * Handles starting or loading a game
     */
    private void onStartGameButtonClickHandler(boolean isNewGame) {
        String gameName = gameNameField.getText().trim();
        if (gameName.isEmpty()) {
            System.out.println("Please enter a game name!");
            return;
        }

        database.loadFromFile(gameName, gameMode);
        System.out.println("Loaded game: " + gameName + " (Mode: " + gameMode + ")");
        if (stage != null) stage.close();
    }

    /**
     * Updates the game mode text (example: called from UI or toggle)
     */
    public void onDisplayGameModeButton(String mode) {
        this.gameMode = mode;
        System.out.println("Game mode set to: " + mode);
    }
}
