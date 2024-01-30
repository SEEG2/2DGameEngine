package physics;

import Constants.components.Box2DCollider;
import Constants.components.CircleCollider;
import Constants.components.Rigidbody2D;
import gmen.GameObject;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import components.Transform;
import org.joml.Vector2f;

import static Constants.Physics.GRAVITY;

public class Physics2D {
    private Vec2 gravity = new Vec2(0, GRAVITY);
    private World world = new World(gravity);
    private float physicsTime = 0;

    private float physicsTimeStep = 1f/60;
    private int velocityIterations = 12;
    private int positionIterations = 5;

    public void add(GameObject gameObject) {
        Rigidbody2D rigidbody2D = gameObject.getComponent(Rigidbody2D.class);
        if (rigidbody2D != null && rigidbody2D.getRawBody() == null) {
            Transform transform = gameObject.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float) Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rigidbody2D.getAngularDamping();
            bodyDef.linearDamping = rigidbody2D.getLinearDamping();
            bodyDef.fixedRotation = rigidbody2D.isFixedRotation();
            bodyDef.bullet = rigidbody2D.isContinuousCollision();

            switch (rigidbody2D.getBodyType()) {
                case Kinematic: bodyDef.type = BodyType.KINEMATIC; break;
                case Static: bodyDef.type = BodyType.STATIC; break;
                case Dynamic: bodyDef.type = BodyType.DYNAMIC; break;
            }

            PolygonShape shape = new PolygonShape();
            CircleCollider circleCollider;
            Box2DCollider boxCollider;

            if ((circleCollider = gameObject.getComponent(CircleCollider.class)) != null) {
                shape.setRadius(circleCollider.getRadius());
            } else if ((boxCollider = gameObject.getComponent(Box2DCollider.class)) != null) {
                Vector2f halfSize = new Vector2f(boxCollider.getHalfSize().mul(0.5f));
                Vector2f offset = boxCollider.getOffset();
                Vector2f origin = new Vector2f(boxCollider.getOrigin());

                shape.setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);

                Vec2 pos = bodyDef.position;
                float xPos = pos.x + offset.x;
                float yPos = pos.y + offset.y;

                bodyDef.position.set(xPos, yPos);
            }

            Body body = this.world.createBody(bodyDef);
            rigidbody2D.setRawBody(body);
            body.createFixture(shape, rigidbody2D.getMass());

        }
    }
    public void update(float dt) {
        physicsTime += dt;
        if (physicsTime >= 0) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }
}
