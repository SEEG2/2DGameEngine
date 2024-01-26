package components;

import editor.PropertiesWindow;
import gmen.GameObject;
import gmen.Prefabs;
import gmen.Window;
import org.joml.Vector4f;
import renderer.Texture;
import util.AssetPool;

public class TranslateGizmo extends Component {
    private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
    private Vector4f xAxisHoverColor = new Vector4f();
    private Vector4f yAxisColor = new Vector4f(0,1,0,1);
    private Vector4f yAxisHoverColor = new Vector4f();
    private transient GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSpriteRenderer;
    private SpriteRenderer yAxisSpriteRenderer;
    private PropertiesWindow propertiesWindow;
    private GameObject activeGameObject = null;

    public TranslateGizmo(Texture texture, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateTextureObject(texture, 81, 81);
        this.yAxisObject = Prefabs.generateTextureObject(texture, 81, 81);
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
        if (this.activeGameObject != null) {
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position.x, this.activeGameObject.transform.position.y - 35);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position.x - 5, this.activeGameObject.transform.position.y);
        }

        this.activeGameObject = this.propertiesWindow.getActiveGameObject();

        if (this.activeGameObject != null) {
            this.setActive();
        } else {
            this.setInactive();
        }
    }

    @Override
    public void start() {
        this.xAxisObject.transform.rotation = 90;
    }

    private void setActive() {
        this.xAxisSpriteRenderer.setColor(xAxisColor);
        this.yAxisSpriteRenderer.setColor(yAxisColor);
    }

    private void setInactive() {
        this.activeGameObject = null;
        this.xAxisSpriteRenderer.setColor(new Vector4f(0,0,0,0));
        this.yAxisSpriteRenderer.setColor(new Vector4f(0,0,0,0));
    }
}
