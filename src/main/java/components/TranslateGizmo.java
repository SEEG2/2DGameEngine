package components;

import editor.PropertiesWindow;
import gmen.GameObject;
import gmen.MouseListener;
import gmen.Prefabs;
import gmen.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class TranslateGizmo extends Gizmo {

    public TranslateGizmo(Texture texture, PropertiesWindow propertiesWindow) {
        super(texture, propertiesWindow);
    }

    @Override
    public void update(float dt) {

        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
            } else if (yAxisActive && !xAxisActive) {
                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            }
        }
        super.update(dt);
    }
}
