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
        for (GameObject obj: brickerGameManager.getGameObjects()) {
            if (obj.getTag().equals(brickerGameManager.EXTRA_PADDLE_TAG)) {
                ExtraPaddle extraPaddle =  (ExtraPaddle) obj;
                extraPaddle.resetHits();
                return;
            }
        }
        brickerGameManager.createExtraPaddle();
    }
}

