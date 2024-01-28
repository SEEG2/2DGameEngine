package scenes;


import components.*;
import gmen.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    private Spritesheet sprites;
    public LevelEditorScene() {}
    GameObject levelEditorComponents = new GameObject("Level Editor");


    //Spritesheet sprites;
    @Override
    public void init() {
        loadResources();
        sprites = AssetPool.getSpritesheet("assets/images/default/spritesheets/spritesheet.png");

        this.camera = new Camera(new Vector2f());

        levelEditorComponents.addComponent(new Transform());
        levelEditorComponents.addComponent(new MouseControl());
        levelEditorComponents.addComponent(new GridLines());
        levelEditorComponents.addComponent(new EditorCamera(this.camera));
        levelEditorComponents.addComponent(new TranslateGizmo(AssetPool.getTexture("assets/images/default/images/gizmo.png"), Window.getImGUILayer().getPropertiesWindow()));
        levelEditorComponents.addComponent(new ScaleGizmo(AssetPool.getTexture("assets/images/default/images/gizmo_scale.png"), Window.getImGUILayer().getPropertiesWindow()));




        levelEditorComponents.start();

       Scene.setCamera(levelEditorComponents.getComponent(EditorCamera.class).getCamera());
    }

    public void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getShader("assets/shaders/line2D.glsl");
        AssetPool.addSpritesheet("assets/images/default/spritesheets/spritesheet.png", new Spritesheet(AssetPool.getTexture("assets/images/default/spritesheets/spritesheet.png"), 120, 120, 4, 0));
        AssetPool.getTexture("assets/images/default/images/default.png");
        AssetPool.getTexture("assets/images/default/images/gizmo.png");
        AssetPool.getTexture("assets/images/default/images/gizmo_scale.png");

        for (GameObject gameObject : this.gameObjects) {
            if (gameObject.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);

                if (spriteRenderer.getTexture() != null) {
                    spriteRenderer.setTexture(AssetPool.getTexture(spriteRenderer.getTexture().getFilepath()));
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        levelEditorComponents.update(dt);
        this.camera.adjustProjection();

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }

    @Override
    public void imGUI() {
        ImGui.begin("Level Editor");
        levelEditorComponents.imGUI();
        ImGui.end();

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

    @Override
    public void render() {
        this.renderer.render();
    }
}
