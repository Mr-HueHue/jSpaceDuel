package sft.sftengine.util;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;
import sft.sftengine.graphics.SFT_Font;
import sft.sftengine.graphics.SFT_Image;

/**
 * Class that uses the great GLApp functions.
 * 
 * @author JJ
 */
public class SFT_Util {

    // Byte size of data types: Used when allocating native buffers
    public static final int SIZE_DOUBLE = 8;
    public static final int SIZE_FLOAT = 4;
    public static final int SIZE_INT = 4;
    public static final int SIZE_BYTE = 1;

    /**
     * Find a power of two equal to or greater than the given value.
     * Ie. getPowerOfTwoBiggerThan(800) will return 1024.
     * <P>
     * @see makeTextureForScreen()
     * @param dimension
     * @return a power of two equal to or bigger than the given dimension
     */
    public static int getPowerOfTwoBiggerThan(int n) {
        if (n < 0) {
            return 0;
        }
        --n;
        n |= n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n + 1;
    }

    /**
     * Allocate a texture (glGenTextures) and return the handle to it.
     */
    public static int allocateTexture() {
        IntBuffer textureHandle = allocInts(1);
        GL11.glGenTextures(textureHandle);
        return textureHandle.get(0);
    }

    /**
     * "Select" the given texture for further texture operations.
     */
    public static void activateTexture(int textureHandle) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
    }

    /**
     * Create a blank square texture with the given size.
     * @return  the texture handle
     */
    public static int makeTexture(int w) {
        ByteBuffer pixels = allocBytes(w * w * SIZE_INT);  // allocate 4 bytes per pixel
        return makeTexture(pixels, w, w, false);
    }

    /**
     * Create a texture from the given pixels in the default Java ARGB int format.<BR>
     * Configure the texture to repeat in both directions and use LINEAR for magnification.
     * <P>
     * @return  the texture handle
     */
    public static int makeTexture(int[] pixelsARGB, int w, int h, boolean anisotropic) {
        if (pixelsARGB != null) {
            ByteBuffer pixelsRGBA = SFT_Image.convertImagePixelsRGBA(pixelsARGB, w, h, true);
            return makeTexture(pixelsRGBA, w, h, anisotropic);
        }
        return 0;
    }

    /**
     * Create a texture from the given pixels in the default OpenGL RGBA pixel format.
     * Configure the texture to repeat in both directions and use LINEAR for magnification.
     * <P>
     * @return  the texture handle
     */
    public static int makeTexture(ByteBuffer pixels, int w, int h, boolean anisotropic) {
        // get a new empty texture
        int textureHandle = allocateTexture();
        // preserve currently bound texture, so glBindTexture() below won't affect anything)
        GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
        // 'select' the new texture by it's handle
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        // set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);

        // make texture "anisotropic" so it will minify more gracefully
        if (anisotropic && extensionExists("GL_EXT_texture_filter_anisotropic")) {
            // Due to LWJGL buffer check, you can't use smaller sized buffers (min_size = 16 for glGetFloat()).
            FloatBuffer max_a = allocFloats(16);
            // Grab the maximum anisotropic filter.
            GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, max_a);
            // Set up the anisotropic filter.
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, max_a.get(0));
        }

        // Create the texture from pixels
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
                0, // level of detail
                GL11.GL_RGBA8, // internal format for texture is RGB with Alpha
                w, h, // size of texture image
                0, // no border
                GL11.GL_RGBA, // incoming pixel format: 4 bytes in RGBA order
                GL11.GL_UNSIGNED_BYTE, // incoming pixel data type: unsigned bytes
                pixels);				// incoming pixels

        // restore previous texture settings
        GL11.glPopAttrib();

        return textureHandle;
    }

    /**
     * Create a texture from the given pixels in ARGB format.  The pixels buffer contains
     * 4 bytes per pixel, in ARGB order.  ByteBuffers are created with native hardware byte
     * orders, so the pixels can be in big-endian (ARGB) order, or little-endian (BGRA) order.
     * Set the pixel_byte_order accordingly when creating the texture.
     * <P>
     * Configure the texture to repeat in both directions and use LINEAR for magnification.
     * <P>
     * NOTE: I'm having problems creating mipmaps when image pixel data is in GL_BGRA format.
     * Looks like GLU type param doesn't recognize GL_UNSIGNED_INT_8_8_8_8 and
     * GL_UNSIGNED_INT_8_8_8_8_REV so I can't specify endian byte order.  Mipmaps look
     * right on PC but colors are reversed on Mac.  Have to stick with GL_RGBA
     * byte order for now.
     * <P>
     * @return  the texture handle
     */
    public static int makeTextureARGB(ByteBuffer pixels, int w, int h) {
        // byte buffer has ARGB ints in little endian or big endian byte order
        int pixel_byte_order = (pixels.order() == ByteOrder.BIG_ENDIAN)
                ? GL12.GL_UNSIGNED_INT_8_8_8_8
                : GL12.GL_UNSIGNED_INT_8_8_8_8_REV;
        // get a new empty texture
        int textureHandle = allocateTexture();
        // 'select' the new texture by it's handle
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        // set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); //GL11.GL_NEAREST);
        // Create the texture from pixels
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D,
                0, // level of detail
                GL11.GL_RGBA8, // internal format for texture is RGB with Alpha
                w, h, // size of texture image
                0, // no border
                GL12.GL_BGRA, // incoming pixel format: 4 bytes in ARGB order
                pixel_byte_order, // incoming pixel data type: little or big endian ints
                pixels);				// incoming pixels
        return textureHandle;
    }

    /**
     * Build Mipmaps for currently bound texture (builds a set of textures at various
     * levels of detail so that texture will scale up and down gracefully)
     *
     * @param textureImg  the texture image
     * @return   error code of buildMipMap call
     */
    public static int makeTextureMipMap(int textureHandle, SFT_Image textureImg) {
        int ret = 0;
        if (textureImg != null && textureImg.isLoaded()) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
            ret = GLU.gluBuild2DMipmaps(GL11.GL_TEXTURE_2D, GL11.GL_RGBA8,
                    textureImg.w, textureImg.h,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureImg.getPixelBytes());
            if (ret != 0) {
                System.err.println("SFT_Util.makeTextureMipMap(): Error occured while building mip map, ret=" + ret + " error=" + GLU.gluErrorString(ret));
            }
            // Assign the mip map levels and texture info
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        }
        return ret;
    }

    /**
     * Create a texture large enough to hold the screen image.  Use RGBA8 format
     * to insure colors are copied exactly.  Use GL_NEAREST for magnification
     * to prevent slight blurring of image when screen is drawn back.
     *
     * @see frameCopy()
     * @see frameDraw()
     */
    public static int makeTextureForScreen(int screenSize) {
        // get a texture size big enough to hold screen (512, 1024, 2048 etc.)
        int screenTextureSize = getPowerOfTwoBiggerThan(screenSize);

        // get a new empty texture
        int textureHandle = allocateTexture();
        ByteBuffer pixels = allocBytes(screenTextureSize * screenTextureSize * SIZE_INT);
        // 'select' the new texture by it's handle
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        // set texture parameters
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        // use GL_NEAREST to prevent blurring during frequent screen restores
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        // Create the texture from pixels: use GL_RGBA8 to insure exact color copy
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, screenTextureSize, screenTextureSize, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
        return textureHandle;
    }

    /**
     * Create a texture and mipmap from the given image file.
     */
    public static int makeTexture(String textureImagePath) {
        int textureHandle = 0;
        SFT_Image textureImg = loadImage(textureImagePath);
        if (textureImg != null) {
            textureHandle = makeTexture(textureImg);
            makeTextureMipMap(textureHandle, textureImg);
        }
        return textureHandle;
    }

    /**
     * Create a texture and optional mipmap with the given parameters.
     *
     * @param mipmap: if true, create mipmaps for the texture
     * @param anisotropic: if true, enable anisotropic filtering
     */
    public static int makeTexture(String textureImagePath, boolean mipmap, boolean anisotropic) {
        int textureHandle = 0;
        SFT_Image textureImg = loadImage(textureImagePath);
        if (textureImg != null) {
            textureHandle = makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, anisotropic);
            if (mipmap) {
                makeTextureMipMap(textureHandle, textureImg);
            }
        }
        return textureHandle;
    }

    /**
     * Create a texture from the given image.
     */
    public static int makeTexture(SFT_Image textureImg) {
        if (textureImg != null) {
            if (isPowerOf2(textureImg.w) && isPowerOf2(textureImg.h)) {
                return makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, false);
            } else {
                System.out.println("SFT_Util.makeTexture(GLImage) Warning: not a power of two: " + textureImg.w + "," + textureImg.h);
                textureImg.convertToPowerOf2();
                return makeTexture(textureImg.pixelBuffer, textureImg.w, textureImg.h, false);
            }
        }
        return 0;
    }

    /**
     * Make a blank image of the given size.
     * @return  the new GLImage
     */
    public static SFT_Image makeImage(int w, int h) {
        ByteBuffer pixels = allocBytes(w * h * SIZE_INT);
        return new SFT_Image(pixels, w, h);
    }

    /**
     * Load an image from the given file and return a GLImage object.
     * @param image filename
     * @return the loaded GLImage
     */
    public static SFT_Image loadImage(String imgFilename) {
        SFT_Image img = new SFT_Image(imgFilename);
        if (img.isLoaded()) {
            return img;
        }
        return null;
    }

    /**
     * Load an image from the given file and return a ByteBuffer containing ARGB pixels.<BR>
     * Can be used to create textures. <BR>
     * @param imgFilename
     * @return
     */
    public static ByteBuffer loadImagePixels(String imgFilename) {
        SFT_Image img = new SFT_Image(imgFilename);
        return img.pixelBuffer;
    }

    /**
     * De-allocate the given texture (glDeleteTextures()).
     */
    public static void deleteTexture(int textureHandle) {
        IntBuffer bufferTxtr = allocInts(1).put(textureHandle);
        GL11.glDeleteTextures(bufferTxtr);
    }

    /**
     *  Returns true if n is a power of 2.  If n is 0 return zero.
     */
    public static boolean isPowerOf2(int n) {
        if (n == 0) {
            return false;
        }
        return (n & (n - 1)) == 0;
    }

    /**
     * Copy pixels from a ByteBuffer to a texture.  The buffer pixels are integers in ARGB format
     * (this is the Java default format you get from a BufferedImage) or BGRA format (this is the
     * native order of Intel systems.
     *
     * The glTexSubImage2D() call treats the incoming pixels as integers
     * in either big-endian (ARGB) or little-endian (BGRA) formats based on the setting
     * of the bytebuffer (pixel_byte_order).
     *
     * @param bb  ByteBuffer of pixels stored as ARGB or BGRA integers
     * @param w   width of source image
     * @param h   height of source image
     * @param textureHandle  texture to copy pixels into
     */
    public static void copyPixelsToTexture(ByteBuffer bb, int w, int h, int textureHandle) {
        int pixel_byte_order = (bb.order() == ByteOrder.BIG_ENDIAN)
                ? GL12.GL_UNSIGNED_INT_8_8_8_8
                : GL12.GL_UNSIGNED_INT_8_8_8_8_REV;

        // "select" the texture that we'll write into
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);

        // Copy pixels to texture
        GL11.glTexSubImage2D(
                GL11.GL_TEXTURE_2D, // always GL_TEXTURE_2D
                0, // texture detail level: always 0
                0, 0, // x,y offset into texture
                w, h, // dimensions of image pixel data
                GL12.GL_BGRA, // format of pixels in texture (little_endian - native for PC)
                pixel_byte_order, // format of pixels in bytebuffer (big or little endian ARGB integers)
                bb // image pixel data
                );
    }

    /**
     * Calls glTexSubImage2D() to copy pixels from an image into a texture.
     */
    public static void copyImageToTexture(SFT_Image img, int textureHandle) {
        copyPixelsToTexture(img.pixelBuffer, img.w, img.h, textureHandle);
    }

    public static ByteBuffer allocBytes(int howmany) {
        return ByteBuffer.allocateDirect(howmany * SIZE_BYTE).order(ByteOrder.nativeOrder());
    }

    public static IntBuffer allocInts(int howmany) {
        return ByteBuffer.allocateDirect(howmany * SIZE_INT).order(ByteOrder.nativeOrder()).asIntBuffer();
    }

    public static FloatBuffer allocFloats(int howmany) {
        return ByteBuffer.allocateDirect(howmany * SIZE_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public static DoubleBuffer allocDoubles(int howmany) {
        return ByteBuffer.allocateDirect(howmany * SIZE_DOUBLE).order(ByteOrder.nativeOrder()).asDoubleBuffer();
    }

    public static ByteBuffer allocBytes(byte[] bytearray) {
        ByteBuffer bb = ByteBuffer.allocateDirect(bytearray.length * SIZE_BYTE).order(ByteOrder.nativeOrder());
        bb.put(bytearray).flip();
        return bb;
    }

    public static IntBuffer allocInts(int[] intarray) {
        IntBuffer ib = ByteBuffer.allocateDirect(intarray.length * SIZE_FLOAT).order(ByteOrder.nativeOrder()).asIntBuffer();
        ib.put(intarray).flip();
        return ib;
    }

    public static FloatBuffer allocFloats(float[] floatarray) {
        FloatBuffer fb = ByteBuffer.allocateDirect(floatarray.length * SIZE_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(floatarray).flip();
        return fb;
    }

    public static DoubleBuffer allocDoubles(double[] darray) {
        DoubleBuffer fb = ByteBuffer.allocateDirect(darray.length * SIZE_DOUBLE).order(ByteOrder.nativeOrder()).asDoubleBuffer();
        fb.put(darray).flip();
        return fb;
    }

    public static void put(ByteBuffer b, byte[] values) {
        b.clear();
        b.put(values).flip();
    }

    public static void put(IntBuffer b, int[] values) {
        b.clear();
        b.put(values).flip();
    }

    public static void put(FloatBuffer b, float[] values) {
        b.clear();
        b.put(values).flip();
    }

    public static void put(DoubleBuffer b, double[] values) {
        b.clear();
        b.put(values).flip();
    }

    /**
     *  copy ints from the given byteBuffer into the given int array.
     *  @param b       source ByteBuffer
     *  @param values  target integer array, must be same length as ByteBuffer capacity/4
     */
    public static void get(ByteBuffer b, int[] values) {
        b.asIntBuffer().get(values, 0, values.length);
    }

    /**
     *  copy ints from the given IntBuffer into the given int array.
     *  @param b       source IntBuffer
     *  @param values  target integer array, must be same length as IntBuffer
     */
    public static void get(IntBuffer b, int[] values) {
        b.get(values, 0, values.length);
    }

    /**
     *  return the contents of the byteBuffer as an array of ints.
     *  @param b  source ByteBuffer
     */
    public static int[] getInts(ByteBuffer b) {
        int[] values = new int[b.capacity() / SIZE_INT];
        b.asIntBuffer().get(values, 0, values.length);
        return values;
    }

    /**
     * Return true if the OpenGL context supports the given OpenGL extension.
     */
    public static boolean extensionExists(String extensionName) {
        String[] GLExtensions = GL11.glGetString(GL11.GL_EXTENSIONS).split(" ");
        ArrayList<String> OpenGLextensions = new ArrayList<String>();
        for (int i = 0; i < GLExtensions.length; i++) {
            OpenGLextensions.add(GLExtensions[i].toUpperCase());
        }

        return OpenGLextensions.contains(extensionName.toUpperCase());
    }

    /**
     * preserve all OpenGL settings that can be preserved.  Use this
     * function to isolate settings changes.  Call pushAttrib() before
     * calling glEnable(), glDisable(), glMatrixMode() etc. After
     * your code executes, call popAttrib() to return to the
     * previous settings.
     *
     * For better performance, call pushAttrib() with specific settings
     * flags to preserve only specific settings.
     *
     * @see popAttrib()
     */
    public static void pushAttrib() {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
    }

    /**
     * preserve the specified OpenGL setting.  Call popAttrib() to return to
     * the preserved state.
     *
     * @see popAttrib()
     */
    public static void pushAttrib(int attribute_bits) {
        GL11.glPushAttrib(attribute_bits);
    }

    /**
     * preserve the OpenGL settings that will be affected when we draw in ortho
     * mode over the scene.  For example if we're drawing an interface layer,
     * buttons, popup menus, cursor, text, etc. we need to turn off lighting,
     * turn on blending, set color to white and turn off depth test.
     * <P>
     * call pushAttribOverlay(), enable settings that you need, when done call popAttrib()
     *
     * @see popAttrib()
     */
    public static void pushAttribOrtho() {
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_TEXTURE_BIT | GL11.GL_LIGHTING_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * preserve the OpenGL viewport settings.
     * <pre>
     *       pushAttribViewport();
     *           setViewport(0,0,displaymode.getWidth(),displaymode.getHeight());
     *           ... do some drawing outside of previous viewport area
     *       popAttrib();
     * </pre>
     *
     * @see popAttrib()
     */
    public static void pushAttribViewport() {
        GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
    }

    /**
     * return to the OpenGL settings that were preserved by the previous pushAttrib() call.
     *
     * @see pushAttrib()
     */
    public static void popAttrib() {
        GL11.glPopAttrib();
    }

    /**
     * Set OpenGL to render in 3D perspective.  Set the projection matrix using
     * GLU.gluPerspective().  The projection matrix controls how the scene is
     * "projected" onto the screen.  Think of it as the lens on a camera, which
     * defines how wide the field of vision is, how deep the scene is, and how
     * what the aspect ratio will be.
     */
    public static void setPerspective(float aspectRatio) {
        // select projection matrix (controls perspective)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(40f, aspectRatio, 1f, 1000f);
        // return to modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * Set OpenGL to render in flat 2D (no perspective) with a one-to-one
     * relation between screen pixels and world coordinates, ie. if
     * you draw a 10x10 quad at 100,100 it will appear as a 10x10 pixel
     * square on screen at pixel position 100,100.
     * <P>
     * <B>ABOUT Ortho and Viewport:</B><br>
     * Let's say we're drawing in 2D and want to have a cinema proportioned
     * viewport (16x9), and want to bound our 2D rendering into that area ie.
     * <PRE>
    ___________1024,576
    |           |
    |  Scene    |      Set the bounds on the scene geometry
    |___________|      to the viewport size and shape
    0,0
    
    ___________1024,576
    |           |
    |  Ortho    |      Set the projection to cover the same
    |___________|      area as the scene
    0,0
    
    ___________ 1024,768
    |___________|
    |           |1024,672
    |  Viewport |      Set the viewport to the same shape
    0,96|___________|      as scene and ortho, but centered on
    |___________|      screen.
    0,0
     *</PRE>
     */
    public static void setOrtho(double viewportW, double viewportH) {
        // select projection matrix (controls view on screen)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // set ortho to same size as viewport, positioned at 0,0
        GL11.glOrtho(
                0, viewportW, // left,right
                0, viewportH, // bottom,top
                -500, 500);    // Zfar, Znear
        // return to modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    public static void setOrtho(int width, int height) {
        // select projection matrix (controls view on screen)
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        // set ortho to same size as viewport, positioned at 0,0
        GL11.glOrtho(
                0, width, // left,right
                0, height, // bottom,top
                -500, 500);    // Zfar, Znear
        // return to modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * Set OpenGL to render in flat 2D (no perspective) on top of current scene.
     * Preserve current projection and model views, and disable depth testing.
     * Ortho world size will be same as viewport size, so any ortho drawing
     * (drawQuad(), drawImageFullscreen(), etc.) will be scaled to fit viewport.
     * <P>
     * NOTE: if the viewport is the same size as the window (by default it is),
     * then setOrtho() will make the world coordinates exactly match the screen
     * pixel positions.  This is convenient for mouse interaction, but be warned:
     * if you setViewport() to something other than fullscreen, then you need
     * to use getWorldCoordsAtScreen() to convert screen xy to world xy.
     * <P>
     * Once Ortho is on, glTranslate() will take pixel coords as arguments,
     * with the lower left corner 0,0 and the upper right corner 1024,768 (or
     * whatever your screen size is).  !!!
     *
     * @param viewportW the width of the viewport
     * @param viewportH the height of the viewport
     * 
     * @see setOrthoOff()
     * @see setViewport(int,int,int,int)
     */
    public static void setOrthoOn() {
        // prepare projection matrix to render in 2D
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();                   // preserve perspective view
        GL11.glLoadIdentity();                 // clear the perspective matrix
        GL11.glOrtho( // turn on 2D mode
                ////viewportX,viewportX+viewportW,    // left, right
                ////viewportY,viewportY+viewportH,    // bottom, top    !!!
                0, Display.getDisplayMode().getWidth(), // left, right
                0, Display.getDisplayMode().getHeight(), // bottom, top
                -500, 500);                        // Zfar, Znear
        // clear the modelview matrix
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();				   // Preserve the Modelview Matrix
        GL11.glLoadIdentity();				   // clear the Modelview Matrix
        // disable depth test so further drawing will go over the current scene
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Turn 2D mode off.  Return the projection and model views to their
     * preserved state that was saved when setOrthoOn() was called, and
     * re-enable depth testing.
     *
     * @see setOrthoOn()
     */
    public static void setOrthoOff() {
        // restore the original positions and views
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        // turn Depth Testing back on
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Define the position and size of the screen area in which the
     * OpenGL context will draw. Position and size of the area are given
     * in exact pixel sizes.  By default the viewport is the same size as
     * the window (displayWidth,displayHeight).
     * <P>
     * NOTE: by default the window size, viewport size and setOrtho() size are all
     * the same, so in ortho mode screen pixel positions exactly match to world
     * coordinates.  THIS IS NO LONGER TRUE if you setViewport() to some other
     * size.  With a custom viewport you need to use getWorldCoordsAtScreen()
     * to convert screen xy to world xy.
     * <P>
     * @param x       position of the lower left of viewport area, in pixels
     * @param y
     * @param width   size of the viewport area, in pixels
     * @param height
     * @return 
     *
     * @see setPerspective()
     * @see setOrtho()
     * @see setOrthoDimensions(int,int)
     */
    public static float setViewport(int x, int y, int width, int height) {
        GL11.glViewport(x, y, width, height);
        return (float) width / (float) height;
    }

    /**
     * Reset the viewport to full screen (displayWidth x displayHeight).
     *
     * @see setViewport(int,int,int,int)
     */
    public static void resetViewport() {
        setViewport(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
    }

    /**
     * A simple way to set eye position.  Calls gluLookat() to place
     * the viewpoint <distance> units up the Z axis from the given target position,
     * looking at the target position. The camera is oriented vertically (Y axis is up).
     * away.
     * 
     * 
     * @param lookatX position in space to look at
     * @param lookatY position in space to look at
     * @param lookatZ position in space to look at
     * @param distance distance upwards from given lookat point
     */
    public static void lookAt(float lookatX, float lookatY, float lookatZ, float distance) {
        // set viewpoint
        GLU.gluLookAt(
                lookatX, lookatY, lookatZ + distance, // eye is at the same XY as the target, <distance> units up the Z axis
                lookatX, lookatY, lookatZ, // look at the target position
                0, 1, 0);                            // the Y axis is up
    }

    public static void print2DText(float posx, float posy, String text, SFT_Font font) {
        if (text != null) {
            // preserve current GL settings
            pushAttribOrtho();
            {
                // turn off lighting
                GL11.glDisable(GL11.GL_LIGHTING);
                // enable alpha blending, so character background is transparent
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                // enable the charset texture
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTexture());
                // prepare to render in 2D
                setOrthoOn();
                // draw the text
                GL11.glTranslatef(posx, posy, 0);        // Position The Text (in pixel coords)
                for (int i = 0; i < text.length(); i++) {
                    GL11.glCallList(font.getFontListBase() + (text.charAt(i) - 32));
                }
                // restore the original positions and views
                setOrthoOff();
            }
            popAttrib();  // restore previous settings
        }
    }

    
    /**
     * 
     * @param text Text to p
     * @param font 
     */
    public static void print3DText(String text, SFT_Font font) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getTexture());
        for (int i = 0; i < text.length(); i++) {
            GL11.glCallList(font.getFontListBase() + (text.charAt(i) - 32));
        }
    }

    /**
     * make a time stamp for filename
     * @return a string with format "YYYYMMDD-hhmmss"
     */
    public static String makeTimestamp() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        int seconds = now.get(Calendar.SECOND);
        String datetime = ""
                + (day < 10 ? "0" : "") + day + "."
                + (month < 10 ? "0" : "") + month + "."
                + year
                + " "
                + (hours < 10 ? "0" : "") + hours + "."
                + (minutes < 10 ? "0" : "") + minutes + "."
                + (seconds < 10 ? "0" : "") + seconds + ".";
        return datetime;
    }

    /**
     * Set the color of a 'positional' light (a light that has a specific
     * position within the scene).  <BR>
     *
     * Pass in an OpenGL light number (GL11.GL_LIGHT1),
     * the 'Diffuse' and 'Ambient' colors (direct light and reflected light),
     * and the position.<BR>
     *
     * @param GLLightHandle
     * @param diffuseLightColor
     * @param ambientLightColor
     * @param position
     */
    public static void setLight(int GLLightHandle,
            float[] diffuseLightColor, float[] ambientLightColor, float[] specularLightColor, float[] position) {
        FloatBuffer ltDiffuse = allocFloats(diffuseLightColor);
        FloatBuffer ltAmbient = allocFloats(ambientLightColor);
        FloatBuffer ltSpecular = allocFloats(specularLightColor);
        FloatBuffer ltPosition = allocFloats(position);
        GL11.glLight(GLLightHandle, GL11.GL_DIFFUSE, ltDiffuse);   // color of the direct illumination
        GL11.glLight(GLLightHandle, GL11.GL_SPECULAR, ltSpecular); // color of the highlight
        GL11.glLight(GLLightHandle, GL11.GL_AMBIENT, ltAmbient);   // color of the reflected light
        GL11.glLight(GLLightHandle, GL11.GL_POSITION, ltPosition);
        GL11.glEnable(GLLightHandle);	// Enable the light (GL_LIGHT1 - 7)
        //GL11.glLightf(GLLightHandle, GL11.GL_QUADRATIC_ATTENUATION, .005F);    // how light beam drops off
    }

    public static void setSpotLight(int GLLightHandle,
            float[] diffuseLightColor, float[] ambientLightColor,
            float[] position, float[] direction, float cutoffAngle) {
        FloatBuffer ltDirection = allocFloats(direction);
        setLight(GLLightHandle, diffuseLightColor, ambientLightColor, diffuseLightColor, position);
        GL11.glLightf(GLLightHandle, GL11.GL_SPOT_CUTOFF, cutoffAngle);   // width of the beam
        GL11.glLight(GLLightHandle, GL11.GL_SPOT_DIRECTION, ltDirection);    // which way it points
        GL11.glLightf(GLLightHandle, GL11.GL_CONSTANT_ATTENUATION, 2F);    // how light beam drops off
        //GL11.glLightf(GLLightHandle, GL11.GL_LINEAR_ATTENUATION, .5F);    // how light beam drops off
        //GL11.glLightf(GLLightHandle, GL11.GL_QUADRATIC_ATTENUATION, .5F);    // how light beam drops off
    }

    /**
     * Set the color of the Global Ambient Light.  Affects all objects in
     * scene regardless of their placement.
     */
    public static void setAmbientLight(float[] ambientLightColor) {
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, allocFloats(ambientLightColor));
    }

    /**
     * Set the position of a light to the given xyz. NOTE: Positional light only,
     * not directional.
     */
    public static void setLightPosition(int GLLightHandle, float x, float y, float z) {
        GL11.glLight(GLLightHandle, GL11.GL_POSITION, allocFloats(new float[]{x, y, z, 1}));
    }

    /**
     * Set the position (or direction) of a light to the given xyz.
     */
    public static void setLightPosition(int GLLightHandle, float[] position) {
        GL11.glLight(GLLightHandle, GL11.GL_POSITION, allocFloats(position));
    }

    /**
     * enable/disable the given light.  The light handle parameter is one of
     * the predefined OpenGL light handle numbers (GL_LIGHT1, GL_LIGHT2 ... GL_LIGHT7).
     */
    public static void setLight(int GLLightHandle, boolean on) {
        if (on) {
            GL11.glEnable(GLLightHandle);
        } else {
            GL11.glDisable(GLLightHandle);
        }
    }

    /**
     * Enable/disable lighting.  If parameter value is false, this will turn off all
     * lights and ambient lighting.
     *
     * NOTE: When lighting is disabled, material colors are disabled as well.  Use
     *       glColor() to set color properties when ligthing is off.
     */
    public static void setLighting(boolean on) {
        if (on) {
            GL11.glEnable(GL11.GL_LIGHTING);
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
        }
    }

    /**
     * Sets a java.awt.Color
     * @param c The color to set.
     */
    public static void setColor(Color c) {
        setColor(new float[]{c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()});
    }

    /**
     * Set the current color with RGBA floats in range 0-1.  The current color
     * is disabled when lighting is enabled.  When lighting is enabled (glEnable(GL_LIGHTING))
     * then material colors are in effect and the current color is ignored.
     */
    public static void setColor(float R, float G, float B, float A) {
        GL11.glColor4f(R, G, B, A);
    }

    /**
     * Set the current color with RGBA bytes in range 0-255. The current color
     * is disabled when lighting is enabled.  When lighting is enabled (glEnable(GL_LIGHTING))
     * then material colors are in effect and the current color is ignored.
     */
    public static void setColorB(int R, int G, int B, int A) {
        GL11.glColor4ub((byte) R, (byte) G, (byte) B, (byte) A);
    }

    /**
     * Set the current color to the given RGB or RGBA float array.  Floats are
     * in range 0-1. The current color is disabled when lighting is enabled.
     * When lighting is enabled (glEnable(GL_LIGHTING)) then
     * material colors are in effect and the current color is ignored.
     */
    public static void setColor(float[] rgba) {
        if (rgba != null) {
            if (rgba.length == 4) {
                GL11.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
            } else if (rgba.length == 3) {
                GL11.glColor4f(rgba[0], rgba[1], rgba[2], 1);
            } else {
                System.err.println("Failed setting color, array of data doesn't have the correct length of 3 or 4.");
            }
        }
    }

    /**
     * Convert a color to floats
     * @param c the Color to convert
     * @return array of floats
     */
    @Deprecated
    public static float[] colorToFloat3(Color c) {
        return new float[]{c.getRed(), c.getGreen(), c.getBlue()};
    }

    /**
     * Convert a color to floats
     * @param c the Color to convert
     * @return array of floats
     */
    public static float[] colorToFloat4(Color c) {
        return new float[]{c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()};
    }

    /**
     * Enable/disable the color-material setting.  When enabled, the glColor() command
     * will change the current material color.  This provides a convenient and
     * efficient way to change material colors without having to call glMaterial().
     * When disabled, the glColor() command functions normally (has no affect on
     * material colors).
     *
     * @param on   when true, glColor() will set the current material color
     */
    public static void setColorMaterial(boolean on) {
        if (on) {
            // glColor() will change the diffuse and ambient material colors
            GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        } else {
            // glColor() behaves normally
            GL11.glDisable(GL11.GL_COLOR_MATERIAL);
        }
    }

    /**
     * For given screen xy and z depth, return the world xyz coords in a float array.
     */
    public static float[] getWorldCoordsAtScreen(int x, int y, float z) {
        float[] resultf = new float[3];
        unProject((float) x, (float) y, (float) z, resultf);
        return resultf;
    }

    /**
     * Return the Z depth of the single pixel at the given screen position.
     */
    public static float getZDepth(int x, int y) {
        ByteBuffer tmpFloat = allocBytes(SIZE_FLOAT);
        tmpFloat.clear();
        GL11.glReadPixels(x, y, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, tmpFloat);
        return ((float) (tmpFloat.getFloat(0)));
    }

    /**
     * Find the Z depth of the origin in the projected world view. Used by getWorldCoordsAtScreen()
     * Projection matrix  must be active for this to return correct results (GL.glMatrixMode(GL.GL_PROJECTION)).
     * For some reason I have to chop this to four decimals or I get bizarre
     * results when I use the value in project().
     */
    public static float getZDepthAtOrigin() {
        float[] resultf = new float[3];
        project(0, 0, 0, resultf);
        return ((int) (resultf[2] * 10000F)) / 10000f;  // truncate to 4 decimals
    }

    /**
     * Return screen coordinates for a given point in world space.  The world
     * point xyz is 'projected' into screen coordinates using the current model
     * and projection matrices, and the current viewport settings.
     *
     * @param x         world coordinates
     * @param y
     * @param z
     * @param resultf    the screen coordinate as an array of 3 floats
     */
    public static void project(float x, float y, float z, float[] resultf) {
        FloatBuffer tmpResult = allocFloats(16);
        // lwjgl 2.0 altered params for GLU funcs
        GLU.gluProject(x, y, z, getModelviewMatrix(), getProjectionMatrix(), getViewport(), tmpResult);
        resultf[0] = tmpResult.get(0);
        resultf[1] = tmpResult.get(1);
        resultf[2] = tmpResult.get(2);
    }

    /**
     * Return world coordinates for a given point on the screen.  The screen
     * point xyz is 'un-projected' back into world coordinates using the
     * current model and projection matrices, and the current viewport settings.
     *
     * @param x         screen x position
     * @param y         screen y position
     * @param z         z-buffer depth position
     * @param resultf   the world coordinate as an array of 3 floats
     * @see             getWorldCoordsAtScreen()
     */
    public static void unProject(float x, float y, float z, float[] resultf) {
        FloatBuffer tmpResult = allocFloats(16);
        GLU.gluUnProject(x, y, z, getModelviewMatrix(), getProjectionMatrix(), getViewport(), tmpResult);
        resultf[0] = tmpResult.get(0);
        resultf[1] = tmpResult.get(1);
        resultf[2] = tmpResult.get(2);
    }

    /**
     * For given screen xy, return the world xyz coords in a float array.  Assume
     * Z position is 0.
     */
    public static float[] getWorldCoordsAtScreen(int x, int y) {
        float z = getZDepthAtOrigin();
        float[] resultf = new float[3];
        unProject((float) x, (float) y, (float) z, resultf);
        return resultf;
    }

    /**
     * Buggy method not working...
     * 
     * retrieve a setting from OpenGL (calls glGetInteger())
     * @param whichSetting  the id number of the value to be returned (same constants as for glGetInteger())
     */
    @Deprecated
    public static int getSettingInt(int whichSetting) {
        IntBuffer tmpInts = allocInts(16);
        tmpInts.clear();
        GL11.glGetInteger(whichSetting, tmpInts);
        return tmpInts.get(0);
    }

    public static FloatBuffer getModelviewMatrix() {
        FloatBuffer bufferModelviewMatrix = allocFloats(16);
        bufferModelviewMatrix.clear();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, bufferModelviewMatrix);
        return bufferModelviewMatrix;
    }

    public static FloatBuffer getProjectionMatrix() {
        FloatBuffer bufferProjectionMatrix = allocFloats(16);
        bufferProjectionMatrix.clear();
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, bufferProjectionMatrix);
        return bufferProjectionMatrix;
    }

    public static IntBuffer getViewport() {
        IntBuffer bufferViewport = allocInts(16);
        bufferViewport.clear();
        GL11.glGetInteger(GL11.GL_VIEWPORT, bufferViewport);
        return bufferViewport;
    }

    /**
     * Convert a FloatBuffer matrix to a 4x4 float array.
     * @param fb   FloatBuffer containing 16 values of 4x4 matrix
     * @return     2 dimensional float array
     */
    public static float[][] getMatrixAsArray(FloatBuffer fb) {
        float[][] fa = new float[4][4];
        fa[0][0] = fb.get();
        fa[0][1] = fb.get();
        fa[0][2] = fb.get();
        fa[0][3] = fb.get();
        fa[1][0] = fb.get();
        fa[1][1] = fb.get();
        fa[1][2] = fb.get();
        fa[1][3] = fb.get();
        fa[2][0] = fb.get();
        fa[2][1] = fb.get();
        fa[2][2] = fb.get();
        fa[2][3] = fb.get();
        fa[3][0] = fb.get();
        fa[3][1] = fb.get();
        fa[3][2] = fb.get();
        fa[3][3] = fb.get();
        return fa;
    }
    public static ArrayList displayLists = new ArrayList();  // will hold display list IDs created by beginDisplayList()

    /**
     * Begin a display list. All following OpenGL geometry commands (up to endDisplayList())
     * will be stored in a display list, not drawn to screen.
     * <P>
     * To use, create a display list in setup():
     * <PRE>
     *      int teapotID = beginDisplayList();
     *      ... // run teapot render code here
     *      endDisplayList();
     * </PRE>
     *
     * Then call the display list later in render():
     * <PRE>
     *      callDisplayList(teapotID);
     * </PRE>
     *
     * @return integer display list id
     * @see endDisplayList(), callDisplayList(), destroyDisplayList()
     */
    public static int beginDisplayList() {
        int DL_ID = GL11.glGenLists(1);         // Allocate 1 new Display List
        GL11.glNewList(DL_ID, GL11.GL_COMPILE); // Start Building A List
        displayLists.add(new Integer(DL_ID)); // save the list ID so we can delete it later (see destroyDisplayLists())
        return DL_ID;
    }

    /**
     * Finish display list creation.  Use this function only after calling
     * beginDisplayList()
     *
     * @see beginDisplayList()
     */
    public static void endDisplayList() {
        GL11.glEndList();
    }

    /**
     * Render the geometry stored in a display list.  Use this function after
     * calling beginDisplayList() and endDisplayList() to create a display list.
     *
     * @see beginDisplayList()
     * @see endDisplayList()
     */
    public static void callDisplayList(int displayListID) {
        GL11.glCallList(displayListID);
    }

    /**
     * Delete the given display list ID.  Frees up resources on the graphics card.
     */
    public static void destroyDisplayList(int DL_ID) {
        GL11.glDeleteLists(DL_ID, 1);
    }

    /**
     * Clean up the allocated display lists.  Called by cleanUp() when app exits.
     *
     * @see cleanUp();
     */
    public static void destroyDisplayLists() {
        while (displayLists.size() > 0) {
            int displaylistID = ((Integer) displayLists.get(0)).intValue();
            GL11.glDeleteLists(displaylistID, 1);
            displayLists.remove(0);
        }
    }
    public static FloatBuffer mtldiffuse = allocFloats(4);     // color of the lit surface
    public static FloatBuffer mtlambient = allocFloats(4);     // color of the shadowed surface
    public static FloatBuffer mtlspecular = allocFloats(4);    // reflection color (typically this is a shade of gray)
    public static FloatBuffer mtlemissive = allocFloats(4);    // glow color
    public static FloatBuffer mtlshininess = allocFloats(4);   // size of the reflection highlight
    public static final float[] colorClear = {0f, 0f, 0f, 0f};
    public static final float[] colorBlack = {0f, 0f, 0f, 1f};
    public static final float[] colorWhite = {1f, 1f, 1f, 1f};
    public static final float[] colorGray = {.5f, .5f, .5f, 1f};
    public static final float[] colorRed = {1f, 0f, 0f, 1f};
    public static final float[] colorGreen = {0f, 1f, 0f, 1f};
    public static final float[] colorBlue = {0f, 0f, 1f, 1f};

    /**
     *  A simple way to set the current material properties to approximate a
     *  "real" surface.  Provide the surface color (float[4]]) and shininess
     *  value (range 0-1).
     *  <P>
     *  Sets diffuse material color to the surfaceColor and ambient material color
     *  to surfaceColor/2.  Based on the shiny value (0-1), sets the specular
     *  property to a color between black (0) and white (1), and sets the
     *  shininess property to a value between 0 and 127.
     *  <P>
     *  Lighting must be enabled for material colors to take effect.
     *  <P>
     *  @param surfaceColor - must be float[4] {R,G,B,A}
     *  @param reflection - a float from 0-1 (0=very matte, 1=very shiny)
     */
    public static void setMaterial(float[] surfaceColor, float reflection) {
        /*mtldiffuse = allocFloats(4);
        mtlambient = allocFloats(4);
        mtlspecular = allocFloats(4);
        mtlemissive = allocFloats(4);
        mtlshininess = allocFloats(4);*/
        float[] reflect = {reflection, reflection, reflection, 1}; // make a shade of gray
        float[] ambient = {surfaceColor[0] * .5f, surfaceColor[1] * .5f, surfaceColor[2] * .5f, 1};  // darker surface color
        mtldiffuse.put(surfaceColor).flip();     // surface directly lit
        mtlambient.put(ambient).flip();          // surface in shadow
        mtlspecular.put(reflect).flip();         // reflected light
        mtlemissive.put(colorBlack).flip();      // no emissive light
        // size of reflection
        int openglShininess = ((int) (reflection * 127f));   // convert 0-1 to 0-127
        if (openglShininess >= 0 && openglShininess <= 127) {
            mtlshininess.put(new float[]{openglShininess, 0, 0, 0}).flip();
        }
        applyMaterial();
    }

    /**
     *  Set the four material colors and calls glMaterial() to change the current
     *  material color in OpenGL.  Lighting must be enabled for material colors to take effect.
     *
     *  @param shininess: size of reflection (0 is matte, 127 is pinpoint reflection)
     */
    public static void setMaterial(float[] diffuseColor, float[] ambientColor, float[] specularColor, float[] emissiveColor, float shininess) {
        /*mtldiffuse = allocFloats(4);
        mtlambient = allocFloats(4);
        mtlspecular = allocFloats(4);
        mtlemissive = allocFloats(4);
        mtlshininess = allocFloats(4);*/
        mtldiffuse.put(diffuseColor).flip();     // surface directly lit
        mtlambient.put(ambientColor).flip();     // surface in shadow
        mtlspecular.put(specularColor).flip();   // reflection color
        mtlemissive.put(emissiveColor).flip();   // glow color
        if (shininess >= 0 && shininess <= 127) {
            mtlshininess.put(new float[]{shininess, 0, 0, 0}).flip();  // size of reflection 0=broad 127=pinpoint
        }
        applyMaterial();
    }

    /**
     * Alter the material opacity by setting the diffuse material color
     * alpha value to the given value
     * @para alpha 0=transparent 1=opaque
     */
    public static void setMaterialAlpha(float alpha) {
        if (alpha < 0) {
            alpha = 0;
        }
        if (alpha > 1) {
            alpha = 1;
        }
        mtldiffuse.put(3, alpha).flip();     // alpha value of diffuse color
        applyMaterial();
    }

    /**
     *  Call glMaterial() to activate these material properties in the OpenGL environment.
     *  These properties will stay in effect until you change them or disable lighting.
     */
    public static void applyMaterial() {
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, mtldiffuse);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, mtlambient);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, mtlspecular);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_EMISSION, mtlemissive);
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SHININESS, mtlshininess);
    }

    /**
     *  Return a String array containing the path portion of a filename (result[0]),
     *  and the fileame (result[1]).  If there is no path, then result[0] will be ""
     *  and result[1] will be the full filename.
     */
    public static String[] getPathAndFile(String filename) {
        String[] pathAndFile = new String[2];
        Matcher matcher = Pattern.compile("^.*/").matcher(filename);
        if (matcher.find()) {
            pathAndFile[0] = matcher.group();
            pathAndFile[1] = filename.substring(matcher.end());
        } else {
            pathAndFile[0] = "";
            pathAndFile[1] = filename;
        }
        return pathAndFile;
    }
}
