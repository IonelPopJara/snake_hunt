package com.example.snake;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.sound.sampled.Clip;

import com.example.snake.game.Game;
import com.example.snake.game.GameLoopRunner;
import com.example.snake.game.MovementController;
import com.example.snake.graphics.Renderer;
import com.example.snake.player.PlayerScore;
import com.example.snake.utils.IOUtils;
import com.example.snake.view.GameView;
import com.example.snake.view.LeaderboardView;
import com.example.snake.view.MainMenuView;
import com.example.snake.view.OptionsView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SnakeApplication extends Application {

  // Arbitrary dimensions for now
  private static final int WINDOW_WIDTH = 640;
  private static final int WINDOW_HEIGHT = 480;

  private final MainMenuView mainMenu = new MainMenuView();
  private final LeaderboardView leaderboardView = new LeaderboardView();
  private final OptionsView optionsView = new OptionsView();
  private final GameView gameView = new GameView(WINDOW_WIDTH, WINDOW_HEIGHT);

  private Game currentGame;
  private GameLoopRunner currentGameLoopRunner;

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
    mainMenu.onLeaderboardButtonPressed(event -> showLeaderboardView(scene));

    leaderboardView.onMainMenuButtonPressed(event -> scene.setRoot(mainMenu.getRoot()));

    optionsView.onMainMenuButtonPressed(event -> scene.setRoot(mainMenu.getRoot()));

    gameView.getGameOverView().onMainMenuButtonPressed(event -> scene.setRoot(mainMenu.getRoot()));
    gameView.getGameOverView().onStartButtonPressed(event -> startGame(scene));
    gameView.getGameOverView().setOnSubmitScoreButtonPressed(event -> saveScore());
  }

  private void showLeaderboardView(Scene scene) {
    leaderboardView.reloadScores();
    scene.setRoot(leaderboardView.getRoot());
  }

  private void saveScore() {
    List<PlayerScore> playerScores = new ArrayList<>(IOUtils.loadScores());

    String playerName = gameView.getGameOverView().getSubmittedPlayerName();
    int score = currentGame.getScore();
    PlayerScore currentPlayerScore = new PlayerScore(playerName, score);
    playerScores.add(currentPlayerScore);

    List<PlayerScore> newPlayerScores = playerScores.stream()
      .sorted(Comparator.comparing(PlayerScore::getScore).reversed())
      .limit(10)
      .toList();

    IOUtils.saveScores(newPlayerScores);
  }

  // TODO: refactor more
  public void startGame(Scene scene) {
    if (currentGameLoopRunner != null) {
      currentGameLoopRunner.stop();
    }

    scene.setRoot(gameView.getRoot());

    Renderer renderer = new Renderer(gameView.getCanvas());

    MovementController movementController = new MovementController();
    scene.setOnKeyPressed(movementController);
    scene.setOnKeyReleased(movementController);

    currentGame = new Game(renderer, movementController);
    currentGameLoopRunner = new GameLoopRunner(delta -> {
      currentGame.update(delta);
      gameView.setPreyLifetime(currentGame.getFoodSpawner().getPreyLifetime());
      gameView.setScoreLabel(currentGame.getScore());
    });

    currentGame.setOnGameOverHandle(() -> {
      gameView.getGameOverView().show();
      gameView.getGameOverView().setScoreLabel(currentGame.getScore());
    });

    currentGameLoopRunner.start();
  }

  /**
   * Music while playing game
   */
  public static void playBackgroundMusic() {
    // TODO: Loop music
    // TODO: See if this is the proper way of using background music.
    try {
//      Clip clip = IOUtils.loadAudioClip("/BackgroundMusic.wav");
      Clip clip = IOUtils.loadAudioClip("/background-music.wav");
      clip.start();
      clip.loop(0);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public static void main(String[] args) {
    launch();
  }
}