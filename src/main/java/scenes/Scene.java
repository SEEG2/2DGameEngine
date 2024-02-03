package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import components.EditorProperties;
import components.Transform;
import gmen.Camera;
import gmen.GameObject;
import gmen.GameObjectDeserializer;
import org.joml.Vector2f;
import physics.Physics2D;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene {

    private Renderer renderer = new Renderer();
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;
    private static Camera currentCamera = null;
    private SceneInitializer sceneInitializer;
    private Physics2D physics2D;

    public Scene(SceneInitializer sceneInitializer) {
        this.sceneInitializer = sceneInitializer;
        this.physics2D = new Physics2D();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
    }

    //called on initialization
    public void init() {
        this.camera = new Camera(new Vector2f());
        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }

    //called once per frame (after the first two frames past)
    public void update(float dt) {
        this.camera.adjustProjection();
        this.physics2D.update(dt);

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.update(dt);

            if (gameObject.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(gameObject);
                this.physics2D.destroyGameObject(gameObject);
                i--;
            }
        }
    }

    public void editorUpdate(float dt) {
        this.camera.adjustProjection();

        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            gameObject.editorUpdate(dt);

            if (gameObject.isDead()) {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(gameObject);
                this.physics2D.destroyGameObject(gameObject);
                i--;
            }
        }
    }

    public void render() {
        this.renderer.render();
    }

    public void start() {
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject go = gameObjects.get(i);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
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
            this.physics2D.add(go);
        }
    }

    public Camera camera() {
        return this.camera;
    }

    public GameObject createGameObject(String name) {
        GameObject go = new GameObject(name);
        go.transform = (Transform) go.addComponent(new Transform());;
        go.editorProperties = (EditorProperties) go.addComponent(new EditorProperties());
        return go;
    }

    public GameObject getGameObject(int gameObjectID) {
        Optional<GameObject> result = this.gameObjects.stream().filter(gameObject -> gameObject.getuID() == gameObjectID).findFirst();
        return result.orElse(null);
    }

    public void imGUI() {
        this.sceneInitializer.imGUI();
    }

    public void save() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();
        try {
            FileWriter writer = new FileWriter("level.world");

            List<GameObject> serializableGameObjects = new ArrayList<>();

            for (GameObject go : this.gameObjects) {
                if (go.isSerializable()) {
                    serializableGameObjects.add(go);
                }
            }

            writer.write(gson.toJson(serializableGameObjects));
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
        }
    }

    public static Camera getCamera() {
        return currentCamera;
    }

    public static void setCamera(Camera camera) {
        currentCamera = camera;
    }

    public List<GameObject> getGameObjects() {
        return this.gameObjects;
    }

    public void destroy() {
        for (GameObject gameObject : gameObjects) {
            gameObject.destroy();
        }
    }
}
