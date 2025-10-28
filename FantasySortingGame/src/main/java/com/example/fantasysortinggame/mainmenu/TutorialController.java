package com.example.fantasysortinggame.mainmenu;

public interface TutorialController {
    void runTutorial();
    /*
        basically go through each phase and explain how the game works
    	SortPhaseHandler.loadItems("unsorted", "default", 1, random.nextInt() )
        after the user confirms their understanding, a button should advance the tutorial
     */
    void displayTutorialPopup();

}
