package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * The UserPaddle class represents the player's paddle in the game.
 * The paddle can be moved left and right using keyboard inputs (arrow keys).
 * It also respects the screen boundaries, ensuring that the paddle does not move outside the allowed borders.
 */
public class UserPaddle extends GameObject {

    private static final float MOVEMENT_SPEED = 400;
    private final UserInputListener inputListener;
    private final int leftBorder;
    private final int rightBorder;

    /**
     * Constructs a new UserPaddle object.
     *
     * @param topLeftCorner Position of the paddle in window coordinates (pixels).
     *                      (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height of the paddle in window coordinates.
     * @param renderable    The renderable representing the paddle's visual appearance.
     * @param inputListener The input listener that listens for user keyboard events.
     * @param leftBorder    The left boundary that restricts the paddle's movement.
     * @param rightBorder   The right boundary that restricts the paddle's movement.
     */
    public UserPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                      Renderable renderable, UserInputListener inputListener, int leftBorder,
                      int rightBorder) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    /**
     * Updates the position of the paddle based on user input.
     * The paddle will move left or right when the respective arrow keys are pressed,
     * while staying within the defined screen boundaries.
     * @param deltaTime The time elapsed since the last frame, used for smooth movement.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;

        boolean inLeftwise = this.getTopLeftCorner().x() > this.leftBorder;
        boolean inRightwise = this.getTopLeftCorner().x() + getDimensions().x() < this.rightBorder;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT) && inLeftwise) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && inRightwise) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
