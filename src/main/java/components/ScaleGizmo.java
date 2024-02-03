package components;

import editor.PropertiesWindow;
import gmen.*;
import renderer.Texture;

import static util.Settings.SWITCH_GIZMO_KEY;

public class ScaleGizmo extends Gizmo {

    public ScaleGizmo(Texture texture, PropertiesWindow propertiesWindow) {
        super(texture, propertiesWindow);
        super.dontUse();
    }

    @Override
    public void editorUpdate(float dt) {
        if (!KeyListener.isKeyPressed(SWITCH_GIZMO_KEY)) {
            super.dontUse();
            return;
        }
        super.use();
        if (activeGameObject != null && activeGameObject.editorProperties.allowGizmoResize) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldDx();
            } else if (yAxisActive && !xAxisActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldDy();
            }
        }

        super.editorUpdate(dt);
    }
}
