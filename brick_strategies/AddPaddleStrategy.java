package bricker.brick_strategies;

import bricker.BrickerGameManager;
import bricker.gameobjects.ExtraPaddle;
import bricker.gameobjects.UserPaddle;
import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class AddPaddleStrategy implements CollisionStrategy {
    private final BrickerGameManager brickerGameManager;

    public AddPaddleStrategy(BrickerGameManager brickerGameManager){
        this.brickerGameManager = brickerGameManager;
    }
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        brickerGameManager.removeBrick(thisObj);
        checkIfIsThereExtraPaddle();
    }

    private void checkIfIsThereExtraPaddle(){
        ExtraPaddle extraPaddle = brickerGameManager.getExtraPaddle();
        if (extraPaddle == null) {
            brickerGameManager.createExtraPaddle();
        }
        else {
            extraPaddle.resetHits();
        }
    }
}

