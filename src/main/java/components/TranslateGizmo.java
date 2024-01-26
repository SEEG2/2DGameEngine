package components;

import gmen.GameObject;
import gmen.Prefabs;
import gmen.Window;
import org.joml.Vector4f;
import util.AssetPool;

public class TranslateGizmo extends Component {
    private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
    private Vector4f xAxisHoverColor = new Vector4f();
    private Vector4f yAxisColor = new Vector4f(0,1,0,1);
    private Vector4f yAxisHoverColor = new Vector4f();
    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSpriteRenderer;
    private SpriteRenderer yAxisSpriteRenderer;
    private GameObject activeGameObject = null;

    public TranslateGizmo(Sprite arrowSprite) {
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, 81, 81);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, 81, 81);
        this.xAxisSpriteRenderer = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSpriteRenderer = this.yAxisObject.getComponent(SpriteRenderer.class);

        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void update(float dt) {
        if (this.activeGameObject != null) {
            this.xAxisObject.transform.position.set(this.activeGameObject.transform.position);
            this.yAxisObject.transform.position.set(this.activeGameObject.transform.position);
        }
    }

    private void setActive(GameObject gameObject) {
        this.activeGameObject = gameObject;
        this.xAxisSpriteRenderer.setColor(xAxisColor);
        this.yAxisSpriteRenderer.setColor(yAxisColor);
    }

    private void setInactive() {
        this.activeGameObject = null;
        this.xAxisSpriteRenderer.setColor(new Vector4f(0,0,0,0));
        this.yAxisSpriteRenderer.setColor(new Vector4f(0,0,0,0));
    }
}
