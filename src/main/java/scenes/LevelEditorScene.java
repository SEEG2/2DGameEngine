package scenes;


import components.*;
import gmen.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.DebugDraw;
import util.AssetPool;

import static Constants.Color.ORANGE;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private Spritesheet sprites;
    public LevelEditorScene() {}
    GameObject levelEditorComponents = new GameObject("Level Editor", new Transform(new Vector2f()), 0);

    //Spritesheet sprites;
    @Override
    public void init() {
        levelEditorComponents.addComponent(new MouseControl());
        levelEditorComponents.addComponent(new GridLines());


        loadResources();

        this.camera = new Camera(new Vector2f());
        sprites = AssetPool.getSpritesheet("assets/images/default/spritesheets/spritesheet.png");
        DebugDraw.addLine2D(new Vector2f(0,0), new Vector2f(800, 800),ORANGE, 60);

        if (levelIsLoaded) {
            this.activeGameObject = gameObjects.get(0);
            return;
        }


        obj1 = new GameObject("Object 1", new Transform(new Vector2f(100,100), new Vector2f(256,256)), 1);
        SpriteRenderer obj1SpriteRenderer = new SpriteRenderer();
        obj1SpriteRenderer.setColor(new Vector4f(1,0,0,1f));
        obj1.addComponent(obj1SpriteRenderer);
        obj1.addComponent(new Rigidbody());
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 1", new Transform(new Vector2f(120,110), new Vector2f(256,256)));
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/default/images/default.png"));

        obj2SpriteRenderer.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);

        this.activeGameObject = obj1;
    }

    public void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getShader("assets/shaders/line2D.glsl");
        AssetPool.addSpritesheet("assets/images/default/spritesheets/spritesheet.png", new Spritesheet(AssetPool.getTexture("assets/images/default/spritesheets/spritesheet.png"), 120, 120, 4, 0));
        AssetPool.getTexture("assets/images/default/images/default.png");
    }

    @Override
    public void update(float dt) {
        levelEditorComponents.update(dt);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imGUI() {
        ImGui.begin("Editor");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;

        for (int i = 0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() / 4;
            float spriteHeight = sprite.getHeight() / 4;
            int id = sprite.getTexId();

            Vector2f[] texCoords = sprite.getTexCoords();


            ImGui.pushID(i);

            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject gameObject = Prefabs.generateSpriteObject(sprite, spriteWidth, spriteHeight);
                levelEditorComponents.getComponent(MouseControl.class).pickupObject(gameObject);
            }

            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);

            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
