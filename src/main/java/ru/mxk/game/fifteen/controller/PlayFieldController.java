package ru.mxk.game.fifteen.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import ru.mxk.game.fifteen.component.ValueButton;
import ru.mxk.game.fifteen.service.FifteenGameService;

import java.util.List;
import java.util.Optional;

public class PlayFieldController {
    private final FifteenGameService gameService = new FifteenGameService();
    public Pane playField;

    private Integer lastSelectedNumber;

    @FXML
    public void initialize() {
        playField.heightProperty().addListener((observable, oldValue, newValue) -> refreshField());
        playField.widthProperty().addListener((observable, oldValue, newValue) -> refreshField());
    }

    private void handleButtonClick(final ActionEvent actionEvent) {
        if (!(actionEvent.getSource() instanceof ValueButton button)) {
            throw new IllegalArgumentException("Unexpected object provided: " + actionEvent.getSource());
        }

        lastSelectedNumber = button.getValue();
        boolean stateIsChanged = gameService.trySwap(lastSelectedNumber);

        if (stateIsChanged) {
            refreshField();
            if (gameService.checkWin()) {
                showMessage("Congratulations!", "You won!");
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

    private void refreshField() {
        final List<Button> buttons = gameService.mapCells(this::createButton);

        ObservableList<Node> children = playField.getChildren();
        children.clear();
        children.addAll(buttons);

        Optional.ofNullable(lastSelectedNumber)
                .flatMap(gameService::getIndexByValue)
                .map(children::get)
                .ifPresent(Node::requestFocus);
    }

    private ValueButton createButton(int value) {
        final ValueButton button = new ValueButton(value);
        button.setOnAction(this::handleButtonClick);
        button.setPrefWidth(Math.floor(playField.getWidth() / gameService.getSize()));
        button.setPrefHeight(Math.floor(playField.getHeight() / gameService.getSize()));
        gameService.getIndexByValue(value)
                   .map(gameService::isEmpty)
                   .map(it -> !it)
                   .ifPresent(button::setVisible);

        return button;
    }
}