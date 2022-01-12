package ru.mxk.game.fifteen.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import ru.mxk.game.fifteen.component.GameButton;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class PlayFieldController {
    private static final int SIZE = 4;
    private static final int EMPTY_BUTTON_VALUE = SIZE * SIZE;
    private final GameButton[] buttons = new GameButton[SIZE * SIZE];
    private final Map<Button, Integer> buttonIndexMap = new HashMap<>(SIZE * SIZE);

    public Pane playField;

    public PlayFieldController() {
        for(int i = 0; i < SIZE * SIZE; i++) {
            final GameButton button = new GameButton(i + 1);
            button.setOnAction(this::handleButtonClick);

            buttons[i] = button;
            buttonIndexMap.put(button, i);

            if (isEmpty(i)) {
                button.setVisible(false);
            }
        }
    }

    @FXML
    public void initialize() {
        playField.heightProperty().addListener((observable, oldValue, newValue) -> refreshField());
        playField.widthProperty().addListener((observable, oldValue, newValue) -> refreshField());
    }

    private boolean isEmpty(final int index) {
        return buttons[index].getValue() == EMPTY_BUTTON_VALUE;
    }

    private void handleButtonClick(final ActionEvent actionEvent) {
        if (!(actionEvent.getSource() instanceof Button button)) {
            throw new IllegalArgumentException("Unexpected object provided: " + actionEvent.getSource());
        }

        final Integer buttonIndex = buttonIndexMap.get(button);
        if (buttonIndex != null) {
            boolean stateIsChanged = trySwap(buttonIndex);

            if (stateIsChanged) {
                refreshField();
                if (checkWin()) {
                    showMessage("Congratulations!", "You won!");
                }
            }
        }
    }

    private void showMessage(final String header, final String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game results");
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private boolean checkWin() {
        int number = 1;
        for (GameButton button : buttons) {
            if (button.getValue() != number++) {
                return false;
            }
        }

        return true;
    }

    private boolean trySwap(int buttonIndex) {
        final Optional<Integer> emptyNeighborIndex =
                Stream.of(buttonIndex - 1, buttonIndex + 1, buttonIndex - SIZE, buttonIndex + SIZE)
                      .filter(index -> index >= 0 && index < SIZE * SIZE)
                      .filter(this::isEmpty)
                      .findFirst();

        if (emptyNeighborIndex.isPresent()) {
            swap(buttonIndex, emptyNeighborIndex.get());
            return true;
        }

        return false;
    }

    private void swap(int firstIndex, int secondIndex) {
        final GameButton temp = buttons[firstIndex];
        buttons[firstIndex] = buttons[secondIndex];
        buttons[secondIndex] = temp;

        buttonIndexMap.put(buttons[firstIndex], firstIndex);
        buttonIndexMap.put(buttons[secondIndex], secondIndex);
    }

    private void refreshField() {
        for (GameButton button : buttons) {
            button.setPrefWidth(Math.floor(playField.getWidth() / SIZE));
            button.setPrefHeight(Math.floor(playField.getHeight() / SIZE));
        }

        playField.getChildren().clear();
        playField.getChildren().addAll(buttons);
    }

}