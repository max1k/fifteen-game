package ru.mxk.game.fifteen;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mxk.game.fifteen.configuration.StageReadyEvent;

import java.io.IOException;

public class FxApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(FifteenApplication.class).run();
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws IOException {
        applicationContext.publishEvent(new StageReadyEvent(stage));

        FXMLLoader fxmlLoader = new FXMLLoader(FxApplication.class.getResource("play-field-view.fxml"));
        Parent parent = fxmlLoader.load();
        applicationContext.getAutowireCapableBeanFactory().autowireBean(fxmlLoader.getController());

        Scene scene = new Scene(parent);
        stage.setTitle("Fifteen");
        stage.setScene(scene);
        stage.show();
    }
}