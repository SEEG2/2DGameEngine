package gmen;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    public Scene() {

    }

    //called on initialization
    public void init() {

    }

    //called once per frame (after the first two frames past)
    public abstract void update(float dt);
    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
        }
    }
}
