package ru.mxk.game.fifteen;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import ru.mxk.game.fifteen.base.BaseController;
import ru.mxk.game.fifteen.component.gameplay.GameplayController;
import ru.mxk.game.fifteen.configuration.JavaFXController;
import ru.mxk.game.fifteen.configuration.StageReadyEvent;

import java.io.IOException;
import java.net.URL;

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

        final GameplayController gameplayController = loadController(GameplayController.class);
        final Scene scene = new Scene(gameplayController.getContent());
        stage.setTitle("Fifteen game");
        stage.setScene(scene);
        stage.show();
    }

    private <T extends BaseController> T loadController(Class<T> controllerClass) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(getFXMLResource(controllerClass));
        final Parent content = fxmlLoader.load();
        final T controller = fxmlLoader.getController();

        controller.setContent(content);
        applicationContext.getAutowireCapableBeanFactory().autowireBean(controller);

        return controller;
    }

    private static URL getFXMLResource(final Class<? extends BaseController> controllerClass) {
        if (!controllerClass.isAnnotationPresent(JavaFXController.class)) {
            throw new IllegalArgumentException(controllerClass +
                                               " should be annotated with @" +
                                               JavaFXController.class.getSimpleName()
            );
        }
        final String fxmlURL = controllerClass.getAnnotation(JavaFXController.class).value();
        return controllerClass.getResource(fxmlURL);
    }
}