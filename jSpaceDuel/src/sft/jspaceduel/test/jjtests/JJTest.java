package sft.jspaceduel.test.jjtests;

import java.awt.Font;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
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
    boolean grabmouse = false;
    
    int numofstars = 50;

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

    float x = 0, y = 0;

    float rotation = 0;

    @Override
    public void init() {
        
        mx = wi/2;
        my = he/2;
        
        by=0;
        bx=0;

        Font f = new Font("Times", Font.PLAIN, 15);
        sftf = new SFT_Font(f, new float[]{1, 0.5f, 0.5f, 1}, new float[]{0, 0, 0, 0});


        w.setVSync(true);



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
