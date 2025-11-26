package com.example.fantasysortinggame.database;

import com.example.fantasysortinggame.datatypes.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Database {
    private String fileName;
    private int day;
    private int seed;
    private ArrayList<Item> usedItems;


    private ArrayList<ArrayList<Item>> allItems;

    private ArrayList<Upgrade> allUpgrades;

    private ArrayList<QuickTimeEvent> allEvents;
    private ArrayList<Npc> allNpcs;
    private ArrayList<Dialogue> allDialogues;
    private String gameMode;
    private double gold = 0.0;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<ArrayList<Item>> getAllItems() {
        return allItems;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<Item> getUsedItems() {
        return usedItems;
    }

    public void setUsedItems(ArrayList<Item> usedItems) {
        this.usedItems = usedItems;
    }

    // Return list for that day
    public ArrayList<Item> getItemsByDayAndSeed() {
     /*
            returns items based on current day and starting seed
            if day = {a specific day where complications occur}
                return even old items and change their currentSort
            else
                select a random batch of items based on the day and the seed
                add these items to usedItems
                return these items.
         */
        return allItems.get(day - 1);
    }

    public void setAllItems(ArrayList<ArrayList<Item>> allItems) {
        this.allItems = allItems;
    }

    public ArrayList<Upgrade> getAllUpgrades() {
        return allUpgrades;
    }

    public void setAllUpgrades(ArrayList<Upgrade> allUpgrades) {
        this.allUpgrades = allUpgrades;
    }


    public ArrayList<QuickTimeEvent> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(ArrayList<QuickTimeEvent> allEvents) {
        this.allEvents = allEvents;
    }

    public ArrayList<Npc> getAllNpcs() {
        return allNpcs;
    }

    public void setAllNpcs(ArrayList<Npc> allNpcs) {
        this.allNpcs = allNpcs;
    }

    public ArrayList<Dialogue> getAllDialogues() {
        return allDialogues;
    }

    public void setAllDialogues(ArrayList<Dialogue> allDialogues) {
        this.allDialogues = allDialogues;
    }

    //Suyog - added fer more getters and setters
    public double getGold(){
        return  gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public void addGold(double amount){
        this.gold += amount;

    }

    // ================================================================
    //                          LOAD FILE
    // ================================================================
    public void loadFromFile(String fileName, String gameMode) {

        // Always use your real save path (ignore argument)
        this.fileName = "src/main/java/com/example/fantasysortinggame/database/data/saveFile.json";

        File file = new File(this.fileName);

        // If file does not exist → create default database
        if (!file.exists()) {
            createDefaultSave();
            saveToFile();
            return;
        }

        Gson gson = new Gson();

        try {
            // First pass: load normally
            FileReader reader = new FileReader(file);
            Database loaded = gson.fromJson(reader, Database.class);
            reader.close();

            this.day = loaded.day;
            this.seed = loaded.seed;

            if (loaded.usedItems != null) {
                this.usedItems = loaded.usedItems;
            } else {
                this.usedItems = new ArrayList<Item>();
            }

            // Detect if "allItems" is missing (old JSON format)
            boolean hasNewFormat = loaded.allItems != null && !loaded.allItems.isEmpty();

            if (!hasNewFormat) {

                // Build new empty 6-day structure
                this.allItems = new ArrayList<ArrayList<Item>>();
                int i;
                for (i = 0; i < 6; i++) {
                    this.allItems.add(new ArrayList<Item>());
                }

                // Second pass: raw JSON to detect day1/day2/day3
                FileReader rawReader = new FileReader(file);
                JsonObject jsonObject = gson.fromJson(rawReader, JsonObject.class);
                rawReader.close();

                if (jsonObject.has("allItems") && jsonObject.get("allItems").isJsonObject()) {

                    JsonObject oldItems = jsonObject.getAsJsonObject("allItems");

                    for (i = 0; i < 6; i++) {
                        String key = "day" + (i + 1);
                        if (oldItems.has(key)) {
                            ArrayList<Item> list = gson.fromJson(
                                    oldItems.get(key),
                                    new TypeToken<ArrayList<Item>>() {}.getType()
                            );
                            this.allItems.set(i, list);
                        }
                    }
                }

            } else {
                this.allItems = loaded.allItems;
            }

            // Load upgrades, events, npcs, dialogues safely
            if (loaded.allUpgrades != null) {
                this.allUpgrades = loaded.allUpgrades;
            } else {
                this.allUpgrades = new ArrayList<Upgrade>();
            }

            if (loaded.allEvents != null) {
                this.allEvents = loaded.allEvents;
            } else {
                this.allEvents = new ArrayList<QuickTimeEvent>();
            }

            if (loaded.allNpcs != null) {
                this.allNpcs = loaded.allNpcs;
            } else {
                this.allNpcs = new ArrayList<Npc>();
            }

            if (loaded.allDialogues != null) {
                this.allDialogues = loaded.allDialogues;
            } else {
                this.allDialogues = new ArrayList<Dialogue>();
            }

        } catch (Exception e) {
            System.out.println("Error loading save file: " + e.getMessage());
        }
    }

    // Create default empty database
    private void createDefaultSave() {

        this.day = 0;
        this.seed = (int) (Math.random() * Integer.MAX_VALUE);

        this.usedItems = new ArrayList<Item>();

        this.allItems = new ArrayList<ArrayList<Item>>();
        int i;
        for (i = 0; i < 6; i++) {
            this.allItems.add(new ArrayList<Item>());
        }

        this.allUpgrades = new ArrayList<Upgrade>();
        this.allEvents = new ArrayList<QuickTimeEvent>();
        this.allNpcs = new ArrayList<Npc>();
        this.allDialogues = new ArrayList<Dialogue>();
    }

    // ================================================================
    //                          SAVE FILE
    // ================================================================
    public void saveToFile() {
        if (fileName == null || fileName.isEmpty()) {
            fileName = "saveFile.json";
        }
        final String SAVE_DIR = "src/main/java/com/example/fantasysortinggame/database/data/";
        File file = new File(SAVE_DIR + fileName);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            FileWriter writer = new FileWriter(fileName);
            gson.toJson(this, writer);
            writer.close();
        } catch (IOException e) {
            System.out.println("error happened");
        }
    }
}
