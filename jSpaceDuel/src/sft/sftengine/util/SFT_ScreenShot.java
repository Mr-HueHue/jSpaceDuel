package sft.sftengine.util;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.Pbuffer;
import sft.sftengine.graphics.SFT_Image;

/**
 *
 * @author JJ
 */
public class SFT_ScreenShot {
    
    public SFT_ScreenShot(String filename) {
        screenShot(filename);
    }
    
    /**
     * Save the current frame buffer to a PNG image. Same as
     * screenShot(filename) but the screenshot filename will be automatically
     * set to <applicationClassName>-<timestamp>.png
     */
    public static void screenShot() {
    	screenShot(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), SFT_Util.makeTimestamp() + ".png");
    }

    /**
     * Save the current frame buffer to a PNG image. Can also
     * be used with the PBuffer class to copy large images or textures that
     * have been rendered into the offscreen pbuffer.
     */
    public static void screenShot(String imageFilename) {
    	screenShot(0, 0, Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight(), imageFilename);
    }

    /**
     * Save the current Pbuffer to a PNG image. Same as screenShot(filename)
     * but the Pbuffer will be saved instead of the framebuffer, and the
     * screenshot filename will be set to <applicationClassName>-<timestamp>.png
     * NOTE: Have to call selectPbuffer() before calling this function.
     */
    public static void screenShot(Pbuffer pb) {
    	screenShot(0, 0, pb.getWidth(), pb.getHeight(), SFT_Util.makeTimestamp() + ".png");
    }

    /**
     * Save a region of the current render buffer to a PNG image.  If the current
     * buffer is the framebuffer then this will work as a screen capture.  Can
     * also be used with the PBuffer class to copy large images or textures that
     * have been rendered into the offscreen pbuffer.
     * <P>
     * WARNING: this function hogs memory!  Call java with more memory
     * (java -Xms128m -Xmx128m)
     * <P>
     * @see   selectPbuffer(Pbuffer)
     * @see   selectDisplay()
     * @see   savePixelsToPNG()
     */
    public static void screenShot(int x, int y, int width, int height, String imageFilename) {
    	// allocate space for ARBG pixels
    	ByteBuffer framebytes = SFT_Util.allocBytes(width * height * SFT_Util.SIZE_INT);
    	int[] pixels = new int[width * height];
    	// grab the current frame contents as ARGB ints (BGRA ints reversed)
    	GL11.glReadPixels(x, y, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, framebytes);
    	// copy ARGB data from ByteBuffer to integer array
    	framebytes.asIntBuffer().get(pixels, 0, pixels.length);
    	// free up this memory
    	framebytes = null;
    	// flip the pixels vertically and save to file
    	SFT_Image.savePixelsToPNG(pixels, width, height, imageFilename, true);
    }

	/**
	 * Save a ByteBuffer of ARGB pixels to a PNG file.
	 * If flipY is true, flip the pixels on the Y axis before saving.
	 */
	public static void savePixelsToPNG(ByteBuffer framebytes, int width, int height, String imageFilename, boolean flipY) {
		if (framebytes != null && imageFilename != null) {
			// copy ARGB data from ByteBuffer to integer array
			int[] pixels = new int[width * height];
			framebytes.asIntBuffer().get(pixels, 0, pixels.length);
			// save pixels to file
			SFT_Image.savePixelsToPNG(pixels, width, height, imageFilename, flipY);
		}
	}

	/**
	 * Save the contents of the current render buffer to a PNG image. This is
	 * an older version of screenShot() that used the default OpenGL GL_RGBA
	 * pixel format which had to be swizzled into an ARGB format. I'm
	 * keeping the function here for reference.
	 * <P>
	 * If the current buffer is the framebuffer then this will work as a screen capture.
	 * Can also be used with the PBuffer class to copy large images or textures that
	 * have been rendered into the offscreen pbuffer.
	 * <P>
	 * WARNING: this function hogs memory!  Call java with more memory
	 * (java -Xms128m -Xmx128)
	 * <P>
	 * @see   selectPbuffer(), selectDisplay()
	 */
	public static void screenShotRGB(int width, int height, String saveFilename) {
		// allocate space for RBG pixels
		ByteBuffer framebytes = SFT_Util.allocBytes(width * height * 3);
		int[] pixels = new int[width * height];
		int bindex;
		// grab a copy of the current frame contents as RGB (has to be UNSIGNED_BYTE or colors come out too dark)
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, framebytes);
		// copy RGB data from ByteBuffer to integer array
		for (int i = 0; i < pixels.length; i++) {
			bindex = i * 3;
			pixels[i] =
				0xFF000000                                          // A
				| ((framebytes.get(bindex) & 0x000000FF) << 16)     // R
				| ((framebytes.get(bindex+1) & 0x000000FF) << 8)    // G
				| ((framebytes.get(bindex+2) & 0x000000FF) << 0);   // B
		}
		// free up some memory
		framebytes = null;
		// save to file (flip Y axis before saving)
		SFT_Image.savePixelsToPNG(pixels, width, height, saveFilename, true);
	}
}
