module com.example.fantasysortinggame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;


    opens com.example.fantasysortinggame.mainmenu to javafx.fxml;
    opens com.example.fantasysortinggame.buyphase to javafx.fxml;
    opens com.example.fantasysortinggame.sortphase to javafx.fxml;

    exports com.example.fantasysortinggame.buyphase;
    exports com.example.fantasysortinggame.sortphase;


    //opens com.example.fantasysortinggame to javafx.fxml;
    //exports com.example.fantasysortinggame;
    exports com.example.fantasysortinggame.datatypes;
   // opens com.example.fantasysortinggame.datatypes to javafx.fxml;
    exports com.example.fantasysortinggame.mainmenu;
   //opens com.example.fantasysortinggame.mainmenu to javafx.fxml;
    exports com.example.fantasysortinggame.salephase;
    //opens com.example.fantasysortinggame.salephase to javafx.fxml;
    //opens com.example.fantasysortinggame.database to com.google.gson;
    opens com.example.fantasysortinggame.database to com.google.gson;

    opens com.example.fantasysortinggame.datatypes to com.google.gson;
}