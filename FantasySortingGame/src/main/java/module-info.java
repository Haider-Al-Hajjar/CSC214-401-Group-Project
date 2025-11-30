module com.example.fantasysortinggame {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;


    // JavaFX FXML
    opens com.example.fantasysortinggame.mainmenu to javafx.fxml;
    opens com.example.fantasysortinggame.gamephasemanager to javafx.fxml;
    opens com.example.fantasysortinggame.datatypes to javafx.fxml, com.google.gson;
    opens com.example.fantasysortinggame.storyhandlers to javafx.fxml;

    // Gson
    opens com.example.fantasysortinggame.database to com.google.gson;

    // Exports
    exports com.example.fantasysortinggame.mainmenu;
    exports com.example.fantasysortinggame.datatypes;
    exports com.example.fantasysortinggame.database;
    exports com.example.fantasysortinggame.gamemodes;
    exports com.example.fantasysortinggame.gamephasemanager;
}