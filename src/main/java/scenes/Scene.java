package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import gmen.Camera;
import gmen.GameObject;
import gmen.GameObjectDeserializer;
import imgui.ImGui;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected boolean levelIsLoaded = false;
    public Scene() {

    }

    //called on initialization
    public void init() {

    }

    //called once per frame (after the first two frames past)
    public abstract void update(float dt);
    public abstract void render();

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

    public GameObject getGameObject(int gameObjectID) {
        Optional<GameObject> result = this.gameObjects.stream().filter(gameObject -> gameObject.getuID() == gameObjectID).findFirst();
        return result.orElse(null);
    }

    public void imGUI() {

    }

    public void saveExit() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        try {
            FileWriter writer = new FileWriter("level.world");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.world")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.isEmpty()) {
            int maxGoID = -1;
            int maxCompID = -1;

            GameObject[] objects = gson.fromJson(inFile, GameObject[].class);

            for (int i = 0; i < objects.length; i++) {
                addGameObjectToScene(objects[i]);

                for (Component c : objects[i].getAllComponents()) {
                    if (c.getuID() > maxCompID) {
                        maxCompID = c.getuID();
                    }

                    if (objects[i].getuID() > maxGoID) {
                        maxGoID = objects[i].getuID();
                    }
                }
            }

            maxGoID++;
            maxCompID++;


            GameObject.init(maxGoID);
            Component.init(maxCompID);

            this.levelIsLoaded = true;
        }
    }
}
