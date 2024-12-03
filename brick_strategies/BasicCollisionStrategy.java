package bricker.brick_strategies;
import bricker.BrickerGameManager;
import danogl.GameManager;
import danogl.GameObject;

public class BasicCollisionStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;
    public BasicCollisionStrategy(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }
    public void onCollision(GameObject thisObj, GameObject otherObj){
        brickerGameManager.removeObject(thisObj);
    }
}

