package com.example.snake.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static javafx.application.Platform.exit;

public class MainMenu {

  private final Button startButton = new Button("Start Game");
  private final Button leaderboardButton = new Button("Leaderboard");
  private final Button optionsButton = new Button("Options");

  private final TilePane menuRoot;

  public MainMenu() {

    menuRoot = new TilePane();
    menuRoot.setBackground(Background.fill(Color.web("#000000")));
    menuRoot.setAlignment(Pos.CENTER);

    Font fontSize = Font.font(25);

    startButton.setFont((fontSize));
    leaderboardButton.setFont((fontSize));
    optionsButton.setFont(fontSize);

    /*
     * Since we are not going to call a event from another class,
     * the exit button can be instantiated as a local variable
     */
    Button exitButton = new Button("Exit Game");
    exitButton.setFont(fontSize);
    exitButton.setOnAction(event -> exit());

    VBox vbox = new VBox(30, startButton, leaderboardButton, optionsButton, exitButton);
    vbox.setAlignment(Pos.CENTER);

    menuRoot.getChildren().addAll(vbox);
  }

  public TilePane getMenuRoot() {
    return this.menuRoot;
  }

  public void onGameStart(EventHandler<ActionEvent> eventHandler) {
    startButton.setOnAction(eventHandler);
  }

  public void onOptionsMenu(EventHandler<ActionEvent> eventHandler) {
    optionsButton.setOnAction(eventHandler);
  }

  public void onLeaderboardMenu(EventHandler<ActionEvent> eventHandler) {
    leaderboardButton.setOnAction(eventHandler);
  }
}
