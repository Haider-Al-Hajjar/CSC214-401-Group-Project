package com.example.fantasysortinggame.database;

import com.example.fantasysortinggame.datatypes.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Database { // might need to be static? I'm not sure. There should only ever be one of these.
    private String fileName;
    private int day;
    private int seed;
    private ArrayList<Item> usedItems;

    private ArrayList<ArrayList<Item>> allItems;

    private ArrayList<Upgrade> allUpgrades;
    private ArrayList<Upgrade> unboughtUpgrades;
    private ArrayList<Upgrade> boughtUpgrades;
    private ArrayList<QuickTimeEvent> allEvents;
    private ArrayList<Npc> allNpcs;
    private ArrayList<Dialogue> allDialogues;

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

    public ArrayList<Item> getItemsByDayAndSeed(int day, long seed) {
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

    public ArrayList<Upgrade> getUnboughtUpgrades() {
        return unboughtUpgrades;
    }

    public void setUnboughtUpgrades(ArrayList<Upgrade> unboughtUpgrades) {
        this.unboughtUpgrades = unboughtUpgrades;
    }

    public ArrayList<Upgrade> getBoughtUpgrades() {
        return boughtUpgrades;
    }

    public void setBoughtUpgrades(ArrayList<Upgrade> boughtUpgrades) {
        this.boughtUpgrades = boughtUpgrades;
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

    void LoadFromFile(String fileName) {


        this.fileName = "src/main/java/com/example/fantasysortinggame/database/data/saveFile.json";

        File file = new File(this.fileName);

        // If file does not exist: create new default save
        if (!file.exists()) {
            this.day = 0;
            this.seed = (int)(Math.random() * Integer.MAX_VALUE);

            this.usedItems = new ArrayList<>();


            this.allItems = new ArrayList<>();
            for (int i = 0; i < 6; i++)
                this.allItems.add(new ArrayList<>());

            this.allUpgrades = new ArrayList<>();
            this.unboughtUpgrades = new ArrayList<>();
            this.boughtUpgrades = new ArrayList<>();
            this.allEvents = new ArrayList<>();
            this.allNpcs = new ArrayList<>();
            this.allDialogues = new ArrayList<>();

            SaveToFile();
            return;
        }

        // Load existing JSON
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {

            Database loaded = gson.fromJson(reader, Database.class);
            this.day = loaded.day;
            this.seed = loaded.seed;

            this.usedItems = (loaded.usedItems != null) ? loaded.usedItems : new ArrayList<>();

            //  If file has old JSON format with "day1", "day2", etc. -> auto convert
            if (loaded.allItems == null || loaded.allItems.size() == 0) {

                this.allItems = new ArrayList<>();
                for (int i = 0; i < 6; i++)
                    this.allItems.add(new ArrayList<>());

                // get raw JSON for day1/day2/day3 keys
                FileReader rawReader = new FileReader(file);
                var jsonObject = gson.fromJson(rawReader, com.google.gson.JsonObject.class);

                for (int i = 0; i < 6; i++) {
                    String dayKey = "day" + (i + 1);
                    if (jsonObject.has(dayKey)) {
                        ArrayList<Item> converted =
                                gson.fromJson(jsonObject.get(dayKey),
                                        new com.google.gson.reflect.TypeToken<ArrayList<Item>>() {}.getType());
                        this.allItems.set(i, converted);
                    }
                }

            } else {
                // already new format
                this.allItems = loaded.allItems;
            }

            this.allUpgrades = (loaded.allUpgrades != null) ? loaded.allUpgrades : new ArrayList<>();
            this.unboughtUpgrades = (loaded.unboughtUpgrades != null) ? loaded.unboughtUpgrades : new ArrayList<>();
            this.boughtUpgrades = (loaded.boughtUpgrades != null) ? loaded.boughtUpgrades : new ArrayList<>();
            this.allEvents = (loaded.allEvents != null) ? loaded.allEvents : new ArrayList<>();
            this.allNpcs = (loaded.allNpcs != null) ? loaded.allNpcs : new ArrayList<>();
            this.allDialogues = (loaded.allDialogues != null) ? loaded.allDialogues : new ArrayList<>();

        } catch (Exception e) {
            System.out.println("Error loading save file: " + e.getMessage());
        }
    }


    void SaveToFile() {
        if (fileName == null || fileName.isEmpty()) {
            fileName = "saveFile.json";
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            System.out.println("error happened");
        }
    }

}
