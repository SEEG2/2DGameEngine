package gmen;

public abstract class Scene {
    protected Camera camera;
    public Scene() {

    }

    //called on initialization
    public void init() {

    }

    //called once per frame (after the first two frames past)
    public abstract void update(float dt);
}
