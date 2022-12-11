package com.example.snake.graphics;

import com.example.snake.model.Food;
import com.example.snake.model.GridPoint;
import com.example.snake.model.Snake;
import com.example.snake.utils.IOUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Collection;

public class Renderer {

  private static final boolean DRAW_GRID = true;

  private final Canvas canvas;

  public Renderer(Canvas canvas) {
    this.canvas = canvas;
  }

  /**
   * @param gameFieldWidth  The width of the game field in the number of grid cells
   * @param gameFieldHeight The height of the game field in the number of grid cells
   */
  public void draw(int gameFieldWidth, int gameFieldHeight, Snake snake, Collection<Food> foods) {
    GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();
    double cellWidth = canvas.getWidth() / gameFieldWidth;
    double cellHeight = canvas.getHeight() / gameFieldHeight;

    // Set the background to pure black. Done by filling with a black rectangle since the clear color
    // in JavaFX seems to be white
    graphicsContext2D.setFill(Color.BLACK);
    graphicsContext2D.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    // Draw a grid to help visualize for debugging purposes
    if (DRAW_GRID) {
      drawGrid(graphicsContext2D, gameFieldWidth, gameFieldHeight, cellWidth, cellHeight);
    }

    drawSnake(graphicsContext2D, snake, cellWidth, cellHeight);
    drawFood(graphicsContext2D, foods, cellWidth, cellHeight);
  }

  private void drawGrid(GraphicsContext graphicsContext2D,
                        int gameFieldWidth,
                        int gameFieldHeight,
                        double cellWidth,
                        double cellHeight) {
    graphicsContext2D.setStroke(Color.WHITE);

    for (int x = 0; x < gameFieldWidth; x++) {
      graphicsContext2D.strokeLine(x * cellWidth, 0, x * cellWidth, canvas.getHeight());
    }
    for (int y = 0; y < gameFieldHeight; y++) {
      graphicsContext2D.strokeLine(0, y * cellHeight, canvas.getWidth(), y * cellWidth);
    }
  }

  private static void drawSnake(GraphicsContext graphicsContext2D,
                                Snake snake,
                                double cellWidth,
                                double cellHeight) {
    Image SnakeHead = IOUtils.loadImage("/SnakeHead.png");
    // graphicsContext2D.setFill(Color.RED);
    // Draw the head in a different color. It's the first element of the list
    GridPoint head = snake.getHead();
    graphicsContext2D.drawImage(SnakeHead, head.x() * cellWidth, head.y() * cellHeight, cellWidth, cellHeight);
    Image SnakeBody = IOUtils.loadImage("/SnakeBody.png");
    for (int i = 1; i < snake.getSize(); i++) {
      GridPoint bodyPart = snake.getPoint(i);
      graphicsContext2D.drawImage(SnakeBody, bodyPart.x() * cellWidth, bodyPart.y() * cellHeight, cellWidth, cellHeight);
    }
  }

  private static void drawFood(GraphicsContext graphicsContext2D,
                               Collection<Food> foods,
                               double cellWidth,
                               double cellHeight) {
    Image heartFood = IOUtils.loadImage("/FoodBox.png");
    for (Food foodPosition : foods) {
      GridPoint position = foodPosition.getPosition();
      graphicsContext2D.drawImage(heartFood, position.x() * cellWidth, position.y() * cellHeight, cellWidth, cellHeight);
    }
  }
}
