package sft.sftengine.graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import sft.sftengine.util.InitLibraries;

/**
 *
 * @author jj
 */
public class SFTEngineWindow {

    Renderer r;
    int heigth, width;
    int heigthFull, widthFull;
    DisplayMode[] modes;
    boolean fullscreen;
    /** time at last frame */
    long lastFrame;
    /** frames per second */
    int fps;
    /** last fps time */
    long lastFPS;

    public SFTEngineWindow(Renderer r, String title) throws LWJGLException {
        this(r, 0, 0, true, title);
    }

    public SFTEngineWindow(Renderer r, int width, int heigth, String title) throws LWJGLException {
        this(r, width, heigth, false, title);
    }

    private SFTEngineWindow(Renderer renderer, int width, int heigth, boolean fullscreen, String title) throws LWJGLException {
        InitLibraries.addlwjgl();
        this.heigth = heigth;
        this.width = width;
        this.heigthFull = Display.getDesktopDisplayMode().getHeight();
        this.widthFull = Display.getDesktopDisplayMode().getWidth();
        this.fullscreen = fullscreen;
        r = renderer;

        modes = Display.getAvailableDisplayModes();

        System.out.println("List of available display modes:");
        for (int i = 0; i < modes.length; i++) {
            DisplayMode current = modes[i];
            System.out.println(current.getWidth() + "x" + current.getHeight() + "x"
                    + current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
        }
        setDisplayMode(width, heigth, fullscreen);
    }

    /**
     * Creates the SFT-Engine window
     */
    public void create() {
        try {
            Display.create();
            Mouse.create();
            Keyboard.create();
        } catch (LWJGLException e) {
            e.printStackTrace(System.err);
            System.exit(0);
        }
    }
    
    public void enableVSync() {
        setVSync(true);
    }
    
    public void disableVSync() {
        setVSync(false);
    }
    
    public void setVSync(boolean enable) {
        Display.setVSyncEnabled(enable);
    }

    /**
     * Tests the window state.
     * @return is the window in fullscreen mode?
     */
    public boolean isInFullscreen() {
        return fullscreen;
    }

    /**
     * Tests the window state.
     * @return is the window in windowed mode (not in fullscreen)
     */
    public boolean isWindowed() {
        return !fullscreen;
    }

    /**
     * Move the window to the specified position
     * @param posx x position to move the window to
     * @param posy 
     */
    public void moveWindow(int posx, int posy) {
        if (isWindowed()) {
            Display.setLocation(posx, posy);
        }
    }

    /**
     * Centers the window.
     */
    public void centerWindow() {
        moveWindow(-1, -1);
    }

    /** 
     * Calculate how many milliseconds have passed 
     * since last frame.
     * 
     * @return milliseconds passed since last frame 
     */
    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;

        return delta;
    }

    /**
     * Get the accurate system time
     * 
     * @return The system time in milliseconds
     */
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    /**
     * Get the accurate system time
     * 
     * @return The system time in nanoseconds
     */
    public long getNanoTime() {
        return System.nanoTime();
    }
    long aktuelleFPS;

    /**
     * Calculate the FPS and set it in the title bar
     */
    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            aktuelleFPS = fps;
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    
    public void setFullscreen(boolean enable) {
        if(enable) {
            setDisplayMode(0, 0, true);
        }
        else {
            setDisplayMode(width, heigth, false);
        }
    }

    /**
     * Set the display mode to be used 
     * 
     * @param width The width of the display required
     * @param height The height of the display required
     * @param fullscreen True if we want fullscreen mode
     */
    public final void setDisplayMode(int width, int height, boolean fullscreen) {

        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width)
                && (Display.getDisplayMode().getHeight() == height)
                && (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {

                int freq = 0;

                for (int i = 0; i < modes.length; i++) {
                    DisplayMode current = modes[i];

                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }

                        // if we've found a match for bpp and frequence against the 
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
                                && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
                
                // dirty stuff nah :D
                System.out.println("Couldn't find current mode, but i'll just try to set it.");
                targetDisplayMode = Display.getDesktopDisplayMode();
                
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }
            
            this.fullscreen = fullscreen;

            if(fullscreen) {
                this.widthFull = targetDisplayMode.getWidth();
                this.heigthFull = targetDisplayMode.getHeight();
            } else {
                this.width = targetDisplayMode.getWidth();
                this.heigth = targetDisplayMode.getHeight();
            }
            

            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    public void start() {

        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime();

        r.init();

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            // Rendering loop


            int delta = getDelta();

            r.update(delta);
            r.render();

            updateFPS();

            Display.update();
            Display.sync(60); // cap fps to 60fps

        }

        destroy();

    }

    public void destroy() {
        Display.destroy();
        Mouse.destroy();
        Keyboard.destroy();
    }
}
