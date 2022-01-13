package ru.mxk.game.fifteen.configuration;

import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public record StageInitializer(GenericApplicationContext context) implements ApplicationListener<StageReadyEvent> {
    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        context.registerBean(Stage.class, event::getStage);
    }
}
