package components;

import editor.PropertiesWindow;
import gmen.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

import static org.lwjgl.glfw.GLFW.*;
import static util.Settings.SWITCH_GIZMO_KEY;

public class ScaleGizmo extends Gizmo {

    public ScaleGizmo(Texture texture, PropertiesWindow propertiesWindow) {
        super(texture, propertiesWindow);
    }

    @Override
    public void update(float dt) {
        if (!KeyListener.isKeyPressed(SWITCH_GIZMO_KEY)) {
            super.dontUse();
            return;
        }
        super.use();
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
