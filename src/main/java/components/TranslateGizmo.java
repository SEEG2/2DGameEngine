package components;

import editor.PropertiesWindow;
import gmen.*;
import renderer.Texture;

import static util.Settings.SWITCH_GIZMO_KEY;

public class TranslateGizmo extends Gizmo {

    public TranslateGizmo(Texture texture, PropertiesWindow propertiesWindow) {
        super(texture, propertiesWindow);
        super.dontUse();
    }

    @Override
    public void editorUpdate(float dt) {
        if (KeyListener.isKeyPressed(SWITCH_GIZMO_KEY)) {
            super.dontUse();
            return;
        }
        super.use();
        if (activeGameObject != null && activeGameObject.editorProperties.allowGizmoMoving) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x = MouseListener.getWorldX()/100;
            } else if (yAxisActive && !xAxisActive) {
                activeGameObject.transform.position.y = MouseListener.getWorldY()/100;
            }
        }

        super.editorUpdate(dt);
    }
}
