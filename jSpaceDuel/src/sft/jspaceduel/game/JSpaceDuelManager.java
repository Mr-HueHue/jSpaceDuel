package sft.jspaceduel.game;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Random;
import org.lwjgl.LWJGLException;
import sft.jspaceduel.graphics.JSpaceDuelRenderer;
import sft.jspaceduel.physics.Kinematics;
import sft.jspaceduel.physics.PhysicsEngine;
import sft.jspaceduel.physics.spaceobjects.Planet;
import sft.sftengine.graphics.SFTEngineWindow;
import sft.sftengine.util.SFT_Util;

/**
 * Topmanager class of JSpaceDuel, its family and children have to wait in line.
 * 
 * 
 * @author JJ
 */
public class JSpaceDuelManager {

    public PhysicsEngine physics;
    public SFTEngineWindow window;
    JSpaceDuelRenderer renderer;
    int wi = 800, he = 800;
    public Planet movable, earth, mars, moon, sun3;
    public String version = "indev";
    
    public JSpaceDuelManager() throws LWJGLException {
        physics = new PhysicsEngine();
        renderer = new JSpaceDuelRenderer(this, physics);
        window = new SFTEngineWindow(SFTEngineWindow.mode.NATIVEDISPLAY, renderer, wi, he, "JSpaceDuel");

        window.enableVSync();
        generateTestSpaceOjects(physics);
    }

    private void generateTestSpaceOjects(PhysicsEngine p) {
        movable = new Planet(p, new Kinematics(1.496e11, 0, 0, 2.978e4, 0, 2), 0, 20, 0);
        sun3 = new Planet(p, new Kinematics(0, 0, 0, 0, 0, -2), 1.99e30, 50, 0);
        //earth = new Planet(p, new Kinematics(200, 200, 0, 0, 0, 0.1), 0, 30, 0);
        //mars = new Planet(p, new Kinematics(-200, -200, 0, 0, 0, -0.1), 0, 30, 0);
        //moon = new Planet(p, new Kinematics(-150, -150, 0, -1, 0, -0.8), 1, 20, 0);
        //sun3 = new Planet(p, new Kinematics(150, 150, 0, 1, 0, 0.8), 1, 20, 0);
        /*Random r = new Random();
        for (int i = 0; i < 20; i++) {
            int x = r.nextInt(1000)-500, y=r.nextInt(1000)-500;
            double omega = r.nextDouble();
            Kinematics kin = new Kinematics(x, y, 0, 0, 0, omega);
            double masse = r.nextInt(11);
            double mass = r.nextInt(50)*Math.pow(10, masse);
            int radius = r.nextInt(39)+1;
            new Planet(p, kin, mass, radius, r.nextInt(2)+4);
        }*/
    }
    
    public void glInit() {
        movable.textureID = SFT_Util.makeTexture("./data/images/earth.png");
        //earth.textureID = SFT_Util.makeTexture("./data/images/sun.png");
        //mars.textureID = SFT_Util.makeTexture("./data/images/mars.png");
        //moon.textureID = SFT_Util.makeTexture("./data/images/moon.png");
        sun3.textureID = SFT_Util.makeTexture("./data/images/sun.png");
    }

    public void changeResolution(int newwi, int newhe) {
        wi = newwi;
        he = newhe;
    }

    public Dimension getResolution() {
        return new Dimension(wi, he);
    }

    public void start() {
        window.start();
    }
}
