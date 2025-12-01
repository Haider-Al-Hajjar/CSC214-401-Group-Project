package com.example.fantasysortinggame.mainmenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class MistakePopupController {

    @FXML
    private Label messageLabel;

    public void setMessage(String msg) {
        messageLabel.setText(msg);
    }

    /** Static factory for showing the popup */
    public static void showPopup(String text) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MistakePopupController.class.getResource(
                            "/com/example/fantasysortinggame/fxmlfiles/MistakePopup.fxml"
                    )
            );
            Parent root = loader.load();

            MistakePopupController controller = loader.getController();
            controller.setMessage(text);

            Stage popup = new Stage();
            popup.setTitle("Mistake");
            popup.setScene(new Scene(root));
            popup.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }

}