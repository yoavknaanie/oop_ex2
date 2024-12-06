package bricker.brick_strategies;

import bricker.BrickerGameManager;
import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.gui.rendering.ImageRenderable;

public class TurboStrategy implements CollisionStrategy{
    private static final float MULT_CONST = (float) 1.4;
    private final BrickerGameManager brickerGameManager;
    private static final String RED_BALL_PATH = "assets/redball.png";
    private final ImageRenderable redBallImage;

    public TurboStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
        this.redBallImage = this.brickerGameManager.getImageReader().readImage(RED_BALL_PATH, true);
    }
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeBrick(thisObj);
        if (otherObj.getTag().equals(brickerGameManager.BALL_TAG)) {
            otherObj.setVelocity(otherObj.getVelocity().mult(MULT_CONST));
            otherObj.renderer().setRenderable(redBallImage);
            int collisions = ((Ball) otherObj).getCollisionCounter();
        }
    }
}
