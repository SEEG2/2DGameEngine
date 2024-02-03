package editor;

import physics.components.Box2DCollider;
import physics.components.CircleCollider;
import physics.components.Rigidbody2D;
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
                if (!currentScene.getGameObject(gameObjectId).name.startsWith("%")) {
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

            if (ImGui.beginPopupContextWindow("Component Adder")) {
                if (ImGui.menuItem("Add Rigidbody")) {
                    if (activeGameObject.getComponent(Rigidbody2D.class) == null) {
                        activeGameObject.addComponent(new Rigidbody2D());
                    }
                }

                if (ImGui.menuItem("Add Box Collider")) {
                    if (activeGameObject.getComponent(Box2DCollider.class) == null && activeGameObject.getComponent(CircleCollider.class) == null) {
                        activeGameObject.addComponent(new Box2DCollider());
                    }
                }

                if (ImGui.menuItem("Add Circle Collider")) {
                    if (activeGameObject.getComponent(CircleCollider.class) == null && activeGameObject.getComponent(Box2DCollider.class) == null) {
                        activeGameObject.addComponent(new CircleCollider());
                    }
                }

                ImGui.endPopup();
            }

            activeGameObject.imGUI();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {
        return activeGameObject;
    }

    public void setActiveGameObject(GameObject gameObject) {
        this.activeGameObject =  gameObject;
    }
}
