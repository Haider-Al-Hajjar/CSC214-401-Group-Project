package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import com.example.fantasysortinggame.gamephasemanager.GamePhaseManager;

public class FantasySortingGameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Database database = new Database();
        // Show main menu first
        StartMenuController.showStartMenu(database, () -> {
            GamePhaseManager manager = new GamePhaseManager(database, stage);
            manager.startDayCycle();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}