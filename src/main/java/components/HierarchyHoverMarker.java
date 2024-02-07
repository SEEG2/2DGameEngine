package components;

import gmen.GameObject;
import gmen.Prefabs;
import gmen.Window;
import org.joml.Vector4f;
import renderer.Texture;

public class HierarchyHoverMarker extends Component {
    GameObject object;
    GameObject activeObject = null;
    public HierarchyHoverMarker(Texture texture) {
        this.object = Prefabs.generateTextureObject(texture, 0.25f, 0.25f, true);
    }

    @Override
    public void update(float dt) {
        if (this.activeObject == null) {
            return;
        }

        this.object.transform.position = activeObject.transform.position;
    }

    public void bindObject(GameObject gameObject) {
        activeObject = gameObject;
        object.getComponent(SpriteRenderer.class).setColor(new Vector4f(1,1,1,1));
    }

    public void unbind() {
        activeObject = null;
        object.getComponent(SpriteRenderer.class).setColor(new Vector4f(0,0,0,0));
    }
}
