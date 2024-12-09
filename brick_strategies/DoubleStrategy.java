package bricker.brick_strategies;

import bricker.BrickType;
import bricker.BrickerGameManager;
import danogl.GameObject;

import java.util.concurrent.ThreadLocalRandom;

public class DoubleStrategy implements CollisionStrategy{
    private static final int ADVANCED_STRATEGIES_NUM = 5;
    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    public DoubleStrategy(CollisionStrategyFactory factory, boolean firstCall){
        BrickType brickType1 = getRandomeAdvancedBrickType();
        BrickType brickType2 = getRandomeAdvancedBrickType();

        while ((!firstCall && (brickType1 == BrickType.DOUBLE_TYPE || brickType2 == BrickType.DOUBLE_TYPE)) ||
                (brickType1 == BrickType.DOUBLE_TYPE) && (brickType2 == BrickType.DOUBLE_TYPE)) {
            brickType1 = getRandomeAdvancedBrickType();
            brickType2 = getRandomeAdvancedBrickType();
        }
        if (brickType1.equals(BrickType.DOUBLE_TYPE)){
            this.strategy1 = new DoubleStrategy(factory, false);
        }
        else
            this.strategy1 = factory.getCollisionStrategy(brickType1);
        if (brickType2.equals(BrickType.DOUBLE_TYPE)){
            this.strategy2 = new DoubleStrategy(factory, false);
        }
        else
            this.strategy2 = factory.getCollisionStrategy(brickType2);
    }

    private BrickType getRandomeAdvancedBrickType() {
        int randomNumber = ThreadLocalRandom.current().nextInt(ADVANCED_STRATEGIES_NUM);
        BrickType[] samplingArray = new BrickType[] {
                BrickType.ADD_PACK_TYPE,
                BrickType.ADD_PADDLE_TYPE,
                BrickType.TURBO_TYPE,
                BrickType.EXTRA_LIFE,
                BrickType.DOUBLE_TYPE,
        };
        return samplingArray[randomNumber];
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        strategy1.onCollision(thisObj, otherObj);
        strategy2.onCollision(thisObj, otherObj);
    }
}