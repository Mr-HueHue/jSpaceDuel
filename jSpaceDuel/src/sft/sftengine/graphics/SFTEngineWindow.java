package sft.sftengine.graphics;

import java.awt.Canvas;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import sft.sftengine.util.SFT_Libraries;

/**
 *
 * @author jj
 */
public class SFTEngineWindow extends Thread {

    Renderer r;
    JFrame f;
    AWTGLCanvas c;
    int heightWindowed, widthWindowed;
    int heightFull, widthFull;
    int height, width;
    DisplayMode[] modes;
    boolean fullscreen;
    /** time at last frame */
    long lastFrame;
    /** frames per second */
    int fps;
    /** last fps time */
    long lastFPS;
    long aktuelleFPS;
    private boolean renderenabled;
    private boolean vsync;
    private int vsynchrate;
    mode m;
    /**
     * If the resulution was changed once, set true
     */
    boolean changedresolution = false;

    public enum mode {

        JFRAME, NATIVEDISPLAY;
    }

    public SFTEngineWindow(Renderer r, String title) throws LWJGLException {
        this(mode.NATIVEDISPLAY, r, 0, 0, true, title);
    }

    public SFTEngineWindow(Renderer r, int width, int heigth, String title) throws LWJGLException {
        this(mode.NATIVEDISPLAY, r, width, heigth, false, title);
    }

    public SFTEngineWindow(mode m, Renderer r, int width, int heigth, String title) throws LWJGLException {
        this(m, r, width, heigth, false, title);
    }

