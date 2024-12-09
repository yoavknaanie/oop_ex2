package bricker.gameobjects;

import bricker.BrickerGameManager;
import bricker.brick_strategies.AddLifeStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class FallingHeart extends GameObject {
    private final AddLifeStrategy addLifeStrategy;

    public FallingHeart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                        AddLifeStrategy addLifeStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.addLifeStrategy = addLifeStrategy;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(BrickerGameManager.USER_PADDLE_TAG)){
            this.addLifeStrategy.addLife();
            this.addLifeStrategy.removeHeart(this);
        }
    }

}
