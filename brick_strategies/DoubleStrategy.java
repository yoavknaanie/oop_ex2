package bricker.brick_strategies;

import bricker.BrickType;
import bricker.BrickerGameManager;
import danogl.GameObject;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The DoubleStrategy class implements the CollisionStrategy interface and is responsible
 * for handling a combination of two collision strategies. When a collision occurs,
 * both strategies are executed sequentially. The strategies are randomly chosen
 * from a set of advanced brick types and may include strategies like adding a puck,
 * adding a paddle, turbo mode, or extra life.
 */
public class DoubleStrategy implements CollisionStrategy {
    private static final int ADVANCED_STRATEGIES_NUM = 5;
    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    /**
     * Constructs a DoubleStrategy object, which combines two random advanced strategies
     * for handling collisions.
     * @param factory The CollisionStrategyFactory responsible for providing the strategies.
     * @param firstCall A flag indicating whether this is the first call or a recursive call.
     */
    public DoubleStrategy(CollisionStrategyFactory factory, boolean firstCall) {
        BrickType brickType1 = getRandomeAdvancedBrickType();
        BrickType brickType2 = getRandomeAdvancedBrickType();
        // Ensure that neither of the two strategies is a DoubleStrategy itself on recursive calls
        while ((!firstCall && (brickType1 == BrickType.DOUBLE_TYPE || brickType2 == BrickType.DOUBLE_TYPE)) ||
                (brickType1 == BrickType.DOUBLE_TYPE) && (brickType2 == BrickType.DOUBLE_TYPE)) {
            brickType1 = getRandomeAdvancedBrickType();
            brickType2 = getRandomeAdvancedBrickType();
        }
        // Initialize the strategies based on the randomly chosen brick types
        if (brickType1.equals(BrickType.DOUBLE_TYPE)) {
            this.strategy1 = new DoubleStrategy(factory, false);
        } else {
            this.strategy1 = factory.getCollisionStrategy(brickType1);
        }
        if (brickType2.equals(BrickType.DOUBLE_TYPE)) {
            this.strategy2 = new DoubleStrategy(factory, false);
        } else {
            this.strategy2 = factory.getCollisionStrategy(brickType2);
        }
    }

    /**
     * Returns a random BrickType from the advanced strategies.
     * @return A randomly selected BrickType.
     */
    private BrickType getRandomeAdvancedBrickType() {
        int randomNumber = ThreadLocalRandom.current().nextInt(ADVANCED_STRATEGIES_NUM);
        BrickType[] samplingArray = new BrickType[]{
                BrickType.ADD_PACK_TYPE,
                BrickType.ADD_PADDLE_TYPE,
                BrickType.TURBO_TYPE,
                BrickType.EXTRA_LIFE,
                BrickType.DOUBLE_TYPE,
        };
        return samplingArray[randomNumber];
    }

    /**
     * Handles the collision by invoking both strategies sequentially.
     * The behavior of the collision depends on the two strategies chosen for this DoubleStrategy.
     * @param thisObj The game object involved in the collision (usually a brick).
     * @param otherObj The other game object involved in the collision (typically a paddle or ball).
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        strategy1.onCollision(thisObj, otherObj);
        strategy2.onCollision(thisObj, otherObj);
    }
}
