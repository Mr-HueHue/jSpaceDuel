package sft.jspaceduel.test;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;
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
        w = new SFTEngineWindow(this, 700, 500, "SFT-Engine-Demo");
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
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // R,G,B,A Set The Color To Blue One Time Only
        glColor3f(0.5f, 0.5f, 1.0f);

        // draw quad
        glPushMatrix();
        glTranslatef(x, y, 0);
        glRotatef(rotation, 0f, 0f, 1f);
        glTranslatef(-x, -y, 0);
        

        glBegin(GL_QUADS);
        glVertex2f(x - 50, y - 50);
        glVertex2f(x + 50, y - 50);
        glVertex2f(x + 50, y + 50);
        glVertex2f(x - 50, y + 50);
        glEnd();
        glPopMatrix();
    }

    @Override
    public void init() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 800, 600, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    @Override
    public void update(long delta) {
        // rotate quad
        rotation += 0.10f * delta;

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
                } else if (Keyboard.getEventKey() == Keyboard.KEY_Q) {
                    w.destroy();
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
