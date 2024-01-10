package gmen;

import imgui.ImGui;
import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;
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
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public Camera camera() {
        return this.camera;
    }

    public void sceneImGUI() {
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imGUI();
            ImGui.end();
        }

        imGUI();
    }

    public void imGUI() {

    }
}
