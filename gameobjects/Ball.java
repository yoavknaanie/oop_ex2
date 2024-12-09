package bricker.gameobjects;

import bricker.brick_strategies.AddLifeStrategy;
import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The Ball class represents the ball in the game. It handles its movement, collision logic,
 * and interaction with game objects. The ball will change direction when colliding with objects
 * and may trigger sound effects.
*/
public class Ball extends GameObject {

    private Sound collisionSound;
    private int collisionsCounter = 0;
    private int turboCollisions = -1;

    /**
     * Constructs a new Ball object with the specified parameters.
     * @param topLeftCorner  Position of the ball in window coordinates (pixels). (0,0) is the top-left corner.
     * @param dimensions     Width and height of the ball in window coordinates.
     * @param renderable     The renderable representing the ball's visual appearance. Can be null.
     * @param collisionSound The sound to play when the ball collides with an object.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
    }

    /**
     * This method is called when a collision with another object occurs.
     * The ball's velocity is updated by flipping it along the normal of the collision surface,
     * and a collision sound is played. The collision counter is also incremented.
     * If the ball is in turbo mode, the turbo collision counter is decremented.
     * @param other      The other object involved in the collision.
     * @param collision  The collision information, including the normal direction of the collision.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        // If the ball collides with a falling heart (from AddLifeStrategy), no action is taken
        if (other.getTag().equals(AddLifeStrategy.FALLING_HEART_TAG)) {
            return;
        }

        // Flip the ball's velocity based on the collision normal
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        collisionsCounter++;
        if (turboCollisions > 0) {
            turboCollisions--;
        }
    }

    /**
     * Gets the number of collisions the ball has encountered so far.
     * @return The number of collisions the ball has had.
     */
    public int getCollisionCounter() {
        return collisionsCounter;
    }
}
