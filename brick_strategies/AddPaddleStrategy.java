package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

public class AddPaddleStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;

    public AddPaddleStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeObject(thisObj);
        brickerGameManager.createPucks();
    }
}

