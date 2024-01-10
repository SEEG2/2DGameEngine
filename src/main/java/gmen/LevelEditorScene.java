package gmen;


import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import imgui.ImGui;
import org.joml.Vector2f;
import renderer.Texture;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    public LevelEditorScene() {}
    //Spritesheet sprites;
    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f());
        //sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100,100), new Vector2f(256,256)), 1);
        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/default.png"))));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 1", new Transform(new Vector2f(120,110), new Vector2f(256,256)));
        obj2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/default.png"))));
        this.addGameObjectToScene(obj2);
        this.activeGameObject = obj1;
    }

    public void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

       //AssetPool.addSpritesheet("assets/images/spritesheet.png", new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"), 16, 16, 26, 0));
    }

    private int pass = 0;
    @Override
    public void update(float dt) {
        if (pass < 100) {
            obj1.transform.position.x += 2;
            obj1.transform.position.y += 2;
        } else if (pass == 200) {
            pass = 0;
        } else if (pass > 100) {
            obj1.transform.position.x -= 2;
            obj1.transform.position.y -= 2;
        }
        obj1.transform.scale.x = pass;
        pass++;

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imGUI() {
        ImGui.begin("Test");
        ImGui.text("Text");
        ImGui.end();
    }
}
