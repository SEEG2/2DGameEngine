package components;

import editor.PropertiesWindow;
import gmen.GameObject;
import gmen.ImGUILayer;
import gmen.MouseListener;
import gmen.Window;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;
import renderer.PickingTexture;
import scenes.Scene;
import util.Settings;

import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControl extends  Component {
    GameObject holdingObject = null;
    private float debounceTime = 0.1f;
    private float debounce = debounceTime;

    public void pickupObject(GameObject gameObject) {
        if (this.holdingObject != null) {
            this.holdingObject.destroy();
        }
        this.holdingObject = gameObject;
        this.holdingObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(0.5f,0.5f,0.5f,0.5f));
        this.holdingObject.editorProperties.allowMouseSelection = false;
        Window.getScene().addGameObjectToScene(gameObject);
    }

    public void place() {
        GameObject newGameObject = this.holdingObject.copy();
        newGameObject.getComponent(SpriteRenderer.class).setColor(new Vector4f(1,1,1,1));
        Window.getScene().addGameObjectToScene(newGameObject);
        this.holdingObject.editorProperties.allowMouseSelection = true;
    }

    @Override
    public void editorUpdate(float dt) {
        debounce -= dt;
        if (holdingObject != null && debounce <= 0) {
            holdingObject.transform.position.x = MouseListener.getWorldX();
            holdingObject.transform.position.y = MouseListener.getWorldY();
            holdingObject.transform.position.x = ((int) Math.floor(holdingObject.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH) + Settings.GRID_WIDTH / 2;
            holdingObject.transform.position.y = ((int) Math.floor(holdingObject.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT) + Settings.GRID_HEIGHT / 2;

            int x =  (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();

            int gameObjectId = Window.getPickingTexture().readPixel(x, y);
            GameObject gameObject = Window.getScene().getGameObject(gameObjectId);

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && MouseListener.isMouseInsideFrameBuffer()) {
                if (MouseListener.isDragging() && (gameObject == null || Window.getLevelEditorSceneInitializer().getLevelEditorComponents().getComponent(EditorSettings.class).allowDragOverride)) {
                    place();
                    Window.getImGUILayer().getPropertiesWindow().setActiveGameObject(null);
                } else if (!MouseListener.isDragging()) {
                    place();
                    debounce = debounceTime;
                }
            }
            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)) {
                holdingObject.destroy();
                holdingObject = null;
                Window.getImGUILayer().getPropertiesWindow().setActiveGameObject(null);
            }
        }
    }
}
