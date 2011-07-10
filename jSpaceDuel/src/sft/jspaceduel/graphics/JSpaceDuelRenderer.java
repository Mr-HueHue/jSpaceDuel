package sft.jspaceduel.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.opengl.GL11.*;
import sft.jspaceduel.game.JSpaceDuelManager;
import sft.sftengine.graphics.Renderer;
import sft.sftengine.graphics.SFT_Font;
import sft.sftengine.util.SFT_DrawTemplates;
import sft.sftengine.util.SFT_Util;

/**
 * This class does all the rendering work.
 * 
 * @author JJ
 */
public class JSpaceDuelRenderer implements Renderer {

    JSpaceDuelManager manager;
    private int wi, he;
    float rotation = 0;
    float x = 200, y = 200, x1 = 0, y1 = 0;
    float mx = x, my = y;
    float mdx = mx, mdy = my;
    boolean fullscreen, vsync;
    private SFT_Font sftf;
    float viewleft = -100, viewright = 100, viewbottom = -100, viewtop = 100;

    public JSpaceDuelRenderer(JSpaceDuelManager m) {
        manager = m;
        Dimension viewsize = m.getResolution();
        wi = viewsize.width;
        he = viewsize.height;
    }

    @Override
    public void render() {
        // Clear The Screen And The Depth Buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glBegin(GL_QUADS);
        {
            glVertex3d(10, 10, 0);
            glVertex3d(10, 90, 0);
            glVertex3d(90, 90, 0);
            glVertex3d(90, 10, 0);
        }
        glEnd();

        SFT_Util.print2DText(0, 0, "/_  Nullpunkt vonnerem Overlay", sftf);
    }

    @Override
    public void init() {

        sftf = new SFT_Font(new Font("Times", Font.PLAIN, 15), new float[]{1, 0.5f, 0.5f, 1}, new float[]{0, 0, 0, 0});

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glLoadIdentity();
        GLU.gluOrtho2D(viewleft, viewright, viewbottom, viewtop);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(0.375f, 0.375f, 0);
    }

    @Override
    public void update(long milliSecondsSinceLastFrame) {
        rotation += 0.050f * milliSecondsSinceLastFrame;

        mx = Mouse.getX();
        my = Mouse.getY();

        mdx = Mouse.getDX();
        mdy = Mouse.getDY();

        y1 += 0.0001f * mdy * milliSecondsSinceLastFrame;
        x1 += 0.0001f * mdx * milliSecondsSinceLastFrame;

        rotation += 0.050f * milliSecondsSinceLastFrame;

        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            viewtop -= 0.35f * milliSecondsSinceLastFrame;
            x -= 0.35f * milliSecondsSinceLastFrame;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            viewtop += 0.35f * milliSecondsSinceLastFrame;

            x += 0.35f * milliSecondsSinceLastFrame;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            y += 0.35f * milliSecondsSinceLastFrame;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            y -= 0.35f * milliSecondsSinceLastFrame;
        }

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_F) {
                    fullscreen = !fullscreen;
                    manager.window.setFullscreen(fullscreen);
                } else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
                    vsync = !vsync;
                    manager.window.setVSync(vsync);
                } else if (Keyboard.getEventKey() == Keyboard.KEY_Q) {
                    manager.window.destroy();
                }
            }
        }
    }

    @Override
    public void changeResolution(int width, int height) {
        wi = width;
        he = height;


        glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // reset the projection shit

        GLU.gluOrtho2D(viewleft, viewright, viewbottom, viewtop);
        glMatrixMode(GL_MODELVIEW);
        SFT_Util.viewportReshape(width, height);
        manager.changeResolution(width, height);
    }
}
