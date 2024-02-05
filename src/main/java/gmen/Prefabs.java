package gmen;

import components.*;
import org.lwjgl.system.CallbackI;
import renderer.Texture;
import util.AssetPool;

public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject gameObject = Window.getScene().createGameObject("GENERATED_OBJECT");
        gameObject.transform.scale.x = sizeX;
        gameObject.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        gameObject.addComponent(renderer);

        return gameObject;
    }

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY, boolean isEditorObject) {
        GameObject gameObject;
        if (isEditorObject) {
            // the "%" prefix makes the selection tool ignore the object
            //useful for gizmos and other editor objects
            gameObject = Window.getScene().createGameObject("%EDITOR_OBJECT");
            gameObject.transform.scale.x = sizeX;
            gameObject.transform.scale.y = sizeY;
            gameObject.transform.zIndex = 255;
        } else {
            gameObject = Window.getScene().createGameObject("GENERATED_OBJECT");
            gameObject.transform.scale.x = sizeX;
            gameObject.transform.scale.y = sizeY;
        }

        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        gameObject.addComponent(renderer);

        return gameObject;
    }

    public static GameObject generateTextureObject(Texture texture, float sizeX, float sizeY) {
        GameObject gameObject = Window.getScene().createGameObject("GENERATED_OBJECT");
        gameObject.transform.scale.x = sizeX;
        gameObject.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setTexture(texture);
        gameObject.addComponent(renderer);

        return gameObject;
    }

    public static GameObject generateTextureObject(Texture texture, float sizeX, float sizeY, boolean isEditorObject) {
        GameObject gameObject;
        if (isEditorObject) {
            gameObject = Window.getScene().createGameObject("EDITOR_OBJECT");
            gameObject.transform.scale.x = sizeX;
            gameObject.transform.scale.y = sizeY;
            gameObject.transform.zIndex = 255;
            gameObject.editorProperties.isEditorObject = true;
            gameObject.disableSerialization();
        } else {
            gameObject = Window.getScene().createGameObject("GENERATED_OBJECT");
            gameObject.transform.scale.x = sizeX;
            gameObject.transform.scale.y = sizeY;
        }

        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setTexture(texture);
        gameObject.addComponent(renderer);

        return gameObject;
    }

    public static GameObject generatePlayerObject() {
        Spritesheet spritesheet = AssetPool.getSpritesheet("assets/images/default/spritesheets/spritesheet.png");

        GameObject gameObject = generateSpriteObject(spritesheet.getSprite(0), 0.25f,0.25f);

        AnimationState run = new AnimationState();
        run.title = "Run";
        float defaultFrameTime = 0.25f;
        run.addFrame(spritesheet.getSprite(0), defaultFrameTime);
        run.addFrame(spritesheet.getSprite(1), defaultFrameTime);
        run.addFrame(spritesheet.getSprite(2), defaultFrameTime);
        run.setLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);

        stateMachine.setDefaultStateTitle(run.title);
        gameObject.addComponent(stateMachine);

        return gameObject;
    }
}
