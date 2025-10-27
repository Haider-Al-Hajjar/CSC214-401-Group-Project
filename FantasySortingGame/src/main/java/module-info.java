module com.example.fantasysortinggame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fantasysortinggame to javafx.fxml;
    exports com.example.fantasysortinggame;
    exports com.example.fantasysortinggame.datatypes;
    opens com.example.fantasysortinggame.datatypes to javafx.fxml;
    exports com.example.fantasysortinggame.mainmenu;
    opens com.example.fantasysortinggame.mainmenu to javafx.fxml;
    exports com.example.fantasysortinggame.salephase;
    opens com.example.fantasysortinggame.salephase to javafx.fxml;
}