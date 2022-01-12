package ru.mxk.game.fifteen.component;

import javafx.scene.control.Button;
import lombok.Getter;

@Getter
public class GameButton extends Button {
    private final int value;

    public GameButton(int value) {
        super(String.valueOf(value));
        this.value = value;
    }
}
