package bricker.gameobjects;

import bricker.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class ExtraPaddle extends UserPaddle{
    private static final int INITIAL_NUMBER_OF_HITS = 4;
    private static final int MINUS_HIT = -1;
    private danogl.util.Counter hits = new danogl.util.Counter(INITIAL_NUMBER_OF_HITS);
    private final BrickerGameManager brickerGameManager;

    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                       Renderable renderable, UserInputListener inputListener, int leftBorder,
                       int rightBorder, BrickerGameManager brickerGameManager){
        super(topLeftCorner, dimensions, renderable, inputListener, leftBorder, rightBorder);
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(brickerGameManager.PACK_TAG) ||
                other.getTag().equals(brickerGameManager.BALL_TAG)) {
            if (hits.value() == 1)
                brickerGameManager.removeObject(this);
            else
                hits.increaseBy(MINUS_HIT);
        }
    }

    public void resetHits() {
        hits = new danogl.util.Counter(INITIAL_NUMBER_OF_HITS);
    }
}
