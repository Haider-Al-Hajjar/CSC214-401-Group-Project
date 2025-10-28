package com.example.fantasysortinggame.mainmenu;

public interface StartMenuController {
    String gameMode = "default";
    void onDisplayStartGameButton();
    /*
    ask the user for a file name
	database.LoadFile(userInput, gameMode)}
     */
    void onDisplayDisplayGameModeButton(String gameMode);
    /*
    gameMode = gameMode
     */
    void displayStartGameButton();

    void displayGameModeButton();
}