package bricker.brick_strategies;

import bricker.BrickerGameManager;
import bricker.gameobjects.FallingHeart;
import danogl.GameObject;
import danogl.util.Vector2;

public class AddLifeStrategy implements CollisionStrategy {
    private static final String FALLING_HEART_TAG = "falling_heart_tag";
    private final BrickerGameManager brickerGameManager;

    public AddLifeStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        FallingHeart heart = new FallingHeart(thisObj.getCenter(),
                brickerGameManager.HEART_DIM,
                brickerGameManager.getHeartImage());
        heart.setTag(FALLING_HEART_TAG);
        brickerGameManager.addGameObject(heart);
        brickerGameManager.removeBrick(thisObj);
    }
}
