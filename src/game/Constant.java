package game;

public interface Constant{
    // Constants for the application dimensions
    int APPLICATION_WIDTH = 400;
    int APPLICATION_HEIGHT = 600;

    int PADDLE_WIDTH = 80;        // Width of the paddle
    int PADDLE_HEIGHT = 10;        // Height of the paddleprivate static final int PADDLE_Y_OFFSET = 30;     // Offset of the paddle from the bottom
    int N_BRICK_ROWS = 10;          // Number of rows of bricks
    int N_BRICKS_PER_ROW = 10;      // Number of bricks per row
    int BRICK_SEP = 2;             // Separation between bricks
    int PADDLE_Y_OFFSET = 30;     // Offset of the paddle from the bottom
    int BRICK_HEIGHT = 8;          // Height of a brick
    int BALL_RADIUS = 10;          // Radius of the ball
    int BRICK_Y_OFFSET = 70;       // Vertical offset for the bricks
    int NUM_TURNS = 3;             // Number of turns or attempts for the player
    int FREEZE_TIME = 1500;         // Freeze time after displaying messages (in milliseconds)
    int PAUSE_TIME = 10;            // Pause time for animations (in milliseconds)
    double BALL_SPEED = 2.0;       // Speed of the ball
}
