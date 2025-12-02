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

/**
 * Represents the main game database for Fantasy Sorting Game.
 * Handles saving, loading, and managing in-game data including items, upgrades, events, NPCs, dialogues, and gold.
 * Supports multiple game modes and tracks player progress over days.
 */
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

    /**
     * Returns the current save file name. * @return the name of the save file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the save file name. * @param fileName the name of the save file to use
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns all items in the game database. * @return list of all items organized by day
     */
    public ArrayList<ArrayList<Item>> getAllItems() {
        return allItems;
    }

    /**
     * Returns the current seed used for randomization. * @return the seed value
     */
    public int getSeed() {
        return seed;
    }

    /**
     * Sets the seed used for randomization. * @param seed the seed value
     */
    public void setSeed(int seed) {
        this.seed = seed;
    }

    /**
     * Returns the current day of the game. * @return the current day
     */
    public int getDay() {
        return day;
    }

    /**
     * Returns the day value wrapped within bounds (1 to MAX_DAY). * @return the day in bound
     */
    public int getDayInBound() {
        return Math.max(day % (MAX_DAY + 1), 1);
    }

    /**
     * Sets the current day of the game. * @param day the day to set
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Returns the number of mistakes made by the player. * @return number of mistakes
     */
    public int getMistakes() {
        return mistakes;
    }

    /**
     * Sets the number of mistakes made by the player. * @param mistakes number of mistakes
     */
    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    /**
     * Returns the list of items the player has already used.
     *
     * @return list of used items
     */
    public ArrayList<Item> getUsedItems() {
        return usedItems;
    }

    /**
     * Sets the list of items the player has already used.
     *
     * @param usedItems list of used items
     */
    public void setUsedItems(ArrayList<Item> usedItems) {
        this.usedItems = usedItems;
    }

    /**
     * Returns the items available for the current day, factoring in upgrades and randomization.
     *
     * @return list of items for the day
     */
    public ArrayList<Item> getItems() {
        ArrayList<Item> dayItems = allItems.get(getDayInBound() - 1);

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

    /**
     * Sets all items in the game database.
     *
     * @param allItems list of all items organized by day
     */
    public void setAllItems(ArrayList<ArrayList<Item>> allItems) {
        this.allItems = allItems;
    }

    /**
     * Returns all upgrades available in the game.
     *
     * @return list of all upgrades
     */
    public ArrayList<Upgrade> getAllUpgrades() {
        return allUpgrades;
    }

    /**
     * Sets all upgrades available in the game.
     *
     * @param allUpgrades list of all upgrades
     */
    public void setAllUpgrades(ArrayList<Upgrade> allUpgrades) {
        this.allUpgrades = allUpgrades;
    }

    /**
     * Returns all Quick Time Events (QTEs) in the game.
     *
     * @return list of all QTEs
     */
    public ArrayList<QuickTimeEvent> getAllEvents() {
        return allEvents;
    }

    /**
     * Sets all Quick Time Events (QTEs) in the game.
     *
     * @param allEvents list of all QTEs
     */
    public void setAllEvents(ArrayList<QuickTimeEvent> allEvents) {
        this.allEvents = allEvents;
    }

    /**
     * Returns all NPCs in the game.
     *
     * @return list of all NPCs
     */
    public ArrayList<Npc> getAllNpcs() {
        return allNpcs;
    }

    /**
     * Sets all NPCs in the game.
     *
     * @param allNpcs list of all NPCs
     */
    public void setAllNpcs(ArrayList<Npc> allNpcs) {
        this.allNpcs = allNpcs;
    }

    /**
     * Returns all dialogues in the game.
     *
     * @return list of all dialogues
     */
    public ArrayList<Dialogue> getAllDialogues() {
        return allDialogues;
    }

    /**
     * Sets all dialogues in the game.
     *
     * @param allDialogues list of all dialogues
     */
    public void setAllDialogues(ArrayList<Dialogue> allDialogues) {
        this.allDialogues = allDialogues;
    }

    /**
     * Returns the current amount of gold the player has.
     *
     * @return current gold amount
     */
    public double getGold() {
        return gold;
    }

    /**
     * Sets the player's gold amount.
     *
     * @param gold amount of gold to set
     */
    public void setGold(double gold) {
        this.gold = gold;
    }

    /**
     * Adds gold to the player's current total.
     *
     * @param amount amount of gold to add
     */
    public void addGold(double amount) {
        this.gold += amount;

    }

    /**
     * Checks if a specific upgrade has been purchased. * @param upgradeName name of the upgrade to check * @return true if the upgrade has been bought, false otherwise
     */
    public boolean upgradeIsBought(String upgradeName) {
        for (Upgrade u : allUpgrades) {
            if (u.getName().equalsIgnoreCase(upgradeName)) {
                return u.isBought();
            }
        }
        return false;
    }


    /**
     * Loads game data from a save file. If the file does not exist, a default save is created.
     * Handles backward-compatibility with old save formats.
     *
     * @param fileName the save file name
     * @param gameMode the game mode being loaded
     * @return true if a previous file existed, false if a new save was created
     */
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

    /**
     * Returns the File object representing the current save file path.
     * Ensures the save directory exists.
     *
     * @return the save file
     */
    private File getSaveFile() {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) dir.mkdirs(); // create folder if it doesn't exist
        return new File(dir, fileName);
    }

    /**
     * Creates a new default save file by copying the master file.
     * Used when no previous save exists.
     */
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

    /**
     * Saves the current database state to disk as JSON.
     * Creates the save file if it does not already exist.
     */
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

    /**
     * Retrieves the next dialogue that should trigger based on game state.
     * Only returns dialogues that have not happened yet and whose conditions are met.
     *
     * @return the dialogue to trigger, or null if none apply
     */
    public Dialogue getTriggeredDialogue() {
        if (allDialogues == null) return null;

        for (Dialogue d : allDialogues) {
            if (!d.hasHappened() && d.shouldTrigger(this)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Returns the current game mode. Defaults to Story mode if unset.
     *
     * @return the current game mode
     */
    public GameModeNames getGameMode() {
        return (this.gameMode == null ? GameModeNames.valueOf("Story") : this.gameMode);
    }

    /**
     * Sets the current game mode.
     *
     * @param gameMode the game mode to apply
     */
    public void setGameMode(GameModeNames gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Returns the maximum number of days supported by the game.
     *
     * @return max day value
     */
    public int getMaxDay() {
        return MAX_DAY;
    }
    /**
     *  Sets all items in the game database.
     *  Replaces the entire day-organized item structure.
     *
     *  @param items list of items organized by day
     */
    public void setItems(ArrayList<ArrayList<Item>> items) {
        this.allItems = items;
    }
}
