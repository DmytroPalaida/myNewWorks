package game;

import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.util.RandomGenerator;
import com.shpp.cs.a.graphics.WindowProgram;

import java.awt.*;
import java.awt.event.*;

/**
 * The BreakoutGame class implements a simple breakout game where the player
 * controls a paddle to bounce a ball and break bricks.
 */

public class BreakoutGame extends WindowProgram implements Constant{

    // Instance variables
    private final GRect paddle = createPaddle();          // Paddle object
    private double vx, vy;                              // Velocity components of the ball
    private int bricksOnStage = N_BRICK_ROWS * N_BRICKS_PER_ROW; // Number of bricks currently on the stage

    /**
     * Initializes the game and runs it.
     */
    public void run() {
        addMouseListeners();
        startGame();
    }

    /**
     * Responds to mouse movement events by updating the position of the paddle.
     *
     * @param e The MouseEvent containing information about the mouse movement.
     */
    public void mouseMoved(MouseEvent e) {
        int mousePositionX = e.getX();
        int paddleX = mousePositionX - PADDLE_WIDTH / 2;
        int paddleY = getHeight() - PADDLE_Y_OFFSET;
        movePaddleToPosition(paddleX, paddleY);
    }

    /**
     * Moves the paddle's position to the specified coordinates (paddleX, paddleY).
     * Ensures that the paddle remains within the horizontal bounds of the game window.
     *
     * @param paddleX The desired X-coordinate for the paddle.
     * @param paddleY The Y-coordinate for the paddle (usually the bottom of the window).
     */
    private void movePaddleToPosition(int paddleX, int paddleY) {
        int minX = 0;
        int maxX = getWidth() - PADDLE_WIDTH;
        paddleX = Math.max(minX, Math.min(paddleX, maxX));
        paddle.setLocation(paddleX, paddleY);
    }


    /**
     * Sets the initial speed of the ball by generating random velocities for the horizontal (vx) and vertical
     * (vy) directions.
     * The horizontal velocity (vx) is randomly chosen between 1.0 and 3.0, and its sign may be negated randomly.
     * The vertical velocity (vy) is set to a constant value (BALL_SPEED).
     */
    private void setRandomBallSpeed() {
        RandomGenerator rgen = RandomGenerator.getInstance();
        vx = rgen.nextDouble(1.0, 3.0);
        if (rgen.nextBoolean(0.5)) {
            vx = -vx;
        }
        vy = BALL_SPEED;
    }


    /**
     * Initializes and manages the game loop, allowing the player a limited number of attempts to win.
     * - Draws the bricks on the game screen.
     * - Displays a label informing the player about the number of attempts remaining.
     * - Enters a loop where the player has a limited number of attempts (NUM_TURNS) and there are bricks left.
     * - Adds the paddle to the game screen.
     * - Sets the ball's initial speed.
     * - Draws the ball on the game screen.
     * - Shows a label with the number of attempts remaining.
     * - Waits for the player to click to start the round.
     * - Calls the playRound method to handle the current round.
     * - Decrements the count of remaining attempts.
     * - Displays a "GAME OVER" label when the game loop ends (when the player runs out of attempts or clears all bricks).
     */
    private void startGame() {
        drawBrickLayout();
        int countAttempts = NUM_TURNS;
        showLabelWithTimer("YOU HAVE THREE ATTEMPTS TO WIN!!! ", getRandomColor());
        while (countAttempts > 0 && bricksOnStage > 0) {
            add(paddle);
            setRandomBallSpeed();
            GOval ball = createBall();
            showLabelWithTimer("Attempts left: " + countAttempts, getRandomColor());
            waitForClick();
            playRound(ball);
            countAttempts--;
        }
        showLabelWithTimer("GAME OVER", getRandomColor());
    }

