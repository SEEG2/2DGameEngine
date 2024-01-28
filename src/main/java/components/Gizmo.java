package components;

import editor.PropertiesWindow;
import gmen.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;
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
    private boolean using;


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
        if (!using) {return;}
        this.activeGameObject = this.propertiesWindow.getActiveGameObject();

        if (this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        if ((xAxisHot || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && !yAxisActive) {
            xAxisActive = true;
            yAxisActive = false;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && !xAxisActive) {
            yAxisActive = true;
            xAxisActive = false;
        } else {
            xAxisActive = false;
            yAxisActive = false;
        }

        this.xAxisObject.transform.position.set(this.activeGameObject.transform.position.x, this.activeGameObject.transform.position.y + (float) gizmoWidth/2);
        this.yAxisObject.transform.position.set(this.activeGameObject.transform.position.x, this.activeGameObject.transform.position.y);

        if (activeGameObject.transform.scale.x > 32*30 || activeGameObject.transform.scale.y > 32*30) {
            this.xAxisObject.transform.scale.set(Math.max(activeGameObject.transform.scale.x, activeGameObject.transform.scale.y));
            this.xAxisObject.transform.scale.x /= gizmoHeight;
            this.xAxisObject.transform.scale.y /= gizmoWidth;


            this.yAxisObject.transform.scale.set(Math.max(activeGameObject.transform.scale.x, activeGameObject.transform.scale.y));
            this.yAxisObject.transform.scale.x /= gizmoHeight;
            this.yAxisObject.transform.scale.y /= gizmoWidth;

        } else {
            this.xAxisObject.transform.scale.set(new Vector2f(gizmoWidth, gizmoHeight));
            this.yAxisObject.transform.scale.set(new Vector2f(gizmoWidth, gizmoHeight));
        }

    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = -90;
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

        if (mousePos.x <= xAxisObject.transform.position.x + xAxisObject.transform.scale.y
                && mousePos.x >= xAxisObject.transform.position.x
                && mousePos.y >= xAxisObject.transform.position.y - xAxisObject.transform.scale.x
                && mousePos.y <= xAxisObject.transform.position.y) {
            this.xAxisSpriteRenderer.setColor(xAxisHoverColor);
            return true;
        }

        xAxisSpriteRenderer.setColor(xAxisColor);
        return false;
    }
    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());

        if (mousePos.x <= yAxisObject.transform.position.x + yAxisObject.transform.scale.x
                && mousePos.x >= yAxisObject.transform.position.x
                && mousePos.y <= yAxisObject.transform.position.y + yAxisObject.transform.scale.y
                && mousePos.y >= yAxisObject.transform.position.y) {
            this.yAxisSpriteRenderer.setColor(yAxisHoverColor);
            return true;
        }

        yAxisSpriteRenderer.setColor(yAxisColor);
        return false;
    }

    protected void use() {
        this.using = true;
    }

    protected void dontUse() {
        this.using = false;
        this.setInactive();
    }
}
