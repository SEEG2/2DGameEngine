package components;

import editor.PropertiesWindow;
import gmen.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class ScaleGizmo extends Gizmo {

    public ScaleGizmo(Texture texture, PropertiesWindow propertiesWindow) {
        super(texture, propertiesWindow);
    }

    @Override
    public void update(float dt) {
        if (!KeyListener.isKeyPressed(GLFW_KEY_Q)) {
            super.setInactive();
            return;
        }
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldDx();
            } else if (yAxisActive && !xAxisActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldDy();
            }
        }

        super.update(dt);
    }
}
