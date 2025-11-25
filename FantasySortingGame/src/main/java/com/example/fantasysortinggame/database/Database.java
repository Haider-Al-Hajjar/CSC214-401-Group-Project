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
    private ArrayList<Item> allItems;
    private ArrayList<Upgrade> allUpgrades;
    private ArrayList<Upgrade> unboughtUpgrades;
    private ArrayList<Upgrade> boughtUpgrades;
    private ArrayList<QuickTimeEvent> allEvents;
    private ArrayList<Npc> allNpcs;
    private ArrayList<Dialogue> allDialogues;
    private double gold = 0.0; //(suyog- added this field)

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<Item> getAllItems() {
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
        return allItems;
    }

    public void setAllItems(ArrayList<Item> allItems) {
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

    public void loadFromFile(String fileName, String gameMode) { // added a parameter but didn't update the code to handle the gameMode accordingly.
        this.fileName = fileName;
        File file = new File(fileName);

      //this will check if file exist and if it doesnt it will start tutorial
        //its empty so far
        if (!file.exists()) {
            //starts new data if empty
            this.day = 0;
            this.seed = (int)(Math.random() * Integer.MAX_VALUE);

            this.usedItems = new ArrayList<>();
            this.unboughtUpgrades = new ArrayList<>();
            this.boughtUpgrades = new ArrayList<>();

            this.allItems = new ArrayList<>();
            this.allUpgrades = new ArrayList<>();
            this.allEvents = new ArrayList<>();
            this.allNpcs = new ArrayList<>();
            this.allDialogues = new ArrayList<>();

            // --- Add stock items so SortPhaseController has something to display ---
            ItemType type1 = new ItemType("Magic Weapon");
            ItemType type2 = new ItemType("Potion");
            ItemType type3 = new ItemType("Treasure");

            allItems.add(new Item("Unsorted", null, "A shiny sword", "Sword of Light", type1, false, new ArrayList<>(), new ArrayList<>(), 100));
            allItems.add(new Item("Unsorted", null, "Heals 50 HP", "Healing Potion", type2, false, new ArrayList<>(), new ArrayList<>(), 25));
            allItems.add(new Item("Unsorted", null, "Ancient gold coins", "Gold Coins", type3, false, new ArrayList<>(), new ArrayList<>(), 200));

            // Copy them into usedItems so SortPhaseController can show them
            this.usedItems.addAll(allItems);

            // Then save the new tutorial started game
            saveToFile();
            return;
        }

        // else there is a save file
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(File.class, new FileAdapter())
                .create();

        try (FileReader reader = new FileReader(file)) {
            Database loaded = gson.fromJson(reader, Database.class);

            // copies trh= all the valuhes into this instance
            this.fileName = loaded.fileName;
            this.day = loaded.day;
            this.seed = loaded.seed;
            this.usedItems = loaded.usedItems;
            this.allItems = loaded.allItems;
            this.allUpgrades = loaded.allUpgrades;
            this.unboughtUpgrades = loaded.unboughtUpgrades;
            this.boughtUpgrades = loaded.boughtUpgrades;
            this.allEvents = loaded.allEvents;
            this.allNpcs = loaded.allNpcs;
            this.allDialogues = loaded.allDialogues;



        } catch (Exception e) {
            System.out.println("error happened");
        }
    }


    public void saveToFile() {
        if (fileName == null || fileName.isEmpty()) {
            fileName = "saveFile.json";
        }

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(File.class, new FileAdapter())
                .create();

        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            System.out.println("error happened");
        }
    }

}
