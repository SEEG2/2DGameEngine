package gmen;

import components.Component;
import components.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private List<Component> components;
    public Transform transform;
    //do not set this to values > 255 (otherwise it will interfere with the editor
    private static int ID_COUNTER = 0;
    private int uID = -1;
    private boolean isSerializable = true;

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
    }

    public GameObject(String name, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
        this.transform.zIndex = zIndex;

        this.uID = ID_COUNTER++;
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
        this.transform.zIndex = zIndex;

        this.uID = ID_COUNTER++;
    }

    public GameObject(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;

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
            c.imGUI();
        }
    }

    public void update(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void start() {
        for (Component component : components) {
            component.start();
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

    public String getName() {
        return this.name;
    }
}
