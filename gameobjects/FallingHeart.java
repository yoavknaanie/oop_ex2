package bricker.gameobjects;

import bricker.BrickerGameManager;
import bricker.brick_strategies.AddLifeStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The FallingHeart class represents a falling heart object that grants the player an extra life
 * when it collides with the user's paddle. It uses an AddLifeStrategy to define the behavior of
 * adding a life and removing the heart object from the game after the collision.
 */
public class FallingHeart extends GameObject {
    private final AddLifeStrategy addLifeStrategy;

    /**
     * Constructs a new FallingHeart object with the specified parameters.
     * @param topLeftCorner   Position of the heart in window coordinates (pixels). (0,0) is the top-left corner.
     * @param dimensions      Width and height of the heart in window coordinates.
     * @param renderable      The renderable representing the visual appearance of the heart.
     * @param addLifeStrategy The strategy that defines how to add a life and remove the heart after collision.
     */
    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        AddLifeStrategy addLifeStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.addLifeStrategy = addLifeStrategy;
    }

    /**
     * This method is called when a collision with another object occurs. If the heart collides with
     * the user's paddle, it grants an extra life to the player and removes the heart object from the game.
     * @param other      The other object involved in the collision.
     * @param collision  The collision information, including the normal direction of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // Check if the heart collides with the user's paddle.
        if (other.getTag().equals(BrickerGameManager.USER_PADDLE_TAG)) {
            this.addLifeStrategy.addLife();  // Add a life to the player.
            this.addLifeStrategy.removeHeart(this);  // Remove the heart from the game.
        }
    }
}
