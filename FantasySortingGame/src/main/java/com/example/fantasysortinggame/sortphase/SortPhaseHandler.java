package com.example.fantasysortinggame.sortphase;

public interface SortPhaseHandler { // may need to be static, as I think there should only be one instance of this
    int unsortedItemCount = 0;
    String filter = "";
    String view = "default";
    int day = 1;
    int seed = 0;
    void loadItems(String filter, String view, int day, int seed);
    // get daily items from the database
    // set unsorted item count to the length of items gotten
    // display items based on view and filter
    //
}
