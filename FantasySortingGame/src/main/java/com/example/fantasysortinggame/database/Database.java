package com.example.fantasysortinggame.database;

import com.example.fantasysortinggame.datatypes.*;
import com.example.fantasysortinggame.gamemodes.GameModeNames;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Database {
    final String SAVE_DIR = "savedfiles";
    private GameModeNames gameMode;
    private String fileName;
    private static final int MAX_DAY = 6;
    private int day;
    private int seed;
    private int mistakes = 0;
    private ArrayList<Item> usedItems;
    private ArrayList<ArrayList<Item>> allItems;
    private ArrayList<Upgrade> allUpgrades;
    private ArrayList<QuickTimeEvent> allEvents;
    private ArrayList<Npc> allNpcs;
    private ArrayList<Dialogue> allDialogues;
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

    public int getDayInBound() {
        return Math.max(day%(MAX_DAY + 1), 1);
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    public ArrayList<Item> getUsedItems() {
        return usedItems;
    }

    public void setUsedItems(ArrayList<Item> usedItems) {
        this.usedItems = usedItems;
    }

    // Return list for that day
    public ArrayList<Item> getItems() {
        ArrayList<Item> dayItems = allItems.get(getDayInBound()-1);

        if (dayItems.isEmpty()) {
            return new ArrayList<>();
        }

        int dayItemFraction = 3;
        int baseCount = Math.max(1, dayItems.size() / dayItemFraction);

        int finalCount = baseCount;
        if (upgradeIsBought("Bottomless Backpack")) {
            double bottomlessBackpackSizeIncreasePercent = 0.20;
            finalCount += (int) Math.ceil(baseCount * bottomlessBackpackSizeIncreasePercent);
        }
        if (finalCount > dayItems.size()) {
            finalCount = dayItems.size();
        }

        java.util.Random rngRandomizer = new java.util.Random(seed + day);
        ArrayList<Item> shuffled = new ArrayList<>(dayItems);
        java.util.Collections.shuffle(shuffled, rngRandomizer);

        ArrayList<Item> result = new ArrayList<>(shuffled.subList(0, finalCount));
        usedItems.addAll(result);

        return result;

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
    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public void addGold(double amount) {
        this.gold += amount;

    }

    public boolean upgradeIsBought(String upgradeName) {
        for (Upgrade u : allUpgrades) {
            if (u.getName().equalsIgnoreCase(upgradeName)) {
                return u.isBought();
            }
        }
        return false;
    }


    // ================================================================
    //                          LOAD FILE
    // ================================================================
    public boolean loadFromFile(String fileName, GameModeNames gameMode) {
        boolean previousFileExists = true;
        this.fileName = fileName; // store the filename first
        File file = getSaveFile(); // now it points to savedfiles/
        // If file does not exist → create default database
        if (!file.exists()) {
            previousFileExists = false;
            createDefaultSave();
            this.gameMode = gameMode;
            this.seed = (int) (Math.random() * 1000);
        }

        Gson gson = new Gson();

        try (FileReader reader = new FileReader(file);) {
            // First pass: load normally
            Database loaded = gson.fromJson(reader, Database.class);

            this.day = loaded.day;
            if (this.seed == 0) {
                this.seed = loaded.seed;
            }
            if (this.gameMode == null) {
                this.gameMode = loaded.gameMode;
            }
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
                                    new TypeToken<ArrayList<Item>>() {
                                    }.getType()
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
        return previousFileExists;
    }

    private File getSaveFile() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) dir.mkdirs(); // create folder if it doesn't exist
        return new File(dir, fileName);
    }

    // Copy masterFile into a new save file
    private void createDefaultSave() {
        Path source = Paths.get("src/main/java/com/example/fantasysortinggame/database/data/masterFile.json");
        Path target = getSaveFile().toPath(); // use helper
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ================================================================
    //                          SAVE FILE
    // ================================================================
    public void saveToFile() {
        if (fileName == null || fileName.isEmpty()) {
            fileName = "saveFile.json";
        }

        File file = getSaveFile();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(this, writer);
            System.out.println("Saved file: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dialogue getTriggeredDialogue() {
        if (allDialogues == null) return null;

        for (Dialogue d : allDialogues) {
            if (!d.hasHappened() && d.shouldTrigger(this)) {
                return d;
            }
        }
        return null;
    }

    public GameModeNames getGameMode() {
        return (this.gameMode == null ? GameModeNames.valueOf("Story") : this.gameMode);
    }

    public void setGameMode(GameModeNames gameMode) {
        this.gameMode = gameMode;
    }

    public int getMaxDay() {
        return MAX_DAY;
    }

    public void setItems(ArrayList<ArrayList<Item>> items) {
        this.allItems = items;
    }
}
