package bricker;
import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.AddPuckStrategy;
import bricker.gameobjects.Brick;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class BrickFactory {
    private final Vector2 brickDim;
    private final Vector2 initialLocation = Vector2.ZERO;
    private final Renderable brickImage;
    private final BrickerGameManager brickerGameManager;

    public BrickFactory(Vector2 brickDim, Renderable brickImage,
                        BrickerGameManager brickerGameManager) {
        this.brickDim = brickDim;
        this.brickImage = brickImage;
        this.brickerGameManager = brickerGameManager;
    }

    public Brick getBrick(BrickType brickType) {
        switch (brickType) {
            case BASIC_TYPE:
                return new Brick(initialLocation, brickDim, brickImage,
                        new BasicCollisionStrategy(brickerGameManager));
            case ADD_PACK_TYPE:
                return new Brick(initialLocation, brickDim, brickImage,
                        new AddPuckStrategy(brickerGameManager));
            default:
                throw new IllegalArgumentException("Unsupported BrickType: " + brickType);
        }
    }
}
