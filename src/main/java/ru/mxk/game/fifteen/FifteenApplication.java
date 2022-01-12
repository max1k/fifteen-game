package ru.mxk.game.fifteen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FifteenApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FifteenApplication.class.getResource("play-field-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Fifteen");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}