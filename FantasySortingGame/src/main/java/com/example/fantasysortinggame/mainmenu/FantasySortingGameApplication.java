package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FantasySortingGameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Database database = new Database();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fantasysortinggame/fxmlfiles/startMenu.fxml"));
        loader.setControllerFactory(param -> new StartMenuController(database));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Start Menu");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
