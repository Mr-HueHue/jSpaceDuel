package sft.jspaceduel.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.opengl.GL11.*;
import sft.jspaceduel.game.JSpaceDuelManager;
import sft.jspaceduel.physics.PhysicsEngine;
import sft.jspaceduel.physics.spaceobjects.Attractor;
import sft.jspaceduel.physics.spaceobjects.Planet;
import sft.jspaceduel.physics.spaceobjects.SpaceObject;
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
    float sidelength = 1000;
    float viewleft = -sidelength / 2, viewright = sidelength / 2, viewbottom = -sidelength / 2, viewtop = sidelength / 2;
    PhysicsEngine engine;
    HashMap<Integer, Color> textcolormap = new HashMap<Integer, Color>();

    public JSpaceDuelRenderer(JSpaceDuelManager m, PhysicsEngine e) {
        manager = m;
        engine = e;
        Dimension viewsize = m.getResolution();
        wi = viewsize.width;
        he = viewsize.height;
    }

    /*void zoomIn() {
    m_d_left_plane *= 0.75f;
    m_d_right_plane *= 0.75f;
    m_d_top_plane *= 0.75f;
    m_d_bottom_plane *= 0.75f;
    updateZooms();
    }
    
    void zoomOut() {
    m_d_left_plane *= 1.33f;
    m_d_right_plane *= 1.33f;
    m_d_top_plane *= 1.33f;
    m_d_bottom_plane *= 1.33f;
    updateZooms();
    }
    
    void updateZooms() {
    double aspect_ratio = (double) wi / (double) he;
    glViewport(0, 0, (int) wi, (int) he);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    if (wi > he) {
    glOrtho(m_d_left_plane * aspect_ratio, m_d_right_plane * aspect_ratio, m_d_bottom_plane, m_d_top_plane, m_d_near_plane, m_d_far_plane);
    } else {
    glOrtho(m_d_left_plane, m_d_right_plane, m_d_bottom_plane * aspect_ratio, m_d_top_plane * aspect_ratio, m_d_near_plane, m_d_far_plane);
    }
    glMatrixMode(GL_MODELVIEW);
    }*/
    @Override
    public void render() {
        // Clear The Screen And The Depth Buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        //glScaled(1.001, 1.001, 1);
        /*double zoom = 1;
        zoom = zoom  - 0.5; // Or whatever you want the step to be
        glMatrixMode(GL_PROJECTION); // You had GL_MODELVIEW
        glOrtho(-1.5 + zoom, 1.0 - zoom, -2.0 + zoom, 0.5 - zoom, -1, 1); // Changed some of the signs here
        glMatrixMode(GL_MODELVIEW);*/



        for (SpaceObject o : engine.allObjects) {
            if (o instanceof Planet) {
                Planet cup = (Planet) o;
                if (cup.textureID == 1) {
                    glColor3d(1, 0, 0);

                } else if (cup.textureID == 2) {
                    glColor3d(0, 1, 0);
                }
                glPushMatrix();
                glTranslated(cup.getPosition()[0], cup.getPosition()[1], 0);
                glRotatef((float) cup.getKinematics().phi, 0, 0, 1);
                glBegin(GL_QUADS);
                {
                    double radius = cup.getRadius();
                    glVertex3d(-radius / 2, -radius / 2, 0);
                    glVertex3d(-radius / 2, radius / 2, 0);
                    glVertex3d(radius / 2, radius / 2, 0);
                    glVertex3d(radius / 2, -radius / 2, 0);
                }
                glEnd();
                glPopMatrix();
            }
        }

        /*glBegin(GL_QUADS);
        {
        glVertex3d(10, 10, 0);
        glVertex3d(10, 90, 0);
        glVertex3d(90, 90, 0);
        glVertex3d(90, 10, 0);
        }
        glEnd();*/
        //SFT_Util.setColor(Color.white);
        double[] delta = manager.p1.getPosDelta(manager.sun);
        SFT_Util.print2DText(0, 0, "Distance: " + Math.sqrt(delta[0] * delta[0] + delta[1] * delta[1]), sftf);
        SFT_Util.print2DText(0, 40, "Distance X: " + delta[0], sftf);
        SFT_Util.print2DText(0, 20, "Distance Y: " + delta[1], sftf);

    }

    @Override
    public void init() {

        sftf = new SFT_Font(new Font("Times", Font.PLAIN, 15), new float[]{1, 1, 1, 1}, new float[]{0, 0, 0, 0});

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
        glColor3d(0.9, 0.9, 0.9);
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

        SpaceObject[] objlist = engine.allObjects.toArray(new SpaceObject[0]);

        for (Attractor a : engine.attractors) {
            for (SpaceObject o : engine.allObjects) {
                if (!o.equals(a)) {
                    a.attract(o);
                }
            }
        }

        for (SpaceObject o : engine.allObjects) {
            o.tick(1);
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
