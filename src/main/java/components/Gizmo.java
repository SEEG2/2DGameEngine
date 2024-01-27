package components;

import editor.PropertiesWindow;
import gmen.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.Texture;

import static org.lwjgl.glfw.GLFW.*;

public class Gizmo extends Component {
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


    public Gizmo(Texture texture, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateTextureObject(texture,gizmoWidth , gizmoHeight, true);
        this.yAxisObject = Prefabs.generateTextureObject(texture, gizmoWidth, gizmoHeight, true);
        this.xAxisSpriteRenderer = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSpriteRenderer = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        xAxisObject.disableSerialization();
        yAxisObject.disableSerialization();

        xAxisObject.setzIndex(255);
        yAxisObject.setzIndex(255);

        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    public Gizmo(Sprite sprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateSpriteObject(sprite,gizmoWidth , gizmoHeight, true);
        this.yAxisObject = Prefabs.generateSpriteObject(sprite, gizmoWidth, gizmoHeight, true);
        this.xAxisSpriteRenderer = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSpriteRenderer = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        xAxisObject.disableSerialization();
        yAxisObject.disableSerialization();

        xAxisObject.setzIndex(255);
        yAxisObject.setzIndex(255);

        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void update(float dt) {

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();

        if (this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        if ((xAxisHot || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = true;
            yAxisActive = false;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            yAxisActive = true;
            xAxisActive = false;
        } else {
            xAxisActive = false;
            yAxisActive = false;
        }

        this.xAxisObject.transform.position.set(this.activeGameObject.transform.position.x + 30, this.activeGameObject.transform.position.y - 4);
        this.yAxisObject.transform.position.set(this.activeGameObject.transform.position.x + 25, this.activeGameObject.transform.position.y - 1);

    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
    }

    protected void setActive() {
        this.xAxisSpriteRenderer.setColor(xAxisColor);
        this.yAxisSpriteRenderer.setColor(yAxisColor);
    }

    protected void setInactive() {
        this.activeGameObject = null;
        this.xAxisSpriteRenderer.setColor(new Vector4f(0,0,0,0));
        this.yAxisSpriteRenderer.setColor(new Vector4f(0,0,0,0));
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());

        if (mousePos.x <= xAxisObject.transform.position.x
                && mousePos.x >= xAxisObject.transform.position.x - gizmoHeight
                && mousePos.y >= xAxisObject.transform.position.y
                && mousePos.y <= xAxisObject.transform.position.y + gizmoWidth) {
            this.xAxisSpriteRenderer.setColor(xAxisHoverColor);
            return true;
        }

        xAxisSpriteRenderer.setColor(xAxisColor);
        return false;
    }
    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());

        if (mousePos.x <= yAxisObject.transform.position.x + gizmoWidth
                && mousePos.x >= yAxisObject.transform.position.x
                && mousePos.y <= yAxisObject.transform.position.y + gizmoHeight
                && mousePos.y >= yAxisObject.transform.position.y) {
            this.yAxisSpriteRenderer.setColor(yAxisHoverColor);
            return true;
        }

        yAxisSpriteRenderer.setColor(yAxisColor);
        return false;
    }
}
