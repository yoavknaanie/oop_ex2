package bricker.brick_strategies;
import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;

public class AddPuckStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;

    public AddPuckStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeObject(thisObj);
        brickerGameManager.createPucks();
    }
}
