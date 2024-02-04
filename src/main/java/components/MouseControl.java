package components;

import gmen.GameObject;
import gmen.MouseListener;
import gmen.Window;
import util.Settings;

import java.util.Set;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControl extends  Component {
    GameObject holdingObject = null;

    public void pickupObject(GameObject gameObject) {
        this.holdingObject = gameObject;
        Window.getScene().addGameObjectToScene(gameObject);
    }

    public void place() {
        this.holdingObject = null;
    }

    @Override
    public void editorUpdate(float dt) {
        if (holdingObject != null) {
            holdingObject.transform.position.x = MouseListener.getWorldX();
            holdingObject.transform.position.y = MouseListener.getWorldY();
            holdingObject.transform.position.x = ((int) Math.floor(holdingObject.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2;
            holdingObject.transform.position.y = ((int) Math.floor(holdingObject.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2;



            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
