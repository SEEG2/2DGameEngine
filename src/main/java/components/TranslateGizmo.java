package components;

import editor.PropertiesWindow;
import gmen.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;
import static util.Settings.SWITCH_GIZMO_KEY;

public class TranslateGizmo extends Gizmo {

    public TranslateGizmo(Texture texture, PropertiesWindow propertiesWindow) {
        super(texture, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {
        if (KeyListener.isKeyPressed(SWITCH_GIZMO_KEY)) {
            super.dontUse();
            return;
        }
        super.use();
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
            } else if (yAxisActive && !xAxisActive) {
                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            }
        }

        super.editorUpdate(dt);
    }
}