    private SFTEngineWindow(mode m, Renderer renderer, int width, int heigth, boolean fullscreen, String title) throws LWJGLException {
        SFT_Libraries.addlwjgl();
        this.height = heigth;
        this.width = width;
        this.fullscreen = fullscreen;
        this.m = m;
        if (m == mode.JFRAME) {
            c = new AWTGLCanvas();
            f = new JFrame(title);
            f.setSize(width, height);
            f.setResizable(false);
            f.add(c);
            f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            f.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent ev) {
                    renderenabled = false;
                }
            });
            
            if (LWJGLUtil.getPlatform() == LWJGLUtil.PLATFORM_WINDOWS) {
                f.addWindowFocusListener(new WindowAdapter() {

                    @Override
                    public void windowGainedFocus(WindowEvent e) {
                        c.requestFocusInWindow();
                    }
                });
            }
            
            ComponentAdapter adapter = new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    int wid = c.getWidth(), heg = c.getHeight();
                    //r.changeResolution(wid, heg);
                }
            };

            c.addComponentListener(adapter);



        } else if (m == mode.NATIVEDISPLAY) {
            Display.setTitle(title);
        }

        renderenabled = true;
        vsync = false;
        vsynchrate = Display.getDisplayMode().getFrequency();
        r = renderer;

        System.out.println("Current desktop mode:");
        System.out.println(Display.getDesktopDisplayMode().getWidth() + "x" + Display.getDesktopDisplayMode().getHeight() + "x" + Display.getDesktopDisplayMode().getBitsPerPixel() + " " + Display.getDesktopDisplayMode().getFrequency() + "Hz");
        setDisplayMode(width, heigth, fullscreen);
    }

    public Canvas getCanvas() {
        return c;
    }

    /**
     * Lists all available display modes.
     */
    public DisplayMode[] listModes() {
        try {
            return Display.getAvailableDisplayModes();
        } catch (LWJGLException ex) {
            System.err.println("Failed getting display modes.");
            return null;
        }
    }

    /**
     * Sets a new text to the title line of the window.
     * @param title the text to be set
     */
    public void setTitle(String title) {
        if (m == m.NATIVEDISPLAY) {
            Display.setTitle(title);
        } else {
            f.setTitle(title);
        }
    }

    /**
     * Creates the SFT-Engine window
     */
    public void create() {
        try {
            System.out.println("SFT-Engine initialising...");
            if (m == mode.JFRAME) {
                f.setVisible(true);
                Display.setParent(c);
            }
            Display.create();
            Mouse.create();
            Keyboard.create();
        } catch (LWJGLException e) {
            e.printStackTrace(System.err);
            System.exit(0);
        }
    }

    /**
     * Enables vsynching of the rendered content.
     */
    public void enableVSync() {
        vsync = true;
        setVSync(true);
    }

    /**
     * Disables vsynching of the rendered content.
     */
    public void disableVSync() {
        vsync = false;
        setVSync(false);
    }

    /**
     * Sets the window vsynching process.
     * @param enable enable vsynching
     */
    public void setVSync(boolean enable) {
        if (vsync != enable) {
            vsync = enable;
            if (enable) {
                System.out.println("Enabling vsynching...");
            } else {
                System.out.println("Disabling vsynching...");
            }
            if (m == mode.NATIVEDISPLAY) {
                Display.setVSyncEnabled(enable);
            } else {
                c.setVSyncEnabled(enable);
            }
        }
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
        if (m == mode.NATIVEDISPLAY) {
            if (isWindowed()) {
                Display.setLocation(posx, posy);
            }
        } else if (m == mode.JFRAME) {
            if (isWindowed()) {
                if (posx == -1 && posy == -1) {
                    f.setLocationRelativeTo(null);
                } else {
                    f.setLocation(posx, posy);
                }
            }
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

    /**
     * Sets fullscreen mode
     * @param enable enable fullscreen
     */
    public void setFullscreen(boolean enable) {
        if (enable) {
            setDisplayMode(0, 0, true);
        } else {
            setDisplayMode(widthWindowed, heightWindowed, false);
        }
    }

    /**
     * Information about the current mode
     * @return the currently activated mode
     */
    public DisplayMode getCurrentMode() {
        return Display.getDisplayMode();
    }

    public final void setDisplayMode(int width, int height, boolean fullscreen) {
        if (m == mode.NATIVEDISPLAY) {
            setDisplayModeNative(width, height, fullscreen);
        } else if (m == mode.JFRAME) {
            setDisplayModeJFrame(width, height, fullscreen);
        }
    }

    public final void setDisplayModeJFrame(int width, int height, boolean fullscreen) {
        if (fullscreen) {
        } else {
            f.setSize(width, height);
            c.setBounds(0, 0, width, height);
        }
    }

    /**
     * Set the display mode to be used 
     * 
     * @param width The width of the display required
     * @param height The height of the display required
     * @param fullscreen True if we want fullscreen mode
     */
    public final void setDisplayModeNative(int width, int height, boolean fullscreen) {

        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width)
                && (Display.getDisplayMode().getHeight() == height)
                && (Display.isFullscreen() == fullscreen)) {
            return;
        }

        try {
            DisplayMode targetDisplayMode = null;

            if (fullscreen) {
                targetDisplayMode = Display.getDesktopDisplayMode();
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }

            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fullscreen=" + fullscreen);
                return;
            }

            this.fullscreen = fullscreen;

            if (fullscreen) {
                this.widthFull = targetDisplayMode.getWidth();
                this.heightFull = targetDisplayMode.getHeight();
            } else {
                this.widthWindowed = width;
                this.heightWindowed = height;
            }

            this.width = targetDisplayMode.getWidth();
            this.height = targetDisplayMode.getHeight();

            //Display.setFullscreen(fullscreen);

            if (fullscreen) {
                System.out.println("Setting fullscreen display mode now...");
                Display.setDisplayModeAndFullscreen(targetDisplayMode);
            } else {
                System.out.println("Setting windowed display mode now...");
                Display.setDisplayMode(targetDisplayMode);
            }
            if (changedresolution) {
                r.changeResolution(this.width, this.height);
            } else {
                changedresolution = true;
            }

        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    /**
     * Main method which starts rendering.
     */
    @Override
    public void run() {
        create();
        getDelta(); // call once before loop to initialise lastFrame
        lastFPS = getTime();

        r.init();
        
        while (renderenabled && !Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            // Rendering loop



            r.update(getDelta());
            r.render();

            updateFPS();
            if (m == mode.NATIVEDISPLAY) {
                Display.update();

                if (vsync) {
                    Display.sync(vsynchrate); // cap fps to 60fps
                }
            } else if (m == mode.JFRAME) {
            }

            try {
                if (!Display.isActive()) {
                    synchronized (this) {
                        if (!Display.isVisible()) {
                            wait(1000); // FPS iw window is hidden/minimized etc
                        } else {
                            wait(50); // FPS if window is covered
                        }
                    }
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace(System.err);
            }

        }

        destroyEntities();
        cleanup();
        System.out.println("SFT-Engine shut down.");
        System.exit(0);
    }

    private void destroyEntities() {
        Display.destroy();
        Mouse.destroy();
        Keyboard.destroy();
        renderenabled = false;
        System.out.println("Destroyed all LWJGL Entities.");
    }

    /**
     * Kills the renderer after letting him finish the last frame.
     */
    public void terminate() {
        renderenabled = false;
    }

    /**
     * Kills the renderer instantly.
     */
    public void terminateHard() {
        destroyEntities();
    }

    private void cleanup() {
        // cleanup stuff
    }

    /**
     * grabs the mouse to the window
     */
    public void grabMouse() {
        setMouseGrabbed(true);
    }

    /**
     * releases a grabbed mouse.
     */
    public void releaseMouse() {
        setMouseGrabbed(false);
    }

    /**
     * Sets the mouse to be grabbed (hidden and caught in the window)
     * @param grab 
     */
    public void setMouseGrabbed(boolean grab) {
        if (grab != Mouse.isGrabbed()) {
            Mouse.setGrabbed(grab);
        }
    }

    /**
     * Test the mouse grabbing status
     * @return is the mouse grabbed
     */
    public boolean isMouseGrabbed() {
        return Mouse.isGrabbed();
    }

    /**
     * Sets the mouse at this window position
     * @param screenX
     * @param screenY 
     */
    public static void setCursorPosition(int screenX, int screenY) {
        Mouse.setCursorPosition(screenX, screenY);
    }
}
