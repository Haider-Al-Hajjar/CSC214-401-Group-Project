package com.example.fantasysortinggame.storyhandlers;

import com.example.fantasysortinggame.datatypes.Item;
import com.example.fantasysortinggame.datatypes.QuickTimeEvent;
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
    @FXML private TextField timeLeftField;
    @FXML private TextField titleField;
    @FXML private TextField descriptionField;
    @FXML private Button option1Field;
    @FXML
    private Button option2Field;
    @FXML private VBox optionsContainer;

    private Stage stage;
    private QuickTimeEvent currentEvent;
    private ArrayList<Item> allItems;
    private Timeline timer;
    private boolean eventActive = false;

    public void setStage(Stage stage) { this.stage = stage; }

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

    private void populateOptions() {
        optionsContainer.getChildren().clear();
        for (String optionText : currentEvent.getOptions()) {
            Button btn = new Button(optionText);
            btn.setOnAction(e -> onOptionClick(optionText));
            optionsContainer.getChildren().add(btn);
        }
    }

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

    private void updateTimeDisplay(long seconds) {
        timeLeftField.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
    }

    private void onOptionClick(String option) {
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
