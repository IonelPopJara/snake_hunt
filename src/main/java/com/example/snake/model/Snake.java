package com.example.snake.model;

import com.example.snake.game.Direction;
import com.example.snake.game.FoodSpawner;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Snake {

  private final List<GridPoint> snakeBody;
  private final float moveInterval;

  private Direction direction;
  private float movementTimer = 0.0f;

  /**
   * @param movementSpeed Number of times the snake moves per second
   */
  public Snake(List<GridPoint> snakeBody, Direction initialDirection, float movementSpeed) {
    if (snakeBody.size() < 2) {
      throw new IllegalArgumentException("Snake must have at least 2 body parts - a head and a tail");
    }

    this.snakeBody = new LinkedList<>(snakeBody);
    this.direction = initialDirection;
    this.moveInterval = 1.0f / movementSpeed;
  }

  public int getSize() {
    return snakeBody.size();
  }

  public GridPoint getHead() {
    return snakeBody.get(0);
  }

  public GridPoint getPoint(int index) {
    return snakeBody.get(index);
  }

  public void update(float delta, FoodSpawner foodSpawner, int gameFieldWidth, int gameFieldHeight) {
    movementTimer += delta;
    if (movementTimer >= moveInterval) {
      movementTimer -= moveInterval;

      moveSnake(gameFieldWidth, gameFieldHeight);
      handleFood(foodSpawner);
      checkCollisions();
    }
  }

  private void moveSnake(int gameFieldWidth, int gameFieldHeight) {
    GridPoint nextPosition = calculateNextPosition(direction, gameFieldWidth, gameFieldHeight);
    snakeBody.add(0, nextPosition);
  }

  private void handleFood(FoodSpawner foodSpawner) {
    Food foodEaten = checkFood(foodSpawner.getFoods());

    if (foodEaten == null) {
      snakeBody.remove(snakeBody.size() - 1);
    } else {
      foodSpawner.removeFood(foodEaten);
    }
  }

  private Food checkFood(Collection<Food> foods) {
    for (Food food : foods) {
      if (food.getPosition().equals(getHead())) {
        return food;
      }
    }
    return null;
  }

  public void setDirection(Direction direction, int gameFieldWidth, int gameFieldHeight) {
    // Calculate what the next position would be, if we were to move in the given direction
    GridPoint nextHypotheticalPosition = calculateNextPosition(direction, gameFieldWidth, gameFieldHeight);

    // If that position is the same as the second part of the snake, then the snake would move
    // back into itself, which we do not want to happen
    GridPoint secondBodyPoint = snakeBody.get(1);
    if (!nextHypotheticalPosition.equals(secondBodyPoint)) {
      this.direction = direction;
    }
  }

  private GridPoint calculateNextPosition(Direction direction, int gameFieldWidth, int gameFieldHeight) {
    // same calculations as in the update method
    return getHead().plus(direction.getDirectionVector()).plusAndMod(gameFieldWidth, gameFieldHeight);
  }

  public void checkCollisions() {
    //check if head collides with body
    for (int i = (snakeBody.size() - 1); i > 0; i--) {
      if (snakeBody.get(0).equals(snakeBody.get(i))) {
        System.out.println("Game Over");
      }
    }
  }
}
