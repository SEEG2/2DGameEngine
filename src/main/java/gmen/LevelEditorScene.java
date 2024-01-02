package gmen;

import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {


    private int vertexID, fragmentID, shaderProgram;
    private float[] vertexArray = {
            //position         color                  UV Coordinates
            100f, 0f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f,   1,1, //bottom right
            0f, 100f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f,   0,0,//top left
            100f, 100f, 0.0f,  1.0f, 0.0f, 1.0f, 1.0f,   1,0, //top right
            0f, 0f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f,   0,1 //bottom left
    };

    //counter-clockwise order
    private int[] elementArray = {
        2, 1, 0, //top right triangle
        0, 1, 3, //button left triangle
    };

    private int vaoID, vboID, eboID;
    private Shader defaultShader;
    private Texture testTexture;
    private GameObject testObj;
    public LevelEditorScene() {
    }

    @Override
    public void init() {
        this.testObj = new GameObject("test");
        this.testObj.addComponent(new SpriteRenderer());
        this.addGameObjectToScene(this.testObj);
        this.camera = new Camera(new Vector2f(-200, -300));
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        //loads the default image replace with a valid file path to load a specific one
        this.testTexture = new Texture("");

        //creating vao object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //creating a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //creating vbo upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //creating the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        defaultShader.use();
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        //binding the vao that we are using
        glBindVertexArray(vaoID);
        //enabling vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //unbinding everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }
}
