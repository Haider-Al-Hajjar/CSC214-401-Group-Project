package com.example.fantasysortinggame.buyphase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Upgrade;
import com.example.fantasysortinggame.mainmenu.SoundEffectController;

public class BuyPhaseController {

    private final Database database;

    public BuyPhaseController(Database database) {
        this.database = database;
    }

    /*
        This method should get all unbought upgrades from the database.
     */
    void loadUpgrades() {
        /*
            unboughtUpgrades = database.getUnboughtUpgrades
            displayBuyMenu(unboughtUpgrades)
            potentially have something related to the current day count.
         */
    }


    /*
        This method should check the database to see if you have enough gold
            if so, decrease gold by said amount and update item to be bought in database
            regardless, redisplay menu.
        No parameters.
        Returns void.
     */
        void onBuyUpgradeClickHandler () {
        /*
            // Until Kayla does her job, this part can stay undone.
            // Basically, you're gonna get the source of the click and then navigate to the name of the object
            // Once you've got the name, then you've gotta search the database to get the Upgrade associated with it
            // Lastly, use it below

            code below isn't perfect but you get the gist
            database.setGold(database.getGold() - upgrade.getCost())
            upgrade.setIsBought(true)
            BuyPhaseUI.displayUpgradeMenu()
                for (Upgrade upgrade : upgrades)
                    if (Upgrade.isBought() != true)
                        upgradeDisplay(upgrade)
         */
        }


        void displayBuyMenu () {
    /*
        // Until kayla does her thing this can stay kind of undone.
        should make a new stage and populate it with an upgrade menu, and then individual upgrades.
        for(Upgrade upgrade : unboutUpgrades)
        displayUpgrade(upgrade)
     */
        }

        void displayUpgrade (Upgrade upgrade){
        /*
            should populate the display with the appropraite fxml.
         */
        }
    }
