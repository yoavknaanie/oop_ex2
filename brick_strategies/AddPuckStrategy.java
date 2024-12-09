package bricker.brick_strategies;
import bricker.BrickerGameManager;
import danogl.GameObject;

public class AddPuckStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;

    public AddPuckStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.createPucks(thisObj.getCenter());
        brickerGameManager.removeBrick(thisObj);
    }
}
