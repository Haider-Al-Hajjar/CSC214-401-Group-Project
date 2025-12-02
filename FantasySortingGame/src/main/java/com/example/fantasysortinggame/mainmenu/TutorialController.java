package com.example.fantasysortinggame.mainmenu;

import com.example.fantasysortinggame.database.Database;
import com.example.fantasysortinggame.gamephasemanager.GameEngine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Controls and displays the full in-game tutorial sequence for the Fantasy Sorting Game.
 * <p>
 * This controller manages:
 * <ul>
 *     <li>Loading tutorial images dynamically from the resources folder.</li>
 *     <li>Displaying tutorial text slides.</li>
 *     <li>Advancing through slides via a “Next” button.</li>
 *     <li>Skipping the tutorial entirely.</li>
 *     <li>Closing the tutorial window once completed.</li>
 *     <li>Starting the game immediately after the tutorial ends.</li>
 * </ul>
 * The tutorial consists of a series of explanatory text slides paired with images.
 * The images are dynamically loaded from
 * <code>/com/example/fantasysortinggame/tutorialinformation</code> inside the JAR or IDE
 * environment.
 * </p>
 *
 * <h3>Usage:</h3>
 * <pre>
 *     TutorialController.showTutorial(database);
 * </pre>
 *
 * <p>
 * The controller expects an FXML layout file named <code>Tutorial.fxml</code> containing:
 * <ul>
 *     <li>An ImageView for slide images</li>
 *     <li>A Label for slide text</li>
 *     <li>A "Next" button</li>
 *     <li>An optional "Skip" button</li>
 * </ul>
 * </p>
 */
public class TutorialController {
    @FXML
    public VBox root;
    @FXML
    public Label tutorialTextBox;
    @FXML
    ImageView tutorialImageView;
    @FXML
    Button nextButton;

    private Database database;
    Stage stage;
    ArrayList<Image> tutorialImages;
    int currentIndex = 0;
    ArrayList<String> tutorialTexts;

