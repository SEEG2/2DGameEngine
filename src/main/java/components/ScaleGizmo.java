package components;

import editor.PropertiesWindow;
import gmen.GameObject;
import gmen.MouseListener;
import gmen.Prefabs;
import gmen.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class ScaleGizmo extends Gizmo {

    private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
    private Vector4f xAxisHoverColor = new Vector4f(0.2f,0.2f,0.2f,0.8f);
    private Vector4f yAxisColor = new Vector4f(0,1,0,1);
    private Vector4f yAxisHoverColor = new Vector4f(0.2f,0.2f,0.2f,0.8f);
    protected transient GameObject xAxisObject, yAxisObject;
    private SpriteRenderer xAxisSpriteRenderer;
    private SpriteRenderer yAxisSpriteRenderer;
    private PropertiesWindow propertiesWindow;
    private transient int gizmoWidth = 15;
    private transient int gizmoHeight = 60;
    protected transient boolean xAxisActive = false, yAxisActive = false;
    protected GameObject activeGameObject = null;

    public ScaleGizmo(Texture texture, PropertiesWindow propertiesWindow) {
        super(texture, propertiesWindow);
    }

    @Override
    public void update(float dt) {

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
