package ru.mxk.game.fifteen.fx_component;

import javafx.scene.control.Button;
import lombok.Getter;

@Getter
public class ValueButton extends Button {
    private final int value;

    public ValueButton(int value) {
        super(String.valueOf(value));
        this.value = value;
    }
}
