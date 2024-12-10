package bricker.brick_strategies;

import bricker.BrickerGameManager;
import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.gui.rendering.ImageRenderable;

/**
 * The TurboStrategy class implements the CollisionStrategy interface and is responsible
 * for handling the activation of turbo mode when a collision with a specific game object (ball) occurs.
 * Upon collision, the strategy removes the brick and triggers turbo mode in the game if the object colliding
 * with the brick is identified as the ball.
 */
public class TurboStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;
    /**
     * Constructs a TurboStrategy object that is tied to a specific game manager.
     * @param brickerGameManager The game manager responsible for managing the game state.
     */
    public TurboStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between a brick and another game object. If the colliding object is the ball,
     * turbo mode is activated by invoking the startTurbo method on the game manager.
     * The brick involved in the collision is removed from the game.
     * @param thisObj The game object representing the brick that is involved in the collision.
     * @param otherObj The other game object involved in the collision (typically a ball).
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeBrick(thisObj);
        if (otherObj.getTag().equals(brickerGameManager.BALL_TAG)) {
            brickerGameManager.startTurbo();
        }
    }
}
