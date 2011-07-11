package sft.jspaceduel.game;

import java.awt.Dimension;
import org.lwjgl.LWJGLException;
import sft.jspaceduel.graphics.JSpaceDuelRenderer;
import sft.jspaceduel.physics.Kinematics;
import sft.jspaceduel.physics.PhysicsEngine;
import sft.jspaceduel.physics.spaceobjects.Planet;
import sft.sftengine.graphics.SFTEngineWindow;

/**
 * Topmanager class of JSpaceDuel, it's family and children have to wait in line.
 * 
 * 
 * @author JJ
 */
public class JSpaceDuelManager {

    public PhysicsEngine physics;
    public SFTEngineWindow window;
    JSpaceDuelRenderer renderer;
    int wi = 800, he = 800;
    public Planet p1, sun;

    public JSpaceDuelManager() throws LWJGLException {
        physics = new PhysicsEngine();
        renderer = new JSpaceDuelRenderer(this, physics);
        window = new SFTEngineWindow(renderer, wi, he, "JSpaceDuel");

        window.enableVSync();
        generateTestSpaceOjects(physics);
    }

    private void generateTestSpaceOjects(PhysicsEngine p) {
        /*double earthorbitspeedinmeterspersecond = 29805.555;
        // converto to distance per nanosecond
        double earthorbitspeedinmeterspernanosecond = earthorbitspeedinmeterspersecond * 10e-9;
        
        Kinematics kin = new Kinematics(1149.60e9, 0, 0, earthorbitspeedinmeterspernanosecond, 0, 0);
        Planet p1 = new Planet(p, kin, 5.9742e24, 6378.1, 1);
        
        // No star implemented by @mic_e -> we must use a planet for it.
        Planet sun = new Planet(p, new Kinematics(0, 0, 0, 0, 0, 0), 1.98892e30, 8000, 2);*/


        Kinematics kin = new Kinematics(100, 0, 0, 1, 0, 0.8);
        p1 = new Planet(p, kin, 1, 20, 1);
        sun = new Planet(p, new Kinematics(0, 0, 0, 0, 0, 0), 15e9, 30, 2);
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
