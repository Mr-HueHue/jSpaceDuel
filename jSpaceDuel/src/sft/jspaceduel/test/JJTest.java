package sft.jspaceduel.test;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sft.sftengine.graphics.Renderer;
import sft.sftengine.graphics.SFTEngineWindow;

/**
 *
 * @author jj
 */
public class JJTest implements Renderer {

    boolean vsync = false;
    boolean fullscreen;
    SFTEngineWindow w;

    public static void main(String[] args) throws LWJGLException {
        new JJTest();
    }

    public JJTest() throws LWJGLException {
        w = new SFTEngineWindow(this, 500, 500, "SFT-Engine-Demo");
        w.create();
        w.start();
    }
    /** position of quad */
    float x = 400, y = 300;
    /** angle of quad rotation */
    float rotation = 0;

    @Override
    public void render() {
        // Clear The Screen And The Depth Buffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // R,G,B,A Set The Color To Blue One Time Only
        GL11.glColor3f(0.5f, 0.5f, 1.0f);

        // draw quad
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glRotatef(rotation, 0f, 0f, 1f);
        GL11.glTranslatef(-x, -y, 0);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x - 50, y - 50);
        GL11.glVertex2f(x + 50, y - 50);
        GL11.glVertex2f(x + 50, y + 50);
        GL11.glVertex2f(x - 50, y + 50);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    @Override
    public void init() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    @Override
    public void update(long delta) {
        // rotate quad
        rotation += 0.15f * delta;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            x -= 0.35f * delta;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            x += 0.35f * delta;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            y -= 0.35f * delta;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            y += 0.35f * delta;
        }

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_F) {
                    fullscreen = !fullscreen;
                    w.setFullscreen(fullscreen);
                } else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
                    vsync = !vsync;
                    w.setVSync(vsync);
                }
            }
        }

        // keep quad on the screen
        if (x < 0) {
            x = 0;
        }
        if (x > 800) {
            x = 800;
        }
        if (y < 0) {
            y = 0;
        }
        if (y > 600) {
            y = 600;
        }
    }
}
