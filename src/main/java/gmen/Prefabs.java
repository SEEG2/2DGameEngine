package gmen;

import components.Sprite;
import components.SpriteRenderer;
import components.Transform;
import org.joml.Vector2f;
import renderer.Texture;

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
            gameObject.properties.setIsEditorObject(true);
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
}
