package bricker.brick_strategies;

import bricker.BrickerGameManager;
import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.gui.rendering.ImageRenderable;

public class TurboStrategy implements CollisionStrategy{
    private final BrickerGameManager brickerGameManager;

    public TurboStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeBrick(thisObj);
//        check who have collided with the brick
        if (otherObj.getTag().equals(brickerGameManager.BALL_TAG)) {
            brickerGameManager.startTurbo();
        }
    }
}
