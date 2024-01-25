package components;

import gmen.Camera;
import gmen.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

public class EditorCamera extends Component {
    private Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private float dragDebounce = 0.032f;
    private float dragSensitive = 30.0f;

    public EditorCamera(Camera levelEditorCamera) {
        this.levelEditorCamera = levelEditorCamera;
        this.clickOrigin = new Vector2f();
    }

    @Override
    public void update(float dt) {
        if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && dragDebounce > 0 && MouseListener.isMouseInsideFrameBuffer()) {
            this.clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= dt;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)  && MouseListener.isMouseInsideFrameBuffer()) {
            Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);

            levelEditorCamera.position.sub(delta.mul(dt).mul(dragSensitive));
            this.clickOrigin.lerp(mousePos, dt);
        }

        if (dragDebounce <= 0 && !MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE)) {
            dragDebounce = 0.032f;
        }
    }
}
