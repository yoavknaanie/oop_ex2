package bricker.brick_strategies;

import bricker.BrickerGameManager;
import bricker.LifeManager;
import bricker.gameobjects.FallingHeart;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Vector2;

/**
 * The AddLifeStrategy class implements the CollisionStrategy interface and is responsible
 * for handling the addition of lives in the game when a falling heart is collected.
 * It creates and manages a FallingHeart object that is added to the game world upon collision
 * and increases the player's life count through the LifeManager.
 */
public class AddLifeStrategy implements CollisionStrategy {
    public static final String FALLING_HEART_TAG = "falling_heart_tag";
    private static final Vector2 FALLING_HEART_VELOCITY = new Vector2(0, 100);
    private final BrickerGameManager brickerGameManager;
    private FallingHeart fallingHeart;
    /**
     * Constructs an AddLifeStrategy object.
     * @param brickerGameManager The game manager responsible for managing game objects and game state.
     */
    public AddLifeStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Adds a life to the player by removing the current falling heart object
     * and calling the LifeManager's addLife method to increase the player's life count.
     */
    public void addLife() {
        this.brickerGameManager.removeObject(this.fallingHeart);
        brickerGameManager.getLifeManager().addLife();
    }

    /**
     * Handles the collision between two game objects. When a brick collides with an object
     * that triggers this strategy, a FallingHeart object is created, added to the game world,
     * and falls downwards. The brick that collided is removed from the game.
     *
     * @param thisObj The game object that is part of the collision (usually a brick).
     * @param otherObj The other game object involved in the collision (typically a heart-related object).
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        this.fallingHeart = new FallingHeart(thisObj.getCenter(),
                LifeManager.HEART_DIM,
                brickerGameManager.getLifeManager().getHeartImage(),
                this);
        fallingHeart.setTag(FALLING_HEART_TAG);
        brickerGameManager.addGameObject(fallingHeart, Layer.DEFAULT);
        fallingHeart.setVelocity(FALLING_HEART_VELOCITY);
        brickerGameManager.removeBrick(thisObj);
    }

    /**
     * Removes the specified FallingHeart object from the game world.
     * @param fallingHeart The FallingHeart object to be removed from the game world.
     */
    public void removeHeart(FallingHeart fallingHeart) {
        this.brickerGameManager.removeObject(fallingHeart);
    }
}
