package bricker.brick_strategies;

import bricker.BrickerGameManager;
import bricker.gameobjects.FallingHeart;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.util.Vector2;

public class AddLifeStrategy implements CollisionStrategy {
    public static final String FALLING_HEART_TAG = "falling_heart_tag";
    private static final Vector2 FALLING_HEART_VELOCITY = new Vector2(0, 100);
    private final BrickerGameManager brickerGameManager;
    private FallingHeart fallingHeart;

    public AddLifeStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }

    public void addLife() {
        this.brickerGameManager.removeObject(this.fallingHeart);
        this.brickerGameManager.addLife();
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        this.fallingHeart = new FallingHeart(thisObj.getCenter(),
                brickerGameManager.HEART_DIM,
                brickerGameManager.getHeartImage(),
                this);
        fallingHeart.setTag(FALLING_HEART_TAG);
        brickerGameManager.addGameObject(fallingHeart, Layer.DEFAULT);
        fallingHeart.setVelocity(FALLING_HEART_VELOCITY);
        brickerGameManager.removeBrick(thisObj);
    }

    public void removeHeart(FallingHeart fallingHeart) {
        this.brickerGameManager.removeObject(fallingHeart);
    }
}
