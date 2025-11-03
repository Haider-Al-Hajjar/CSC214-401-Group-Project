package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;

public class TutorialController {
    private final Database database;

    public TutorialController(Database database) {
        this.database = database;
    }

    void runTutorial() {
        /*
            basically display a series of images explaining how the game works.
         */
    }
    void displayTutorial() {
        /*
            should start a new stage with the tutorial fxml
         */
    }

    void onNextButtonHandler() {
        /*
            should figure out which picture is up now and then display the next picture.
         */
    }

}
