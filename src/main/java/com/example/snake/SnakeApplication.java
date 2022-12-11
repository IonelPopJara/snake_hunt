package com.example.snake;

import com.example.snake.game.Game;
import com.example.snake.game.GameLoopRunner;
import com.example.snake.game.MovementController;
import com.example.snake.graphics.Renderer;
import com.example.snake.view.GameView;
import com.example.snake.view.LeaderboardView;
import com.example.snake.view.MainMenu;
import com.example.snake.utils.IOUtils;
import com.example.snake.view.OptionsView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SnakeApplication extends Application {

  // Arbitrary dimensions for now
  private static final int WINDOW_WIDTH = 640;
  private static final int WINDOW_HEIGHT = 480;

  private final MainMenu mainMenu = new MainMenu();
  private final LeaderboardView leaderboardView = new LeaderboardView();
  private final OptionsView optionsView = new OptionsView();
  private final GameView gameView = new GameView(WINDOW_WIDTH, WINDOW_HEIGHT);

  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(mainMenu.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

    setUpEventHandlers(scene);

    // Disabled resizing for now
    stage.getIcons().add(IOUtils.loadImage("/icon.png"));
    stage.setResizable(false);
    stage.setTitle("Snake Hunt");
    stage.setScene(scene);
    stage.show();
  }

  private void setUpEventHandlers(Scene scene) {
    mainMenu.onStartButtonPressed(event -> startGame(scene));
    mainMenu.onOptionsButtonPressed(event -> scene.setRoot(optionsView.getRoot()));
    mainMenu.onLeaderboardButtonPressed(event -> scene.setRoot(leaderboardView.getRoot()));

    leaderboardView.onMainMenuButtonPressed(event -> scene.setRoot(mainMenu.getRoot()));

    //(d)to go back from option view to main menu view
    optionsView.onMainMenuButtonPressed(event -> scene.setRoot(mainMenu.getRoot()));
  }

  // TODO: refactor more
  public void startGame(Scene scene) {
    scene.setRoot(gameView.getRoot());

    Renderer renderer = new Renderer(gameView.getCanvas());

    MovementController movementController = new MovementController();
    scene.setOnKeyPressed(movementController);
    scene.setOnKeyReleased(movementController);

    Game game = new Game(renderer, movementController);
    GameLoopRunner gameLoopRunner = new GameLoopRunner(delta -> {
      game.update(delta);
      gameView.setPreyLifetime(game.getFoodSpawner().getPreyLifetime());
    });
    gameLoopRunner.start();
  }

  public static void main(String[] args) {
    launch();
  }
}