    /**
     * Manages a single round of the game where the ball interacts with bricks and the paddle.
     * - Enters a loop that continues until the round is over (when gameOver is false).
     * - Checks for collisions between the ball and bricks using the collideObject method.
     * - Handles collisions with stage limits (walls and ceiling) using the collideStageLimits method.
     * - Checks if the ball has missed the paddle using the hasMissedPaddle method.
     * - If the ball has missed the paddle:
     * - Displays "WASTED" message.
     * - Clears the stage, removing the ball.
     * - Breaks out of the loop to end the round.
     * - Moves the ball based on its velocity (vx, vy).
     * - Checks if there are any bricks left using the areBricksLeft method.
     * - If there are no bricks left:
     * - Displays "YOU WIN!" message.
     * - Clears the stage, removing the ball.
     * - Sets gameOver to true to indicate that the game has been won.
     * - Pauses for a short duration (PAUSE_TIME) to control the animation speed.
     *
     * @param ball The GOval representing the game ball.
     */
    private void playRound(GOval ball) {
        boolean gameOver = false;
        while (!gameOver) {
            bricksOnStage = collideObject(ball, bricksOnStage);
            collideWithStageLimits(ball);
            if (hasMissedPaddle(ball)) {
                showLabelWithTimer("WASTED", getRandomColor());
                clearStage(ball);
                break;
            }
            ball.move(vx, vy);
            if (areBricksLeft(bricksOnStage)) {
                showLabelWithTimer("YOU WIN!", getRandomColor());
                clearStage(ball);
                gameOver = true;
            }
            pause(PAUSE_TIME);
        }
    }


    /**
     * Detects and handles collisions between the ball and game objects (paddle and bricks).
     * - Takes a GOval ball and the current count of bricks on the stage as parameters.
     * - Retrieves the object (collider) that the ball collides with using the getCollidingObject method.
     * - Checks if the collider is the paddle and the ball is moving downward (vy > 0).
     * - If true, the ball's vertical velocity (vy) is reversed, causing it to bounce off the paddle.
     * - Checks if the collider is a brick.
     * - If true, removes the brick from the stage using the remove method.
     * - Reverses the ball's vertical velocity (vy), making it bounce.
     * - Decrements the count of bricks on the stage (bricksOnStage) by 1.
     * - Returns the updated count of bricks on the stage after handling collisions.
     *
     * @param ball          The GOval representing the game ball.
     * @param bricksOnStage The current count of bricks remaining on the stage.
     * @return The updated count of bricks on the stage after handling collisions.
     */
    private int collideObject(GOval ball, int bricksOnStage) {
        GObject collider = checkCollisionsWithBall(ball);

        if (isPaddle(collider) && vy > 0) {
            vy = -vy;
        } else if (isBrick(collider)) {
            remove(collider);
            vy = -vy;
            bricksOnStage -= 1;
            changeBallColor(ball);
        }

        return bricksOnStage;
    }


    /**
     * Generates and returns a random color.
     * - Utilizes the RandomGenerator class to create random RGB values for the color.
     *
     * @return A randomly generated Color object.
     */
    private Color getRandomColor() {
        RandomGenerator rgen = RandomGenerator.getInstance();
        return new Color(rgen.nextInt(256), rgen.nextInt(256), rgen.nextInt(256));
    }

    /**
     * Changes the color of the specified GOval object to a random color.
     * - Accepts a GOval object (ball) as a parameter.
     * - Checks if the ball is not null.
     * - If true, sets the color of the ball to a randomly generated color using getRandomColor.
     *
     * @param ball The GOval object whose color will be changed.
     */
    private void changeBallColor(GOval ball) {
        if (ball != null) {
            ball.setColor(getRandomColor());
        }
    }

    private void drawBrickLayout() {
        int brickWidth = (getWidth() - (N_BRICKS_PER_ROW - 1) * BRICK_SEP) / N_BRICKS_PER_ROW;
        int topLeftY = BRICK_Y_OFFSET;

        for (int i = 0; i < N_BRICK_ROWS; i++) {
            // Get the color for the current row of bricks (alternating colors)
            Color brickColor = getBrickColor(i / 2);
            drawRowOfBricks(topLeftY, brickWidth, brickColor);
            topLeftY += BRICK_HEIGHT + BRICK_SEP;
        }
    }

