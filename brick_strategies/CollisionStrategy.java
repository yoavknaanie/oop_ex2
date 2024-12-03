package bricker.brick_strategies;

import bricker.BrickerGameManager;
import danogl.GameObject;

public interface CollisionStrategy {
    void onCollision(GameObject thisObj, GameObject otherObj);
}

