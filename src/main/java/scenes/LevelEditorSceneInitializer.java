package scenes;


import components.*;
import gmen.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import util.AssetPool;

public class LevelEditorSceneInitializer extends SceneInitializer {

    private Spritesheet sprites;
    public LevelEditorSceneInitializer() {}
    private GameObject levelEditorComponents;


    //Spritesheet sprites;
    @Override
    public void init(Scene scene) {
        sprites = AssetPool.getSpritesheet("assets/images/default/spritesheets/spritesheet.png");

        levelEditorComponents = scene.createGameObject("Editor");
        levelEditorComponents.disableSerialization();
        levelEditorComponents.addComponent(new MouseControl());
        levelEditorComponents.addComponent(new GridLines());
        levelEditorComponents.addComponent(new EditorCamera(scene.camera()));
        Scene.setCamera(levelEditorComponents.getComponent(EditorCamera.class).getCamera());
        levelEditorComponents.addComponent(new TranslateGizmo(AssetPool.getTexture("assets/images/default/images/gizmo.png"), Window.getImGUILayer().getPropertiesWindow()));
        levelEditorComponents.addComponent(new ScaleGizmo(AssetPool.getTexture("assets/images/default/images/gizmo_scale.png"), Window.getImGUILayer().getPropertiesWindow()));
        levelEditorComponents.addComponent(new EditorSettings());
        levelEditorComponents.addComponent(new HierarchyHoverMarker(AssetPool.getTexture("assets/images/default/images/gizmo.png")));

        scene.addGameObjectToScene(levelEditorComponents);
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getShader("assets/shaders/line2D.glsl");
        AssetPool.addSpritesheet("assets/images/default/spritesheets/spritesheet.png", new Spritesheet(AssetPool.getTexture("assets/images/default/spritesheets/spritesheet.png"), 120, 120, 4, 0));
        AssetPool.getTexture("assets/images/default/images/default.png");
        AssetPool.getTexture("assets/images/default/images/gizmo.png");
        AssetPool.getTexture("assets/images/default/images/gizmo_scale.png");

        for (GameObject gameObject : scene.getGameObjects()) {
            if (gameObject.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spriteRenderer = gameObject.getComponent(SpriteRenderer.class);

                if (spriteRenderer.getTexture() != null) {
                    spriteRenderer.setTexture(AssetPool.getTexture(spriteRenderer.getTexture().getFilepath()));
                }
            }

            if (gameObject.getComponent(StateMachine.class) != null) {
                gameObject.getComponent(StateMachine.class).refreshTexture();
            }
        }
    }

    @Override
    public void imGUI() {
        ImGui.begin("Level Editor");
        levelEditorComponents.imGUI();
        ImGui.end();

        ImGui.begin("Editor");

        if (ImGui.beginTabBar("WindowTabBar")) {
            if (ImGui.beginTabItem("Sprites")) {
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
                        GameObject gameObject = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
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
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Prefabs")) {
                Spritesheet spritesheet = AssetPool.getSpritesheet("assets/images/default/spritesheets/spritesheet.png");
                Sprite sprite = spritesheet.getSprite(0);
                float spriteWidth = sprite.getWidth() / 4;
                float spriteHeight = sprite.getHeight() / 4;
                int id = sprite.getTexId();

                Vector2f[] texCoords = sprite.getTexCoords();

                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                    GameObject gameObject = Prefabs.generatePlayerObject();
                    levelEditorComponents.getComponent(MouseControl.class).pickupObject(gameObject);
                }
                ImGui.sameLine();

                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }
        ImGui.end();
    }

    public GameObject getLevelEditorComponents() {
        return this.levelEditorComponents;
    }
}
