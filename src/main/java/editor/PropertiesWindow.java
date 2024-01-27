package editor;

import gmen.GameObject;
import gmen.MouseListener;
import imgui.ImGui;
import renderer.PickingTexture;
import scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;

public class PropertiesWindow {
    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;
    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    //only check if mouse is inside the framebuffer
    public void update(float dt, Scene currentScene) {

        this.debounce -= dt;

        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && MouseListener.isMouseInsideFrameBuffer() && debounce < 0 && !MouseListener.isDragging()) {
            int x =  (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();

            int gameObjectId = pickingTexture.readPixel(x, y);

            if (!(currentScene.getGameObject(gameObjectId) == null)) {
                if (!currentScene.getGameObject(gameObjectId).getName().startsWith("%")) {
                    activeGameObject = currentScene.getGameObject(gameObjectId);
                }
            } else {
                activeGameObject = null;
            }

            this.debounce = 0.2f;
        }
    }

    public void imGUI() {
        if (activeGameObject != null) {
            ImGui.begin("Properties");
            activeGameObject.imGUI();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {
        return activeGameObject;
    }
}
