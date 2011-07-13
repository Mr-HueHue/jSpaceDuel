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
    public Planet p1, sun, sun2, p2;

    public JSpaceDuelManager() throws LWJGLException {
        physics = new PhysicsEngine();
        renderer = new JSpaceDuelRenderer(this, physics);
        window = new SFTEngineWindow(renderer, wi, he, "JSpaceDuel");

        window.enableVSync();
        generateTestSpaceOjects(physics);
    }

    private void generateTestSpaceOjects(PhysicsEngine p) {
        p1 = new Planet(p, new Kinematics(100, 100, 0, 1, 0, 0.8), 1, 20, 1);
        sun = new Planet(p, new Kinematics(50, 50, 1, 0, 0, 0.1), 2e12, 30, 2);
        sun2 = new Planet(p, new Kinematics(-50, -50, -1, 0, 0, -0.1), 2e12, 30, 2);
        p2 = new Planet(p, new Kinematics(-100, -100, 0, -1, 0, -0.8), 1, 20, 3);
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
