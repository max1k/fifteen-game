package ru.mxk.game.fifteen;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FifteenApplication {

    public static void main(String[] args) {
        Application.launch(FxApplication.class, args);
    }

}
