package com.example.fantasysortinggame.salephase;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.datatypes.Item;

import java.util.ArrayList;

public class SalePhaseController {
    private final Database database;
    private ArrayList<Item> usedItems;
    String filter = "";
    String view = "";

    public SalePhaseController(Database database) {
        this.database = database;
        this.usedItems = database.getUsedItems();
    }

    void loadItems() {
        /*
            load items from the database based on which ones have been used but not bought
            display them
         */
    }

    void onChangeFilterButtonClickedHandler(String filter) {
        /*
            // probably gon7na have to navigate to wherever the filter string is to get the string
            // this part can wait until Kayla's done.
            this.filter = filter.
         */

    }

    void onChangeViewButtonClickedHandler(String view) {
        /*
            // probably gonna have to navigate to wherever the view button is to get the string
            // this part can wait until Kayla's done.
            this.view = view.
         */

    }

    void onSellButtonClickedHandler(Item itemClicked) {
        /*
            // probably gonna have to navigate to whever the item name is stored
            // then search for the item in usedItems
            check for story event
                if so, call story event handler
            check for dialogue
                if so, call dialogue handler
            increase money by item cost
            change the item to be sold
            displaySaleMenu(usedItems)
         */


        //comment test
    }

    void displaySaleMenu(ArrayList<Item> usedItems) {
        /*
            // make or update the current stage with the appropriate FXML
            // this part can wait until Kayla's done.
            for (Item item: usedItems)
                if item.isSold != ture;
                display
         */
    }

    void displayItem(Item item, String view) {
        /*
            update the stage with the item given
            change information displayed based on the current view.
         */
    }
}
