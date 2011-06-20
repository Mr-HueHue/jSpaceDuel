package sft.jspaceduel.test;

import java.awt.Font;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.GLU;
import static org.lwjgl.opengl.GL11.*;
import static sft.sftengine.util.SFT_Util.*;
import static sft.sftengine.util.SFT_DrawTemplates.*;
import sft.sftengine.graphics.Renderer;
import sft.sftengine.graphics.SFTEngineWindow;
import sft.sftengine.graphics.SFT_Font;
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
    int sphereDL = 0, cubeDL = 0;

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
    float x = -70, y = 0;
    /** angle of quad rotation */
    float rotation = 0;
    int marbleTextureHandle, groundTextureHandle;
    // Light position: if last value is 0, then this describes light direction.  If 1, then light position.
    float lightPosition1[] = {0f, 0f, 0f, 1f};
    float lightPosition2[] = {0f, -10f, 0f, 0f};
    float lightPosition3[] = {0f, 0f, 0f, 1f};
    float[] redmaterial = new float[]{0.8f, 0.3f, 0.2f, 1.0f};

    @Override
    public void render() {
        // Clear The Screen And The Depth Buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // select model view for subsequent transforms
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // set the viewpoint
        GLU.gluLookAt(0f, 3f, 15f, // where is the eye
                0f, -1f, 0f, // what point are we looking at
                0f, 1f, 0f);    // which way is up

        //---------------------------------------------------------------
        // desktop
        //---------------------------------------------------------------

        // dark reddish material
        setMaterial(redmaterial, .2f);

        // enable texture
        glBindTexture(GL_TEXTURE_2D, groundTextureHandle);

        // draw the ground plane
        glPushMatrix();
        {
            glTranslatef(0f, x*0.1f, y*0.1f); // down a bit
            callDisplayList(cubeDL);
        }
        glPopMatrix();


        // orbit
        glRotatef(rotation, 0, 1, 0);
        glTranslatef(3, 0, 0);

        glRotatef(rotation / 2, 0, 1, 0);
        glTranslatef(-2, 0, 0);


        //---------------------------------------------------------------
        // glowing white ball
        //---------------------------------------------------------------

        // for point lights set position each frame so light moves with scene
        // white light at same position as marble ball
        setLightPosition(GL_LIGHT1, lightPosition1);

        // glowing white material
        setMaterial(
                new float[]{1.0f, 1.0f, 1.0f, 1.0f}, // diffuse color
                new float[]{1.0f, 1.0f, 1.0f, 1.0f}, // ambient
                new float[]{0.0f, 0.0f, 0.0f, 1.0f}, // specular
                new float[]{1.0f, 1.0f, 1.0f, 1.0f}, // emissive
                0f);        // not shiny

        // enable texture
        glBindTexture(GL_TEXTURE_2D, marbleTextureHandle);

        // draw marble sphere
        glPushMatrix();
        {
            glScalef(.9f, .9f, .9f);    // make it smaller
            callDisplayList(sphereDL);       // draw the sphere display list
        }
        glPopMatrix();


        // orbit
        glRotatef(rotation * 1.4f, 0, 1, 0);
        glTranslatef(4, 0, 0);


        //---------------------------------------------------------------
        // shiny blue ball
        //---------------------------------------------------------------

        // shiny dark blue material
        setMaterial(new float[]{0.3f, 0.3f, 0.6f, 1.0f}, .9f);

        // set blue light at same spot as blue sphere
        setLightPosition(GL_LIGHT3, lightPosition3);

        // no texture (texture handle 0)
        glBindTexture(GL_TEXTURE_2D, 0);

        // Draw sphere
        glPushMatrix();
        {
            glScalef(1.6f, 1.6f, 1.6f);     // make it smaller
            callDisplayList(sphereDL);
        }
        glPopMatrix();
        
        SFT_Util.print2DText(0, 0, "x is " + x + "; y is "+ y + ";", sftf);
    }

    @Override
    public void init() {

        Font f = new Font("Times", Font.PLAIN, 15);
        sftf = new SFT_Font(f, new float[]{1, 0.5f, 0.5f, 1}, new float[]{0, 0, 0, 0});


        w.setVSync(true);


        // Create sphere texture
        marbleTextureHandle = makeTexture("data/images/tex1.png");

        // Create texture for ground plane
        groundTextureHandle = makeTexture("images/mahog_texture.jpg");

        setPerspective((float) wi / (float) he);

        setLight(GL_LIGHT1,
                new float[]{1.0f, 1.0f, 1.0f, 1.0f}, // diffuse color
                new float[]{0.2f, 0.2f, 0.2f, 1.0f}, // ambient
                new float[]{1.0f, 1.0f, 1.0f, 1.0f}, // specular
                lightPosition1);                         // position

        // Create a directional light (dark red, to simulate reflection off wood surface)
        setLight(GL_LIGHT2,
                new float[]{0.95f, 0.35f, 0.15f, 1.0f}, // diffuse color
                new float[]{0.0f, 0.0f, 0.0f, 1.0f}, // ambient
                new float[]{0.03f, 0.0f, 0.0f, 1.0f}, // specular
                lightPosition2);   // position (pointing up)

        // Create a point light (dark blue, to simulate reflection off wood surface)
        setLight(GL_LIGHT3,
                new float[]{0.35f, 0.45f, 0.95f, 1.0f}, // diffuse color
                new float[]{0.0f, 0.0f, 0.0f, 1.0f}, // ambient
                new float[]{0.3f, 0.4f, 0.7f, 1.0f}, // specular
                lightPosition3);   // position (pointing up)

        sphereDL = beginDisplayList();
        renderSphere();
        endDisplayList();

        cubeDL = beginDisplayList();
        renderCube(10, 40);
        endDisplayList();

        // enable lighting and texture rendering
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);

        // select model view for subsequent transforms
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
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
