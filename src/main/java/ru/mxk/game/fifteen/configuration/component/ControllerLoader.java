package ru.mxk.game.fifteen.configuration.component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.mxk.game.fifteen.base.BaseController;
import ru.mxk.game.fifteen.configuration.annotation.JavaFXController;

import java.io.IOException;
import java.net.URL;

@Component
@AllArgsConstructor
public class ControllerLoader {
    private final ApplicationContext applicationContext;


    public  <T extends BaseController> T loadController(Class<T> controllerClass) throws IOException {
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

        //TODO: add based on a class name fxml file name inference in case of fxmlURL.isEmpty()
        return controllerClass.getResource(fxmlURL);
    }

}
