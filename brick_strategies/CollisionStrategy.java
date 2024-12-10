package bricker.brick_strategies;

import danogl.GameObject;

/**
 * The CollisionStrategy interface defines the contract for handling collision logic
 * in the game. Any class that implements this interface must provide a specific behavior
 * for when a collision occurs between two game objects.
 */
public interface CollisionStrategy {
    /**
     * This method is called when a collision occurs between two game objects.
     * The implementing class should define the specific actions to take upon collision.
     * @param thisObj The game object that is part of the collision (the object that is calling this method).
     * @param otherObj The other game object involved in the collision (the object that thisObj collided with).
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}
