package ru.mxk.game.fifteen;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mxk.game.fifteen.component.gameplay.GameplayController;
import ru.mxk.game.fifteen.configuration.component.ControllerLoader;
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
    public void start(final Stage stage) throws IOException {
        applicationContext.publishEvent(new StageReadyEvent(stage));

        final ControllerLoader controllerLoader = applicationContext.getBean(ControllerLoader.class);
        final GameplayController gameplayController = controllerLoader.loadController(GameplayController.class);
        final Scene scene = new Scene(gameplayController.getContent());
        stage.setTitle("Fifteen game");
        stage.setScene(scene);
        stage.show();
    }


}