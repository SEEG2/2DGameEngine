package util;

import components.Spritesheet;
import gmen.Sound;
import renderer.Shader;
import renderer.Texture;

import javax.swing.plaf.SliderUI;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    public static Shader getShader(String resourceName) {
        File file = new File(resourceName);
        if(AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName) {
        File file = new File(resourceName);
        if (textures.containsKey(file.getAbsolutePath())) {
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {}
        Texture texture = new Texture();
        texture.init(resourceName);
        AssetPool.textures.put(file.getAbsolutePath(), texture);
        return texture;
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet) {
        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            AssetPool.spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        File file = new File(resourceName);
        if (!AssetPool.spritesheets.containsKey(file.getAbsolutePath())) {
            assert false : "Couldn't access spritesheet (" + resourceName + ").";
        }

        return AssetPool.spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }

    public static Sound getSound(String soundFile) {
        File file = new File(soundFile);
        String path = file.getAbsolutePath();

        if (sounds.containsKey(path)) {
            return sounds.get(path);
        } else {
            assert false : "Sound file not added yet.";
        }

        return null;
    }

    public static Sound addSound(String soundFile, boolean loops) {
        File file = new File(soundFile);
        String path = file.getAbsolutePath();

        if (sounds.containsKey(path)) {
            return sounds.get(path);
        }
        Sound sound = new Sound(soundFile, loops);
        AssetPool.sounds.put(path, sound);
        return sound;
    }

    public static Collection<Sound> getAllSounds() {
        return sounds.values();
    }
}
