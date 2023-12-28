package gmen;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    //shader source code (assets/shaders/)
    private String vertexShaderSrc = "#version 330 core\n" +
            "\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos,1.0);\n" +
            "}";

    //fragment shader source code (assets/shaders/)
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main() {\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;
    private float[] vertexArray = {
            //position            color
            0.5f, -0.5f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f, //bottom right
            -0.5f, 0.5f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f, //top left
            0.5f, 0.5f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f, //top right
            -0.5f, -0.5f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f, //bottom left
    };

    //counter-clockwise order
    private int[] elementArray = {
        2, 1, 0, //top right triangle
        0, 1, 3, //button left triangle
    };

    private int vaoID, vboID, eboID;
    public LevelEditorScene() {
    }

    @Override
    public void init() {
        //loading + compiling vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //pass shader source code "to the gpu"
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);
        //check for errors
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Couldn't compile vertex shader.");
            assert false : "";
        }

        //loading + compiling fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //pass shader source code "to the gpu"
        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);
        //check for errors
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Couldn't compile fragment shader.");
            assert false : "";
        }
        //linking shader
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        //check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("Couldn't link shaders.");
            assert false : "";
        }
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
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        //binding shader program
        glUseProgram(shaderProgram);
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

        glUseProgram(0);
    }
}
