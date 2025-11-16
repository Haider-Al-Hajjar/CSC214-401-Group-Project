package com.example.fantasysortinggame.database;

import com.example.fantasysortinggame.datatypes.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
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

    void LoadFromFile(String fileName) {
        /*
            if no file
                create new file
                run tutorial from tutorial handler class
         */
    }

    void SaveToFile() {
        if (fileName == null || fileName.isEmpty()) {
            fileName = "saveFile.json";
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(this, writer);
            System.out.println("Saved everything to " + fileName);
        } catch (IOException e) {
            System.out.println("Couldn't save file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // this is a test case;
        Database db = new Database();
        db.setFileName("saveFile.json");
        db.setDay(3);
        db.setSeed(9876);

        ArrayList<Upgrade> upgrades = new ArrayList<>();
        upgrades.add(new Upgrade("Sharper Blades", 200, false));
        db.setAllUpgrades(upgrades);

        db.SaveToFile();
    }


}
