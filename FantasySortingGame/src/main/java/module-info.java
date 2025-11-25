module com.example.fantasysortinggame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;

    // JavaFX FXML
    opens com.example.fantasysortinggame.mainmenu to javafx.fxml;
    opens com.example.fantasysortinggame.buyphase to javafx.fxml;
    opens com.example.fantasysortinggame.sortphase to javafx.fxml;
    opens com.example.fantasysortinggame.salephase to javafx.fxml;
    opens com.example.fantasysortinggame.datatypes to javafx.fxml, com.google.gson;


    // Gson
    opens com.example.fantasysortinggame.database to com.google.gson;

    // Exports
    exports com.example.fantasysortinggame.mainmenu;
    exports com.example.fantasysortinggame.buyphase;
    exports com.example.fantasysortinggame.sortphase;
    exports com.example.fantasysortinggame.salephase;
    exports com.example.fantasysortinggame.datatypes;
}