    private void drawRowOfBricks(int y, int brickWidth, Color color) {
        for (int j = 0; j < Constant.N_BRICKS_PER_ROW; j++) {
            drawBrick((brickWidth + BRICK_SEP) * j, y, brickWidth, color);
        }
    }

    private void drawBrick(int x, int y, int width, Color color) {
        GRect brick = new GRect(x, y, width, Constant.BRICK_HEIGHT);
        brick.setFilled(true);
        brick.setColor(color);
        add(brick);
    }

    /**
     * Returns the color for a given row of bricks based on alternating colors.
     *
     * @param row The row number for which to determine the color.
     * @return The color for the specified row.
     */
    private Color getBrickColor(int row) {
        Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};
        return colors[row % colors.length];
    }

    /**
     * Checks for collisions with different parts of the ball and returns the colliding object.
     *
     * @param ball The ball to check for collisions.
     * @return The colliding object or null if there is no collision.
     */
    private GObject checkCollisionsWithBall(GOval ball) {
        // Get the current position and diameter of the ball
        double x = ball.getX();
        double y = ball.getY();
        double diameter = 2 * BALL_RADIUS;

        // Check for collisions with the four corners of the ball
        GObject leftTop = getElementAt(x, y);
        GObject rightTop = getElementAt(x + diameter, y);
        GObject leftBottom = getElementAt(x, y + diameter);
        GObject rightBottom = getElementAt(x + diameter, y + diameter);

        // Return the first non-null colliding object, or null if no collision
        if (leftTop != null) {
            return leftTop;
        } else if (rightTop != null) {
            return rightTop;
        } else if (leftBottom != null) {
            return leftBottom;
        } else {
            return rightBottom;
        }
    }


    /**
     * Creates and returns a paddle (rectangular platform) with the specified dimensions and appearance.
     *
     * @return The GRect representing the paddle.
     */
    private GRect createPaddle() {
        // Calculate the initial X and Y coordinates for the paddle
        int paddleX = (getWidth() - PADDLE_WIDTH) / 2;
        int paddleY = (getHeight() - PADDLE_Y_OFFSET);

        // Create the paddle GRect object
        GRect rect = new GRect(paddleX, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Set the paddle appearance
        rect.setFilled(true);
        rect.setColor(Color.BLACK);

        // Return the created paddle
        return rect;
    }


    /**
     * Creates and returns a GOval representing the game ball with the specified dimensions and appearance.
     *
     * @return The GOval representing the game ball.
     */
    private GOval createBall() {
        // Calculate the initial position and dimensions for the ball
        double ballX = getWidth() / 2.0 - BALL_RADIUS;
        double ballY = getHeight() / 2.0 - BALL_RADIUS;
        double ballDiameter = 2 * BALL_RADIUS;

        GOval ball = new GOval(ballX, ballY, ballDiameter, ballDiameter);
        ball.setFilled(true);
        ball.setColor(Color.gray);
        add(ball);
        return ball;
    }

    /**
     * Checks if there are any bricks left on the stage.
     *
     * @param bricksOnStage The number of bricks on the stage.
     * @return true if there are no bricks left, false otherwise.
     */
    private boolean areBricksLeft(int bricksOnStage) {
        return bricksOnStage == 0;
    }

    /**
     * Clears the game stage by removing the ball and paddle from the canvas.
     *
     * @param ball The ball object to be removed.
     */
    private void clearStage(GOval ball) {
        remove(ball);
        remove(paddle);
    }

    /**
     * Displays a label with the specified text and color, centered on the canvas,
     * and removes it after a specified time interval.
     *
     * @param text  The text to be displayed on the label.
     * @param color The color of the label's text.
     */
    private void showLabelWithTimer(String text, Color color) {
        GLabel label = createCenteredLabel(text, color);
        add(label);
        pause(FREEZE_TIME);
        remove(label);
    }

    /**
     * Creates a GLabel with the specified text and color, centered on the canvas.
     *
     * @param text  The text to be displayed on the label.
     * @param color The color of the label's text.
     * @return The created GLabel.
     */
    private GLabel createCenteredLabel(String text, Color color) {
        GLabel label = new GLabel(text);
        label.setFont("Arial-" + calculateFontSize(text));
        label.setColor(color);

        // Position the label at the center of the canvas
        double x = (getWidth() - label.getWidth()) / 2;
        double y = (getHeight() - label.getHeight()) / 2;
        label.setLocation(x, y);

        return label;
    }

    /**
     * Calculates and returns an appropriate font size for displaying the given text
     * within the canvas bounds while leaving some margin.
     *
     * @param text The text for which the font size needs to be determined.
     * @return The calculated font size that fits the text within canvas bounds.
     */
    private int calculateFontSize(String text) {
        int fontSize = 60; // Initial font size to start with
        int maxWidth = getWidth() - 20; // Maximum width within canvas bounds with a margin
        int maxHeight = getHeight() - 20; // Maximum height within canvas bounds with a margin

        while (true) {
            GLabel tempLabel = new GLabel(text);
            tempLabel.setFont("Arial-" + fontSize);

            if (tempLabel.getBounds().getWidth() <= maxWidth && tempLabel.getBounds().getHeight() <= maxHeight) {
                return fontSize; // Return the calculated font size
            }

            fontSize--; // Decrease the font size if the label doesn't fit
        }
    }


    /**
     * Checks if the given GObject is a paddle object.
     *
     * @param object The GObject to be checked.
     * @return True if the object is a paddle, otherwise false.
     */
    private boolean isPaddle(GObject object) {
        return (object == paddle);
    }

    /**
     * Checks if the given GObject represents a brick object.
     *
     * @param object The GObject to be checked.
     * @return True if the object is a brick, otherwise false.
     */
    private boolean isBrick(GObject object) {
        return object != null && object != paddle;
    }

    /**
     * Checks for collisions between the ball and the stage limits and updates the ball's velocity accordingly.
     *
     * @param ball The ball object to check for collisions.
     */
    private void collideWithStageLimits(GOval ball) {
        if (isBallCollidingWithCeiling(ball)) {
            vy = -vy;
        }
        if (isBallCollidingWithRightWall(ball) || isBallCollidingWithLeftWall(ball)) {
            vx = -vx;
        }
    }

    /**
     * Checks if the ball is colliding with the left wall of the stage.
     *
     * @param ball The ball object to check for collisions.
     * @return True if the ball is colliding with the left wall, otherwise false.
     */
    private boolean isBallCollidingWithLeftWall(GOval ball) {
        return ball.getX() <= 0;
    }

    /**
     * Checks if the ball is colliding with the right wall of the stage.
     *
     * @param ball The ball object to check for collisions.
     * @return True if the ball is colliding with the right wall, otherwise false.
     */
    private boolean isBallCollidingWithRightWall(GOval ball) {
        return ball.getX() + 2 * BALL_RADIUS >= getWidth();
    }

    /**
     * Checks if the ball is colliding with the ceiling of the stage.
     *
     * @param ball The ball object to check for collisions.
     * @return True if the ball is colliding with the ceiling, otherwise false.
     */
    private boolean isBallCollidingWithCeiling(GOval ball) {
        return ball.getY() <= 0;
    }

    /**
     * Checks if the ball has missed the paddle and reached the bottom of the stage.
     *
     * @param ball The ball object to check.
     * @return True if the ball has missed the paddle, otherwise false.
     */
    private boolean hasMissedPaddle(GOval ball) {
        return ball.getY() + 2 * BALL_RADIUS >= getHeight();
    }
}