    @FXML
    private Button skipButton; // add this in FXML and link
    /**
     * Initializes the tutorial UI once the FXML has loaded.
     * <p>
     * This method:
     * <ul>
     *     <li>Creates lists for images and text slides.</li>
     *     <li>Populates all tutorial text entries.</li>
     *     <li>Loads tutorial images from the resource folder.</li>
     *     <li>Displays the first image/text slide (if available).</li>
     *     <li>Binds button actions for advancing and skipping.</li>
     * </ul>
     * <p>
     * This is automatically called by JavaFX after FXML injection.
     * </p>
     */
    @FXML
    void initialize() {
        tutorialImages = new ArrayList<>();
        tutorialTexts = new ArrayList<>();

        tutorialTexts.addAll(List.of("Welcome to the Fantasy Sorting Game. You play a nameless character whose grandfather was a great adventurer in his time.", "It's been a long time since you last heard from him, but you justify it to yourself by saying you were never really that close anyway.", "Then, you discover a letter in the mail telling you that he's passed away and to come to this location to collect your inheritance.", "After pausing to process this, you ride out to the location... only to discover that it is a pigsty of a warehouse, filled to the brim with items of all kinds, most of whom you can scarcely identify.", "With a sigh, you roll up your sleeves and get to work sorting everything out so you can collect on your inheritance.", "Your job in this game is to inspect incoming items and decide which category each item belongs to. On the first few days, you'll only be dealing with two: Junk, and Treasure, but as the days progress you'll find increasingly specific subcategories.", "Unsorted Items. All new items start here. Review each one and sort it before continuing.", "Junk: Junk is anything that isn't worth anything, and is either going to be thrown away, or more likely sold.", "Junk is divided into: Usable Junk, Broken Junk, and Curious Junk.", "Usable Junk. Items that still function. Sub-types include: Consumables, Tools, and Everyday.", "Consumables. Consumables are items that are a one time use, like potions or scrolls.", "Tools. Tools are items that have a specific, labor-related purpose, like shovels or pickaxes or normal swords. Magical or rare items do not count as tools, because those are Treasure, not Junk.", "Everyday. Everyday items are items that you'd find around a house, like a chair, a paperweight, or a set of cutlery.", "Broken Junk. Items that are damaged beyond normal use. Sub-types: Depleted, Weathered, and Miscellaneous.", "Depleted. Depleted items are effectively consumables that have been used up. Empty potion bottles, used scrolls, etc.", "Weathered. Weathered items are items that are old and have many deficiencies. It might be rusted, broken, shattered, torn, weakened, or just aged, but it is in some way deteriorated.", "Miscellaneous. Anything that isn't worth much but doesn't fall into any other category. Loose marbles, pocket lint, scraps of paper, etc", "Curious Junk. Strange or valuable materials. Sub-types: Oddities, Crafting Materials, and Collectibles.", "Oddities. Oddities are items that would be otherwise unremarkable, except for one distinguishing feature that makes them distinct. It might be an ordinary rug with strange markings, or have a strange substance attached to it, or something.", "Crafting Materials. Crafting materials are items that could be made into gear, but isn't valuable. Something like a dragon scale would be too valuable for this, as would something magical. This is more on the level of boar hides and wolf claws.", "Collectibles. Generic items that are part of a set. A series of books or action figures or similar things falls into this category.", "And that's all for Junk! Now, onto the other, more interesting categories of the game...", "Treasure. Treasure is anything that's worth keeping. Be it just sentimental or actually worth some cash if you sell it, treasured items are what your character cares about at the end of the day.", "Artifacts. Artifacts are treasured items that are magical in nature, no matter how powerful or sinister.. A holy grail, a cursed blade, a heating jacket fall into this category.", "Relics. Any powerful magical artifacts without any other significance go here.", "Cursed / Dangerous. Any magical artifacts with negative side effects go here.", "Minor / Utility Magic. Any magcial artifacts with simple or not flashy magic go here.", "Historical Treasure. Any items that aren't magical but are worth keeping for historical or sentimental value go here.", "Keepsakes. Any items that are worth keeping because of your connection to them go here.", "Documents / Maps. Any journal entries, map scraps, or books on ancient topics go here.", "Luxurious Treasure. This is stuff that's pretty or is literally just cash. A pile of gold, a gilded portrait.", "Jewelry. Non-enchanted jewelry goes here. Diamond rings, sapphire necklaces, etc.", "Treasure hoard. Fungible items of wealth go here. Bags of gold, bars of silver, etc.", "Decorative / Ornamental. Stuff you'd keep in a fancy house. Gilded portarits, elaborate furniture, etc.", "And that's all for treasure!", "And, by extension, that's every category in the game! Feel free to refer back to this at any point by starting a new game briefly. It's super useful information!", "How to Sort: During the first phase of each day, you'll see a menu with a buncha items in it. You can filter using the buttons at the top. Changing the views will alter what information each item displays. Clicking on the item's current sort will open up a menu where you can change it from where it is now to where it should be.", "Once you've sorted everything on that day, you'll see a button to proceed. That will advance to the Sale Phase. You can keep changing categories as long as you want, but once you advance to the Sale Phase, your choices are locked in.", "During the Sale Phase, you can sell any items you've collected. If they're sorted correctly, you'll get a bonus!", "You can advance to the Buy Phase at any time by clicking the button at the bottom.", "During the Buy Phase, you can purchase any upgrades to change the game a little bit.", "Ending the Buy Phase will advance you to the next day.", "On later days, NPCs will show up and talk to you. Some of them have unique dialogue for sorting specific items in a specific way!", "On later days, some NPCs might interfere with your sorting! Make sure to pay attention to what they say, or they might just get made and take something if they think you're mishandling it!", "Complete all six days to win the game!"));

        loadTutorialImages(); // dynamically load images from folder

        if (!tutorialImages.isEmpty()) {
            tutorialImageView.setImage(tutorialImages.get(0));
            tutorialTextBox.setText(tutorialTexts.get(0));
        }

        nextButton.setOnAction(e -> onNextButtonHandler());
        nextButton.setOnAction(e -> onNextButtonHandler());

        if (skipButton != null) {
            skipButton.setOnAction(e -> onSkipTutorial());
        }

    }
    /**
     * Skips the entire tutorial and immediately starts the game.
     * <p>
     * Closes the tutorial window and triggers {@link GameEngine#startDayCycle()}.
     * </p>
     */
    private void onSkipTutorial() {
        if (stage != null) stage.close();
        GameEngine.startDayCycle(); // immediately start the game
    }
    /**
     * Assigns stage so we can control it
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    /**
     * Opens the tutorial window in a new JavaFX stage.
     * <p>
     * Loads the {@code Tutorial.fxml}, injects the database, attaches the stage,
     * and displays the UI.
     * </p>
     *
     * @param database shared game database to inject into the controller
     */
    public static void showTutorial(Database database) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    TutorialController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/Tutorial.fxml")
            );
            Parent root = loader.load();

            TutorialController controller = loader.getController();
            controller.setDatabase(database);

            Stage stage = new Stage();
            controller.setStage(stage);

            stage.setTitle("Tutorial");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Stores a reference to the game’s database.
     *
     * @param database the database instance used by the game
     */
    private void setDatabase(Database database) {
        this.database = database;
    }
    /**
     * Dynamically loads all tutorial images from the
     * <code>com/example/fantasysortinggame/tutorialinformation</code> folder.
     * <p>
     * Supports both IDE “file” mode and packaged JAR “jar” mode by detecting
     * the resource protocol.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Finds all PNG/JPG files in the tutorial image directory.</li>
     *     <li>Adds them to {@link #tutorialImages} in alphabetical order.</li>
     *     <li>Prints errors when resources cannot be loaded.</li>
     * </ul>
     */
    private void loadTutorialImages() {
        tutorialImages = new ArrayList<>();
        try {
            var loader = getClass().getClassLoader();
            var resource = loader.getResource("com/example/fantasysortinggame/tutorialinformation");

            if (resource == null) {
                System.err.println("Folder not found");
                return;
            }

            if (resource.getProtocol().equals("file")) {
                // Running in IDE / unpacked
                File folder = new File(resource.toURI());
                for (File file : folder.listFiles()) {
                    if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
                        tutorialImages.add(new Image(file.toURI().toString()));
                    }
                }
            } else if (resource.getProtocol().equals("jar")) {
                // Running from JAR
                String path = "/com/example/fantasysortinggame/tutorialinformation/";
                try (var jar = ((java.net.JarURLConnection) resource.openConnection()).getJarFile()) {
                    for (var entry : jar.stream().toList()) {
                        if (entry.getName().startsWith(path) &&
                                (entry.getName().endsWith(".png") || entry.getName().endsWith(".jpg"))) {
                            tutorialImages.add(new Image(loader.getResourceAsStream(entry.getName())));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Advances to the next tutorial slide.
     * <p>
     * When the player reaches the final slide:
     * <ul>
     *     <li>The tutorial window closes</li>
     *     <li>The day cycle of the game begins</li>
     * </ul>
     *
     * <h3>Image Cycling:</h3>
     * If there are fewer images than text slides, images loop using modulo.
     */
    public void onNextButtonHandler() {
        currentIndex++;
        if (currentIndex >= tutorialTexts.size()) {
            if (stage != null) stage.close();
            GameEngine.startDayCycle();
            return;
        }

        // Loop images if there are fewer than texts
        Image img = tutorialImages.get(currentIndex % tutorialImages.size());
        tutorialImageView.setImage(img);

        tutorialTextBox.setText(tutorialTexts.get(currentIndex));
    }

    /**
     * Displays the first slide of the tutorial sequence.
     * <p>
     * This method resets the tutorial index, verifies that tutorial images
     * are available, and updates the UI to show the first image and text.
     * It also reassigns the "Next" button handler to ensure slide navigation
     * functions correctly even if this method is called more than once.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Does nothing if no tutorial images are loaded.</li>
     *     <li>Resets the internal slide index to the first slide.</li>
     *     <li>Warns if the ImageView was not injected properly from FXML.</li>
     *     <li>Displays the first tutorial image (with modulo safety).</li>
     *     <li>Displays the corresponding tutorial text.</li>
     *     <li>Rebinds the "Next" button to {@link #onNextButtonHandler()}.</li>
     * </ul>
     *
     * <h3>Usage Notes:</h3>
     * <ul>
     *     <li>Typically called at startup when showing the tutorial window.</li>
     *     <li>May also be used to restart the tutorial from the beginning.</li>
     * </ul>
     */
    public void displayTutorial() {
        if (tutorialImages == null || tutorialImages.isEmpty()) return;
        currentIndex = 0;
        if (tutorialImageView == null) {
            System.err.println("tutorialImageView not injected—FXML path is wrong.");
        }

        tutorialImageView.setImage(tutorialImages.get(currentIndex % tutorialImages.size()));
        tutorialTextBox.setText(tutorialTexts.get(currentIndex));

        nextButton.setOnAction(e -> onNextButtonHandler());
    }
}
