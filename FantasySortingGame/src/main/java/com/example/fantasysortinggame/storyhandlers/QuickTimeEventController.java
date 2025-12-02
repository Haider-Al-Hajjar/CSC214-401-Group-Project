package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.datatypes.QuickTimeEvent;
import com.example.fantasysortinggame.gamephasemanager.GameEngine;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class QuickTimeEventController {

    public AnchorPane rootPane;
    @FXML
    TextField timeLeftField;
    @FXML
    TextField titleField;
    @FXML
    TextField descriptionField;
    @FXML private Button option1Field;
    @FXML
    private Button option2Field;
    @FXML
    VBox optionsContainer;

    private Stage stage;
    private QuickTimeEvent currentEvent;
    private ArrayList<Item> allItems;
    private Timeline timer;
    private boolean eventActive = false;

    public void setStage(Stage stage) { this.stage = stage; }

    /**
     * Displays the Quick Time Event (QTE) window as a modal JavaFX stage.
     * <p>
     * This method loads the {@code quickTimeEvent.fxml} layout, initializes its controller,
     * and starts the specified {@link QuickTimeEvent}. The window is shown with
     * {@code showAndWait()}, blocking interaction with the owner stage until the
     * QTE is completed or dismissed.
     * </p>
     *
     * @param event     the {@link QuickTimeEvent} instance containing the QTE logic,
     *                  prompts, timing, and required item interactions
     * @param allItems  the list of all {@link Item} objects available to the player;
     *                  these may be used by the controller during the QTE
     * @param owner     the parent {@link Stage} that owns this modal window; the QTE
     *                  window will block input to this stage until finished
     *
     * @see QuickTimeEventController
     * @see FXMLLoader
     * @see Stage#showAndWait()
     */
    public static void showQuickTimeEventWindow(QuickTimeEvent event, java.util.List<Item> allItems, Stage owner) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    QuickTimeEventController.class.getResource("/com/example/fantasysortinggame/fxmlfiles/quickTimeEvent.fxml")
            );
            Parent root = loader.load();
            QuickTimeEventController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Quick Time Event");
            stage.setScene(new Scene(root));
            stage.initOwner(owner);
            stage.initModality(Modality.WINDOW_MODAL);

            controller.setStage(stage);
            controller.startEvent(event, allItems);

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Initializes and starts a Quick Time Event (QTE) within the controller.
     * <p>
     * This method prepares the UI, resets event state, stores the full item list,
     * loads the option buttons, and begins the countdown timer defined by the QTE.
     * If an event is already active, the method exits immediately.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Prevents overlapping QTEs by ignoring calls when an event is already active.</li>
     *     <li>Stores the provided {@link QuickTimeEvent} and all available {@link Item} objects.</li>
     *     <li>Resets timing and correctness flags on the event.</li>
     *     <li>Updates UI fields such as title and description.</li>
     *     <li>Populates the selectable options for the QTE.</li>
     *     <li>Starts the countdown timer using the event’s maximum allowed time.</li>
     * </ul>
     *
     * @param event     the {@link QuickTimeEvent} object containing the QTE data
     *                  such as title, description, correct items, and time limit
     * @param allItems  the full list of {@link Item} objects accessible to the player;
     *                  used to generate option buttons and validate the response
     *
     * @see #populateOptions()
     * @see #startTimer(long)
     * @see QuickTimeEvent
     */
    public void startEvent(QuickTimeEvent event, java.util.List<Item> allItems) {
        if (eventActive) return;
        eventActive = true;

        this.currentEvent = event;
        this.allItems = (ArrayList<Item>) allItems;   // store all items
        currentEvent.setStart(System.currentTimeMillis());
        currentEvent.setSolvedInTime(false);
        currentEvent.setEventSolvedCorrectly(false);

        titleField.setText(event.getTitle());
        descriptionField.setText(event.getDescription());

        populateOptions();
        startTimer(event.getMaxTime());
    }

    /**
     * Populates the UI with option buttons for the current Quick Time Event (QTE).
     * <p>
     * This method clears any previously displayed options, then generates a new
     * {@link Button} for each option text defined in the active
     * {@link QuickTimeEvent}. Each button is configured to notify the controller
     * through {@link #onOptionClick(String)} when clicked.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Clears the options container of old buttons.</li>
     *     <li>Creates and configures a button for each option.</li>
     *     <li>Sets the button action to handle the player's choice.</li>
     *     <li>Adds all constructed buttons to the UI container.</li>
     * </ul>
     *
     * @see QuickTimeEvent#getOptions()
     * @see #onOptionClick(String)
     */
    private void populateOptions() {
        optionsContainer.getChildren().clear();
        for (String optionText : currentEvent.getOptions()) {
            Button btn = new Button(optionText);
            btn.setOnAction(e -> onOptionClick(optionText));
            optionsContainer.getChildren().add(btn);
        }
    }
    /**
     * Starts the countdown timer for the Quick Time Event (QTE).
     * <p>
     * This method counts down once per second, updates the timer display, and
     * triggers {@link #onTimeExpired()} when the time reaches zero.
     * </p>
     *
     * @param maxTimeMillis the total duration of the QTE, in milliseconds
     */
    private void startTimer(long maxTimeMillis) {
        long[] secondsLeft = {maxTimeMillis / 1000};
        updateTimeDisplay(secondsLeft[0]);

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsLeft[0]--;
            updateTimeDisplay(secondsLeft[0]);
            if (secondsLeft[0] <= 0) onTimeExpired();
        }));
        timer.setCycleCount((int) secondsLeft[0]);
        timer.play();
    }
    /**
     * Updates the on-screen timer display to show the remaining time.
     * <p>
     * Converts the given number of seconds into a <code>mm:ss</code> format and
     * updates the timer text field accordingly. This method does not perform any
     * countdown logic—only formatting and UI display.
     * </p>
     *
     * @param seconds the remaining time in whole seconds to be displayed
     *
     * @see String#format(String, Object...)
     */
    private void updateTimeDisplay(long seconds) {
        timeLeftField.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
    }
    /**
     * Handles the player's selection of an option during a Quick Time Event (QTE).
     * <p>
     * This method is triggered whenever the user clicks one of the option buttons.
     * It stops the timer, records the event's completion time, checks whether
     * the selected option is correct, updates event result flags, applies related
     * item effects, and then closes the QTE window.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Plays a UI click sound via the {@link GameEngine} sound controller.</li>
     *     <li>Ignores input if the event is no longer active.</li>
     *     <li>Stops the countdown timer if it is running.</li>
     *     <li>Records the end timestamp for the event.</li>
     *     <li>Determines whether the clicked option matches the event’s correct answer.</li>
     *     <li>Marks the event as solved and indicates whether it was solved correctly.</li>
     *     <li>Applies any item-based effects triggered by solving the event.</li>
     *     <li>Closes the QTE stage/window.</li>
     * </ul>
     *
     * @param option the text of the option the player clicked
     *
     * @see QuickTimeEvent#getCorrectOption()
     * @see #applyItemEffects()
     * @see #closeStage()
     */
    private void onOptionClick(String option) {
        GameEngine.getSoundController().playButtonClick();
        if (!eventActive) return;
        if (timer != null) timer.stop();

        currentEvent.setEnd(System.currentTimeMillis());
        boolean correct = option.equals(currentEvent.getCorrectOption());
        currentEvent.setEventSolvedCorrectly(correct);
        currentEvent.setSolvedInTime(true);

        applyItemEffects();

        eventActive = false;
        closeStage();
    }
    /**
     * Handles the expiration of the timer during a Quick Time Event (QTE).
     * <p>
     * This method is invoked when the countdown reaches zero before the player
     * selects an option. The event is treated as incorrectly solved, item effects
     * are applied, and the QTE window is closed.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Does nothing if the event is no longer active (prevents double-handling).</li>
     *     <li>Logs that the timer expired (useful for debugging).</li>
     *     <li>Records the end timestamp of the event.</li>
     *     <li>Marks the event as "attempted" but incorrectly solved.</li>
     *     <li>Executes any item-related effects associated with time-outs.</li>
     *     <li>Flags the event as inactive and closes the QTE stage.</li>
     * </ul>
     *
     * <h3>Note:</h3>
     * Even though the player did not choose an option, the event is marked as
     * {@code solvedInTime = true} to indicate the QTE completed normally
     * (i.e., not canceled). The correctness flag is always {@code false}.
     *
     * @see #applyItemEffects()
     * @see #closeStage()
     * @see QuickTimeEvent#setEventSolvedCorrectly(boolean)
     */
    private void onTimeExpired() {
        if (!eventActive) return;

        System.out.println("Time ran out!");
        currentEvent.setEnd(System.currentTimeMillis());

        // Consider time-out as a wrong answer
        currentEvent.setSolvedInTime(true);          // mark it as "attempted"
        currentEvent.setEventSolvedCorrectly(false); // mark it as incorrect

        applyItemEffects();

        eventActive = false;
        closeStage();
    }
    /**
     * Applies the item changes defined by the current Quick Time Event (QTE).
     * <p>
     * This method updates the properties of all items affected by the event,
     * modifying their title, description, sorting category, type, and value based
     * on whether the event was solved correctly or incorrectly.
     * Only items whose titles match names listed in
     * {@link QuickTimeEvent#getItemsAffected()} are modified.
     * </p>
     *
     * <h3>Behavior:</h3>
     * <ul>
     *     <li>Returns immediately if no event is active or the item list is unavailable.</li>
     *     <li>Iterates through all affected item names specified by the event.</li>
     *     <li>Finds matching {@link Item} objects in the full item list via case-insensitive title comparison.</li>
     *     <li>If the event was solved correctly:
     *         <ul>
     *             <li>Updates the item's title, description, category (sort), type, and value using the event's success fields.</li>
     *         </ul>
     *     </li>
     *     <li>If the event was solved incorrectly or timed out:
     *         <ul>
     *             <li>Applies the failure versions of the title, description, sort, type, and value.</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * <h3>Note:</h3>
     * This method directly mutates item objects. These changes persist in the
     * player's inventory and should be followed by a UI refresh in the calling system.
     *
     * @see QuickTimeEvent#getItemsAffected()
     * @see QuickTimeEvent#eventIsSolvedCorrectly()
     * @see Item
     */
    private void applyItemEffects() {
        if (currentEvent == null || allItems == null) return;

        for (String name : currentEvent.getItemsAffected()) {
            for (Item item : allItems) {
                if (item.getTitle().equalsIgnoreCase(name)) {
                    if (currentEvent.eventIsSolvedCorrectly()) {
                        item.setTitle(currentEvent.getSuccessName());
                        item.setDescription(currentEvent.getSuccessDescription());
                        item.setItemSort(currentEvent.getSuccessSort());
                        item.setItemType(currentEvent.getSuccessType());
                        item.setValue(currentEvent.getSuccessValue());
                    }
                    else {
                        item.setTitle(currentEvent.getFailureName());
                        item.setDescription(currentEvent.getFailureDescription());
                        item.setItemSort(currentEvent.getFailureSort());
                        item.setItemType(currentEvent.getFailureType());
                        item.setValue(currentEvent.getFailureValue());
                    }

                }
            }
        }
    }


    private void closeStage() {
        if (timer != null) timer.stop();
        if (stage != null) stage.close();
    }
}
