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
    private SFT_Font sftf, sftf2;
    float sidelength = 1000;
    float viewleft = -sidelength / 2, viewright = sidelength / 2, viewbottom = -sidelength / 2, viewtop = sidelength / 2;
    PhysicsEngine engine;
    HashMap<Integer, Color> textcolormap = new HashMap<Integer, Color>();
    float aspect_ratio = 1;
    
    float metersperpixel = 1;

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
    /*
     * Zoom edge coordinates:
     */
    double origminx = 0, origminy = 0, origmaxx = 0, origmaxy = 0;
    float xmin, xmax, ymin, ymax, deltax, deltay;
    
    @Override
    public void render() {
        // Clear The Screen And The Depth Buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        setView();

        // draw coordinate system


        glBegin(GL_LINES);
        {
            SFT_Util.setColor(Color.gray);
            glVertex2d(0, ymax);
            glVertex2d(0, ymin);
            glVertex2d(xmin, 0);
            glVertex2d(xmax, 0);
        }
        glEnd();



        origminx = origminy = Double.POSITIVE_INFINITY;
        origmaxx = origmaxy = Double.NEGATIVE_INFINITY;
        for (SpaceObject o : engine.allObjects) {

            double aktposx = o.getPosition()[0];
            double aktposy = o.getPosition()[1];
            double radius = o.getRadius();



            // lower left corner
            origminx = Math.min(aktposx - radius, origminx);
            origminy = Math.min(aktposy - radius, origminy);

            // upper right corner
            origmaxx = Math.max(aktposx + radius, origmaxx);
            origmaxy = Math.max(aktposy + radius, origmaxy);



            if (o instanceof Planet) {

                Planet cup = (Planet) o;

                if (cup.textureID > 0) {
                    glBindTexture(GL_TEXTURE_2D, cup.textureID);
                } else {
                    glBindTexture(GL_TEXTURE_2D, 0);
                }

                glPushMatrix();
                {

                    glTranslated(aktposx, aktposy, 0);
                    glRotatef((float) cup.getKinematics().phi, 0, 0, 1);

                    // Draw a planet
                    glBegin(GL_QUADS);
                    {

                        if (cup.textureID <= 0) {
                            glVertex3d(-radius / 2, -radius / 2, 0);
                            glVertex3d(-radius / 2, radius / 2, 0);
                            glVertex3d(radius / 2, radius / 2, 0);
                            glVertex3d(radius / 2, -radius / 2, 0);
                        } else {
                            glTexCoord2d(0, 0);
                            glVertex3d(-radius / 2, -radius / 2, 0);
                            glTexCoord2d(0, 1);
                            glVertex3d(-radius / 2, radius / 2, 0);
                            glTexCoord2d(1, 1);
                            glVertex3d(radius / 2, radius / 2, 0);
                            glTexCoord2d(1, 0);
                            glVertex3d(radius / 2, -radius / 2, 0);
                        }
                    }
                    glEnd();
                }
                glPopMatrix();
            }
        }




        SFT_Util.setColor(Color.white);
        SFT_Util.print2DText(10, he - 30, "jSpaceDuel " + manager.version, sftf2);

        /*double[] pos = manager.movableSun.getPosition();
        SFT_Util.print2DText(0, 20, "Pos X: " + pos[0], sftf);
        SFT_Util.print2DText(0, 0, "Pos Y: " + pos[1], sftf);
        SFT_Util.print2DText(250, 20, "Accel X: " + manager.movableSun.getKinematics().a[0], sftf);
        SFT_Util.print2DText(250, 0, "Accel Y: " + manager.movableSun.getKinematics().a[1], sftf);*/
        
        SFT_Util.print2DText(0, 0, "KinE-Dichte: " + manager.movable, sftf);
        for (SpaceObject o : engine.allObjects) {
            o.tick(1);
        }
    }

    @Override
    public void init() {


        sftf = new SFT_Font(new Font("Times", Font.PLAIN, 15), new float[]{1, 1, 1, 1}, new float[]{0, 0, 0, 0});

        sftf2 = new SFT_Font(new Font("Times", Font.PLAIN, 22), new float[]{1, 1, 1, 1}, new float[]{0, 0, 0, 0});

        manager.glInit();

        changeResolution(wi, he);

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
    double accelkezb = 0.00911;

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
            manager.movable.accelerate(new double[]{-accelkezb * milliSecondsSinceLastFrame * 0.25, 0});

            viewtop -= 0.35f * milliSecondsSinceLastFrame;
            x -= 0.35f * milliSecondsSinceLastFrame;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            manager.movable.accelerate(new double[]{accelkezb * milliSecondsSinceLastFrame * 0.25, 0});

            viewtop += 0.35f * milliSecondsSinceLastFrame;

            x += 0.35f * milliSecondsSinceLastFrame;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            manager.movable.accelerate(new double[]{0, accelkezb * milliSecondsSinceLastFrame * 0.25});

            y += 0.35f * milliSecondsSinceLastFrame;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            manager.movable.accelerate(new double[]{0, -accelkezb * milliSecondsSinceLastFrame * 0.25});

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
                    manager.window.terminate();
                }
            }
        }
        float zoomstepwidth = 4e-5f;
        metersperpixel += milliSecondsSinceLastFrame * zoomstepwidth * Mouse.getDWheel();
        if(metersperpixel < 0) {
            metersperpixel = 0;
        }

        for (Attractor a : engine.attractors) {
            for (SpaceObject o : engine.allObjects) {
                if (!o.equals(a)) {
                    a.attract(o);
                }
            }
        }



    }

    @Override
    public final void changeResolution(int width, int height) {
        wi = width;
        he = height;

        //GLU.gluOrtho2D(viewleft, viewright, viewbottom, viewtop);

        manager.changeResolution(width, height);

        aspect_ratio = SFT_Util.viewportReshape(width, height);


        /*
         *         glMatrixMode(GL_PROJECTION);
        glLoadIdentity(); // reset the projection shit
        
        GLU.gluOrtho2D(viewleft, viewright, viewbottom, viewtop);
        glMatrixMode(GL_MODELVIEW);
        SFT_Util.viewportReshape(width, height);
        manager.changeResolution(width, height);
         */
    }

    /**
     * Set the view "verticies"
     */
    void setView() {

        float oversizefactor = 0.2f;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        //GLU.gluOrtho2D((float)eckedx, (float)eckeux, (float)eckedy, (float)eckeuy);


        xmin = (float) origminx;
        xmax = (float) origmaxx;
        ymin = (float) origminy;
        ymax = (float) origmaxy;


        deltax = xmax - xmin;
        deltay = ymax - ymin;

        float morex = deltax * oversizefactor;
        float morey = deltay * oversizefactor;

        xmin -= morex / 2;
        ymin -= morey / 2;
        xmax += morex / 2;
        ymax += morex / 2;

        //deltax = xmax - xmin;
        //deltay = ymax - ymin;


        /*float objectratio = deltax / deltay;
        float xmean = (xmax + xmin) / 2;
        float ymean = (ymax + ymin) / 2;
        
        
        if (objectratio < aspect_ratio) {
        deltax = aspect_ratio * deltay;
        } else {
        deltay = deltax / aspect_ratio;
        }
        
        xmin = xmean - deltax / 2;
        xmax = xmean + deltax / 2;
        
        ymin = ymean - deltay / 2;
        ymax = ymean + deltay / 2;*/


        float centerx = (float) manager.movable.getPosition()[0];
        float centery = (float) manager.movable.getPosition()[1];

        deltax = 2 * Math.max(centerx - xmin, xmax - centerx);
        deltay = 2 * Math.max(centery - ymin, ymax - centery);

        float mppmin = Math.max(deltax / wi, deltay / he); //mpp = meters per pixel
        float mpp;
        if(false) {
            mpp = discretizise(mppmin);
        } else {
            mpp = metersperpixel;
        }
        
        
        xmin = centerx - mpp * wi / 2;
        xmax = centerx + mpp * wi / 2;
        ymin = centery - mpp * he / 2;
        ymax = centery + mpp * he / 2;

        SFT_Util.setColor(Color.pink);
        //glColor3d(0.5, 0.2, 0.1);
        
        SFT_Util.print2DText(0, 0, "Meters per pixel: " + mpp, sftf);

        GLU.gluOrtho2D(xmin, xmax, ymin, ymax);
        glMatrixMode(GL_MODELVIEW);
    }

    private float discretizise(double input) {
        float maxmpp = 2.5f;
        //final double basezommlevels = 1.4;
        //return (float) Math.pow(basezommlevels, Math.ceil(Math.log(input)/Math.log(basezommlevels)));
        return Math.max(Math.min((float) input, maxmpp), 0.2f);
    }
}
