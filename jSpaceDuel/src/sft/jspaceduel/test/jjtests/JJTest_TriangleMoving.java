package sft.jspaceduel.test.jjtests;

import java.awt.Font;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import sft.sftengine.graphics.Renderer;
import sft.sftengine.graphics.SFTEngineWindow;
import sft.sftengine.graphics.SFT_Font;
import sft.sftengine.util.SFT_Util;
import static org.lwjgl.opengl.GL11.*;

/**
 * Tests of moving triangles.
 * @author JJ
 */
public final class JJTest_TriangleMoving implements Renderer {

    private int wi = 600, he = 600;
    float rotation = 0;
    float x = 200, y = 200;
    float mx = x, my = y;
    float mdx = mx, mdy = my;
    boolean fullscreen, vsync;
    SFTEngineWindow w;

    public static void main(String[] args) throws LWJGLException {
        new JJTest_TriangleMoving();
    }
    private SFT_Font sftf;

    public JJTest_TriangleMoving() throws LWJGLException {
        w = new SFTEngineWindow(this, wi, he, "JJTest_TriangleMoving");
        w.create();
        w.start();
    }

    @Override
    public void render() {


        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        glBegin(GL_TRIANGLES);
        glColor3f(1, 0, 0);
        glVertex3f(-1, -1, 0);
        glColor3f(0, 0, 1);
        glVertex3f(1, -1, 0);
        glColor3f(0, 1, 0);
        glVertex3f(0, 1, 0);
        glEnd();

        SFT_Util.print2DText(0, 0, "/_ 0,0", sftf);
    }

    @Override
    public void init() {


        sftf = new SFT_Font(new Font("Times", Font.PLAIN, 15), new float[]{1, 1, 1, 1}, new float[]{0, 0, 0, 0});

        w.setVSync(true);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        //glEnable(GL_BLEND);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        GLU.gluOrtho2D(0, 0, y, x);

    }

    @Override
    public void update(long milliSecondsSinceLastFrame) {
        rotation += 0.050f * milliSecondsSinceLastFrame;

        mx = Mouse.getX();
        my = Mouse.getY();

        mdx = Mouse.getDX();
        mdy = Mouse.getDY();

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            x -= 0.35f * milliSecondsSinceLastFrame;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            x += 0.35f * milliSecondsSinceLastFrame;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            y -= 0.35f * milliSecondsSinceLastFrame;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            y += 0.35f * milliSecondsSinceLastFrame;
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
    }

    @Override
    public void changeResolution(int width, int height) {
        wi = width;
        he = height;

        SFT_Util.viewportReshape(width, height);
    }
}
