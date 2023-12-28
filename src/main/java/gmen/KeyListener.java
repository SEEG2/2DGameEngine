package gmen;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    //singleton instance
    private static KeyListener instance;
    //stores state of every key
    private boolean keyPressed[] = new boolean[350];

    private KeyListener() {

    }

    //returns an instance of this singleton / creates a new one it doesn't exist yet
    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    //updates values
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    //call this to check if a button is pressed
    public static boolean isKeyPressed(int keyCode) {
        if (keyCode < get().keyPressed.length) {
            return get().keyPressed[keyCode];
        } else {
            return false;
        }
    }
}
