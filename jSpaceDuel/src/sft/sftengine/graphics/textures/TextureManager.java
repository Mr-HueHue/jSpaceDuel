package sft.sftengine.graphics.textures;

import java.util.HashMap;

/**
 *
 * @author JJ
 */
public class TextureManager {

    static int textcount = 0;
    /**
     * Name to internal id map
     */
    public static HashMap<String, Integer> textidmap = new HashMap<String, Integer>();
    /**
     * Internal id to textures map
     */
    public static HashMap<Integer, SFTTexture> idtextmap = new HashMap<Integer, SFTTexture>();

    /**
     * Adds a texture to OpenGL, it overrides textures with same name.
     * @param name The name which is used to call the texture
     * @param filepath Path of the image to use as texture.
     * @return the internal texture ID
     */
    public static int addTexture(String name, String filepath) {
        textcount++;
        textidmap.put(name, textcount);
        SFTTexture tex = new SFTTexture(filepath);
        idtextmap.put(textcount, tex);
        return textcount;
    }

    /**
     * Called internally as soon as the SFTEngineWindow has started up.
     */
    public static void createTextures() {
        for (int i = 0; i <= textcount; i++) {
            int openglid = idtextmap.get(i).createTexture();

        }
    }

    /**
     * Activate a texture
     * @param name the name of the texture set at creation.
     */
    public static void setTexture(String name) {
    }

    public static void setTextureO(int openglid) {
    }

    public static void setTexture(int internalid) {
    }
}
