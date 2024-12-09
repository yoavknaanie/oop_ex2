package bricker.gameobjects;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The ExtraPaddle class extends UserPaddle and represents an extra paddle in the game,
 * which can be destroyed after a number of hits by a ball or a puck.
 */
public class ExtraPaddle extends UserPaddle {
    private static final int INITIAL_NUMBER_OF_HITS = 4;
    private static final int MINUS_HIT = -1;
    private danogl.util.Counter hits = new danogl.util.Counter(INITIAL_NUMBER_OF_HITS);
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs an ExtraPaddle object, extending the functionality of the UserPaddle class.
     * @param topLeftCorner   Position of the extra paddle in window coordinates (pixels).
     *                        (0,0) is the top-left corner of the window.
     * @param dimensions      Width and height of the extra paddle in window coordinates.
     * @param renderable      The renderable representing the extra paddle's visual appearance.
     * @param inputListener   The input listener that listens for user keyboard events.
     * @param leftBorder      The left boundary that restricts the extra paddle's movement.
     * @param rightBorder     The right boundary that restricts the extra paddle's movement.
     * @param brickerGameManager The game manager responsible for managing the game state.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                       Renderable renderable, UserInputListener inputListener, int leftBorder,
                       int rightBorder, BrickerGameManager brickerGameManager) {
        super(topLeftCorner, dimensions, renderable, inputListener, leftBorder, rightBorder);
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * This method is called when a collision with another object occurs.
     * If the extra paddle collides with a pack or a ball, the hit counter is decreased.
     * If the counter reaches 0, the extra paddle is removed from the game.
     *
     * @param other      The other object involved in the collision.
     * @param collision  The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(brickerGameManager.PACK_TAG) ||
                other.getTag().equals(brickerGameManager.BALL_TAG)) {
            if (hits.value() == 1) {
                brickerGameManager.removeObject(this);
            } else {
                hits.increaseBy(MINUS_HIT);
            }
        }
    }

    /**
     * Resets the number of hits the extra paddle can take back to its initial value.
     */
    public void resetHits() {
        hits = new danogl.util.Counter(INITIAL_NUMBER_OF_HITS);
    }
}
