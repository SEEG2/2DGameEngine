package components;

import editor.PropertiesWindow;
import gmen.*;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
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
    private transient float gizmoWidth = (float) 1 / 10;
    private transient float  gizmoHeight = (float) 4 / 10;
    protected transient boolean xAxisActive = false, yAxisActive = false;
    protected GameObject activeGameObject = null;
    private boolean using = false;


    public Gizmo(Texture texture, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateTextureObject(texture,gizmoWidth , gizmoHeight, true);
        this.yAxisObject = Prefabs.generateTextureObject(texture, gizmoWidth, gizmoHeight, true);
        this.xAxisSpriteRenderer = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSpriteRenderer = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

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
    public void editorUpdate(float dt) {
        if (!using) {
            return;
        }

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();

        if (this.activeGameObject != null) {
            this.use();
            if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && KeyListener.keyBeginPress(GLFW_KEY_D)) {
                GameObject newGameObject = this.activeGameObject.copy();
                Window.getScene().addGameObjectToScene(newGameObject);
                newGameObject.transform.position.add(0.1f, 0.1f);
                this.propertiesWindow.setActiveGameObject(newGameObject);
                return;
            } else if (KeyListener.keyBeginPress(GLFW_KEY_DELETE)) {
                activeGameObject.destroy();
                this.setInactive();
                this.propertiesWindow.setActiveGameObject(null);
                return;
            }
        } else {
            this.dontUse();
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

        this.xAxisObject.transform.position.set(this.activeGameObject.transform.position.x, this.activeGameObject.transform.position.y - gizmoWidth*2);
        this.yAxisObject.transform.position.set(this.activeGameObject.transform.position.x - gizmoWidth*2, this.activeGameObject.transform.position.y);

        //TODO: fix gizmo resizing
//        if (activeGameObject.transform.scale.x > 20 || activeGameObject.transform.scale.y > 20) {
//            this.xAxisObject.transform.scale.set(Math.max(activeGameObject.transform.scale.x, activeGameObject.transform.scale.y));
//            this.xAxisObject.transform.scale.x *= gizmoHeight;
//            this.xAxisObject.transform.scale.y *= gizmoWidth;
//
//
//            this.yAxisObject.transform.scale.set(Math.max(activeGameObject.transform.scale.x, activeGameObject.transform.scale.y));
//            this.yAxisObject.transform.scale.x *= gizmoHeight;
//            this.yAxisObject.transform.scale.y *= gizmoWidth;
//
//        } else {
//            this.xAxisObject.transform.scale.set(new Vector2f(gizmoWidth, gizmoHeight));
//            this.yAxisObject.transform.scale.set(new Vector2f(gizmoWidth, gizmoHeight));
//        }
//
   }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = -90;
    }

    @Override
    public void update(float dt) {
        if (using) {
            dontUse();
        }
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

        if (mousePos.x <= xAxisObject.transform.position.x + xAxisObject.transform.scale.y / 2
                && mousePos.x >= xAxisObject.transform.position.x - xAxisObject.transform.scale.y / 2
                && mousePos.y >= xAxisObject.transform.position.y - xAxisObject.transform.scale.x / 2
                && mousePos.y <= xAxisObject.transform.position.y + xAxisObject.transform.scale.x / 2) {
            this.xAxisSpriteRenderer.setColor(xAxisHoverColor);
            return true;
        }

        xAxisSpriteRenderer.setColor(xAxisColor);
        return false;
    }

    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());

        if (mousePos.x <= yAxisObject.transform.position.x + yAxisObject.transform.scale.x / 2
                && mousePos.x >= yAxisObject.transform.position.x - yAxisObject.transform.scale.x / 2
                && mousePos.y <= yAxisObject.transform.position.y + yAxisObject.transform.scale.y / 2
                && mousePos.y >= yAxisObject.transform.position.y - yAxisObject.transform.scale.y / 2) {
            this.yAxisSpriteRenderer.setColor(yAxisHoverColor);
            return true;
        }

        yAxisSpriteRenderer.setColor(yAxisColor);
        return false;
    }

    protected void use() {
        this.using = true;
        this.setActive();
    }

    protected void dontUse() {
        this.using = false;
        this.setInactive();
    }
}
