package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

public class TurboStrategy implements CollisionStrategy{
    private static final float MULT_CONST = (float) 1.4;
    private final BrickerGameManager brickerGameManager;

    public TurboStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeBrick(thisObj);
        if (otherObj.getTag().equals(brickerGameManager.BALL_TAG)) {
            otherObj.setVelocity(otherObj.getVelocity().mult(MULT_CONST));
        }
    }
}
