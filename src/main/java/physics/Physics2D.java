package physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

import static Constants.Physics.GRAVITY;

public class Physics2D {
    private Vec2 gravity = new Vec2(0, GRAVITY);
    private World world = new World(gravity);
    private float physicsTime = 0;

    private float physicsTimeStep = 1f/60;
    private int velocityIterations = 12;
    private int positionIterations = 5;

    public void update(float dt) {
        physicsTime += dt;
        if (physicsTime >= 0) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }
}
