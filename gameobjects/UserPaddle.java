package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class UserPaddle extends GameObject {
    private static final float MOVEMENT_SPEED = 400;
    private final UserInputListener inputListener;
    private final int leftBorder;
    private final int rightBorder;

    /**
     * Construct a new GameObject instance.
     *  @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param inputListener
     */
    public UserPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                      Renderable renderable, UserInputListener inputListener, int leftBorder,
                      int rightBorder) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.leftBorder = leftBorder;
        this.rightBorder = rightBorder;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        
        boolean inLeftwise = this.getTopLeftCorner().x() > this.leftBorder;
        boolean inRightwise =this.getTopLeftCorner().x() + getDimensions().x() < this.rightBorder;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && inLeftwise) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && inRightwise) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
