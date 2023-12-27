package gmen;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private static Window window = null;
    private long glfwWindow;

    private static int currentSceneIndex = -1;
    private static Scene currentScene;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "test";
    }
    public static void changeScene(int newScene) {
        if (newScene == 0) {
            currentScene = new LevelEditorScene();
        } else if (newScene == 1) {
            currentScene = new LevelScene();
        } else {
            assert false : "Unknown scene (" + newScene + ")";
        }
    }
    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }
        return Window.window;
    }
    public void run() {
        System.out.println("Running!");
        init();
        loop();

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
    public void init() {
        GLFWErrorCallback. createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Couldn't initialize GLFW.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE,GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED,GLFW_TRUE);

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw  new IllegalStateException("Couldn't create GLFW window.");
        }

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();


        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
    }
    public void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(0.0f,0.5f,0.5f,1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                currentScene.upadte(dt);
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
