package gmen;

import components.Sprite;
import components.SpriteRenderer;
import org.joml.Vector2f;
import renderer.Texture;

public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject gameObject = new GameObject("GENERATED_SPRITE_OBJECT", new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        gameObject.addComponent(renderer);

        return gameObject;
    }

    public static GameObject generateTextureObject(Texture texture, float sizeX, float sizeY) {
        GameObject gameObject = new GameObject("GENERATED_TEXTURE_OBJECT", new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setTexture(texture);
        gameObject.addComponent(renderer);

        return gameObject;
    }

    public static GameObject generateTextureObject(Texture texture, float sizeX, float sizeY, boolean isEditorObject) {
        GameObject gameObject;
        if (isEditorObject) {
            // the "%" prefix makes the selection tool ignore the object
            //useful for gizmos and other editor objects
            gameObject = new GameObject("%EDITOR_OBJECT", new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        } else {
            gameObject = new GameObject("GENERATED_TEXTURE_OBJECT", new Transform(new Vector2f(), new Vector2f(sizeX, sizeY)), 0);
        }

        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setTexture(texture);
        gameObject.addComponent(renderer);

        return gameObject;
    }
}
