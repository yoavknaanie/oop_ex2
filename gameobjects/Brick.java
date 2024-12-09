package bricker.gameobjects;

import bricker.BrickerGameManager;
import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The Brick class represents a brick in the game. It handles the collision behavior and
 * interaction with other game objects. The collision strategy defines how the brick should
 * respond to collisions, and different types of bricks can implement their own behavior.
 */
public class Brick extends GameObject {
    private final CollisionStrategy collisionStrategy;

    /**
     * Constructs a new Brick object with the specified parameters.
     * @param topLeftCorner   Position of the brick in window coordinates (pixels). (0,0) is the top-left corner.
     * @param dimensions      Width and height of the brick in window coordinates.
     * @param renderable      The renderable representing the brick's visual appearance. Can be null.
     * @param collisionStrategy The strategy that defines the behavior when the brick collides with another object.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    /**
     * This method is called when a collision with another object occurs. It delegates the
     * collision handling to the collision strategy, which defines the specific response.
     * @param other      The other object involved in the collision.
     * @param collision  The collision information, including the normal direction of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }

    /**
     * This method checks whether the brick should collide with the given object.
     * It prevents the brick from colliding with other bricks by checking the tag.
     * @param other The other object to check for a collision with.
     * @return True if the brick should collide with the other object, otherwise false.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return !(other.getTag().equals(BrickerGameManager.BRICK_TAG));
    }
}
