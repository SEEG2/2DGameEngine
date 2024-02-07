package components;

import gmen.GameObject;
import gmen.Prefabs;
import gmen.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;
import renderer.Texture;
import scenes.Scene;

public class HierarchyHoverMarker extends Component {
    GameObject object;
    GameObject activeObject = null;
    public HierarchyHoverMarker(Texture texture) {
        this.object = Prefabs.generateTextureObject(texture, 0.5f, 0.5f, true);
        Window.getScene().addGameObjectToScene(object);
    }

    @Override
    public void editorUpdate(float dt) {
        if (activeObject == null) {
            return;
        }
        
        object.transform.position = activeObject.transform.position;
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
