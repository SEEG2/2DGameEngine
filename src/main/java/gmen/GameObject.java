package gmen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import components.SpriteRenderer;
import components.Transform;
import imgui.ImGui;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    public String name;
    private List<Component> components;
    public transient Transform transform;
    //do not set this to values > 255 (otherwise it will interfere with the editor
    private static int ID_COUNTER = 0;
    private int uID = -1;
    private boolean isSerializable = true;
    private boolean isDead = false;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();

        this.uID = ID_COUNTER++;
    }

    public GameObject(String name, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.transform.zIndex = zIndex;

        this.uID = ID_COUNTER++;
    }


    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false: "Couldn't cast component.";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i=0; i<components.size();i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public Component addComponent(Component c) {
        c.generateID();
        this.components.add(c);
        c.gameObject = this;

        return c;
    }

    public void imGUI() {
        for (Component c : components) {
            if (ImGui.collapsingHeader(c.getClass().getSimpleName()))
                c.imGUI();
        }
    }

    public void update(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void start() {
        for (int i=0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public void destroy() {
        this.isDead = true;
        for (int i = 0; i < components.size(); i++) {
            components.get(i).destroy();
        }
    }

    public int getzIndex() {
        return this.transform.zIndex;
    }

    public void setzIndex(int zIndex) {
        this.transform.zIndex = zIndex;
    }

    public int getuID() {
        return this.uID;
    }

    public static void init(int maxID) {
        ID_COUNTER = maxID;
    }

    public void editorUpdate(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).editorUpdate(dt);
        }
    }

    public GameObject copy() {
        //TODO better solution for this
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String objAsJson = gson.toJson(this);

        GameObject object = gson.fromJson(objAsJson, GameObject.class);
        object.generateUID();

        for (Component c: object.getAllComponents()) {
            c.generateID();
        }

        SpriteRenderer spriteRenderer = object.getComponent(SpriteRenderer.class);

        if (spriteRenderer != null && spriteRenderer.getTexture() != null) {
            spriteRenderer.setTexture(AssetPool.getTexture(spriteRenderer.getTexture().getFilepath()));
        }

        return object;
    }

    public void generateUID() {
        this.uID = ID_COUNTER++;
    }

    public List<Component> getAllComponents() {
        return this.components;
    }

    public void disableSerialization() {
        this.isSerializable = false;
    }

    public void enableSerialization() {
        this.isSerializable = true;
    }

    public boolean isSerializable() {
        return this.isSerializable;
    }

    public boolean isDead() {
        return this.isDead;
    }
}
