package com.example.fantasysortinggame.buyphase;

import com.example.fantasysortinggame.datatypes.Upgrade;

public interface BuyPhaseController {
    void loadUpgrades();
    /*
        unboughtUpgrades = database.getUnboughtUpgrades
        BuyPhaseUI.buyMenuDisplay()
                for(Upgrade upgrade : unboutUpgrades)
                BuyPhaseUI.buyUpgradeDisplay()
        potentially have something related to the current day count.
     */
    void onBuyUpgradeClick(Upgrade upgrade);
    /*
	database.setGold(database.getGold() - upgrade.getCost())
	upgrade.setIsBought(true)
	BuyPhaseUI.displayUpgradeMenu()
		for (Upgrade upgrade : upgrades)
			if (Upgrade.isBought() != true)
				upgradeDisplay(upgrade)
     */
    void displayBuyMenu();
    void displayUpgrade(Upgrade upgrade);
}
