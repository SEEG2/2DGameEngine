package gmen;


import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    public LevelEditorScene() {}

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        loadResources();
    }

    public void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }
    @Override
    public void update(float dt) {
        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
