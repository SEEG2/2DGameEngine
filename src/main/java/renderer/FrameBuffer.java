package renderer;

import static org.lwjgl.opengl.GL30.*;

public class FrameBuffer {
    private int fboID = 0;
    private Texture texture;
    public FrameBuffer(int width, int height) {
        // generate framebuffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        // creating texture to render data on
        this.texture = new Texture(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getTexID(), 0);

        // creating renderbuffer
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, width, height);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        assert glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE : "Framebuffer is not complete!";
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public int getFboID() {
        return fboID;
    }

    public int getTextureId() {
        return texture.getTexID();
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
