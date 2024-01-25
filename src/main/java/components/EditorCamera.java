package components;

import gmen.Camera;
import gmen.KeyListener;
import gmen.MouseListener;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCamera extends Component {
    private Camera levelEditorCamera;
    private Vector2f clickOrigin;
    private float dragDebounce = 0.032f;
    private float dragSensitive = 30f;
    private float scrollSensitivity = 0.1f;
    private float lerpTime = 0.0f;
    private boolean reset = false;

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

        if (MouseListener.getScrollY() != 0  && MouseListener.isMouseInsideFrameBuffer()) {
            float addValue = (float) Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity), 1 / levelEditorCamera.getZoom());
            addValue *= -Math.signum(MouseListener.getScrollY());
            levelEditorCamera.addZoom(addValue);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_R) && MouseListener.isMouseInsideFrameBuffer()) {
            reset = true;
        }

        if (reset) {
            levelEditorCamera.position.lerp(new Vector2f(0,0), lerpTime);
            levelEditorCamera.setZoom(this.levelEditorCamera.getZoom() + (1 - levelEditorCamera.getZoom()) * lerpTime);
            this.lerpTime += 0.1f * dt;
            if (Math.abs(levelEditorCamera.position.x) <= 5 && Math.abs(levelEditorCamera.position.y) <= 5 && Math.abs(levelEditorCamera.getZoom() - 1) <= 0.1f) {
                this.lerpTime = 0;
                this.levelEditorCamera.setZoom(1);
                reset = false;
            }
        }
    }
}
