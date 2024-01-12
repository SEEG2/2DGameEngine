package gmen;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private static Window window = null;
    private long glfwWindow;
    private static Scene currentScene;
    private ImGUILayer imGUILayer;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "test";
    }

    public static void changeScene(int newScene) {
        if (newScene == 0) {
            currentScene = new LevelEditorScene();
            currentScene.init();
            currentScene.start();
        } else if (newScene == 1) {
            currentScene = new LevelScene();
            currentScene.init();
            currentScene.start();
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

    public static Scene getScene() {
        return get().currentScene;
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

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.imGUILayer = new ImGUILayer(glfwWindow);
        this.imGUILayer.initImGui();

        changeScene(0);
    }
    public void loop() {
        float beginTime = (float) glfwGetTime();
        float endTime;
        float dt = -1.0f;
        currentScene.load();

        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents();
            glClearColor(0.0f,0.5f,0.5f,1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0) {
                currentScene.update(dt);
            }
            this.imGUILayer.update(dt,currentScene);
            glfwSwapBuffers(glfwWindow);

            endTime = (float) glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }

        currentScene.saveExit();
    }

    public static int getWidth() {
        return get().width;
    }


    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int width) {
        get().width = width;
    }

    public static void setHeight(int height) {
        get().height = height;
    }
}
