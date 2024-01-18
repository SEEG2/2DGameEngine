package renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D {

    private Vector2f from;
    private Vector2f to;
    private Vector3f color;
    private boolean useRealTime;
    private float lifetime;

    public Line2D(Vector2f from, Vector2f to, Vector3f color, float lifetime) {
        this.from = from;
        this.to = to;
        this.color = color;
        this.lifetime = lifetime;
        this.useRealTime = false;
    }

    public Line2D(Vector2f from, Vector2f to, Vector3f color, float lifetime, boolean useRealTime) {
        this.from = from;
        this.to = to;
        this.color = color;
        this.lifetime = lifetime;
        this.useRealTime = useRealTime;
    }

    public float beginFrame(float dt) {
        if (this.useRealTime) {
            lifetime -= dt;
        } else {
            lifetime--;
        }
        return this.lifetime;
    }

    public Vector2f getFrom() {
        return from;
    }

    public Vector2f getTo() {
        return to;
    }

    public Vector3f getColor() {
        return color;
    }

}
