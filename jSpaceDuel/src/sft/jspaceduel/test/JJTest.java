package sft.jspaceduel.test;

import java.awt.Font;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL30;
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
    float by, bx;
    SFT_Font sftf;
    int sphereDL = 0, cubeDL = 0;
    boolean grabmouse = false;

    public static void main(String[] args) throws LWJGLException {
        new JJTest();
    }
    private int mx, my;
    private int dmx, dmy;

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
    public void init() {
        
        mx = wi/2;
        my = he/2;
        
        by=0;
        bx=0;

        Font f = new Font("Times", Font.PLAIN, 15);
        sftf = new SFT_Font(f, new float[]{1, 0.5f, 0.5f, 1}, new float[]{0, 0, 0, 0});


        w.setVSync(true);



        // Create sphere texture
        marbleTextureHandle = makeTexture("data/images/tex1.png");

        // Create texture for ground plane
        groundTextureHandle = makeTexture("data/images/texCube.png");




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

        glEnable(GL_DEPTH_TEST);

        // select model view for subsequent transforms
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    @Override
    public void update(long delta) {

        mx = Mouse.getX();
        my = Mouse.getY();

        dmx = Mouse.getDX();
        dmy = Mouse.getDY();
        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                if (Mouse.getEventButton() == 0) {
                    grabmouse = !grabmouse;
                    w.setMouseGrabbed(grabmouse);
                }
            }
        }


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
        
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            by += 0.01f * delta;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            bx -= 0.01f * delta;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            by -= 0.01f * delta;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            bx += 0.01f * delta;
        }

        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_F) {
                    fullscreen = !fullscreen;
                    w.setFullscreen(fullscreen);
                } else if (Keyboard.getEventKey() == Keyboard.KEY_V) {
                    w.setVSync(vsync);
                    vsync = !vsync;
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
    public void render() {
        // Clear The Screen And The Depth Buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // select model view for subsequent transforms
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // set the viewpoint
        GLU.gluLookAt(0f, 3f, 15f, // where is the eye
                0.05f * (mx - wi / 2), 0.05f * (my - he / 2), 0f, // what point are we looking at
                0f, 1f, 0f);    // which way is up

        //---------------------------------------------------------------
        // desktop
        //---------------------------------------------------------------

        // dark reddish material
        setMaterial(redmaterial, .2f);

        // enable texture
        glBindTexture(GL_TEXTURE_2D, groundTextureHandle);

        // draw the ground cube
        glPushMatrix();
        {
            glTranslatef(0f, x * 0.1f, y * 0.1f);
            callDisplayList(cubeDL);
        }
        glPopMatrix();


        glTranslatef(bx, by, 0);
        glRotatef(rotation, 0, 1, 0);
        


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

        SFT_Util.print2DText(0, 20, "bx is " + bx + "; by is " + by + ";", sftf);
        SFT_Util.print2DText(0, 0, "x is " + x + "; y is " + y + "; rotation is " + rotation + ";", sftf);

        SFT_Util.print2DText(300, 20, "Mouse x is " + mx + "; Mouse y is " + my + ";", sftf);
        SFT_Util.print2DText(300, 0, "Mouse dx is " + dmx + "; Mouse dy is " + dmy + ";", sftf);


    }

    @Override
    public void changeResolution(int width, int height) {
        wi = width;
        he = height;
        System.out.println("changing resolution to " + width + "x" + height);
        SFT_Util.setPerspective(SFT_Util.viewportReshape(width, height));
    }
}
