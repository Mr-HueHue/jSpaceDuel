package sft.sftengine.graphics.textures;

import org.lwjgl.opengl.GL11;
import sft.sftengine.util.SFT_Util;

/**
 *
 * @author JJ
 */
public class SFTTexture {

    String filepath;
    int internalid, openglid;

    public SFTTexture(String filepath) {
        this(filepath, -1);
    }

    public SFTTexture(String filepath, int internalid) {
        this.filepath = filepath;
        this.internalid = internalid;
    }

    int createTexture() {
        openglid = SFT_Util.makeTexture(filepath);
        return openglid;
    }

    void set() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, openglid);
    }

    void activate() {
        set();
    }
    
    void remove() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
