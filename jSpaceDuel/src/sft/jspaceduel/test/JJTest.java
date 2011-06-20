package sft.jspaceduel.test;

import java.awt.Color;
import java.awt.Font;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.opengl.GL11.*;
import sft.sftengine.graphics.Renderer;
import sft.sftengine.graphics.SFTEngineWindow;
import sft.sftengine.graphics.SFT_Font;
import sft.sftengine.util.SFT_DrawTemplates;
import sft.sftengine.util.SFT_Util;

/**
 *
 * @author jj
 */
public class JJTest implements Renderer {

    boolean vsync = false;
    boolean fullscreen;
    SFTEngineWindow w;
    int wi, he;
    SFT_Font sftf;

    public static void main(String[] args) throws LWJGLException {
        new JJTest();
    }

    public JJTest() throws LWJGLException {
        wi = 800;
        he = 800;
        w = new SFTEngineWindow(this, wi, he, "SFT-Engine-Demo");
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

        //SFT_Util.setLight(GL_LIGHT1, SFT_Util.colorToFloat4(Color.white), SFT_Util.colorToFloat4(Color.lightGray), SFT_Util.colorToFloat4(Color.white), new float[]{ 100,100,0,0 });


        glPushMatrix();
        glTranslatef(x - 10, y - 10, 0);
        glRotatef(rotation, 0f, 0f, 1f);
        glColor3d(0, 0.123, 0.9);
        glBegin(GL_QUADS);
        glVertex2f(- 50, - 50);
        glVertex2f(50, - 50);
        glVertex2f(50, 50);
        glVertex2f(- 50, 50);
        glEnd();
        glPopMatrix();


        glPushMatrix();
        glColor3f(0.9f, 0.9f, 0f);
        glTranslatef(x, y, 0);
        SFT_DrawTemplates.drawFilledCircleD(50, 50, 100);
        glPopMatrix();



        glPushMatrix();
        glTranslatef(x + 100, y + 100, 0);
        glRotatef(rotation, 0f, 0f, -1f);
        glColor3f(0.9f, 0.1f, 0.0f);
        glBegin(GL_TRIANGLES);

        glVertex2d(-50, -50);
        glVertex2d(50, 50);
        glVertex2d(0, 50);

        glEnd();
        glPopMatrix();

        glPushMatrix();
        //glTranslated(x, y, 0);
        SFT_Util.setColor(Color.red);


        glPointSize(5f);
        glBegin(GL_POINTS);
        glVertex2d(0, 0);
        glEnd();
        glPopMatrix();

        SFT_Util.setColor(Color.CYAN);



        SFT_Util.printText(x, -y + 1000, "Obend!  3=========D ~~~~ \\ /^\\ /  (.^.)", sftf);

        SFT_Util.printText(0, 0, "/_ ZERO", sftf);

        SFT_Util.printText(0, 100, "/_ 0,100", sftf);

        SFT_Util.printText(100, 100, "/_ 100,100", sftf);

        SFT_Util.printText(50, 50, "x is " + x + " ; y is " + y + " , rotation is " + rotation + " ;", sftf);
    }

    @Override
    public void init() {

        Font f = new Font("Times", Font.PLAIN, 15);
        sftf = new SFT_Font(f, new float[]{1, 0.5f, 0.5f, 1}, new float[]{0, 0, 0, 0});


        w.setVSync(true);

        glMatrixMode(GL_PROJECTION);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glLoadIdentity();
        glOrtho(0, 800, 800, 0, 0, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(0.375f, 0.375f, 0);
    }

    @Override
    public void update(long delta) {
        // rotate quad
        rotation += 0.050f * delta;

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
        /*if (x < 0) {
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
        }*/
    }

    @Override
    public void changeResolution(int width, int height) {
        wi = width;
        he = height;
        //glViewport(0, 0, wi, he); //NEW
        //glMatrixMode(GL_PROJECTION);
        //glLoadIdentity();
        //GLU.gluOrtho2D(0, wi, he, 0);

    }
}
