package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;

public class StartMenuController {
    private final Database database;

    String gameMode = "default";

    public StartMenuController(Database database) {
        this.database = database;
    }

    void onStartGameButtonClickHandler() {
        /*
            ask the user for a file name
            database.LoadFile(userInput, gameMode)}
         */
    }

    void onDisplayDisplayGameModeButton(String gameMode) {
        /*
            // Probably going to have to navigate to where the currenct game mode button is and then grab the text.
            // then set it accordingly.
            gameMode = gameMode
        */
    }

    void displayStartMenu() {
        /*
            // This part can wait until Kayla's done.
            Should make a new stage using the start menu FXML
         */
    }
}