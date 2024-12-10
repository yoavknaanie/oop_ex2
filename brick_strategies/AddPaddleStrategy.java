package bricker.brick_strategies;

import bricker.BrickerGameManager;
import bricker.gameobjects.ExtraPaddle;
import bricker.gameobjects.UserPaddle;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The AddPaddleStrategy class implements the CollisionStrategy interface and is responsible
 * for adding an extra paddle to the game when a specific collision occurs.
 * It ensures that an ExtraPaddle object is created if it doesn't already exist, or
 * resets the hit counter if it does.
 */
public class AddPaddleStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;

    /**
     * Constructs an AddPaddleStrategy object.
     * @param brickerGameManager The game manager responsible for managing game objects and game state.
     */
    public AddPaddleStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Handles the collision between the game objects. When a brick collides with another object,
     * it removes the brick from the game world and checks if an ExtraPaddle needs to be created or reset.
     * @param thisObj The game object that is part of the collision (usually a brick).
     * @param otherObj The other game object involved in the collision (typically a paddle-related object).
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeBrick(thisObj);
        checkIfIsThereExtraPaddle();
    }

    private void checkIfIsThereExtraPaddle(){
        ExtraPaddle extraPaddle = brickerGameManager.getExtraPaddle();
        if (extraPaddle == null) {
            brickerGameManager.createExtraPaddle();
        }
        else {
            extraPaddle.resetHits();
        }
    }
}
