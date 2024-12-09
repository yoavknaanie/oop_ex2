package bricker.brick_strategies;

import bricker.BrickType;
import bricker.BrickerGameManager;

public class CollisionStrategyFactory {
    private final BrickerGameManager brickerGameManager;

    public CollisionStrategyFactory(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    public CollisionStrategy getCollisionStrategy(BrickType brickType) {
        switch (brickType) {
            case BASIC_TYPE:
                return new BasicCollisionStrategy(brickerGameManager);
            case ADD_PACK_TYPE:
                return new AddPuckStrategy(brickerGameManager);
            case ADD_PADDLE_TYPE:
                return new AddPaddleStrategy(brickerGameManager);
            case TURBO_TYPE:
                return new TurboStrategy(brickerGameManager);
            case EXTRA_LIFE:
                return new AddLifeStrategy(brickerGameManager);
            case DOUBLE_TYPE:
                return new DoubleStrategy(this, true);
            default:
//                will not gete here
                throw new IllegalArgumentException("Unsupported BrickType: " + brickType);
        }
    }
}
