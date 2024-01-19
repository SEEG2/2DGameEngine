package renderer;

import org.lwjgl.BufferUtils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private  String filepath;
    private int texID;
    private int width, height;


    public void init(String filepath) {
        this.filepath = filepath;

        //Generating texture on GPU

        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        //setting texture parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        //blur when stretching/shrinking
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if (image != null) {
            this.width = width.get(0);
            this.height = height.get(0);
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB , width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false: "Unknown number of channels (" + filepath + ").";
            }
        } else {
            image = stbi_load("assets/images/default/images/default.png", width, height, channels, 0);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA , width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            //assert false: "Couldn't load texture (" + filepath + ").";
        }
        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getTexID() {return this.texID;}
    public String getFilepath() {
        return this.filepath;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Texture)) return false;

        Texture tex = (Texture) o;
        return tex.getWidth() == this.width && tex.getHeight() == this.height && tex.getTexID() == this.texID && tex.getFilepath().equals(this.filepath);
    }
}
