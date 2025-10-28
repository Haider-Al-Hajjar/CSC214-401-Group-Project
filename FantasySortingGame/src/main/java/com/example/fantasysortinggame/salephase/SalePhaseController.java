package com.example.fantasysortinggame.salephase;

import com.example.fantasysortinggame.datatypes.Item;

public interface SalePhaseController {
    String filter = "";
    String view = "";
    void loadItems();
    void onChangeFilterButtonClicked(String filter);
    void onChangeViewButtonClicked(String view);
    void onSellButtonClicked(Item itemClicked);
    void displaySaleMenu();
    void displayItem(Item item, String view);
}
