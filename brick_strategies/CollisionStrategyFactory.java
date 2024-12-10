package bricker.brick_strategies;

import bricker.BrickType;
import bricker.BrickerGameManager;

/**
 * The CollisionStrategyFactory class is responsible for providing the appropriate
 * collision strategy based on the type of brick that is involved in a collision.
 * It uses the BrickType enum to determine which strategy to return.
 */
public class CollisionStrategyFactory {
    private final BrickerGameManager brickerGameManager;
    /**
     * Constructs a CollisionStrategyFactory object.
     * @param brickerGameManager The game manager responsible for managing game state and objects.
     */
    public CollisionStrategyFactory(BrickerGameManager brickerGameManager) {
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Returns the appropriate collision strategy based on the given BrickType.
     * @param brickType The type of brick involved in the collision.
     * @return The CollisionStrategy corresponding to the brick type.
     * @throws IllegalArgumentException If the BrickType is unsupported.
     */
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
                // This should never be reached due to the provided BrickType cases
                throw new IllegalArgumentException("Unsupported BrickType: " + brickType);
        }
    }
}
