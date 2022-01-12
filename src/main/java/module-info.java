module ru.mxk.fifteentag {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires static lombok;

    opens ru.mxk.game.fifteen to javafx.fxml;
    exports ru.mxk.game.fifteen;
    exports ru.mxk.game.fifteen.controller;
    opens ru.mxk.game.fifteen.controller to javafx.fxml;
}