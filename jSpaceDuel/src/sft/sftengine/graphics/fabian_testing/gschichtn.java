package sft.sftengine.graphics.fabian_testing;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.util.glu.GLU.*;
import static org.lwjgl.opengl.GL11.*;
import sft.sftengine.util.InitLibraries;

/**
 *
 * @author fabian
 */
public class gschichtn {

    public static void main(String[] args) {
        InitLibraries.addlwjgl();
        new gschichtn();
    }
    public static final int DISPLAY_HEIGHT = 700;
    public static final int DISPLAY_WIDTH = 700;

    public gschichtn() {
        try {

            Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
            Display.create();
            //Keyboard
            Keyboard.create();

            //Mouse
            //Mouse.setGrabbed(false);
            Mouse.create();

            initAll();

            run();
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        } finally {
            destroy();
        }
    }

    public void run() {

        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            if (Display.isVisible()) {
                render();
            } else {
                if (Display.isDirty()) {
                    render();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                }
            }
            Display.update();
            Display.sync(50);
        }
    }

    public void destroy() {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    public void initAll() {
        glClearColor(0.5f, 0.11f, 0.1138f, 0.0f); // color rgba from 0-1

        glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT); // drawable area

        //glDisable(GL_LIGHTING);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0.0f, DISPLAY_WIDTH, 0.0f, DISPLAY_HEIGHT);
        //gluPerspective(20, 1, 10, 1000);
        glPushMatrix();

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glPushMatrix();
    }

    private void render() {
        
        
        
        int mx = Mouse.getX();
        int my = Mouse.getY();
        
        //gluLookAt(0, 0, 10, mx, my, 0, 0, 0, 0);
        
        glClear(GL_COLOR_BUFFER_BIT);

        glColor3f(0f, 1f, 0f);
        glBegin(GL_POLYGON);
        
        glVertex2f(10f, 10f);
        glVertex2f(10, 200);
        glVertex2f(50, 300);
        glVertex2f(100, 200);
        glVertex2f(100, 10);
                
        glEnd();


        
        glPointSize(100f);
        glBegin(GL_POINTS);

        glColor3f(1f, 0f, 0f);
        glVertex2f(400f, 500f);
        glVertex2f(100f, 100f);
        
        glEnd();
        
        glPushMatrix();
        //glTranslated(mx, my, 0);
        //glRotatef(1, mx, my, 0);
        glBegin(GL_POLYGON);
        glColor3d(1, 1, 0.2);
        glVertex2d(0, 0);
        glVertex2d(mx, 0);
        glVertex2d(mx, my);
        glVertex2d(0, my);
        glEnd();
        glPopMatrix();


        glBegin(GL_LINES);
        
        glColor3d(0, 0, 1);
        glVertex2d(0, 0);
        glVertex2d(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        
        glColor3d(0.5, 0.5, 0);
        glVertex2d(DISPLAY_WIDTH/2, 0);
        glVertex2d(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        
        glEnd();
        
        glBegin(GL_QUAD_STRIP);
        
        glVertex2d(0, DISPLAY_HEIGHT);
        glVertex2d(0, DISPLAY_HEIGHT-200);
        glVertex2d(40, DISPLAY_HEIGHT-100);
        glVertex2d(100, DISPLAY_HEIGHT-300);
        glVertex2d(300, DISPLAY_HEIGHT-200);
        glVertex2d(300, DISPLAY_HEIGHT-400);
        
        glEnd();
        
        
        
        

    }
}
