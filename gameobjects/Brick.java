package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Brick extends GameObject {
    private final CollisionStrategy collisionStrategy;

    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy){
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return !(other instanceof Brick); // todo find another implemantation
    }

//    public void onCollisionEnter(GameObject other, Collision collision) {
//        super.onCollisionEnter(other, collision);
//        Vector2 newVel = getVelocity().flipped(collision.getNormal());
//        setVelocity(newVel);
//        collisionSound.play();
//    }
}
