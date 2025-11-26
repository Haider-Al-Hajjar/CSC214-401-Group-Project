package com.example.fantasysortinggame.database;

import com.example.fantasysortinggame.datatypes.*;
import java.util.ArrayList;

public class DatabaseIntegrityCheck {

    public static void main(String[] args) {
        System.out.println("===== DATABASE DATA INTEGRITY TEST =====");

        Database db = new Database();
        db.LoadFromFile("ignored");

        boolean pass = true;

        // --- Basic Checks ---
        if (db == null) {
            System.out.println("❌ Database is null.");
            return;
        } else {
            System.out.println("✔ Database object created.");
        }

        System.out.println("Day: " + db.getDay());
        System.out.println("Seed: " + db.getSeed());

        System.out.println("Used items: " + (db.getUsedItems() != null ? db.getUsedItems().size() : "NULL"));

        // --- Check allItems ---
        if (db.getAllItems() == null) {
            System.out.println("❌ allItems is null.");
            pass = false;
        } else if (db.getAllItems().size() != 6) {
            System.out.println("❌ allItems does not have 6 days. Found: " + db.getAllItems().size());
            pass = false;
        } else {
            System.out.println("✔ allItems initialized for 6 days.");
            for (int i = 0; i < db.getAllItems().size(); i++) {
                ArrayList<Item> dayItems = db.getAllItems().get(i);
                System.out.println(" Day " + (i + 1) + " item count: " + (dayItems != null ? dayItems.size() : "NULL"));
            }
        }

        // --- Check other lists ---
        if (db.getAllDialogues() == null) {
            System.out.println("❌ allDialogues is null.");
            pass = false;
        } else {
            System.out.println("✔ Dialogues: " + db.getAllDialogues().size());
        }

        if (db.getAllEvents() == null) {
            System.out.println("❌ allEvents is null.");
            pass = false;
        } else {
            System.out.println("✔ Events: " + db.getAllEvents().size());
        }

        if (db.getAllUpgrades() == null) {
            System.out.println("❌ allUpgrades is null.");
            pass = false;
        } else {
            System.out.println("✔ Upgrades: " + db.getAllUpgrades().size());
        }

        if (db.getAllNpcs() == null) {
            System.out.println("❌ allNpcs is null.");
            pass = false;
        } else {
            System.out.println("✔ NPCs: " + db.getAllNpcs().size());
        }

        // --- Verify sample item if available ---
        if (db.getAllItems().size() > 0 && db.getAllItems().get(0).size() > 0) {
            Item sample = db.getAllItems().get(0).get(0);
            System.out.println("\n--- SAMPLE ITEM ---");
            System.out.println("Title: " + sample.getTitle());
            System.out.println("Sort: " + sample.getItemSort());
            System.out.println("Description: " + sample.getDescription());
            if (sample.getItemType() != null)
                System.out.println("ItemType: " + sample.getItemType().toString());
            else
                System.out.println("ItemType: NULL");
        } else {
            System.out.println("⚠ No items found in Day 1.");
        }

        // --- Final verdict ---
        if (pass) {
            System.out.println("\n✅ Database loaded successfully and passed all checks!");
        } else {
            System.out.println("\n❌ Some parts of the database failed to load correctly.");
        }
    }
}
